import java.awt.Color;
import java.io.Serializable;


public class Ellipse implements Serializable {

	int x;
	int y;
	int width;
	int height;
	Color color;
	
	public Ellipse(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public Ellipse(int x, int y, int width, int height) {
		this(x, y, width, height, Color.BLUE);
	}
	
	public Ellipse(int x, int y) {
		this(x, y, 10, 10);
	}
	
	public Ellipse() {
		this(0, 0);
	}
	
}
