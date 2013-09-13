package Compare3D;

public interface Compare3D<T> {

	// Returns the x-coordinate field of the user data object.
	public double getX();
	   
	// Returns the y-coordinate field of the user data object.
	public double getY();
	
	// Returns the z-coordinate field of the user data object.
	public double getZ();
	
	/**
	 * Returns an indicator to the direction of the users data object from the location
	 * (X, Y, Z)
	 * 
	 * Indicators as follows
	 * 		U: getZ() great than or equal to Z
	 * 		D: getZ() less than Z
	 * 		NE:  vector from (X, Y, Z) to user data object has a direction in the 
	 * 			range [0, 90) degrees relative to the positive x-axis, or is on the z-axis.
	 * 		NW:  same as above, but direction is in the range [90, 180) 
	 * 		SW:  same as above, but direction is in the range [180, 270)
	 * 		SE:  same as above, but direction is in the range [270, 360)  
	 * 		NOQUADRANT:  location of user object is equal to (X, Y, Z)
	 */
	public Direction directionFrom(double X, double Y, double Z);
	
	public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi, double zLo, double zHi);
	
	public boolean inBox(double xLo, double xHi, double yLo, double yHi, double zLo, double zHi);
	
	public boolean equals(Object o);
}
