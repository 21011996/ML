/**
 * Created by Ilya239 on 13.11.2016.
 */
public class Dot {
    double id;
    double x;
    double y;

    public Dot(int id, double x, double y) {
        this.id = id - 1;
        this.x = x;
        this.y = y;
    }

    public Dot(Dot dot) {
        this.id = dot.id;
        this.x = dot.x;
        this.y = dot.y;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    @Override
    public boolean equals(Object v) {
        if (v instanceof Dot) {
            Dot dot = (Dot) v;
            return dot.x == this.x;
        }
        return false;
    }
}
