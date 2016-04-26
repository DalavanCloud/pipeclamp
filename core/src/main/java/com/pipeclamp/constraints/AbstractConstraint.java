package com.pipeclamp.constraints;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.pipeclamp.AbstractRegisteredItem;
import com.pipeclamp.api.Parameter;
import com.pipeclamp.api.TaggedItem;
import com.pipeclamp.avro.AvroConstraintUtil;

/**
 *
 * @author Brian Remedios
 */
public abstract class AbstractConstraint extends AbstractRegisteredItem implements TaggedItem {

	public final boolean nullsAllowed;

	protected static String typeIn(Map<String, String> params) {
		return params.get(AvroConstraintUtil.TypeKey);
	}

	protected AbstractConstraint(String theId, boolean nullsAllowedFlag) {
		super(theId, null);
	
		nullsAllowed = nullsAllowedFlag;
	}

	public Map<Parameter<?>, Object> parameters() { return Collections.emptyMap(); }
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other) return true;
		if (other == null) return false;
		
		if (!(other instanceof AbstractConstraint)) return false;
		
		AbstractConstraint ari = AbstractConstraint.class.cast(other);
		
		return nullsAllowed == ari.nullsAllowed;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + Objects.hash(nullsAllowed);
	}
}