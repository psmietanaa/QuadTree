package net.datastructures;

public interface Visitor<E> {
    public void visit(Position<E> p);
}
