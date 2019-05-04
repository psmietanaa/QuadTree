package apps;

import net.datastructures.Coord;
import net.datastructures.SpatialTreeMap;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;

public class EarthquakePointQueries {
    public static void main(String[] args) throws IOException {
        //https://www.ngdc.noaa.gov/nndc/struts/form?t=101650&s=1&d=1
        SpatialTreeMap<Double, Double, CSVRecord> quakes = new SpatialTreeMap<>();
        EarthquakeData.readDataIntoMap(quakes);

        //quake.dump();
        System.out.println("size: " + quakes.size());
        System.out.println("actual height: " + quakes.treeHeight());
        System.out.println("minimum possible height: " + Math.ceil(Math.log(quakes.size()) / Math.log(4)));

        // one from near the top of file
        CSVRecord rec = quakes.get(gpsCoord(31.5,35.3));
        EarthquakeData.reportQuake(rec);

        // one from near the bottom of file
        CSVRecord rec2 = quakes.get(gpsCoord(26.374,90.165));
        EarthquakeData.reportQuake(rec2);

        // try some others!
    }

    public static Coord<Double, Double> gpsCoord(double lat, double lon) {
        // X is lon and Y is lat so that directions NW/NE/SW/SE match geography
        return new Coord<>(lon, lat);
    }

}
