import java.awt.Color;


public class FoodMass extends Food {

	public FoodMass(int x, int y) {
		super(x, y);
		ellipse.color = Color.BLUE;
		while (Math.random() < 0.3) {
			ellipse.color = ellipse.color.darker();
		}
	}

	@Override
	public void interact(Game game, World world, PlayerPiece piece) {
		piece.addMass(mass);
		piece.powerUp(Power.MASS, mass);
	}

}
