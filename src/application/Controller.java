package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class Controller implements Initializable {

	private Label sourceTown;
	private Label targetTown;
	@FXML
	private Label coordinates;
	@FXML
	private Label showTown;
	@FXML
	private AnchorPane pane = new AnchorPane();
	@FXML
	private TextField TSpace = new TextField();

	@FXML
	private TextField TTime = new TextField();

	@FXML
	private ScrollPane parent = new ScrollPane();
	@FXML
	private ChoiceBox<String> source;
	@FXML
	private ChoiceBox<String> target;
	@FXML
	private ListView<String> pathListView;
	@FXML
	private TextField distanceView;
	private ObservableList<String> villagePath = FXCollections.observableArrayList();
	private ObservableList<String> villageList = FXCollections.observableArrayList();
	private ArrayList<Town> town = new ArrayList<>();
	private ArrayList<Edge> edges = new ArrayList<>();
	private ArrayList<Line> allLines = new ArrayList<>();
	private ArrayList<Line> lines = new ArrayList<>();
	private String sor = "none";
	private int time, space;

	@FXML
	void clear(ActionEvent event) {
		distanceView.setText("");
		source.setValue("none");
		target.setValue("none");

		// reset the total distance Text Field
		pathListView.getItems().clear();
		// reset the total distance, time and space TextFields
		distanceView.setText("0.0");
		TTime.setText("0");
		TSpace.setText("0");
		distanceView.setText("0.0");
		sourceTown.setLayoutX(-20);
		targetTown.setLayoutX(-20);

		for (int i = 0; i < lines.size(); i++) {
			pane.getChildren().remove(lines.get(i));
		}
		if (!(sor == "none"))
			getTown(sor).getCircle().setFill(Color.web("#008CBA"));

		for (int i = 0; i < villagePath.size(); i++) {
			getTown(villagePath.get(i)).getCircle().setFill(Color.web("#008CBA"));

		}

		for (int i = 0; i < allLines.size(); i++) {
			pane.getChildren().remove(allLines.get(i));
		}
		allLines.clear();

		// clear lines Array List
		villagePath.clear();
		lines.clear();
		sor = "none";

		// villagePath.clear();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			getData();

			coordinates.setText("Longitude: " + "   , Latitude: ");

			sourceTown = new Label("");
			targetTown = new Label("");
			sourceTown.setLayoutX(-20);
			targetTown.setLayoutX(-20);
			final double MAX_FONT_SIZE = 22.0;
			sourceTown.setFont(new Font(MAX_FONT_SIZE));
			sourceTown.setTextFill(Color.BLUE);
			targetTown.setFont(new Font(MAX_FONT_SIZE));
			targetTown.setTextFill(Color.BLUE);
			pane.getChildren().addAll(sourceTown, targetTown);

			villageList.add("none");
			source.setValue("none");
			target.setValue("none");
			for (int i = 0; i < town.size(); i++) {
				villageList.add(town.get(i).getName());
			}
			source.setItems(villageList);
			target.setItems(villageList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		initalizeMap();
	}

	/*
	 * fill the map with town by putting each city on its right coordinates. each
	 * city with its name and a red circle that represents it
	 */
	public void initalizeMap() {
		for (int i = 0; i < town.size(); i++) {
			// the circle that represents the city on the map
			Circle point = new Circle(5);

			// a label that hold the city name
			Label cityName = new Label(town.get(i).getName());

			final double MAX_FONT_SIZE = 11;
			cityName.setFont(new Font(MAX_FONT_SIZE));

			// set circle coordinates

			point.setCenterX(((town.get(i).getCoordinateY() - 34.95) * 2425));
			point.setCenterY(854 - ((town.get(i).getCoordinateX() - 31.8) * 2846));

			System.out.print((town.get(i).getCoordinateX() - 29.23) * 222 + "x ");
			System.out.print((town.get(i).getCoordinateY() - 33) * 300 + "y ");

			// set label beside the circle
			cityName.setLayoutX(((town.get(i).getCoordinateY() - 34.95) * 2425) - 15);
			cityName.setLayoutY(854 - ((town.get(i).getCoordinateX() - 31.8) * 2846) - 16);

			point.setFill(Color.web("#008CBA"));

			point.setStroke(Color.BLACK);
			point.setStrokeWidth(.3);

			Tooltip tooltip = new Tooltip(
					town.get(i).toString().replaceAll("_", " ").replaceAll("Y", "X").replaceFirst("X", " Y"));
			tooltip.setAutoFix(true);
			Tooltip.install(point, tooltip);

			// setting city circle to the circle above
			town.get(i).setCircle(point);

			// add the circle and the label to the scene
			pane.getChildren().addAll(town.get(i).getCircle(), cityName);
			String temp = town.get(i).toString();

			/*
			 * Get city name and coordinates, and choose it in the choice box(if it is null)
			 * when clicking on the circle
			 */
			point.setOnMouseClicked(e -> {
				String cityInfo = temp.replaceAll("_", " ").replaceAll("Y", "X").replaceFirst("X", " Y");

				showTown.setText(cityInfo);
				String[] sp = temp.split("[-]");
				if (source.getValue() == "none") {
					source.setValue(sp[0].trim());
				} else if (target.getValue() == "none") {
					target.setValue(sp[0].trim());
				}
			});

			point.setOnMouseEntered(e -> {
				point.setFill(Color.LIGHTGREEN);
			});
			point.setOnMouseExited(e -> {
				point.setFill(Color.web("#008CBA"));
			});
		}
	}

	/*
	 * Run the program by clicking on the (Run) button and get the shortest path
	 */
	@FXML
	void run(ActionEvent event) {
		// if all choice boxes are not null
		pathListView.getItems().clear();
		// reset the total distance, time and space TextFields
		distanceView.setText("0.0");

		if (source.getValue() != "none" && target.getValue() != "none") {
			getTown(source.getValue()).getCircle().setFill(Color.web("#1ACF26"));
			getTown(target.getValue()).getCircle().setFill(Color.web("#DB241A"));

			if (!getTown(source.getValue()).equals(getTown(target.getValue()))) {
				for (int i = 0; i < villagePath.size(); i++) {
					getTown(villagePath.get(i)).getCircle().setFill(Color.web("#008CBA"));

				}
				if (!(sor == "none"))
					getTown(sor).getCircle().setFill(Color.web("#008CBA"));
				printPath(aStar(new Node(getTown(source.getValue())), new Node(getTown(target.getValue()))));

				sor = source.getValue();
				for (int i = 0; i < lines.size(); i++) {
					lines.get(i).setStrokeWidth(2);
					pane.getChildren().add(lines.get(i));

				}
			} else {

				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("heeeey!");
				alert.setContentText("You are already in the city");
				alert.show();
			}

		} else {
			JOptionPane.showMessageDialog(null, "You have to choose two town", "Worning!", JOptionPane.PLAIN_MESSAGE);
		}
		source.setValue("none");
		target.setValue("none");

	}

	/*
	 * method to trace the path between two town
	 */

	public void printPath(Node target) {
		Node n = target;
		DecimalFormat df = new DecimalFormat("###.##");
		ArrayList<String> pathSt = new ArrayList<String>();
		pathSt.add(" (End)");
		if (n == null)
			return;

		distanceView.setText(df.format(target.g));

		while (n.getSourceTown() != null) {
			villagePath.add(n.getCurrentTown().getName());
			n.getCurrentTown().getCircle().setFill(Color.web("#E66325"));

			lines.add(new Line(((n.getCurrentTown().getCoordinateY() - 34.95) * 2425),
					854 - ((n.getCurrentTown().getCoordinateX() - 31.8) * 2846),
					((n.getSourceTown().getCurrentTown().getCoordinateY() - 34.95) * 2425),
					854 - ((n.getSourceTown().getCurrentTown().getCoordinateX() - 31.8) * 2846)));

			pathSt.add(n.getSourceTown().getCurrentTown().getName() + " ---> " + n.getCurrentTown().getName() + " ="
					+ df.format(n.getDistance()) + "km");
			n = n.sourceTown;
		}
		pathSt.add("(start)");

		Collections.reverse(pathSt);
		pathListView.getItems().addAll(pathSt);
		villagePath.add(n.getCurrentTown().getName());
		n.getCurrentTown().getCircle().setFill(Color.web("#1ACF26"));
		target.getCurrentTown().getCircle().setFill(Color.web("#DB241A"));
		TSpace.setText(String.valueOf(space));
		TTime.setText(String.valueOf(time));

	}

	public Node aStar(Node start, Node target) {
		// remove previous lines
		for (int i = 0; i < lines.size(); i++) {
			pane.getChildren().remove(lines.get(i));
		}
		// clear lines Array List
		lines.clear();

		// clear the Observable List that holds all town between the target and source
		villagePath.clear();
		// clear the list view that shows all town between the target and source
		pathListView.getItems().clear();
		// reset the total distance Text Field
		distanceView.setText("0.0");
		PriorityQueue<Node> closedList = new PriorityQueue<>();
		PriorityQueue<Node> openList = new PriorityQueue<>();
		start.g = 0;
		start.f = start.g + start.calculateHeuristic(target);
		openList.add(start);
		space = 1;
		time = 0;
		while (!openList.isEmpty()) {
			Node n = openList.peek();
			space++;
			time++;

			if (n.getCurrentTown().getName().equalsIgnoreCase(target.getCurrentTown().getName())) {

				time++;
				return n;

			}

			ArrayList<Adjacent> adj = n.getCurrentTown().getAdjacent();

			for (Adjacent edge : adj) {
				Node m = new Node(edge.getTown(), edge.getDistance());
				double totalWeight = n.g + edge.getDistance();

				if (!openList.contains(m) && !closedList.contains(m)) {
					time++;
					m.sourceTown = n;
					m.g = totalWeight;
					m.f = m.g + m.calculateHeuristic(target);
					openList.add(m);
				} else {
					if (totalWeight < m.g) {
						time++;
						m.sourceTown = n;
						m.g = totalWeight;
						m.f = m.g + m.calculateHeuristic(target);
						if (closedList.contains(m)) {
							time++;
							closedList.remove(m);
							openList.add(m);

						}
					}
				}
			}

			openList.remove(n);
			closedList.add(n);
		}

		return null;
	}

	// Haversine formula
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

	}

	/*
	 * Get Town coordinates by its name from the town Array List
	 */
	private String getTownCoordinates(String cityName) {
		for (int i = 0; i < villageList.size(); i++) {
			if (town.get(i).getName() == cityName) {
				return town.get(i).getCoordinateX() + "," + town.get(i).getCoordinateY();
			}
		}
		return null;
	}

	/*
	 * Get the Town by its name from the town Array List
	 */
	private Town getTown(String townName) {

		for (int i = 0; i < town.size(); i++) {
			if (town.get(i).getName().equalsIgnoreCase(townName)) {
				return town.get(i);
			}
		}
		return null;
	}

	private void getData() throws FileNotFoundException {

		getTowns();
		getDistances();
		addAdjacents();

	}

	/*
	 * Read Places file and store its content to the town Array List
	 */
	private void getTowns() throws FileNotFoundException {
		File file = new File("Places.csv");
		Scanner input = new Scanner(file);
		while (input.hasNextLine()) {
			String str = input.nextLine();
			String[] spStr = str.trim().split(",");

			town.add(new Town(Double.parseDouble(spStr[1]), Double.parseDouble(spStr[2]), spStr[0], new Circle()));

		}
		input.close();

	}

	/*
	 * Read roads file and store its content to the Edges Array List
	 */
	private void getDistances() throws FileNotFoundException {
		File file = new File("roads.csv");
		Scanner input = new Scanner(file);

		while (input.hasNextLine()) {
			String str = input.nextLine();
			String[] spStr = str.trim().split(",");

			edges.add(new Edge(getTown(spStr[0].trim()), getTown(spStr[1].trim()), Double.parseDouble(spStr[2])));

		}

		input.close();
	}

	/*
	 * Get each city and set all its adjacent town
	 */
	private void addAdjacents() {
		for (int i = 0; i < town.size(); i++) {
			for (int j = 0; j < edges.size(); j++) {
				if (town.get(i).getName().equalsIgnoreCase(edges.get(j).getSourceTown().getName())) {
					Town c = edges.get(j).getTargetTown();
					Adjacent n = new Adjacent(c, edges.get(j).getDistance());
					town.get(i).getAdjacent().add(n);

				} else if (town.get(i).getName().equalsIgnoreCase(edges.get(j).getTargetTown().getName())) {
					Town c = edges.get(j).getSourceTown();
					Adjacent n = new Adjacent(c, edges.get(j).getDistance());
					town.get(i).getAdjacent().add(n);
				}

			}
		}
	}

	/*
	 * Show all the paths on the map
	 */
	@FXML
	void showPaths(ActionEvent event) throws FileNotFoundException {
		try {
			for (int i = 0; i < edges.size(); i++) {

				double startX = ((edges.get(i).getSourceTown().getCoordinateY() - 34.95) * 2425);
				double startY = 854 - ((edges.get(i).getSourceTown().getCoordinateX() - 31.8) * 2846);
				double endX = ((edges.get(i).getTargetTown().getCoordinateY() - 34.95) * 2425);
				double endY = 854 - ((edges.get(i).getTargetTown().getCoordinateX() - 31.8) * 2846);
				allLines.add(new Line(startX, startY, endX, endY));
				pane.getChildren().add(allLines.get(i));
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Worning!");
			alert.setContentText("Paths already shown!");
			alert.show();

		}
	}

	/*
	 * Display mouse coordinates
	 */
	@FXML
	void mouseCoordinates(MouseEvent event) {
		coordinates.setText("Longitude: " + ((event.getX() / 2425) + 34.95) + " * Latitude: "
				+ (((854 - event.getY()) / 2846) + 31.8));
	}

	/*
	 * Exit the Window
	 */
	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void help(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About...");
		alert.setContentText("Ramallah A* Map");
		alert.show();
	}

}
