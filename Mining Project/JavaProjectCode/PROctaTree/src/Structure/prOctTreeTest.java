package Structure;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

import DataObjects.Point;

public class prOctTreeTest {
	prOctTree<Point> tree = new prOctTree<Point>(-10, 10, -10, 10, -10, 10);
	Point[] test = {
			new Point(5, 5, 5),
			new Point(5, 5, -5),
			new Point(5, -5, 5),
			new Point(5, -5, -5),
			new Point(-5, 5, 5),
			new Point(-5, 5, -5),
			new Point(-5, -5, 5),
			new Point(-5, -5, -5),
	};
	@Test
	public void testInsertFind() {
		for(Point x: test)
			assertTrue(tree.insert(x));
		for(Point x: test)
			assertEquals(x ,tree.find(x));
	}

	@Test
	public void testDelete() {
		for(Point x: test)
			assertTrue(tree.insert(x));
		for(Point x: test){
			assertTrue(tree.delete(x));
			assertNull(tree.find(x));
		}
	}
	
	@Test
	public void testFindOnLine(){
		for(Point x: test)
			assertTrue(tree.insert(x));
		@SuppressWarnings("unused")
		Vector<Point> test = tree.findOnLine(new Point(5,5,6), new Point(5,5,4));
		assertTrue(tree.findOnLine(new Point(0, 0, 0), new Point(1, 1, 1)).isEmpty());				
	}

}
