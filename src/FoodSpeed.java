import java.awt.Color;


public class FoodSpeed extends Food {

	public FoodSpeed(int x, int y) {
		super(x, y);
		ellipse.color = Color.BLUE;
		while (Math.random() < 0.3) {
			ellipse.color = ellipse.color.darker();
		}
	}

	@Override
	public void interact(Game game, World world, PlayerPiece piece) {
		game.addForce(5);
		piece.addMass(10);
		piece.powerUp(Power.SPEED, 1);
	}
	
}
