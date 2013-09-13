package DataObjects;

import Compare3D.Compare3D;
import Compare3D.Direction;

public class Point implements Compare3D<Point> {
	private double X,Y,Z;
	
	public Point(double x, double y, double z){
		X = x;
		Y = y;
		Z = z;
	}
	
	@Override
	public double getX() {
		return X;
	}

	@Override
	public double getY() {
		return Y;
	}

	@Override
	public double getZ() {
		return Z;
	}

	@Override
	public Direction directionFrom(double X, double Y, double Z) {
		double deltaX = this.X - X;
		double deltaY = this.Y - Y;
		double deltaZ = this.Z - Z;
		if(deltaZ >= 0){
			if((deltaX > 0 && deltaY >= 0) || (deltaX == 0 && deltaY == 0 && deltaZ != 0))
				return Direction.UNE;
			else if(deltaX <= 0 && deltaY > 0)
				return Direction.UNW;
			else if(deltaX < 0 && deltaY <= 0)
				return Direction.USW;
			else if(deltaX >= 0 && deltaY < 0)
				return Direction.USE;
		}
		else{
			if((deltaX > 0 && deltaY >= 0) || (deltaX == 0 && deltaY == 0))
				return Direction.DNE;
			else if(deltaX <= 0 && deltaY > 0)
				return Direction.DNW;
			else if(deltaX < 0 && deltaY <= 0)
				return Direction.DSW;
			else if(deltaX >= 0 && deltaY < 0)
				return Direction.DSE;
		}
		return Direction.NOQUADRANT;
	}
	
	public void setX(double x){
		X = x;
	}

	@Override
	public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi,
			double zLo, double zHi) {
		if(!inBox(xLo, xHi, yLo, yHi, zLo, zHi))
			return Direction.NOQUADRANT;
		double averageX = (xLo + xHi) / 2;
		double averageY = (yLo + yHi) / 2;
		double averageZ = (zLo + zHi) / 2;
		return directionFrom(averageX, averageY, averageZ);
	}

	@Override
	public boolean inBox(double xLo, double xHi, double yLo, double yHi,
			double zLo, double zHi) {
		boolean inXRegion = (xLo <= X) && (X <= xHi);
		boolean inYRegion = (yLo <= Y) && (Y <= yHi);
		boolean inZRegion = (zLo <= Z) && (Z <= zHi);
		return inXRegion && inYRegion && inZRegion;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object a){
		if(!(a instanceof Compare3D))
			return false;
		return X == ((Compare3D) a).getX() && Y == ((Compare3D) a).getY() && Z == ((Compare3D) a).getZ();
	}
	
	public String toString(){
		return "(" + X + "," + Y + "," + Z + ")";
	}

}
