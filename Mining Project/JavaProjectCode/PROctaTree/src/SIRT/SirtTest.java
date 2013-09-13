package SIRT;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import DataObjects.Point;
import DataObjects.vRecord;

public class SirtTest {

	@Test
	public void testSirt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddVRecord() {
		Sirt testAlg = new Sirt(0, 10, 0, 10, 0, 10);
		vRecord recs[] = {
							new vRecord(new Point(0, 0, 0), new Point(1, 0, 0), 2),
							new vRecord(new Point(0, 0, 0), new Point(2, 0, 0), 4), 
							new vRecord(new Point(0, 0, 0), new Point(3, 0, 0), 6)
						};
		for(vRecord x : recs){
			testAlg.addVRecord(x);
		}
	}

	@Test
	public void testNext() {
		Sirt testRegion = new Sirt(0, 1, 0, 1, 0, 1);
		vRecord recs[] = {
							new vRecord(new Point(0, 0, 0), new Point(1, 1, 1), 1),
							new vRecord(new Point(0, 1, 0), new Point(1, 0, 1), 1), 
							new vRecord(new Point(1, 1, 0), new Point(0, 0, 1), 1),
							new vRecord(new Point(1, 0, 0), new Point(0, 1, 1), 1),
							new vRecord(new Point(0, 0, 0), new Point(.5, .5, .5), .5)
						};
		for(vRecord x : recs){
			testRegion.addVRecord(x);
		}
		for(int i = 0; i < 10; i++){
			testRegion.next();
		}
	}
}
