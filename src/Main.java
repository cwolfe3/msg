import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JPanel implements KeyListener {
	
	private List<Ellipse> shapes;
	private Player client;
	
	private int currentX;
	private int currentY;
	
	private Thread listeningThread;
	private Thread renderThread;
	
	private boolean running;
	private boolean[] keys;
	
	private static Color backgroundColor = Color.BLACK;
	private double zoom = 1;
	
	private PlayerMover mover;
	private PlayerPiece piece;
	
	private int timer = 700;
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		Main main = new Main();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					main.stop();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		String ip = "localhost";
		int portNumber = 27005;
		
		if (args.length > 0) {
			ip = args[0];
			if (args.length > 1) {
				portNumber = Integer.parseInt(args[1]);
			}
		}
		
		main.connect(ip, portNumber);
		main.start();
		main.setBackground(backgroundColor);
		
		JFrame window = new JFrame();
		window.setSize(800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(main);
		window.setVisible(true);
		window.setResizable(false);
		
		window.addKeyListener(main);
		window.setFocusable(true);
	}

	public Main() throws IOException, InterruptedException, ClassNotFoundException {
		shapes = new ArrayList<Ellipse>();
		keys = new boolean[256];
		
		mover = new PlayerMover(300);
		piece = new PlayerPiece();
	}
	
	public void connect(String ip, int port) throws UnknownHostException, IOException {
		client = new Player(new Socket(ip, port));
		client.output.writeBoolean(true);
		client.output.flush();
	}
	
	public void start() {
		running = true;
		

		listeningThread = new Thread() {
			public void run() {
				while (running) {
					try {
						client.output.writeObject(keys.clone());
						currentX = client.input.readInt();
						currentY = client.input.readInt();
						double mass = client.input.readInt();
						double speed = client.input.readInt();
						zoom = client.input.readInt();
						shapes = (List<Ellipse>) client.input.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					//timer--;
					if (timer == 0) {
						try {
							Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome https://www.youtube.com/watch?v=dQw4w9WgXcQ"});
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		listeningThread.start();
		
		renderThread = new Thread() {
			public void run() {
				while (running) {
					long startTime = System.nanoTime();
					repaint();
					long endTime = System.nanoTime();
					handleInput();
					piece.update((endTime - startTime) / 100000000.);
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
		
		renderThread.start();
		
	}
	
	public void handleInput() {
		mover.handleInput(piece, new ClientInput(0, keys));
	}
	
	public void stop() throws IOException, InterruptedException {
		running = false;
		client.socket.close();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		super.paintComponent(g2);
		
		double scale = Math.min(zoom / 100.0 + 1, 3);
		
		g2.translate(piece.getX(), piece.getY());
		g2.scale(1 / scale, 1 / scale);
		g2.translate(-piece.getX(), -piece.getY());
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(-currentX + getWidth() * scale / 2, -currentY + getHeight() * scale / 2);
		
		
		
		//g2.setColor(Color.BLACK);
		//g2.fillRect(0, 0, getWidth(), getHeight());
		for (Ellipse shape : shapes) {
			if (shape.x + shape.width < currentX - getWidth() || shape.x > currentX + getWidth() 
					|| shape.y + shape.height < currentY - getHeight() || shape.y > currentY + getHeight()) {
				continue;
			}
			g2.setColor(shape.color.darker());
			g2.fillOval(shape.x - shape.width / 2, shape.y - shape.height / 2, shape.width, shape.height);
			
			g2.setColor(shape.color);
			g2.fillOval(shape.x - shape.width / 2 + 2, shape.y - shape.height / 2 + 2, shape.width - 4, shape.height - 4);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
