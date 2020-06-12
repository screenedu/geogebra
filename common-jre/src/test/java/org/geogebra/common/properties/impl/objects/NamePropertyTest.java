package org.geogebra.common.properties.impl.objects;

import static org.junit.Assert.fail;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.kernel.geos.GeoElement;
import org.junit.Test;

public class NamePropertyTest extends BaseUnitTest {

	@Test
	public void testConstructorSucceeds() {
		GeoElement slider = addAvInput("1");
		try {
			new NameProperty(slider);
		} catch (NotApplicablePropertyException e) {
			fail(e.getMessage());
		}
	}
}