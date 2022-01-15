import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class World {
	
	private Map<Integer, PlayerPiece> playerPieces = new HashMap<Integer, PlayerPiece>();
	private Map<PlayerPiece, Integer> ids = new HashMap<PlayerPiece, Integer>();
	
	private Map<IntegerPair, List<Food>> foodDots = new HashMap<IntegerPair, List<Food>>();
	private List<FloatingPoint> floaters = new ArrayList<FloatingPoint>();
	
	private Game game;
	
	private int width, height;
	
	private double dotTimer;
	private double dotCycle = 0.05;
	private double eatingConstant = 6 / 5.;
	
	private int cellWidth = 100;
	private int maxDotsPerCell = 5;
	
	public World(Game game, int width, int height) {
		this.width = width;
		this.height = height;
		this.game = game;
		
		for (int x = 0; x < 30; x++) {
			for (int y = 0; y < 30; y++) {
				//floaters.add(new FloatingPoint(x * 30, y * 30));
			}
		}
		
		for (int i = 0; i < width * height * 10; i++) {
			addDot();
		}
		System.out.println("Done adding dots");
	}
	
	public void addPlayerPiece(Integer id) {
		PlayerPiece playerPiece = new PlayerPiece(Math.random() * width, Math.random() * height);
		if (ids.containsKey(playerPiece)) {
			ids.remove(playerPiece);
		}
		if (playerPieces.containsKey(id)) {
			playerPieces.remove(id);
		}
		playerPieces.put(id, playerPiece);
		ids.put(playerPiece, id);
	}
	
	public void update(double dt) {
		dotTimer -= dt;
		if (dotTimer < 0) {
			dotTimer = dotCycle;
			addDot();
		}
		
		for (PlayerPiece player : playerPieces.values()) {
			player.update(dt);
		}
		
		/*
		for (FloatingPoint point : floaters) {
			point.pushToCenter();
			for (PlayerPiece piece : playerPieces.values()) {
				point.pushTowards(piece.getX(), piece.getY(), piece.getMass());
			}
			point.update(dt);
		}
		*/
		
		List<Food> toRemove = new ArrayList<Food>();
		List<PlayerPiece> toRemove2 = new ArrayList<PlayerPiece>();

		for (PlayerPiece player : playerPieces.values()) {
			/*for (List<Food> foodList : foodDots.values()) {
				for (Food food : foodList) {
					if (player.collides(food)) {
						toRemove.add(food);
						player.consume(food.getMass());
					}
				}
			}*/
			
			int radius = (int)(player.getRadius());
			int posX = (int)player.getX();
			int posY = (int)player.getY();
			for (int x = posX - radius; x < posX + radius + cellWidth; x += cellWidth) {
				for (int y = posY - radius; y < posY + radius + cellWidth; y += cellWidth) {
					IntegerPair cell = new IntegerPair(x / cellWidth, y / cellWidth);
					if (foodDots.containsKey(cell)) {
						for (Food food : foodDots.get(cell)) {
							if (player.collides(food)) {
								toRemove.add(food);
								food.interact(game, this, player);
							}
						}
					}
				}
			}
			
			for (PlayerPiece food : playerPieces.values()) {
				if (food.equals(player)) {
					continue;
				}
				if (player.collides(food) && food.getRadius() * eatingConstant < player.getRadius()) {
					toRemove2.add(food);
					player.consume(food.getMass());
				}
			}
		}
		for (Food food : toRemove) {
			foodDots.get(new IntegerPair(food.getX() / cellWidth, food.getY() / cellWidth)).remove(food);
		}
		for (PlayerPiece piece : toRemove2) {
			playerPieces.remove(ids.get(piece));
			addPlayerPiece(ids.get(piece));
		}
	}
	
	public void writeClientCopy(ObjectOutputStream stream) throws IOException {
		List<Ellipse> rects = new ArrayList<Ellipse>();
		
		for (FloatingPoint point : floaters) {
			rects.add(new Ellipse((int)(point.getX()), (int)(point.getY()), 2, 2, point.getColor()));
		}
		for (List<Food> foodList : foodDots.values()) {
			for (Food food : foodList) {
				rects.add(food.ellipse);
			}
		}
		for (PlayerPiece player : playerPieces.values()) {
			int d = (int)(player.getRadius() * 2);
			rects.add(new Ellipse((int)(player.getX()), (int)(player.getY()), d, d, player.getColor()));
		}
		stream.writeObject(rects);
		stream.flush();
	}
	
	public PlayerPiece getPlayerPieceById(int id) {
		return playerPieces.get(id);
	}
		
	public void addDot() {
		int x = (int)(Math.random() * width);
		int y = (int)(Math.random() * height);
		IntegerPair cell = new IntegerPair(x / cellWidth, y / cellWidth);
		if (!foodDots.containsKey(cell)) {
			foodDots.put(cell, new ArrayList<Food>());
		}
		if (foodDots.get(cell).size() < maxDotsPerCell) {
			Food food;
			double select = Math.random();
			if (select < 0.7) {
				food = new FoodSpeed(x, y);
			} else if (select < 0.9) {
				food = new FoodSpeed(x, y);
			} else if (select < 0.99) {
				food = new FoodZoom(x, y);
			} else {
				food = new FoodColor(x, y);
			}
			foodDots.get(cell).add(food);
		}
	}
	
	private class IntegerPair {
		private Integer x;
		private Integer y;
		
		public IntegerPair(Integer x, Integer y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(Object o) {
			IntegerPair p = (IntegerPair) o;
			return (x == p.x) && (y == p.y);
		}
		
		public int hashCode() {
			return (x + y) * (x + y + 1) / 2 + y;
		}
	}

}
