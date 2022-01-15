import java.awt.Color;


public class FoodColor extends Food {

	public FoodColor(int x, int y) {
		super(x, y);
		ellipse.color = new Color(160, 32, 240);
		while (Math.random() < 0.3) {
			ellipse.color = ellipse.color.darker();
		}
	}

	@Override
	public void interact(Game game, World world, PlayerPiece piece) {
		piece.randomColor();
	}

}
