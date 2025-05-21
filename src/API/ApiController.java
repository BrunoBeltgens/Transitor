package API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiController {
    private static final String baseURL = "https://project12.ashish.nl/get_coordinates?postcode={postcode}";
    private static String errorMessage = "";

    public static Coordinate getCoords(String postCode) {
        try {
            // Adapted from chatGPT

            URL url = new URL(baseURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"postcode\": \"" + postCode + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String json = sb.toString();

                int latitudeStart = json.indexOf("\"latitude\":") + "\"latitude\":".length() + 1;
                int latitudeEnd = json.indexOf(",", latitudeStart);
                double latitude = Double.parseDouble(json.substring(latitudeStart, latitudeEnd).replace("\"", ""));

                int longitudeStart = json.indexOf("\"longitude\":") + "\"longitude\":".length() + 1;
                int longitudeEnd = json.indexOf(",", longitudeStart);
                double longitude = Double.parseDouble(json.substring(longitudeStart, longitudeEnd).replace("\"", ""));

                reader.close();
                conn.disconnect();

                errorMessage = "";

                return new Coordinate(longitude, latitude);

            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                errorMessage = "Post code not found.";
                return null;
            } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                errorMessage = "Failed to fetch coordinates.";
                return null;
            } else if (responseCode == 429) {
                errorMessage = "Too many requests.";
                return null;
            } else {
                errorMessage = "Unknown API error.";
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }
}
