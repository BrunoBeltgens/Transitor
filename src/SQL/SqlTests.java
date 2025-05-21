package SQL;

import java.sql.*;

public class SqlTests {
    public static void main(String[] args) {
        String url = "jdbc:mysql://transitor23-transitor23.f.aivencloud.com  :12420/";
        String username = "avnadmin";
        String password = "AVNS_M7ZVAsX2XZnMxHOUvBF";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String sql = "SELECT route_id, route_color FROM transitorgroup23.routes WHERE route_color IS NOT NULL";
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String routeId = resultSet.getString("route_id");
                    String routeColor = resultSet.getString("route_color");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
