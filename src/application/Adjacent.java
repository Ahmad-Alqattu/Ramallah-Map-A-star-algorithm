package application;


public class Adjacent{
	private Town town;
	private double distance;

	public Adjacent(Town town, double distance) {
		this.setTown(town);
		this.distance = distance;
		
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}
	
	

}
