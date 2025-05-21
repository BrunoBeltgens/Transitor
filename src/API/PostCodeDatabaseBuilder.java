package API;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PostCodeDatabaseBuilder {
    private static final String databaseFilePath = "resources/PostCodes.csv";

    public static ArrayList<Object[]> getPostCodeArrayList() {
        ArrayList<Object[]> postCodes = new ArrayList<>();
        File file = new File(databaseFilePath);

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                Object[] postCode = scanner.nextLine().split(",");
                postCodes.add(postCode);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return postCodes;
    }
}