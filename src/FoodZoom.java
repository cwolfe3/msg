import java.awt.Color;


public class FoodZoom extends Food {

	public FoodZoom(int x, int y) {
		super(x, y);
		ellipse.color = Color.GREEN;
		while (Math.random() < 0.3) {
			ellipse.color = ellipse.color.darker();
		}
	}

	@Override
	public void interact(Game game, World world, PlayerPiece piece) {
		piece.powerUp(Power.ZOOM, 1);
	}

	
	
}
