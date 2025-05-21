package App;

import API.Coordinate;
import SQL.DatabaseController;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.overlay.Marker;

import API.PostCodeHashMap;
import Routing.Point;
import Routing.RoutingConfig;
import Routing.RoutingEngine;
import Routing.Route.Route;
import Routing.Route.Transfer;
import Routing.Graph.Transport.Bus;
import Routing.Graph.Transport.Walking;
import Routing.Graph.algorithms.DistanceCalculator;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class App extends JFrame {
    private final RoutingEngine routingEngine = new RoutingEngine();
    private final List<Layer> routeLayers = new ArrayList<>();
    private final JTextField startPostCode;
    private final JTextField endPostCode;
    private final JLabel realDistanceLabel;
    private final JLabel aerialDistanceLabel;
    private final JLabel walkTimeLabel;
    private final JLabel bikeTimeLabel;
    private final JLabel busTimeLabel;
    private final MapPanel mapPanel;
    private final DataPanel dataPanel;
    private JTable busRouteInfoTable;
    private final JSpinner timeSpinner;
    private final JSpinner doubleSpinner;

    public App() {
        startPostCode = new JTextField(10);
        endPostCode = new JTextField(10);
        realDistanceLabel = new JLabel();
        aerialDistanceLabel = new JLabel();
        walkTimeLabel = new JLabel();
        bikeTimeLabel = new JLabel();
        busTimeLabel = new JLabel();
        mapPanel = new MapPanel();
        dataPanel = new DataPanel();
        busRouteInfoTable = new JTable();
        ImageIcon walkIcon = new ImageIcon("resources/icons/walk.png");
        ImageIcon bikeIcon = new ImageIcon("resources/icons/bike.png");
        ImageIcon busIcon = new ImageIcon("resources/icons/bus.png");
        walkIcon = new ImageIcon(walkIcon.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
        bikeIcon = new ImageIcon(bikeIcon.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
        busIcon = new ImageIcon(busIcon.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
        JLabel walkLabel = new JLabel(walkIcon);
        JLabel bikeLabel = new JLabel(bikeIcon);
        JLabel busLabel = new JLabel(busIcon);
        JLabel startLabel = new JLabel("Start Post Code:");
        JLabel endLabel = new JLabel("End Post Code:");
        JLabel departureTimeLabel = new JLabel("Time of Departure:");
        JLabel walkSpeedLabel = new JLabel("Walk speed (m/s):");
        JLabel startLat = new JLabel();
        JLabel startLong = new JLabel();
        JLabel endLat = new JLabel();
        JLabel endLong = new JLabel();
        JLabel titleLabel = new JLabel("TRANSITOR");
        JButton invisibleButton = new JButton();
        JButton busInvisibleButton = new JButton();
        JButton accessibilityButton = new JButton("Show accessibility");
        JButton timeNowButton = new JButton("Now");
        JPanel routeInfoPanel = new JPanel();

        SpinnerDateModel model = new SpinnerDateModel();
        timeSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(editor);
        timeSpinner.setValue(new Date());

        final double initialValue = 1.4;
        final double minimumValue = 0.1;
        final double maximumValue = 50.0;
        final double stepSize = 0.1;
        SpinnerNumberModel modelWalkSpeed = new SpinnerNumberModel(initialValue, minimumValue, maximumValue, stepSize);
        doubleSpinner = new JSpinner(modelWalkSpeed);
        JSpinner.NumberEditor editorWalkSpeed = new JSpinner.NumberEditor(doubleSpinner, "#0.0");
        doubleSpinner.setEditor(editorWalkSpeed);

        routeInfoPanel.setBounds(900, 700, 500, 200); // Adjust the size and position as needed
        routeInfoPanel.setBackground(java.awt.Color.decode("#D899EB"));
        add(routeInfoPanel);

        accessibilityButton.addActionListener(e -> {
            Layers layers = mapPanel.getLayerManager().getLayers();
            for (Layer routeLayersSection : routeLayers)
                layers.remove(routeLayersSection);

            for (ScoreLabel sl : getScoreLabels()) {
                routeLayers.add(sl);
                layers.add(sl);
            }
        });

        busInvisibleButton.addActionListener(e -> {
            if (startPostCode.getText().isEmpty() || endPostCode.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a start and end post code.");
            } else {
                String start = startPostCode.getText().replaceAll("\\s", "").toUpperCase();
                String end = endPostCode.getText().replaceAll("\\s", "").toUpperCase();

                Coordinate startCoords = PostCodeHashMap.getCoordsFromPostCode(start);
                Coordinate endCoords = PostCodeHashMap.getCoordsFromPostCode(end);

                if (startCoords != null && endCoords != null) {
                    double startCoordsLat = startCoords.getLat();
                    double startCoordsLong = startCoords.getLon();

                    double endCoordsLat = endCoords.getLat();
                    double endCoordsLong = endCoords.getLon();

                    LatLong origin = new LatLong(startCoordsLat, startCoordsLong);
                    LatLong destination = new LatLong(endCoordsLat, endCoordsLong);

                    double walkSpeed = (double) doubleSpinner.getValue();
                    RoutingConfig rc = new RoutingConfig();
                    rc.setWalkSpeed(walkSpeed);
                    routingEngine.setConfig(rc);

                    LocalDateTime localDateTime = getStartDateTime();

                    SwingWorker<Route, Void> worker = new SwingWorker<>() {
                        @Override
                        protected Route doInBackground() {
                            return routingEngine.routePublicTransport(localDateTime, startCoords, endCoords);
                        }

                        @Override
                        protected void done() {
                            try {
                                Route route = get();
                                drawRoute(route, origin, destination);
                                setCoordinateInfo(origin, destination);

                                DecimalFormat df = new DecimalFormat("#.##");

                                double realDistance = (double) route.getDistance() / 1000d;
                                double aerialDistance = new DistanceCalculator().haversine(startCoords, endCoords) / 1000;
                                realDistance = Double.parseDouble(df.format(realDistance));
                                realDistanceLabel.setText("Real Distance: " + realDistance + " km");

                                aerialDistance = Double.parseDouble(df.format(aerialDistance));
                                aerialDistanceLabel.setText("Aerial Distance: " + aerialDistance + " km");

                                walkTimeLabel.setText("Walk Time: N/A");
                                bikeTimeLabel.setText("Bike Time: N/A");
                                busTimeLabel.setText("Bus Time: " + df.format(route.getDuration().toSeconds() / 60d));

                                setBusRouteInfoTable(route);
                            } catch (Exception ex) {
                                System.out.println("Could not update the UI");
                            }
                        }
                    };

                    worker.execute();

                } else {
                    if (PostCodeHashMap.isInvalidPostCode) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid post code. Please enter a valid 6-digit post code.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Post code not found.");
                    }
                }
            }
        });

        invisibleButton.addActionListener(e -> {
            if (startPostCode.getText().isEmpty() || endPostCode.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a start and end post code.");
            } else {
                String start = startPostCode.getText().replaceAll("\\s", "").toUpperCase();
                String end = endPostCode.getText().replaceAll("\\s", "").toUpperCase();

                Coordinate startCoords = PostCodeHashMap.getCoordsFromPostCode(start);
                Coordinate endCoords = PostCodeHashMap.getCoordsFromPostCode(end);

                if (startCoords != null && endCoords != null) {
                    double startCoordsLat = startCoords.getLat();
                    double startCoordsLong = startCoords.getLon();

                    double endCoordsLat = endCoords.getLat();
                    double endCoordsLong = endCoords.getLon();

                    LatLong origin = new LatLong(startCoordsLat, startCoordsLong);
                    LatLong destination = new LatLong(endCoordsLat, endCoordsLong);

                    double walkSpeed = (double) doubleSpinner.getValue();
                    RoutingConfig rc = new RoutingConfig();
                    rc.setWalkSpeed(walkSpeed);
                    routingEngine.setConfig(rc);

                    LocalDateTime localDateTime = getStartDateTime();

                    SwingWorker<Route, Void> worker = new SwingWorker<>() {
                        @Override
                        protected Route doInBackground() {
                            return routingEngine.routeWalking(localDateTime, startCoords, endCoords);
                        }

                        @Override
                        protected void done() {
                            try {
                                Route route = get();
                                drawRoute(route, origin, destination);
                                setCoordinateInfo(origin, destination);

                                DecimalFormat df = new DecimalFormat("#.##");

                                double realDistance = (double) route.getDistance() / 1000d;
                                double aerialDistance = new DistanceCalculator().haversine(startCoords, endCoords) / 1000;
                                realDistance = Double.parseDouble(df.format(realDistance));
                                realDistanceLabel.setText("Real Distance: " + realDistance + " km");

                                aerialDistance = Double.parseDouble(df.format(aerialDistance));
                                aerialDistanceLabel.setText("Aerial Distance: " + aerialDistance + " km");

                                double time = (double) route.getDuration().toSeconds() / 60d;
                                time = Double.parseDouble(df.format(time));
                                walkTimeLabel.setText("Walk Time: " + time + " minutes");

                                double bikeTime = TimeCalculator.calculateBikeTime(realDistance);
                                bikeTime = Double.parseDouble(df.format(bikeTime));
                                bikeTimeLabel.setText("Bike Time: " + bikeTime + " minutes");

                                busTimeLabel.setText("Bus Time: N/A");
                            } catch (Exception ex) {
                                System.out.println("Could not update the UI");
                            }
                        }
                    };

                    worker.execute();

                } else {
                    if (PostCodeHashMap.isInvalidPostCode) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid post code. Please enter a valid 6-digit post code.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Post code not found.");
                    }
                }
            }
        });

        timeNowButton.addActionListener(e -> {
            timeSpinner.setValue(new Date()); // Set the spinner to the current time
        });

        setLayout(null);
        getContentPane().setBackground(java.awt.Color.decode("#D899EB"));

        titleLabel.setFont(new Font("DialogInput", Font.BOLD, 50));
        titleLabel.setForeground(java.awt.Color.decode("#007fff"));
        titleLabel.setBounds(900, -25, 500, 150);
        add(titleLabel);

        walkLabel.setBounds(850, 228, 75, 75);
        add(walkLabel);

        bikeLabel.setBounds(1000, 228, 75, 75);
        add(bikeLabel);

        accessibilityButton.setBounds(950, 380, 200, 30);
        add(accessibilityButton);

        invisibleButton.setBounds(840, 228, 225, 75);
        invisibleButton.setOpaque(false);
        invisibleButton.setContentAreaFilled(false);
        invisibleButton.setBorderPainted(false);
        add(invisibleButton);

        busLabel.setBounds(1150, 228, 75, 75);
        add(busLabel);

        busInvisibleButton.setBounds(1150, 228, 75, 75);
        busInvisibleButton.setOpaque(false);
        busInvisibleButton.setContentAreaFilled(false);
        busInvisibleButton.setBorderPainted(false);
        add(busInvisibleButton);

        startPostCode.setBounds(1000, 100, 150, 20);
        add(startPostCode);

        timeSpinner.setBounds(1000, 150, 75, 28);
        add(timeSpinner);

        doubleSpinner.setBounds(1000, 185, 150, 28);
        add(doubleSpinner);

        walkSpeedLabel.setBounds(890, 190, 150, 20);
        add(walkSpeedLabel);

        timeNowButton.setBounds(1075, 150, 75, 28);
        add(timeNowButton);

        endPostCode.setBounds(1000, 125, 150, 20);
        add(endPostCode);

        startLabel.setBounds(900, 100, 150, 20);
        add(startLabel);

        endLabel.setBounds(906, 125, 150, 20);
        add(endLabel);

        departureTimeLabel.setBounds(889, 154, 150, 20);
        add(departureTimeLabel);

        startLat.setBounds(858, 325, 150, 20);
        add(startLat);

        startLong.setBounds(858, 350, 150, 20);
        add(startLong);

        endLat.setBounds(1030, 325, 150, 20);
        add(endLat);

        endLong.setBounds(1030, 350, 150, 20);
        add(endLong);

        realDistanceLabel.setBounds(975, 345, 150, 20);
        add(realDistanceLabel);

        aerialDistanceLabel.setBounds(975, 325, 150, 20);
        add(aerialDistanceLabel);

        walkTimeLabel.setBounds(810, 300, 150, 20);
        add(walkTimeLabel);

        bikeTimeLabel.setBounds(975, 300, 150, 20);
        add(bikeTimeLabel);

        busTimeLabel.setBounds(1125, 300, 150, 20);
        add(busTimeLabel);

        mapPanel.setBounds(0, 0, 808, 809);
        add(mapPanel);

        dataPanel.setBounds(800, 420, 500, 125);
        dataPanel.setBackground(java.awt.Color.decode("#D899EB"));
        add(dataPanel);

        busRouteInfoTable.setVisible(false);
        add(busRouteInfoTable);

        setTitle("Map Representation");
        setSize(mapPanel.getWidth() + 500, mapPanel.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void drawRoute(Route route, LatLong origin, LatLong destination) {
        Layers layers = mapPanel.getLayerManager().getLayers();
        int tileSize = mapPanel.getTileSize();

        for (Layer routeLayersSection : routeLayers)
            layers.remove(routeLayersSection);

        Paint paintStroke = AwtGraphicFactory.INSTANCE.createPaint();
        ArrayList<LatLong> latLongs = new ArrayList<>();
        paintStroke.setColor(Color.GREEN);
        paintStroke.setStrokeWidth(6);
        latLongs.add(origin);
        latLongs.add(destination);
        Polyline pl = new Polyline(latLongs, paintStroke, tileSize);
        routeLayers.add(pl);
        layers.add(pl);
        
        for (Transfer transfer : route.getTransfers()) {
            System.out.println(transfer.getEndDateTime());

            Color color = Color.BLACK;
            Paint paint = AwtGraphicFactory.INSTANCE.createPaint();
            paint.setStrokeWidth(6);
            if (transfer.getTransport() instanceof Walking) {
                color = Color.RED;
            } else if (transfer.getTransport() instanceof Bus) {
                color = Color.BLUE;
            }
            paint.setColor(color);

            ArrayList<LatLong> waypoints = new ArrayList<>();
            List<Point> points = transfer.getPoints();
            for (Point v : points)
                waypoints.add(new LatLong(v.getLat(), v.getLon()));
            Polyline routeLayersSection = new Polyline(waypoints, paint, tileSize);
            routeLayers.add(routeLayersSection);
            layers.add(routeLayersSection);

            if (!waypoints.isEmpty()) {
                LatLong start = waypoints.get(0);
                LatLong end = waypoints.get(points.size() - 1);

                Layer dotStart = new Dot(start, tileSize);
                Layer dotEnd = new Dot(end, tileSize);

                routeLayers.add(dotStart);
                routeLayers.add(dotEnd);
                layers.add(dotStart);
                layers.add(dotEnd);
            }
        }

        Marker startMarker = new RouteMarker(origin);
        routeLayers.add(startMarker);
        layers.add(startMarker);
        Marker endMarker = new RouteMarker(destination);
        routeLayers.add(endMarker);
        layers.add(endMarker);
    }

    private LocalDateTime getStartDateTime() {
        Date date = (Date) timeSpinner.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        LocalTime localTime = LocalTime.of(hour, minute);
        LocalDate currentDate = LocalDate.now();
        return LocalDateTime.of(currentDate, localTime);
    }

    private void setCoordinateInfo(LatLong origin, LatLong destination) {
        dataPanel.removeRows();
        Object[] latitudeCoords = { "Latitude:", origin.getLatitude(), destination.getLongitude() };
        Object[] longitudeCoords = { "Longitude:", origin.getLongitude(), destination.getLongitude() };
        dataPanel.addRow(latitudeCoords);
        dataPanel.addRow(longitudeCoords);
    }

    public List<ScoreLabel> getScoreLabels() {
        List<ScoreLabel> list = new ArrayList<>();
        int tileSize = mapPanel.getTileSize();
        try (Connection connection = DriverManager.getConnection(DatabaseController.getUrl(), DatabaseController.getUsername(), DatabaseController.getPassword())) {
            String sql = "SELECT accessibility_score, lat, lon FROM transitorgroup23.post_codes_maastricht";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    double score = resultSet.getFloat("accessibility_score");
                    double lat = resultSet.getDouble("lat");
                    double lon = resultSet.getDouble("lon");

                    list.add(new ScoreLabel(new LatLong(lat, lon), score, tileSize));
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve accessibility scores");
        }
        return list;
    }

    public void setBusRouteInfoTable(Route route) {
        remove(busRouteInfoTable);

        List<Transfer> transfers = route.getTransfers();
        int numberOfRows = transfers.size() + 1;

        String[][] data = new String[numberOfRows][5];

        data[0] = new String[] { "Type", "Wait", "To", "Arrival", "Bus Line" };

        boolean previousStepWasWalking = false;

        for (int i = 1; i <= transfers.size(); i++) {

            Transfer transfer = transfers.get(i - 1);
            data[i][0] = transfer.getType();

            if (transfer.getTransport() instanceof Walking)
                data[i][3] = transfer.getEndDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            if (previousStepWasWalking) {
                data[i - 1][1] = "-";
                data[i - 1][2] = DatabaseController
                        .getStopNameFromID(((Bus) transfer.getTransport()).getOriginStopID());
                data[i - 1][4] = "-";
                previousStepWasWalking = false;
            }

            if (transfer.getTransport() instanceof Bus) {
                data[i][1] = formatDuration(((Bus) transfer.getTransport()).getWaitTime());
                data[i][2] = DatabaseController
                        .getStopNameFromID(((Bus) transfer.getTransport()).getDestinationStopID());
                data[i][3] = (((Bus) transfer.getTransport()).getArrivalTime()).toString();
                data[i][4] = DatabaseController.getHeadSignFromTripId(((Bus) transfer.getTransport()).getTripId());
            } else {
                previousStepWasWalking = true;
                if (i == transfers.size()) {
                    data[i][1] = "-";
                    data[i][2] = "Destination";
                    data[i][3] = route.getETA().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    data[i][4] = "-";
                }
            }
        }

        String[] header = { "", "", "", "", "" };

        busRouteInfoTable = new JTable(data, header);
        busRouteInfoTable.getColumnModel().getColumn(0).setMaxWidth(40);
        busRouteInfoTable.getColumnModel().getColumn(1).setMaxWidth(60);
        busRouteInfoTable.getColumnModel().getColumn(2).setMaxWidth(180);
        busRouteInfoTable.getColumnModel().getColumn(3).setMaxWidth(60);
        busRouteInfoTable.getColumnModel().getColumn(4).setMaxWidth(110);
        busRouteInfoTable.setBounds(825, 570, 450, numberOfRows * 16);

        busRouteInfoTable.setVisible(true);
        add(busRouteInfoTable);

        revalidate();
        repaint();
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02d:%02d", hours, minutes);
    }
}
