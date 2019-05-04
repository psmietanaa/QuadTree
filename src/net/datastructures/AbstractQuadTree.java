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
import java.util.List;

/**
 * An abstract base class providing some functionality of the QuadTree interface.
 *
 * The following five methods remain abstract, and must be implemented
 * by a concrete subclass: size, root, parent, nw, ne, sw, se.
 *
 * @author Brandon Myers
 */
public abstract class AbstractQuadTree<E> extends AbstractTree<E>
                                             implements QuadTree<E> {

  /**
   * Returns the number of children of Position p.
   *
   * @param p    A valid Position within the tree
   * @return number of children of Position p
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public int numChildren(Position<E> p) {
    int count=0;
    if (nw(p)!=null)
      count++;
    if (sw(p)!=null)
      count++;
    if (ne(p)!=null)
      count++;
    if (se(p)!=null)
      count++;
    return count;
  }

  /**
   * Returns an iterable collection of the Positions representing p's children.
   *
   * @param p    A valid Position within the tree
   * @return iterable collection of the Positions of p's children
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public Iterable<Position<E>> children(Position<E> p) {
    List<Position<E>> snapshot = new ArrayList<>(4);
    if (nw(p) != null)
      snapshot.add(nw(p));
    if (sw(p) != null)
      snapshot.add(sw(p));
    if (ne(p) != null)
      snapshot.add(ne(p));
    if (se(p) != null)
      snapshot.add(se(p));
    return snapshot;
  }

}
