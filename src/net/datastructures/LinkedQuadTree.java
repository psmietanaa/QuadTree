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

/**
 * Concrete implementation of a quad tree using a node-based, linked structure.
 *
 * @author Brandon Myers
 */

public class LinkedQuadTree<E> extends AbstractQuadTree<E> {

  //---------------- nested Node class ----------------
  /** Nested static class for a quad tree node. */
  protected static class Node<E> implements Position<E> {
    private E element;          // an element stored at this node
    private Node<E> parent;     // a reference to the parent node (if any)
    private Node<E> nw;
    private Node<E> ne;
    private Node<E> sw;
    private Node<E> se;

    /**
     * Constructs a node with the given element and neighbors.
     *
     * @param e  the element to be stored
     * @param above       reference to a parent node
     * @param nwChild reference to a nw child node
     * @param neChild reference to a ne child node
     * @param swChild reference to a nw child node
     * @param seChild reference to a ne child node
     */
    public Node(E e, Node<E> above, Node<E> nwChild, Node<E> neChild, Node<E> swChild, Node<E> seChild) {
      element = e;
      parent = above;
      nw = nwChild;
      ne = neChild;
      sw = swChild;
      se = seChild;
    }


    // accessor methods
    @Override
    public E getElement() { return element; }
    public Node<E> getParent() { return parent; }
    public Node<E> getNW() { return nw; }
    public Node<E> getNE() { return ne; }
    public Node<E> getSW() { return sw; }
    public Node<E> getSE() { return se; }

    // update methods
    public void setElement(E e) { element = e; }
    public void setParent(Node<E> parentNode) { parent = parentNode; }
    public void setNW (Node<E> nwChild) { nw = nwChild; }
    public void setNE (Node<E> neChild) { ne = neChild; }
    public void setSW (Node<E> swChild) { sw = swChild; }
    public void setSE (Node<E> seChild) { se = seChild; }


  } //----------- end of nested Node class -----------

  /** Factory function to create a new node storing element e. */
  protected Node<E> createNode(E e, Node<E> parent,
                                  Node<E> nw, Node<E> ne, Node<E> sw, Node<E> se) {
    return new Node<E>(e, parent, nw, ne, sw, se);
  }

  // LinkedQuadTree instance variables
  /** The root of the quad tree */
  protected Node<E> root = null;     // root of the tree

  /** The number of nodes in the binary tree */
  private int size = 0;              // number of nodes in the tree

  // constructor
  /** Construts an empty binary tree. */
  public LinkedQuadTree() { }      // constructs an empty quad tree

  // nonpublic utility
  /**
   * Verifies that a Position belongs to the appropriate class, and is
   * not one that has been previously removed. Note that our current
   * implementation does not actually verify that the position belongs
   * to this particular list instance.
   *
   * @param p   a Position (that should belong to this tree)
   * @return    the underlying Node instance for the position
   * @throws IllegalArgumentException if an invalid position is detected
   */
  protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
    if (!(p instanceof Node))
      throw new IllegalArgumentException("Not valid position type: " + p);
    Node<E> node = (Node<E>) p;       // safe cast
    if (node.getParent() == node)     // our convention for defunct node
      throw new IllegalArgumentException("p is no longer in the tree");
    return node;
  }

  // accessor methods (not already implemented in AbstractQuadTree)
  /**
   * Returns the number of nodes in the tree.
   * @return number of nodes in the tree
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns the root Position of the tree (or null if tree is empty).
   * @return root Position of the tree (or null if tree is empty)
   */
  @Override
  public Position<E> root() {
    return root;
  }

  /**
   * Returns the Position of p's parent (or null if p is root).
   *
   * @param p    A valid Position within the tree
   * @return Position of p's parent (or null if p is root)
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  @Override
  public Position<E> parent(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getParent();
  }

  /**
   * Returns the Position of p's NW child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the left child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  // PUT nw method HERE
  @Override
  public Position<E> nw(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getNW();
  }

  /**
   * Returns the Position of p's NE child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the right child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  // PUT ne method HERE
  @Override
  public Position<E> ne(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getNE();
  }

  /**
   * Returns the Position of p's SW child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the right child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  // PUT sw method HERE
  @Override
  public Position<E> sw(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getSW();
  }

  /**
   * Returns the Position of p's SE child (or null if no child exists).
   *
   * @param p A valid Position within the tree
   * @return the Position of the right child (or null if no child exists)
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   */
  // PUT se method HERE
  @Override
  public Position<E> se(Position<E> p) throws IllegalArgumentException {
    Node<E> node = validate(p);
    return node.getSE();
  }

  // update methods supported by this class
  /**
   * Places element e at the root of an empty tree and returns its new Position.
   *
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalStateException if the tree is not empty
   */
  public Position<E> addRoot(E e) throws IllegalStateException {
    if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
    root = createNode(e, null, null, null, null, null);
    size = 1;
    return root;
  }

  /**
   * Creates a new NW child of Position p storing element e and returns its Position.
   *
   * @param p   the Position to the left of which the new element is inserted
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   * @throws IllegalArgumentException if p already has a left child
   */
  public Position<E> addNW(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> parent = validate(p);
    if (parent.getNW() != null)
      throw new IllegalArgumentException("p already has a NW child");
    Node<E> child = createNode(e, parent, null, null, null, null);
    parent.setNW(child);
    size++;
    return child;
  }

  /**
   * Creates a new NE child of Position p storing element e and returns its Position.
   *
   * @param p   the Position to the right of which the new element is inserted
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p already has a right child
   */
  public Position<E> addNE(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> parent = validate(p);
    if (parent.getNE() != null)
      throw new IllegalArgumentException("p already has a NE child");
    Node<E> child = createNode(e, parent, null, null, null, null);
    parent.setNE(child);
    size++;
    return child;
  }

  /**
   * Creates a new SW child of Position p storing element e and returns its Position.
   *
   * @param p   the Position to the left of which the new element is inserted
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree
   * @throws IllegalArgumentException if p already has a left child
   */
  public Position<E> addSW(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> parent = validate(p);
    if (parent.getSW() != null)
      throw new IllegalArgumentException("p already has a SW child");
    Node<E> child = createNode(e, parent, null, null, null, null);
    parent.setSW(child);
    size++;
    return child;
  }

  /**
   * Creates a new SE child of Position p storing element e and returns its Position.
   *
   * @param p   the Position to the right of which the new element is inserted
   * @param e   the new element
   * @return the Position of the new element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p already has a right child
   */
  public Position<E> addSE(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> parent = validate(p);
    if (parent.getSE() != null)
      throw new IllegalArgumentException("p already has a SE child");
    Node<E> child = createNode(e, parent, null, null, null, null);
    parent.setSE(child);
    size++;
    return child;
  }


  /**
   * Replaces the element at Position p with element e and returns the replaced element.
   *
   * @param p   the relevant Position
   * @param e   the new element
   * @return the replaced element
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   */
  public E set(Position<E> p, E e) throws IllegalArgumentException {
    Node<E> node = validate(p);
    E temp = node.getElement();
    node.setElement(e);
    return temp;
  }

  /**
   * Removes the node at Position p and replaces it with its child, if any.
   *
   * @param p   the relevant Position
   * @return element that was removed
   * @throws IllegalArgumentException if p is not a valid Position for this tree.
   * @throws IllegalArgumentException if p has two children.
   */
  public E remove(Position<E> p) throws IllegalArgumentException {
    throw new UnsupportedOperationException("This QuadTree only supports adding not removing");
  }
} //----------- end of LinkedBinaryTree class -----------
