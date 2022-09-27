package application;


public class Node  implements Comparable<Node>{

	private Town currentTown;

	private double distance;	
    // Parent in the path
    public Node sourceTown = null;
    // Evaluation functions
    public double f = Double.MAX_VALUE;
    public double g = Double.MAX_VALUE;
    // Hardcoded heuristic
    public double h; 


	@Override
    public int compareTo(Node n) {
          return Double.compare(this.f, n.f);
    }
    
	public Node(Town currentTown, double distance) {
		super();
		this.currentTown = currentTown;
		this.distance = distance;
	}
	
    
	public Node(Town currentTown) {
		super();
		this.currentTown = currentTown;
		this.distance = 0;
	}



	public Town getCurrentTown() {
		return currentTown;
	}

	public void setCurrentTown(Town currentTown) {
		this.currentTown = currentTown;
	}



	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Node getSourceTown() {
		return sourceTown;
	}

	public void setSourceTown(Node sourceTown) {
		this.sourceTown = sourceTown;
	}

	@Override
	public String toString() {
		return "Table [currentTown=" + currentTown  + ", distance=" + distance + ", sourceTown="
				+ sourceTown + "]";
	}

	public double calculateHeuristic(Node target) {
		h= getDistance(this.currentTown.getCoordinateX(), this.currentTown.getCoordinateY(),
				target.getCurrentTown().getCoordinateX(), target.getCurrentTown().getCoordinateY());
		return h;
	}
	
	public double getDistance(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {

		// Convert latitudes nad longitude to radians

		double dLat = Math.toRadians(toLatitude - fromLatitude);
		double dLon = Math.toRadians(toLongitude - fromLongitude);

		// convert to radians
		fromLatitude = Math.toRadians(fromLatitude);
		toLatitude = Math.toRadians(toLatitude);

		// apply formulae
		double a = Math.pow(Math.sin(dLat / 2), 2)
				+ Math.pow(Math.sin(dLon / 2), 2) * Math.cos(fromLatitude) * Math.cos(toLatitude);
		double rad = 6371;
		double c = 2 * Math.asin(Math.sqrt(a));
		return rad * c;

		// Haversine formula

	}
}
