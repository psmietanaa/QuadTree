package net.datastructures;

public class PrintVisitor<E> implements Visitor<E> {
    @Override
    public void visit(Position<E> p) {
        System.out.println("visit " + p.getElement());
    }
}
