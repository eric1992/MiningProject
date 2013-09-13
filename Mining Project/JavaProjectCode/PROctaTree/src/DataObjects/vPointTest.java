package DataObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class vPointTest {
	@Test
	public void testAddError() {
		vPoint a = new vPoint(0, 0, 0, 1);
		a.addError(1.0);
		a.addError(2.0);
		a.addError(3.0);
		assertEquals(1.0, a.getError().get(0).doubleValue(), 1.0);
		assertEquals(2.0, a.getError().get(1).doubleValue(), 1.0);
		assertEquals(3.0, a.getError().get(2).doubleValue(), 1.0);
	}

	@Test
	public void testCondenseVelocity() {
		vPoint a = new vPoint(0, 0, 0, 1);
		a.addError(1.0);
		a.addError(2.0);
		a.addError(3.0);
		a.condenseVelocity();
		assertEquals(2, a.getVelocity(), 1.0);
		assertTrue(a.getError().isEmpty());
	}

}
