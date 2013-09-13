package DataObjects;

import static org.junit.Assert.*;

import org.junit.Test;

import Compare3D.Direction;

public class PointTest {

	@Test
	public void testGetX() {
		Point a = new Point(1, 2, 3);
		assertEquals(1.0, a.getX(), .01);
	}

	@Test
	public void testGetY() {
		Point a = new Point(1, 2, 3);
		assertEquals(2.0, a.getY(), .01);
	}

	@Test
	public void testGetZ() {
		Point a = new Point(1, 2, 3);
		assertEquals(3.0, a.getZ(), .01);
	}

	@Test
	public void testDirectionFrom() {
		Point a = new Point(0, 0, 0);
		//checks equality
		assertEquals(Direction.NOQUADRANT, a.directionFrom(0, 0, 0));
		//checks in quadrants
		assertEquals(Direction.UNE, a.directionFrom(-1, -1,-1));
		assertEquals(Direction.UNW, a.directionFrom(1, -1,-1));
		assertEquals(Direction.USW, a.directionFrom(1, 1,-1));
		assertEquals(Direction.USE, a.directionFrom(-1, 1,-1));
		assertEquals(Direction.DNE, a.directionFrom(-1, -1, 1));
		assertEquals(Direction.DNW, a.directionFrom(1, -1, 1));
		assertEquals(Direction.DSW, a.directionFrom(1, 1, 1));
		assertEquals(Direction.DSE, a.directionFrom(-1, 1, 1));
		//Checking the axis
		assertEquals(Direction.DNE, a.directionFrom(0, 0, 1));
		assertEquals(Direction.UNE, a.directionFrom(0, 0, -1));
		assertEquals(Direction.USE, a.directionFrom(0, 1, 0));
		assertEquals(Direction.UNW, a.directionFrom(0, -1, 0));
		assertEquals(Direction.USW, a.directionFrom(1, 0, 0));
		assertEquals(Direction.UNE, a.directionFrom(-1, 0, 0));
		//Checking on the planes
		assertEquals(Direction.USW, a.directionFrom(1, 1, 0));
		assertEquals(Direction.UNW, a.directionFrom(1, -1, 0));
		assertEquals(Direction.USE, a.directionFrom(-1, 1, 0));
		assertEquals(Direction.UNE, a.directionFrom(-1, -1, 0));
		assertEquals(Direction.DSW, a.directionFrom(1, 0, 1));
		assertEquals(Direction.USW, a.directionFrom(1, 0, -1));
		assertEquals(Direction.DNE, a.directionFrom(-1, 0, 1));
		assertEquals(Direction.UNE, a.directionFrom(-1, 0, -1));
		assertEquals(Direction.DSE, a.directionFrom(0, 1, 1));
		assertEquals(Direction.USE, a.directionFrom(0, 1, -1));
		assertEquals(Direction.DNW, a.directionFrom(0, -1, 1));
		assertEquals(Direction.UNW, a.directionFrom(0, -1, -1));		
	}

	@Test
	public void testInQuadrant() {
		Point a = new Point(0, 0, 0);
		assertEquals(Direction.NOQUADRANT, a.inQuadrant(-1, 1, -1, 1, -1, 1));
		assertEquals(Direction.NOQUADRANT, a.inQuadrant(.5, 1, .5, 1, .5, 1));
		
	}

	@Test
	public void testInBox() {
		Point a = new Point(0, 0, 0);
		assertTrue(a.inBox(0, 0, 0, 0, 0, 0));
		assertTrue(a.inBox(0, 0, 0, 0, -1, 1));
		assertTrue(a.inBox(-1, 1, 0, 0, 0, 0));
		assertTrue(a.inBox(0, 0, -1, 1, 0, 0));
		assertTrue(a.inBox(-1, 1, -1, 1, 0, 0));
		assertTrue(a.inBox(-1, 1, 0, 0, -1, 1));
		assertTrue(a.inBox(0, 0, -1, 1, -1, 1));
		assertTrue(a.inBox(-1, 1, -1, 1, -1, 1));
	}
	
	@Test
	public void testEquals(){
		Point a = new Point(0, 0, 0);
		Point b = new Point(0, 0, 0);
		Point c = new Point(10, 0, 0);
		assertEquals(a, a);
		assertEquals(a, b);
		assertNotSame(a, c);
	}
	
	@Test
	public void testToString(){
		Point a = new Point(0, 0, 0);
		assertEquals("(0.0,0.0,0.0)", a.toString());
	}

}
