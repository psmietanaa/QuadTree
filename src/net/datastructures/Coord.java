package net.datastructures;

import java.util.Objects;

public class Coord<X,Y> {
    private final X x;
    private final Y y;

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public Coord(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord<?, ?> coord = (Coord<?, ?>) o;
        return Objects.equals(x, coord.x) &&
                Objects.equals(y, coord.y);
    }

    /*
    @Override
    public String toString() {
        return "Coord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    */

    @Override
    public String toString() {
        return
                "x" + x +
                "y" + y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
