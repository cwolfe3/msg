import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class BootstrapClient {
	
	public static void main(String[] args) throws Exception {
		
		String ip = "localhost";
		int portNumber = 27005;
		
		if (args.length > 0) {
			ip = args[0];
			if (args.length > 1) {
				portNumber = Integer.parseInt(args[1]);
			}
		}
		
		Socket sock = new Socket(ip, portNumber);
		Player client = new Player(sock);
		client.output.writeBoolean(false);
		client.output.flush();
		
		FileOutputStream fos = new FileOutputStream("./client.jar");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		long bytesLeft = client.input.readLong();
		byte[] mybytearray = new byte[1024];
		while (bytesLeft > 0) {
			int bytesRead = client.input.read(mybytearray, 0, mybytearray.length);
			bytesLeft -= bytesRead;
			System.out.println(bytesRead);
			bos.write(mybytearray, 0, bytesRead);
		}
		bos.close();
		sock.close();
		
		Process p = new ProcessBuilder("java", "-jar", "./client.jar", ip, String.valueOf(portNumber)).start();
	}
}