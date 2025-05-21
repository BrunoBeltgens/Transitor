package API;

import java.util.ArrayList;
import java.util.HashMap;

public class PostCodeHashMap {
    private static final HashMap<String, Coordinate> postCodeHashMap = new HashMap<>();

    public static boolean isInvalidPostCode = false;

    private static void buildPostCodeHashMap() {
        ArrayList<Object[]> postCodeArrayList = PostCodeDatabaseBuilder.getPostCodeArrayList();
        postCodeHashMap.clear();

        for (Object[] postCode : postCodeArrayList) {

            String code = (String) postCode[0];
            Coordinate coordinate = new Coordinate(Double.parseDouble((String) postCode[2]),
                    Double.parseDouble((String) postCode[1]));

            postCodeHashMap.put(code, coordinate);
        }
    }

    public static Coordinate getCoordsFromPostCode(String postCode) {

        if (postCode.length() != 6) {
            isInvalidPostCode = true;
            return null;
        }
        else{isInvalidPostCode = false;}

        if (postCodeHashMap.isEmpty()) { // if the hashMap has not already been built
            buildPostCodeHashMap();
        }

        if (postCodeHashMap.containsKey(postCode)) {
            return postCodeHashMap.get(postCode);
        } else {
            if (ApiRequestLimiter.authorize()) {
                try {
                    return ApiController.getCoords(postCode);
                } catch (Exception e) {
                    System.out.println("API ERROR. " + ApiController.getErrorMessage());
                    return null;
                }
            } else { // USER NOT AUTHORIZED TO ACCESS API
                return null;
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(getCoordsFromPostCode("6229EN").getLat());
            System.out.println(getCoordsFromPostCode("6229EN").getLon());
        } catch (NullPointerException e) {
            System.out.println("Postcode not found.");
        }
    }
}