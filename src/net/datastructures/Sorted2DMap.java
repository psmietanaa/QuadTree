package net.datastructures;

/**
 * A map where keys are composed of a "Coord" (short for coordinate) of two ordered dimensions X and Y.
 * X and Y need not be the same type.
 *
 * The total ordering of X is the natural ordering of the X component of keys, by default,
 * or it can be defined by providing an optional Comparator.
 *
 * The total ordering of Y is the natural ordering of the Y component of keys, by default,
 * or it can be defined by providing an optional Comparator.
 *
 * @author Brandon Myers
 */
public interface Sorted2DMap<X,Y,V> extends Map<Coord<X,Y>,V>{

    public Iterable<Entry<Coord<X,Y>,V>> subMap(Coord<X,Y> nwCorner, Coord<X,Y> seCorner, Visitor<Entry<Coord<X,Y>,V>> visitor) throws IllegalArgumentException;
}
