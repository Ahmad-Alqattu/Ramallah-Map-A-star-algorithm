package application;

public class Edge {

	private Town Town1;
	private Town Town2;
	private double distance;
	
	public Edge(Town Town1, Town Town2, double distance) {
		super();
		this.Town1 = Town1;
		this.Town2 = Town2;
		this.distance = distance;
	}

	public Town getSourceTown() {
		return Town1;
	}

	public void setSourceTown(Town sourceTown) {
		this.Town1 = sourceTown;
	}

	public Town getTargetTown() {
		return Town2;
	}

	public void setTargetTown(Town targetTown) {
		this.Town2 = targetTown;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Edge [sourceTown=" + Town1 + ", targetTown=" + Town2 + ", distance=" + distance + "]";
	}
	
}
