package SIRT;
import java.util.LinkedList;
import java.util.Vector;

import Structure.prOctTree;
import DataObjects.*;

public class Sirt{
	private LinkedList<vRecord> records;
	private prOctTree<vPoint> presentStructure;
	private double xMin, xMax, yMin, yMax, zMin, zMax;
	public Sirt(double xLo, double xHi, double yLo, double yHi, double zLo, double zHi){
		records = new LinkedList<vRecord>();
		xMin = xLo;
		xMax = xHi;
		yMin = yLo;
		yMax = yHi;
		zMin = zLo;
		zMax = zHi;
		presentStructure = new prOctTree<vPoint>(xLo, xHi, yLo, yHi, zLo, zHi);
	}
	
	public boolean addVRecord(vRecord x){
		if(!x.getSource().inBox(xMin, xMax, yMin, yMax, zMin, zMax) && !x.getReceiver().inBox(xMin, xMax, yMin, yMax, zMin, zMax))
			return false;
		records.add(x);
		Point a = x.getSource();
		Point b = x.getReceiver();
		presentStructure.insert(new vPoint(a.getX(), a.getY(), a.getZ(), 1));
		presentStructure.insert(new vPoint(b.getX(), b.getY(), b.getZ(), 1));
		return true;
	}
	
	public void next(){
		Vector<vPoint> lineItems = null;
		double expectedToActual = 0;
		//for keeping hold of the acutual vPoint in the structure
		vPoint tempPointer = null;
		//Temporary fix to hold onto what the coordinates should be
		Point tempPoint = null;
		for(vRecord x: records){
			lineItems = presentStructure.findOnLine(x.getSource(), x.getReceiver());
			if(lineItems != null && !lineItems.isEmpty()){
				tempPoint = x.getReceiver();
				tempPointer = new vPoint(tempPoint.getX(), tempPoint.getY(), tempPoint.getZ(), 0);
				expectedToActual = x.getVelocity() / presentStructure.find(tempPointer).getVelocity();
				for(vPoint pointToChange: lineItems){
					pointToChange.addError(expectedToActual);
				}
			}
		}		
	}
}
