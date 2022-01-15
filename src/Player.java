import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Player {

	Socket socket;
	ObjectOutputStream output;
	ObjectInputStream input;
	
	public Player(Socket socket) throws IOException {
		this.socket = socket;
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}
	
}
