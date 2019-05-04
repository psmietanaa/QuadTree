package apps;

import net.datastructures.Coord;
import net.datastructures.SpatialTreeMap;

import java.nio.charset.Charset;
import java.util.Random;

public class RandomDataPointQueries {
    public static void main(String[] args) {
        SpatialTreeMap<Integer, Integer, String> m = new SpatialTreeMap<>();
        // giving the Random a specific seed makes it deterministic (same random numbers every time you run it)
        // try changing the seed to another integer to insert different data
        Random r = new Random(2230);
        final int N = 1000;

        // will randomly select a coordinate to look up after adding all elements
        int pick = r.nextInt(N);
        Coord<Integer, Integer> toTryFinding = null;
        String toTryFindingName = null;

        for (int i = 0; i < N; i++) {
            byte[] array = new byte[5]; //5-character names
            r.nextBytes(array);
            String name = new String(array, Charset.forName("UTF-8"));
            Coord<Integer, Integer> c = new Coord<Integer, Integer>(r.nextInt(), r.nextInt());
            m.put(c, name);

            if (pick == i) {
                toTryFinding = c;
                toTryFindingName = name;
            }
        }

        System.out.println("Did you find " + toTryFinding + " -> "+toTryFindingName  + "\n" + m.get(toTryFinding));

        //m.dump();
        System.out.println("size: " + m.size() + ", expected: " + N);
        System.out.println("actual height: " + m.treeHeight());
        System.out.println("minimum possible height: " + Math.ceil(Math.log(m.size()) / Math.log(4)));
    }
}
