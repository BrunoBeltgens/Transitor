package SQL;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointInPolygon {

    public static void main(String[] args) {
        double[][] polygon6211 = {
                {50.84213529260483, 5.698658709178079},
                {50.86348464699843, 5.6974999950332155},
                {50.855059863533164, 5.685011630764588},
                {50.8495328123572, 5.677372700139524},
                {50.84305665932185, 5.683252101749313},
                {50.84213529260483, 5.698658709178079}
        };

        double[][] polygon6212 = {
            {50.812206820761695, 5.692220075233539},
            {50.84197143394821, 5.698657376322076},
            {50.84321798901599, 5.682778700303685},
            {50.8229980080513, 5.669818267445431},
            {50.81605722898475, 5.6729940026491095},
            {50.812206820761695, 5.692220075233539}
        };

        double[][] polygon6213 = {
            {50.81721147067022, 5.672348504807189},
            {50.82344721980313, 5.673292642300174},
            {50.822688129642884, 5.66728449461754},
            {50.842745651492336, 5.683249001317113},
            {50.84453414695919, 5.6805882502005165},
            {50.83407318626353, 5.6499466970190815},
            {50.81721147067022, 5.672348504807189} 
        };

        double[][] polygon6214 = {
            {50.84066806778709, 5.6666929650598385},
            {50.84480065645467, 5.681198350179341},
            {50.84943414022912, 5.6774218002074},
            {50.84874321131917, 5.670169107647648},
            {50.84625036312711, 5.670941583778273},
            {50.84476000982398, 5.668559782375514},
            {50.84516795837956, 5.666392719520147},
            {50.84318064331395, 5.66344418133781},
            {50.84066806778709, 5.6666929650598385}
        };

        double[][] polygon6215 = {
            {50.83331003015006, 5.648294086007619},
            {50.84068184888342, 5.666661851780243},
            {50.84358147314239, 5.662584894424171},
            {50.84450281134885, 5.654430979712023},
            {50.847239620486945, 5.65198480529838},
            {50.84764799192595, 5.643524156146457},
            {50.84500903032504, 5.642077398069954},
            {50.84290790416133, 5.641225418313791},
            {50.83331003015006, 5.648294086007619}
        };

        double[][] polygon6216 = {
            {50.84196563709317, 5.657744362661557},
            {50.84467549725599, 5.668644859171479},
            {50.848902564839356, 5.672163917099879},
            {50.85405039906682, 5.6637525103441915},
            {50.858899681040484, 5.643625215607366},
            {50.84706004400806, 5.642981485498512},
            {50.847331007517994, 5.651907876341283},
            {50.84196563709317, 5.657744362661557}
        };

        double[][] polygon6217 = {
            {50.849272626187776, 5.677337352964078},
            {50.8543933274133, 5.683817569393206},
            {50.8636037556136, 5.672917072883284},
            {50.856885740456526, 5.655235952560102},
            {50.84878491103675, 5.672187512093251},
            {50.849272626187776, 5.677337352964078}
        };

        double[][] polygon6218 = {
            {50.85674161453375, 5.655066936822188},
            {50.86113011858934, 5.661160915186002},
            {50.863351302715586, 5.672061411695925},
            {50.88079204573871, 5.6732630412324525},
            {50.86421807757332, 5.6418490119203915},
            {50.85917972333073, 5.642106503963933},
            {50.85674161453375, 5.655066936822188}
        };

        double[][] polygon6219 = {
            {50.85460679274439, 5.6841770856515375},
            {50.85818280571776, 5.68984191060945},
            {50.85959146281392, 5.689670249247088},
            {50.86235447428855, 5.6979958253215965},
            {50.880542617134495, 5.678699125147063},
            {50.88113830682682, 5.6731201308703305},
            {50.863101780760545, 5.672175993377346},
            {50.85460679274439, 5.6841770856515375}
        };

        double[][] polygon6221 = {
            {50.84167341046238, 5.700902046232498},
            {50.84167341046238, 5.714806616583737},
            {50.846957525094666, 5.706953109255723},
            {50.857090511374, 5.704807342226211},
            {50.8601245602333, 5.707639754705166},
            {50.86015164906613, 5.698584617840624},
            {50.84167341046238, 5.700902046232498}
        };

        double[][] polygon6222 = {
            {50.85874637817828, 5.713367703945668},
            {50.88211089562466, 5.735977341044033},
            {50.89781201679037, 5.718028528966513},
            {50.860395848130345, 5.698195587444391},
            {50.85874637817828, 5.713367703945668}
        };

        double[][] polygon6223 = {
            {50.86773237380474, 5.698843823206175},
            {50.899626903895175, 5.719529017370673},
            {50.90763771687299, 5.716954096935258},
            {50.90834130351076, 5.697470532307287},
            {50.87926890921839, 5.681763517651257},
            {50.86773237380474, 5.698843823206175}
        };

        double[][] polygon6224 = {
            {50.842444433135256, 5.7171368978654415},
            {50.84507297280955, 5.715377368901241},
            {50.84442262626498, 5.719840564322627},
            {50.85160303345808, 5.71851018876433},
            {50.85924285672459, 5.714304485386485},
            {50.860190974436904, 5.707480946232637},
            {50.856262932696104, 5.704090634326007},
            {50.845885893241864, 5.707738438276179},
            {50.84122495722711, 5.716021099010095},
            {50.842444433135256, 5.7171368978654415}
        };

        double[][] polygon6225 = {
            {50.853040304008665, 5.737958224061481},
            {50.86972657912324, 5.764651565908613},
            {50.874005538120514, 5.731864245697665},
            {50.857970961354944, 5.713839802649763},
            {50.855722429530495, 5.716371807744587},
            {50.8553160567904, 5.727315219668047},
            {50.853040304008665, 5.737958224061481}
        };

        double[][] polygon6226 = {
            {50.84413272898669, 5.719827297460279},
            {50.841937728830814, 5.7548033000413295},
            {50.85754437516088, 5.7551895381066425},
            {50.85578346984256, 5.71635115487247},
            {50.84426821943959, 5.719913128141459},
            {50.84413272898669, 5.719827297460279}
        };

        double[][] polygon6227 = {
            {50.83158271150473, 5.728099853035339},
            {50.835377315833256, 5.726383239411729},
            {50.83583806821106, 5.732563048456725},
            {50.83028163328674, 5.746467618807964},
            {50.84090604414959, 5.748098401750394},
            {50.84513345325699, 5.715311081539445},
            {50.83117612845732, 5.717928917315451},
            {50.83158271150473, 5.728099853035339}
        };

        double[][] polygon6228 = {
            {50.81508085542792, 5.7229172256413365},
            {50.8214795570548, 5.744889880023543},
            {50.82988329903734, 5.746949816371876},
            {50.83592763604345, 5.732573177274142},
            {50.835466884549916, 5.726264622207377},
            {50.83159097133358, 5.728024151171577},
            {50.831067134573935, 5.717724081508052},
            {50.81512226824129, 5.722736974430181},
            {50.81508085542792, 5.7229172256413365}
        };

        double[][] polygon6229 = {
            {50.80398724844708, 5.717012234321493},
            {50.81445495151405, 5.717269726365034},
            {50.81505148569942, 5.723020382004126},
            {50.819281236815655, 5.723363704728849},
            {50.821016408503006, 5.719672985438088},
            {50.841616643549436, 5.716754742277951},
            {50.841399846315625, 5.701133558303101},
            {50.81076711650545, 5.6952112413016485},
            {50.8050178518051, 5.708600827565804},
            {50.80398724844708, 5.717012234321493}
        };

        try {
            Connection conn = DriverManager.getConnection(DatabaseController.getUrl(), DatabaseController.getUsername(), DatabaseController.getPassword());
            
            // Process each table
            List<String> tableNames = List.of("tourism", "amenity", "shop");
            Map<String, Double> statuses = processTablesForPolygon(conn, polygon6229, tableNames);

            double tourismStatus = statuses.get("tourism");
            double amenityStatus = statuses.get("amenity");
            double shopStatus = statuses.get("shop");
            BigDecimal averageBD = new BigDecimal((tourismStatus + amenityStatus + shopStatus) / 3.0);
            averageBD = averageBD.setScale(2, RoundingMode.HALF_UP);
            double average = averageBD.doubleValue();

            System.out.printf("Average socio-economic score: %.2f%n", average);

            String insertQuery = "UPDATE transitorgroup23.post_codes_maastricht SET postcode_general_score = ? WHERE post_code LIKE '6229%'";

            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setDouble(1, average);
                pstmt.executeUpdate();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Double> processTablesForPolygon(Connection conn, double[][] polygon, List<String> tableNames) throws Exception {
        Map<String, Double> statusMap = new HashMap<>();
        for (String tableName : tableNames) {
            double status = processTable(conn, polygon, tableName);
            statusMap.put(tableName, status);
        }
        return statusMap;
    }

    public static double processTable(Connection conn, double[][] polygon, String tableName) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT latitude, longitude, socioeconomic_score FROM transitorgroup23." + tableName);

        int count = 0;
        double socioEconomicScoreSum = 0;

        while (rs.next()) {
            double latitude = rs.getDouble("latitude");
            double longitude = rs.getDouble("longitude");
            double socioEconomicScore = rs.getDouble("socioeconomic_score");
            double[] point = {latitude, longitude};

            if (isPointInPolygon(polygon, point) && socioEconomicScore != 0.0) {
                count++;
                socioEconomicScoreSum += socioEconomicScore;
            }
        }

        System.out.println("Table: " + tableName);
        System.out.println("Number of places inside the polygon: " + count);
        System.out.println("Sum of socio-economic scores: " + socioEconomicScoreSum);
        if (count > 0){
            System.out.printf("Average socio-economic score: %.2f%n", socioEconomicScoreSum / (double) count);
            return socioEconomicScoreSum / (double) count;}
            return -1;
    }

    public static boolean isPointInPolygon(double[][] polygon, double[] point) {
        boolean result = false;
        for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            if ((polygon[i][0] > point[0]) != (polygon[j][0] > point[0]) &&
                (point[1] < (polygon[j][1] - polygon[i][1]) * (point[0] - polygon[i][0]) / (polygon[j][0]-polygon[i][0]) + polygon[i][1])) {
                result = !result;
            }
        }
        return result;
    }
}