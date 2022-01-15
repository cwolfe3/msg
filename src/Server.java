import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server {
	
	private int portNumber;
	private Game game;
	
	private boolean listening;
	
	private ServerSocket serverSocket;
	private List<Player> clients = Collections.synchronizedList(new ArrayList<Player>());
	private List<Player> defunctClients = new ArrayList<Player>();
	private List<Player> newClients = new ArrayList<Player>();
	
	private Map<Integer, Player> clientMap = new HashMap<Integer, Player>();
	private Map<Player, Integer> idMap = new HashMap<Player, Integer>();
	
	public Server(Game game, int portNumber) throws IOException {
		this.portNumber = portNumber;
		this.game = game;
		
		serverSocket = new ServerSocket(portNumber);
	}
	
	public void startListening() {
		listening = true;
		System.out.println("Server: Listening on port " + portNumber);
		
		new Thread() {
			public void run() {
				while (listening) {
					Player newClient;
					try {
						newClient = new Player(serverSocket.accept());
						if (newClient.input.readBoolean()) {
							newClients.add(newClient);
						} else {
							File myFile = new File("C:\\Users\\Colby_000\\Projects\\eclipse\\MSG\\builds\\client.jar");
							
							byte[] mybytearray = new byte[(int) myFile.length()];
							BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
							bis.read(mybytearray, 0, mybytearray.length);
							System.out.println("Bytes sent " + mybytearray.length);
							newClient.output.writeLong(myFile.length());
							newClient.output.write(mybytearray, 0, mybytearray.length);
							newClient.output.flush();
							newClient.socket.close();
							bis.close();

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void stopListening() {
		listening = false;
	}
	
	public void checkForClients() {
		for (Player newClient : newClients) {
			int id = game.addPlayer();
			clientMap.put(id, newClient);
			idMap.put(newClient, id);
			clients.add(newClient);
		}
		newClients.clear();
	}
	
	public void cleanClients() {
		for (Player client : defunctClients) {
			if (clients.contains(client)) {
				clients.remove(client);
				clientMap.remove(client);
				idMap.remove(idMap.get(client));
			}
		}
		defunctClients.clear();
	}
	
	public void send() throws IOException {
		for (Player client : clients) {
			try {
				PlayerPiece piece = game.getPlayerPieceById(idMap.get(client));
				client.output.writeInt((int)piece.getX());
				client.output.writeInt((int)piece.getY());
				client.output.writeInt((int)piece.getPowerUp(Power.MASS));
				client.output.writeInt((int)piece.getPowerUp(Power.SPEED));
				client.output.writeInt((int)piece.getPowerUp(Power.ZOOM));

				game.writeClientCopy(client.output);
			} catch (IOException e) {
				e.printStackTrace();
				client.socket.close();
				defunctClients.add(client);
			}
		}
	}
	
	public List<ClientInput> receive() throws IOException, ClassNotFoundException {
		List<ClientInput> inputs = new ArrayList<ClientInput>();
		for (Player client : clients) {
			try {
				ObjectInputStream input = client.input;
				boolean[] keys = (boolean[]) input.readObject();
				ClientInput clientInput = new ClientInput(idMap.get(client), keys);
				inputs.add(clientInput);
			} catch (IOException e) {
				client.socket.close();
				defunctClients.add(client);
			}
		}
		return inputs;
	}
	
}
