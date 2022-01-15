import java.awt.event.KeyEvent;


public class PlayerMover {
	
	private double pushSpeed = 300;
	
	public PlayerMover(double pushSpeed) {
		this.pushSpeed = pushSpeed;
	}
	
	public void handleInput(PlayerPiece piece, ClientInput input) {
		boolean[] keys = input.keys;
		if (keys[KeyEvent.VK_W]) {
			piece.push(0, -pushSpeed);
		}
		if (keys[KeyEvent.VK_S]) {
			piece.push(0, pushSpeed);
		}
		if (keys[KeyEvent.VK_A]) {
			piece.push(-pushSpeed, 0);
		}
		if (keys[KeyEvent.VK_D]) {
			piece.push(pushSpeed, 0);
		}
	}
	
}
