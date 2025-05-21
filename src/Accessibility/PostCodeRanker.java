package Accessibility;

import Routing.Point;
import Routing.RoutingEngine;
import Routing.Route.Route;
import Routing.Graph.Vertex;
import SQL.DatabaseController;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class PostCodeRanker {
    private static final RoutingEngine routingEngine = new RoutingEngine();

    private static void rankPostCodesInDB() {
        try (Connection connection = DriverManager.getConnection(DatabaseController.getUrl(),
                DatabaseController.getUsername(), DatabaseController.getPassword())) {
            String sql = "SELECT post_code, lat, lon, postcode_general_score FROM transitorgroup23.post_codes_maastricht ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String post_code = resultSet.getString("post_code");
                    double lat = resultSet.getDouble("lat");
                    double lon = resultSet.getDouble("lon");
                    double score = resultSet.getDouble("postcode_general_score");
                    double walkingScore = getWalkingScoreForCoords(lat, lon);

                    score += walkingScore;

                    String updateSql = "UPDATE transitorgroup23.post_codes_maastricht SET accessibility_score = ? WHERE post_code = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                        updateStatement.setDouble(1, score);
                        updateStatement.setString(2, post_code);
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double getWalkingScoreForCoords(double src_lat, double src_lon) {
        double score = 0;

        // first select all the key locations within 1 km
        try (Connection connection = DriverManager.getConnection(DatabaseController.getUrl(),
                DatabaseController.getUsername(), DatabaseController.getPassword())) {
            String sql = "SELECT socioeconomic_score, transitorgroup23.haversineFromLatLon(?, ?, key_locations_scores.latitude, "
                    +
                    "key_locations_scores.longitude) AS distance, longitude, latitude FROM transitorgroup23.key_locations_scores HAVING distance <= 1000;";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setDouble(1, src_lat);
                statement.setDouble(2, src_lon);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int base_score = resultSet.getInt("socioeconomic_score");
                    double dest_lat = resultSet.getDouble("latitude");
                    double dest_lon = resultSet.getDouble("longitude");

                    Point a = new Vertex(src_lat, src_lon);
                    Point b = new Vertex(dest_lat, dest_lon);

                    Route route = routingEngine.routeWalking(LocalDateTime.now(), a, b);

                    Duration duration = route.getDuration();
                    if (!duration.isNegative()) {
                        double minutesTaken = route.getDuration().getSeconds() / 60.0;
                        if (minutesTaken == 0)
                            minutesTaken = 1;
                        score += base_score * (1 / minutesTaken);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return score;
    }

    public static void main(String[] args) {
        rankPostCodesInDB();
    }
}
