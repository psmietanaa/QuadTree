package apps;

import net.datastructures.*;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class EarthquakeRangeQueries {

    public static void main(String[] args) throws IOException {
        SpatialTreeMap<Double, Double, CSVRecord> quakes = new SpatialTreeMap<>();
        EarthquakeData.readDataIntoMap(quakes);

        // Find quakes in a bounding box that roughly covers the lower 48  US states
        reportQuakesIn(  quakes, gpsCoord( 47.88, -127.73), gpsCoord(21.14, -71.16), "USA", false);

        // Find quakes in a bounding box that roughly covers Italy
        reportQuakesIn(quakes, gpsCoord(46.381044, 3.993234), gpsCoord(35.536696,20.509447), "Italy", false);

        // Find quakes in a bounding box that roughly covers Japan
        reportQuakesIn(quakes, gpsCoord(45.217357,127.434924), gpsCoord(31.590234,145.924457), "Japan", false);

        // Find quakes in a bounding box that roughly covers Iowa
        reportQuakesIn(quakes, gpsCoord(43.360882,-96.585850), gpsCoord(40.316970,-90.084788), "Japan", false);

        // Find quakes in a bounding box that roughly covers Los Angeles
        reportQuakesIn(quakes, gpsCoord(34.617316,-119.269167), gpsCoord(33.360284,-117.017954), "Los Angeles", false);
    }

    public static void reportQuakesIn(SpatialTreeMap<Double ,Double, CSVRecord> quakes,
                                      Coord<Double,Double> nwCorner, Coord<Double,Double> seCorner, String regionName, boolean verbose) {
        System.out.println("~~ Earthquakes in " + regionName + " ~~");

        Set<Coord<Double,Double>> foundLinear = new HashSet<>();
        Set<Coord<Double,Double>> foundTree = new HashSet<>();
        {
            System.out.println("\t with linear search");
            CountingVisitor<Entry<Coord<Double, Double>, CSVRecord>> s = new CountingVisitor<>();
            long start = System.nanoTime();
            Iterable<Entry<Coord<Double, Double>, CSVRecord>> foundWithLinear = quakes.subMapLinear(nwCorner, seCorner, s);
            long end = System.nanoTime();

            int i = 0;
            for (Entry<Coord<Double,Double>,CSVRecord> e : foundWithLinear) {
                foundLinear.add(e.getKey());
                if (verbose) EarthquakeData.reportQuake(e.getValue());
                i++;
            }

            System.out.println("\t\t explored " + s.getCount() + " of " + quakes.size());
            System.out.println("\t\t found " + i);
            System.out.println("\t\t took " + ((double)(end-start)/(1000*1000)) + " ms");
        }

        {
            System.out.println("\t with tree search");
            CountingVisitor<Entry<Coord<Double, Double>, CSVRecord>> s = new CountingVisitor<>();
            long start = System.nanoTime();
            Iterable<Entry<Coord<Double, Double>, CSVRecord>> foundWithTree = quakes.subMap(nwCorner, seCorner, s);
            long end = System.nanoTime();

            int i = 0;
            for (Entry<Coord<Double,Double>,CSVRecord> e : foundWithTree) {
                foundTree.add(e.getKey());
                if (verbose) EarthquakeData.reportQuake(e.getValue());
                i++;
            }
            System.out.println("\t\t explored " + s.getCount() + " of " + quakes.size());
            System.out.println("\t\t found " + i);
            System.out.println("\t\t took " + ((double)(end-start)/(1000*1000)) + " ms");
        }

        if (!foundLinear.equals(foundTree)) {
            System.err.println("subMapLinear and subMap got different results");
        }
    }

    public static Coord<Double, Double> gpsCoord(double lat, double lon) {
        // X is lon and Y is lat so that directions NW/NE/SW/SE match geography
        return new Coord<>(lon, lat);
    }
}
