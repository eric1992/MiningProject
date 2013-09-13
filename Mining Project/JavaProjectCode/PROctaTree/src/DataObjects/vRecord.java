package DataObjects;


public class vRecord {
	private Point source;
	private Point receiver;
	private double delay;
	private double velocity;
	public vRecord(Point one, Point two, double dT){
		source = one;
		receiver = two;
		delay = dT;
		velocity = Math.sqrt(Math.pow(source.getX() - receiver.getX(), 2) + 
							 Math.pow(source.getY() - receiver.getY(), 2) + 
							 Math.pow(source.getZ() - receiver.getZ(), 2)) / delay;
	}
	
	public Point getSource(){
		return source;
	}
	
	public Point getReceiver(){
		return receiver;
	}
	
	public double getVelocity(){
		return velocity;
	}
}
