
public class Map {
	private int[][] map;
	private String locationName;
	private int height;
	private int width;
	
	public Map(int[][] map, String locationName) {
		this.map = map;
		this.locationName = locationName;
	}
	
	public String toString() {
		return String.format("Map Name: %s with Size: %d X %d", this.locationName, this.map.length, this.map[0].length);
	}

}
