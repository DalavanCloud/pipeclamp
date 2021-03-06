package com.pipeclamp.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;

import com.pipeclamp.api.Constraint;
import com.pipeclamp.api.ConstraintFilter;
import com.pipeclamp.api.MultiValueConstraint;
import com.pipeclamp.api.ValueConstraint;
import com.pipeclamp.api.Violation;
import com.pipeclamp.avro.SimpleAvroPath;
import com.pipeclamp.path.Path;

/**
 *
 * @author Brian Remedios
 */
public abstract class AbstractValidator<T extends Object> {

	private final Map<Path<T, ?>, Collection<ValueConstraint<?>>> constraintsByPath = new HashMap<>();
	private final Map<Path<T, ?>, Collection<MultiValueConstraint<T>>> multivalueConstraints = new HashMap<>();
	protected final Map<Path<T, ?>, Object> cachedValuesByPath = new HashMap<>();
	
	protected static Collection<Violation> violationsFor(Collection<ValueConstraint<?>> constraints, Object value) {

		Collection<Violation> violations = new ArrayList<>(constraints.size());

		for (ValueConstraint<?> constraint : constraints) {
			Violation violation = constraint.errorFor(value);
			if (violation != null) violations.add(violation);
			}
		return violations;
	}

	protected static Collection<ValueConstraint<?>> filter(Collection<ValueConstraint<?>> existing, ConstraintFilter predicate) {
		
		if (predicate == null) return existing;
		
		Collection<ValueConstraint<?>> filtered = new ArrayList<>(existing.size());
		for (ValueConstraint<?> vc : existing) {
			if (predicate.accept(vc)) filtered.add(vc);
		}
		return filtered;
	}
	
	protected AbstractValidator() { }

	protected AbstractValidator(Map<Path<T, ?>, Collection<Constraint<?>>> theConstraintsByPath) {
		
		for (Entry<Path<T, ?>, Collection<Constraint<?>>> entry : theConstraintsByPath.entrySet()) {
			Path path = entry.getKey();
			for (Constraint cst : entry.getValue()) {
				if (cst instanceof ValueConstraint) {
					register((ValueConstraint)cst, path);
					} else {
						if (cst instanceof MultiValueConstraint) {
							register((MultiValueConstraint)cst, path);
						}
					}
			}
		}
	//	constraintsByPath = theConstraintsByPath;
	//	multivalueConstraints = theMultivalues;
	}

	public void register(ValueConstraint<?> constraint, String... thePath) {
		register(constraint, new SimpleAvroPath(thePath));
	}
	
	/**
	 * Walks the path for every registered constraint to extract the value
	 * and evaluates them. Any violations are held and returned in a map
	 * that denotes the path.  All available constraints are applied.
	 *
	 * @param record
	 *
	 * @return Map<Path<T, ?>, Collection<Violation>>
	 */
	public Map<Path<T, ?>, Collection<Violation>> validate(T record) {
		return validate(record, null);
	}
	
	/**
	 * Walks the path for every acceptable constraint to extract the value
	 * and evaluates them. Any violations are held and returned in a map
	 * that denotes the path.
	 *
	 * @param record
	 * @param filter
	 *
	 * @return Map<Path<T, ?>, Collection<Violation>>
	 */
	public abstract Map<Path<T, ?>, Collection<Violation>> validate(T record, ConstraintFilter filter);

	protected Set<Entry<Path<T, ?>, Collection<ValueConstraint<?>>>> valueConstraints() {
		return constraintsByPath.entrySet();
	}

	protected Set<Entry<Path<T, ?>, Collection<MultiValueConstraint<T>>>> multiValueConstraints() {
		return multivalueConstraints.entrySet();
	}
	
	protected void cache(Path<T, ?> path, Object value) {
		if (multivalueConstraints.isEmpty()) return;
		cachedValuesByPath.put(path, value);
	}

	protected void validationStarting() {
		cachedValuesByPath.clear();
	}
	
	/**
	 * Register a new constraint against the specified path.
	 *
	 * @param constraint
	 * @param path
	 */
	public void register(ValueConstraint<?> constraint, Path<T, ?> path) {

		Collection<ValueConstraint<?>> constraints = constraintsByPath.get(path);
		if (constraints == null) {
			constraints = new ArrayList<>();
			constraintsByPath.put(path, constraints);
		}
		constraints.add(constraint);
	}
	
	/**
	 * Register a new constraint against the specified path.
	 *
	 * @param constraint
	 * @param path
	 */
	public void register(MultiValueConstraint<T> constraint, Path<T, ?> path) {

		Collection<MultiValueConstraint<T>> constraints = multivalueConstraints.get(path);
		if (constraints == null) {
			constraints = new ArrayList<>();
			multivalueConstraints.put(path, constraints);
		}
		constraints.add(constraint);
	}

	/**
	 * Lists each path and then the constraint(s) associated with them.
	 *
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		for (Entry<Path<T, ?>, Collection<ValueConstraint<?>>> entry : constraintsByPath.entrySet()) {
			sb.append(entry.getKey()).append('\n');
			for (ValueConstraint<?> cnst : entry.getValue())
				sb.append('\t').append(cnst).append('\n');
		}
		return sb.toString();
	}
}