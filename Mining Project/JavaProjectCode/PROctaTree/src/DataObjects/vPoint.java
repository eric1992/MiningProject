package DataObjects;

import java.util.LinkedList;


import Compare3D.Compare3D;
import Compare3D.Direction;

public class vPoint implements Compare3D<vPoint>{
	private Point coord;
	private double velocity;
	private LinkedList<Double> error;
	public vPoint(double x, double y, double z, double v){
		coord = new Point(x, y, z);
		velocity = v;
		error = new LinkedList<Double>();
	}
	
	public boolean addError(Double e){
		if(e <= 0)
			return false;
		error.add(e);
		return true;			
	}
	
	public boolean condenseVelocity(){
		if(error.isEmpty())
			return false;
		int size = 0;
		double total = 0;
		for(Double x: error){
			size++;
			total += x.doubleValue();
		}
		total /= size;
		velocity *= total;
		error = new LinkedList<Double>();
		return true;
	}
	
	public Point getCoord(){
		return coord;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public LinkedList<Double> getError(){
		return error;
	}

	@Override
	public double getX() {
		return coord.getX();
	}

	@Override
	public double getY() {
		return coord.getY();
	}

	@Override
	public double getZ() {
		return coord.getZ();
	}

	@Override
	public Direction directionFrom(double X, double Y, double Z) {
		return coord.directionFrom(X, Y, Z);
	}

	@Override
	public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi,
			double zLo, double zHi) {
		return coord.inQuadrant(xLo, xHi, yLo, yHi, zLo, zHi);
	}

	@Override
	public boolean inBox(double xLo, double xHi, double yLo, double yHi,
			double zLo, double zHi) {
		return coord.inBox(xLo, xHi, yLo, yHi, zLo, zHi);
	}
	
	/**
	 *
	 */
	public boolean equals(Object t){
		if(t == null)
			return false;
		if(!(t instanceof vPoint))
			return false;
		vPoint handle = (vPoint) t;
		return handle.getCoord().equals(getCoord());
		
	}
}
