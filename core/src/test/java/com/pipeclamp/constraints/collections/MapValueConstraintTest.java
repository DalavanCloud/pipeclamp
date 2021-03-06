package com.pipeclamp.constraints.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Map;

import org.apache.avro.Schema.Type;
import org.testng.annotations.Test;

import com.pipeclamp.api.Constraint;
import com.pipeclamp.api.ConstraintBuilder;
import com.pipeclamp.api.ValueConstraint;
import com.pipeclamp.avro.AvroConfiguration;
import com.pipeclamp.constraints.AbstractConstraintTest;
import com.pipeclamp.constraints.string.StringLengthConstraint;

public class MapValueConstraintTest extends AbstractConstraintTest {

	@Test
	public void testMapValueConstraintBuilder() {

		Map<String,String> paramsByKey = asParams(
				MapValueConstraint.VALUE_TYPE, "string",
				AbstractMapConstraint.CONSTRAINT_ID, StringLengthConstraint.TypeTag, 
				StringLengthConstraint.MIN_LENGTH, 3);

		Collection<Constraint<?>> vcs = MapValueConstraint.builderWith(AvroConfiguration.ConstraintFactory).constraintsFrom(Type.MAP, false, paramsByKey);

		assertNotNull(vcs);
		assertEquals(vcs.size(), 1);
	}

	@Test
	public void typedErrorFor() {
		//    TODO
	}
	
	@Override
	protected ConstraintBuilder<?> sampleBuilder() { return MapValueConstraint.builderWith(AvroConfiguration.ConstraintFactory); }

	@Override
	protected ValueConstraint<?> sampleConstraint() {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	protected ValueConstraint<?> sampleConstraint() {
//		
//		Map<String,String> paramsByKey = asParams(
//				MapKeyConstraint.KEY_TYPE, "string",
//				AbstractMapConstraint.CONSTRAINT_ID, StringLengthConstraint.TypeTag, 
//				StringLengthConstraint.MIN_LENGTH, 3);
//
//		Collection<ValueConstraint<?>> vcs = MapValueConstraint.builderWith(AvroConfiguration.ConstraintFactory).constraintsFrom(Type.MAP, false, paramsByKey);
//		return vcs.iterator().next();
//	}
}
