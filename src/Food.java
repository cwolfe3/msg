import java.awt.Color;


public abstract class Food {

	Ellipse ellipse;
	double mass = 50;
	
	public Food(int x, int y) {
		ellipse = new Ellipse((int)x, (int)y, 6, 6, Color.getHSBColor((float)Math.random(), 1, 1));
	}
	
	public double getMass() {
		return mass;
	}
	
	public int getX() {
		return ellipse.x;
	}
	
	public int getY() {
		return ellipse.y;
	}
	
	public abstract void interact(Game game, World world, PlayerPiece piece);
	
}
