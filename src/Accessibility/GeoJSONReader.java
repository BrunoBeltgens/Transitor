package Accessibility;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJSONReader {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/transitorgroup23";
    private static final String USER = "root";
    private static final String PASS = "";

    public GeoJSONReader(String filePath) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            JsonNode features = rootNode.get("features");

            String insertQuery = "INSERT INTO shop (id, brand, type, longitude, latitude) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);

            if (features != null) {
                for (JsonNode feature : features) {
                    // Extract information from each feature
                    JsonNode properties = feature.get("properties");
                    String id = properties.has("@id") && !properties.get("@id").isNull()
                            ? properties.get("@id").asText()
                            : "NULL";
                    String type = properties.has("shop") && !properties.get("shop").isNull()
                            ? properties.get("shop").asText()
                            : "NULL";
                    String brand = properties.has("name") && !properties.get("name").isNull()
                            ? properties.get("name").asText()
                            : "NULL";

                    // Extract geometry information
                    JsonNode geometry = feature.get("geometry");
                    JsonNode coordinates = geometry.get("coordinates");

                    if (coordinates != null && coordinates.size() >= 2) {
                        double longitude = coordinates.get(0).asDouble();
                        double latitude = coordinates.get(1).asDouble();

                        // Set parameters and execute insert statement
                        pstmt.setString(1, id.equals("NULL") ? null : id);
                        pstmt.setString(2, brand.equals("NULL") ? null : brand);
                        pstmt.setString(3, type.equals("NULL") ? null : type);
                        pstmt.setDouble(4, longitude);
                        pstmt.setDouble(5, latitude);
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GeoJSONReader reader = new GeoJSONReader("C:/Users/Vlad/Desktop/JSON/Phase 3/shop.geojson");
    }
}