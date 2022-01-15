import java.awt.Color;


public class FloatingPoint {
	
	private double goalX;
	private double goalY;
	
	private double posX;
	private double posY;
	private double velX;
	private double velY;
	
	private double friction = 0.5;
	private double forceConstant = 100;
	
	private double forceX;
	private double forceY;
	
	private double maxForce = 0.01;
	private double maxSpeed = 50;
	
	private double mass = 1;
	private Color color;

	public FloatingPoint(double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
		
		goalX = posX;
		goalY = posY;
		color = Color.WHITE;
	}
	
	public void push(double forceX, double forceY) {
		this.forceX += forceX * forceConstant;
		this.forceY += forceY * forceConstant;
		
		forceX = clamp(forceX, -maxForce, maxForce);
		forceY = clamp(forceY, -maxForce, maxForce);
	}
	
	public void update(double dt) {
		posX += velX * dt;
		posY += velY * dt;
		
		velX += (forceX / mass) * dt;
		velY += (forceY / mass) * dt;
		
		velX = clamp(velX, -maxSpeed, maxSpeed);
		velY = clamp(velY, -maxSpeed, maxSpeed);
		
		velX *= friction;
		velY *= friction;
		
		forceX = 0;
		forceY = 0;
	}
	
	public void pushToCenter() {
		double dispX = posX - goalX;
		double dispY = posY - goalY;
		double magForce = -0.000001 * (dispX * dispX + dispY * dispY) * (dispX * dispX + dispY * dispY);
		push(magForce * dispX, magForce * dispY);
	}
	
	public void pushTowards(double goalX, double goalY, double mass) {
		double dispX = posX - goalX;
		double dispY = posY - goalY;
		double magForce = -5 * mass / (dispX * dispX + dispY * dispY);
		push(magForce * dispX, magForce * dispY);
	}
	
	public double getX() {
		return posX;
	}
	
	public double getY() {
		return posY;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double clamp(double x, double l, double h) {
		if (x < l) {
			return l;
		}
		if (x > h) {
			return h;
		}
		return x;
		
	}
	
}
