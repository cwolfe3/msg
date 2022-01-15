import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Game {

	private World world;
	private Server server;
	private PlayerMover mover;
	
	private boolean running;
	private double pushSpeed = 300;
	
	int nextId = 0;
	
	public Game() {
		world = new World(this, 2000, 2000);
		mover = new PlayerMover(300);
		try {
			server = new Server(this, 27005);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
		System.out.println("Game has started");
		running = true;
		server.startListening();
		
		new Thread() {
			public void run() {
				try {
					gameLoop();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void stop() {
		running = false;
		server.stopListening();
	}
	
	public void gameLoop() throws InterruptedException, IOException, ClassNotFoundException {
		double targetFrameTime = 1000000000. / 60;
		
		while (running) {
			long startTime = System.nanoTime();
			
			server.checkForClients();
			server.cleanClients();
			List<ClientInput> clientInputs = server.receive();
			for (ClientInput clientInput : clientInputs) {
				handleInput(clientInput);
			}
			world.update(targetFrameTime / 1000000000.);
			server.send();
			
			long endTime = System.nanoTime();
			
			double frameTime = endTime - startTime;
			double extraTime = targetFrameTime - frameTime;
			
			if (extraTime > 0) {
				Thread.sleep((long) (extraTime / 1000000));
			} else {
				//System.out.println("Can't reach target frame rate");
			}
		}
		
	}
	
	public Integer addPlayer() {
		world.addPlayerPiece(nextId);
		return nextId++;
	}
	
	public void writeClientCopy(ObjectOutputStream stream) throws IOException {
		world.writeClientCopy(stream);
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void handleInput(ClientInput input) {
		int id = input.id;
		mover.handleInput(getPlayerPieceById(id), input);
	}
	
	public PlayerPiece getPlayerPieceById(int id) {
		return world.getPlayerPieceById(id);
	}
	
	public void addForce(double pushSpeed) {
		//this.pushSpeed += pushSpeed;
	}
	
}
