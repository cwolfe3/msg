
public enum Power {
	

	MASS(0),
	SPEED(1),
	ZOOM(2);
	
	private final int id;

	
	private Power(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
