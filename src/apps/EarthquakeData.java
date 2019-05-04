package apps;

import net.datastructures.Coord;
import net.datastructures.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class EarthquakeData {
    public static void readDataIntoMap(Map<Coord<Double,Double>, CSVRecord> m) throws IOException {
        for (CSVRecord rec : CSVFormat.DEFAULT.withHeader().parse(new InputStreamReader(new FileInputStream(new File("earthquakes.csv")))).getRecords()) {
            String slat = rec.get("LATITUDE");
            String slon = rec.get("LONGITUDE");
            if (slat.isEmpty() || slon.isEmpty()) continue; // skip records with missing lat or lon
            Double lat;
            Double lon;
            try { // sometimes it parse whitespace
                lat = Double.parseDouble(slat);
                lon = Double.parseDouble(slon);
            } catch (NumberFormatException e) {
                continue;
            }
            m.put(new Coord<>(lon, lat), rec);
        }
    }

    public static void reportQuake(CSVRecord r) {
        System.out.println("In the year "+r.get("YEAR") + ", " +
                r.get("COUNTRY") + " had a magnitude " + r.get("EQ_PRIMARY") + " quake");
    }
}
