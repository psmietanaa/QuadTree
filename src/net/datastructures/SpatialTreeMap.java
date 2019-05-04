/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * An implementation of a map using a quad search tree.
 *
 * @author Brandon Myers
 */
public class SpatialTreeMap<X,Y,V> extends AbstractMap<Coord<X,Y>,V> implements Sorted2DMap<X,Y,V>  {

  protected LinkedQuadTree<Entry<Coord<X,Y>,V>> tree = new LinkedQuadTree<>();

  /** Constructs an empty map */
  public SpatialTreeMap() {
    this(new DefaultComparator<X>(), new DefaultComparator<Y>());
  }

  public SpatialTreeMap(Comparator<X> cx, Comparator<Y> cy) {
    this.compX = cx;
    this.compY = cy;
    tree.addRoot(null);       // create a sentinel leaf as root
  }

  // instance variable for an AbstractSortedMap
  /** The comparator defining the ordering of keys in the map. */
  private Comparator<X> compX;
  private Comparator<Y> compY;

  /** Determines whether a key is valid. */
  protected boolean checkKey(Coord<X,Y> key) throws IllegalArgumentException {
    try {
      return (compX.compare(key.getX(),key.getX())==0) && (compY.compare(key.getY(),key.getY())==0);   // see if key can be compared to itself
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Incompatible key");
    }
  }

  /**
   * Returns the number of entries in the map.
   * @return number of entries in the map
   */
  @Override
  public int size() {
      // keep in mind only internal nodes have elements
      return (tree.size() - 1) / 4;
  }

  /** Utility used when inserting a new entry at a leaf of the tree */
  private void expandExternal(Position<Entry<Coord<X,Y>,V>> p, Entry<Coord<X,Y>,V> entry) {
      tree.set(p, entry);
      tree.addNW(p, null);
      tree.addNE(p, null);
      tree.addSW(p, null);
      tree.addSE(p, null);
  }


  /**
   * Returns the position in p's subtree having the given key (or else the terminal leaf).
   * @param key  a target key
   * @param p  a position of the tree serving as root of a subtree
   * @return Position holding key, or last node reached during search
   */
  private Position<Entry<Coord<X,Y>,V>> treeSearch(Position<Entry<Coord<X,Y>,V>> p, Coord<X,Y> key) {
      if (tree.isExternal(p)){
          return p;
      }
      int compareX = compX.compare(p.getElement().getKey().getX(),key.getX());
      int compareY = compY.compare(p.getElement().getKey().getY(),key.getY());
      if (compareX == 0 && compareY == 0){
          return p;
      }
      else if (compareX < 0 && compareY > 0){
          return treeSearch(tree.nw(p), key);
      }
      else if (compareX >= 0 && compareY >= 0){
          return treeSearch(tree.ne(p), key);
      }
      else if (compareX < 0 && compareY <= 0){
          return treeSearch(tree.sw(p), key);
      }
      else {
          return treeSearch(tree.se(p), key);
      }
  }

  /**
   * Returns the value associated with the specified key, or null if no such entry exists.
   * @param key  the key whose associated value is to be returned
   * @return the associated value, or null if no such entry exists
   */
  @Override
  public V get(Coord<X,Y> key) throws IllegalArgumentException {
      checkKey(key);
      Position<Entry<Coord<X,Y>,V>> p = treeSearch(tree.root(), key);
      if (tree.isExternal(p)) return null;
      return p.getElement().getValue();
  }

  /**
   * Associates the given value with the given key. If an entry with
   * the key was already in the map, this replaced the previous value
   * with the new one and returns the old value. Otherwise, a new
   * entry is added and null is returned.
   * @param key    key with which the specified value is to be associated
   * @param value  value to be associated with the specified key
   * @return the previous value associated with the key (or null, if no such entry)
   */
  @Override
  public V put(Coord<X,Y> key, V value) throws IllegalArgumentException {
      checkKey(key);
      Entry<Coord<X,Y>,V> newEntry = new MapEntry<>(key, value);
      Position<Entry<Coord<X,Y>,V>> p = treeSearch(tree.root(), key);
      if (tree.isExternal(p)) {
          expandExternal(p, newEntry);
          return null;
      } else {
          V old = p.getElement().getValue();
          tree.set(p, newEntry);
          return old;
      }
  }

  /**
   * Removes the entry with the specified key, if present, and returns
   * its associated value. Otherwise does nothing and returns null.
   * @param key  the key whose entry is to be removed from the map
   * @return the previous value associated with the removed key, or null if no such entry exists
   */
  @Override
  public V remove(Coord<X,Y> key) throws IllegalArgumentException {
      throw new UnsupportedOperationException("Remove not supported in this Map");
  }

  // Support for iteration
  /**
   * Returns an iterable collection of all key-value entries of the map.
   *
   * @return iterable collection of the map's entries
   */
  @Override
  public Iterable<Entry<Coord<X,Y>,V>> entrySet() {
    ArrayList<Entry<Coord<X,Y>,V>> buffer = new ArrayList<>(size());
    for (Position<Entry<Coord<X,Y>,V>> p : tree.breadthfirst())
      if (tree.isInternal(p)) buffer.add(p.getElement());
    return buffer;
  }

  public Iterable<Entry<Coord<X,Y>,V>> subMapLinear(Coord<X,Y> nwCorner, Coord<X,Y> seCorner, Visitor<Entry<Coord<X,Y>,V>> visitor) throws IllegalArgumentException {
    checkKey(nwCorner);
    checkKey(seCorner);
    ArrayList<Entry<Coord<X,Y>,V>> buffer = new ArrayList<>(size());
    // use one of Tree's traversal methods to iterate through the elements of tree
    // for each one check whether it is within the bounding box
    // For each Position checked, call visitor.visit on it
    for (Position<Entry<Coord<X,Y>,V>> p : tree.breadthfirst())
        if (p.getElement() != null){
            if (compX.compare(p.getElement().getKey().getX(),nwCorner.getX()) >= 0 &&  compX.compare(p.getElement().getKey().getX(),seCorner.getX()) <= 0 &&
                    compY.compare(p.getElement().getKey().getY(),nwCorner.getY()) <= 0 && compY.compare(p.getElement().getKey().getY(),seCorner.getY()) >= 0) {
                visitor.visit(p);
                buffer.add(p.getElement());
            }
        }
    return buffer;
  }

  /**
   * Returns an iterable containing all entries with keys in the range from
   * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
   * @return iterable with keys in desired range
   * @throws IllegalArgumentException if <code>fromKey</code> or <code>toKey</code> is not compatible with the map
   */
  @Override
  public Iterable<Entry<Coord<X,Y>,V>> subMap(Coord<X,Y> nwCorner, Coord<X,Y> seCorner, Visitor<Entry<Coord<X,Y>,V>> visitor) throws IllegalArgumentException {
    checkKey(nwCorner);
    checkKey(seCorner);
    ArrayList<Entry<Coord<X,Y>,V>> buffer = new ArrayList<>(size());
    // only call subMapRecurse on a valid bounding box
    if (compX.compare(nwCorner.getX(), seCorner.getX()) < 0 && compY.compare(nwCorner.getY(), seCorner.getY()) > 0)
      subMapRecurse(nwCorner, seCorner, tree.root(), buffer, visitor);
    return buffer;
  }

  // utility to fill subMap buffer recursively
  private void subMapRecurse(Coord<X,Y> nwCorner, Coord<X,Y> seCorner, Position<Entry<Coord<X,Y>,V>> p,
                             ArrayList<Entry<Coord<X,Y>,V>> buffer, Visitor<Entry<Coord<X,Y>,V>> visitor) {

      if (tree.isInternal(p)) {
          visitor.visit(p);
          int xNW = compX.compare(p.getElement().getKey().getX(), nwCorner.getX());
          int yNW = compY.compare(p.getElement().getKey().getY(), nwCorner.getY());
          int xSE = compX.compare(p.getElement().getKey().getX(), seCorner.getX());
          int ySE = compY.compare(p.getElement().getKey().getY(), seCorner.getY());
          // Case 1 - key is west or north of the nwCorner
          if (xNW < 0 || yNW > 0){
                  subMapRecurse(nwCorner, seCorner, tree.ne(p), buffer, visitor);
                  subMapRecurse(nwCorner, seCorner, tree.nw(p), buffer, visitor);
                  subMapRecurse(nwCorner, seCorner, tree.sw(p), buffer, visitor);
          }
          // Case 2 - key is east or south of the nwCorner.
          else {
              if (xSE <= 0 && ySE >= 0) {
                  buffer.add(p.getElement());
              }
              subMapRecurse(nwCorner, seCorner, tree.ne(p), buffer, visitor);
              subMapRecurse(nwCorner, seCorner, tree.nw(p), buffer, visitor);
              subMapRecurse(nwCorner, seCorner, tree.se(p), buffer, visitor);
              subMapRecurse(nwCorner, seCorner, tree.sw(p), buffer, visitor);
          }
      }
  }


  // remainder of class is for debug purposes only
  /** Prints textual representation of tree structure (for debug purpose only). */
  //protected void dump() {
    public void dump() {
    dumpRecurse(tree.root(), 0, "ROOT");
  }

  /** This exists for debugging only */
  // use a XML tree viewer like https://jsonformatter.org/xml-viewer to see the nesting
  private void dumpRecurse(Position<Entry<Coord<X,Y>,V>> p, int depth, String from) {
    String indent = (depth == 0 ? "" : String.format("%" + (2*depth) + "s", ""));
    if (tree.isExternal(p))
      System.out.println(indent + "<leaf-"+from+"/>");
    else {
      System.out.println(indent + "<"+p.getElement().getKey()+"-"+from+">");
      dumpRecurse(tree.nw(p), depth+1, "NW");
      dumpRecurse(tree.ne(p), depth+1, "NE");
      dumpRecurse(tree.sw(p), depth+1,"SW");
      dumpRecurse(tree.se(p), depth+1, "SE");
      System.out.println(indent + "</"+p.getElement().getKey()+"-"+from+">");
    }
  }

  // for debugging - ought to be protected but we want to use it in our apps
  /*protected*/ public int treeHeight() {
    return tree.height(tree.root());
  }

}
