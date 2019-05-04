package net.datastructures;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SpatialTreeMapTest {

    private SpatialTreeMap<Integer, Integer, Integer> small() {
        SpatialTreeMap<Integer, Integer, Integer> m = new SpatialTreeMap<>();
        m.put(new Coord<>(0, 0), 0);
        m.put(new Coord<>(-3, 4), 1);
        m.put(new Coord<>(3, 2), 2);
        m.put(new Coord<>(-5, -6), 3);
        m.put(new Coord<>(6, -5), 4);
        m.put(new Coord<>(10, 12), 5);
        m.put(new Coord<>(7, 7), 6);
        //m.dump();
        return m;
    }

    private SpatialTreeMap<Integer, Integer, Integer> medium() {
        SpatialTreeMap<Integer, Integer, Integer> m = new SpatialTreeMap<>();
        int k = 0;
        for (int i = -20; i < 20; i += 8) {
            for (int j = -20; j < 20; j += 8) {
                //System.out.println(new Coord<>(i,j));
                m.put(new Coord<>(i, j), k);
                k++;
            }
        }
        //m.dump();
        return m;
    }

    @Test
    public void testSmallPut() {
        SpatialTreeMap<Integer, Integer, Integer> m = small();
        assertEquals(7, m.size());
        assertEquals(4, m.treeHeight());
        // added
        m.put(new Coord<>(7, 7), 1001);
        assertEquals((int)1001, (int)m.get(new Coord<>(7, 7)));
    }


    @Test
    public void testMediumPut() {
        SpatialTreeMap<Integer, Integer, Integer> m = medium();
        assertEquals(25, m.size());
        assertEquals(9, m.treeHeight());
        // added
        m.put(new Coord<>(7, 7), 1001);
        assertEquals((int)1001, (int)m.get(new Coord<>(7, 7)));
        m.put(new Coord<>(1000,1000),1000);
        assertEquals((int)1000, (int)m.get(new Coord<>(1000, 1000)));

    }

    @Test
    public void testSmallGet() {
        SpatialTreeMap<Integer, Integer, Integer> m = small();
        assertEquals(null, m.get(new Coord<>(0,2)));
        assertEquals(null, m.get(new Coord<>(-6,-5)));
        assertEquals((int)3, (int)m.get(new Coord<>(-5,-6)));
        assertEquals((int)6, (int)m.get(new Coord<>(7,7)));
        assertEquals((int)0, (int)m.get(new Coord<>(0,0)));
    }

    @Test
    public void testMediumGet() {
        SpatialTreeMap<Integer, Integer, Integer> m = medium();
        assertEquals(null, m.get(new Coord<>(0,2)));
        assertEquals(null, m.get(new Coord<>(0,0)));
        assertEquals((int)1, (int)m.get(new Coord<>(-20,-12)));
        assertEquals((int)10, (int)m.get(new Coord<>(-4, -20)));
        assertEquals((int)20, (int)m.get(new Coord<>(12,-20)));
    }

    @Test
    public void myTest1() {
        SpatialTreeMap<Integer, Integer, Integer> m = medium();
        assertEquals(null, m.get(new Coord<>(0,0)));
        assertEquals(null, m.get(new Coord<>(0,0)));
        assertEquals((int)1, (int)m.get(new Coord<>(-20,-12)));
        assertEquals((int)10, (int)m.get(new Coord<>(-4, -20)));
        // added
        assertEquals(null, m.put(new Coord<>(1,3),420));
        m.put(new Coord<>(1,3),420);
        assertEquals((int)420, (int)m.put(new Coord<>(1,3),420));
        m.put(new Coord<>(7, 7), 1001);
        assertEquals((int)1001, (int)m.get(new Coord<>(7, 7)));
        m.put(new Coord<>(1000,1000),1000);
        assertEquals((int)1000, (int)m.get(new Coord<>(1000, 1000)));

    }

    @Test
    public void smallSubMapTest() {
        SpatialTreeMap<Integer, Integer, Integer> m = small();

        Set<Coord<Integer, Integer>> found = new HashSet<>();
        Visitor<Entry<Coord<Integer, Integer>, Integer>> s = new CountingVisitor<>();
        //Visitor<Entry<Coord<Integer, Integer>, Integer>> s = new PrintVisitor<>();

        for (Entry<Coord<Integer, Integer>, Integer> e : m.subMap(new Coord<Integer,Integer>(-4, 4), new Coord<Integer,Integer>(4, -4), s)) {
            found.add(e.getKey());
            //System.out.println(e);
        }
        assertEquals(3, found.size());
        assertTrue(found.contains(new Coord<>(0, 0)));
        assertTrue(found.contains(new Coord<>(-3, 4)));
        assertTrue(found.contains(new Coord<>(3, 2)));
    }

    @Test
    public void mediumSubMapTest() {
        SpatialTreeMap<Integer, Integer, Integer> m = medium();

        Set<Coord<Integer, Integer>> found = new HashSet<>();
        Visitor<Entry<Coord<Integer, Integer>, Integer>> s = new CountingVisitor<>();
        //Visitor<Entry<Coord<Integer, Integer>, Integer>> s = new PrintVisitor<>();

        for (Entry<Coord<Integer, Integer>, Integer> e : m.subMap(new Coord<Integer,Integer>(-10, 10), new Coord<Integer,Integer>(10, -10), s)) {
            found.add(e.getKey());
            //System.out.println(e);
        }
        assertEquals(4, found.size());
        assertTrue(found.contains(new Coord<>(-4, -4)));
        assertTrue(found.contains(new Coord<>(4, -4)));
        assertTrue(found.contains(new Coord<>(-4, 4)));
        assertTrue(found.contains(new Coord<>(4, 4)));
    }

    @Test
    public void mediumSubMapLinearTest() {
        SpatialTreeMap<Integer, Integer, Integer> m = medium();

        Set<Coord<Integer, Integer>> found = new HashSet<>();
        Visitor<Entry<Coord<Integer, Integer>, Integer>> s = new CountingVisitor<>();
        //Visitor<Entry<Coord<Integer, Integer>, Integer>> s = new PrintVisitor<>();

        for (Entry<Coord<Integer, Integer>, Integer> e : m.subMapLinear(new Coord<>(-10, 10), new Coord<>(10, -10), s)) {
            found.add(e.getKey());
            //System.out.println(e);
        }
        assertEquals(4, found.size());
        assertTrue(found.contains(new Coord<>(-4, -4)));
        assertTrue(found.contains(new Coord<>(4, -4)));
        assertTrue(found.contains(new Coord<>(-4, 4)));
        assertTrue(found.contains(new Coord<>(4, 4)));
    }


}