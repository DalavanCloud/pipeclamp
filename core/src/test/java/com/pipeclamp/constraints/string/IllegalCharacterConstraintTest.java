package com.pipeclamp.constraints.string;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.apache.avro.Schema;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.pipeclamp.api.Constraint;
import com.pipeclamp.api.ConstraintBuilder;
import com.pipeclamp.api.ValueConstraint;
import com.pipeclamp.api.Violation;
import com.pipeclamp.constraints.AbstractConstraintTest;

/**
 * 
 * @author Brian Remedios
 */
public class IllegalCharacterConstraintTest extends AbstractConstraintTest {

	@Test
	public void testBuilder() {

		Map<String,String> paramsByKey = asParams(IllegalCharacterConstraint.BAD_CHARS, "./?");

		Collection<Constraint<?>> vcs = IllegalCharacterConstraint.Builder.constraintsFrom(Schema.Type.STRING, false, paramsByKey);

		assertNotNull(vcs);
		assertTrue(paramsByKey.isEmpty());

		paramsByKey = asParams(WhitespaceConstraint.NO_LEADING, "./?");

		vcs = WhitespaceConstraint.Builder.constraintsFrom(Schema.Type.STRING, false, paramsByKey);

		assertNull(vcs);


		paramsByKey = asParams(WhitespaceConstraint.NO_TRAILING, false);

		vcs = WhitespaceConstraint.Builder.constraintsFrom(Schema.Type.STRING, false, paramsByKey);

		Assert.assertNull(vcs);
	}

	@Test
	public void typedErrorFor() {

		Map<String,String> paramsByKey = asParams(IllegalCharacterConstraint.BAD_CHARS, "./?");

		Collection<Constraint<?>> vcs = IllegalCharacterConstraint.Builder.constraintsFrom(Schema.Type.STRING, false, paramsByKey);
		ValueConstraint<?> vc = (ValueConstraint<?>)vcs.iterator().next();

		Violation violation = vc.errorFor("hello world");
		assertNull(violation);

		violation = vc.errorFor("");
		assertNull(violation);

		violation = vc.errorFor("hello world?");
		assertNotNull(violation);

		violation = vc.errorFor(".hello world?");
		assertNotNull(violation);
	}

	@Override
	protected ConstraintBuilder<?> sampleBuilder() { return IllegalCharacterConstraint.Builder; }

	@Override
	protected ValueConstraint<?> sampleConstraint() {

		Map<String,String> paramsByKey = asParams(IllegalCharacterConstraint.BAD_CHARS, "./?");
		Collection<Constraint<?>> vcs = IllegalCharacterConstraint.Builder.constraintsFrom(Schema.Type.STRING, false, paramsByKey);
		return (ValueConstraint<?>)vcs.iterator().next();
	}
}
