package net.datastructures;

public class CountingVisitor<E> implements Visitor<E> {
    private int c = 0;

    @Override
    public void visit(Position<E> p) { c++; }

    public int getCount() {
        return c;
    }
}
