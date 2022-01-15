import java.awt.Color;
import java.util.HashMap;
import java.util.Map;


public class PlayerPiece {
	
	private double posX;
	private double posY;
	private double velX;
	private double velY;
	
	private double friction;
	private double forceConstant = 300;
	
	private double forceX;
	private double forceY;
	
	private double mass = 100;
	private Color color;
	
	private double growthPower = 0.5;
	private double massPower = 0.3;
	
	
	private double[] powerups = new double[16];
	
	public PlayerPiece() {
		this(0, 0);
		friction = 0.5;
	}
	
	public PlayerPiece(double x, double y) {
		this(x, y, Color.getHSBColor((float)Math.random(), 1, 1));
		friction = 0.5;
	}
	
	public PlayerPiece(double x, double y, Color color) {
		setPosition(x, y);
		this.color = color;
	}
	
	public void push(double forceX, double forceY) {
		this.forceX += forceX * forceConstant;
		this.forceY += forceY * forceConstant;
	}
	
	public void update(double dt) {
		posX += velX * dt;
		posY += velY * dt;
		
		velX += (forceX / Math.pow(mass, massPower)) * dt;
		velY += (forceY / Math.pow(mass, massPower)) * dt;
		
		velX *= friction;
		velY *= friction;
		
		forceX = 0;
		forceY = 0;
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
	
	public double getMass() {
		return mass;
	}
	
	public double getRadius() {
		return Math.pow(mass, growthPower);
	}
	
	public void consume(double mass) {
		this.mass += mass;
	}
	
	public boolean collides(Food food) {
		return Math.pow(posX - food.ellipse.x, 2) + Math.pow(posY - food.ellipse.y, 2) <= Math.pow(getRadius(), 2);
	}
	
	public boolean collides(PlayerPiece food) {
		return Math.pow(getX() - food.getX(), 2) + Math.pow(getY() - food.getY(), 2) <= Math.pow(getRadius() * 2 / 3. + food.getRadius(), 2);
	}
	
	public void setPosition(double x, double y) {
		this.posX = x;
		this.posY = y;
	}
	
	public void addMass(double mass) {
		this.mass += mass;
	}
	
	public void powerUp(Power power, double mass2) {
		powerups[power.getId()] += mass2;
	}
	
	public double getPowerUp(Power power) {
		return powerups[power.getId()];
	}
	
	public void randomColor() {
		color = Color.getHSBColor((float)Math.random(), 1, 1);
	}
		
}
