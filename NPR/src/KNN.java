import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ilya239 on 13.11.2016.
 */
public class KNN {

    public Dot getA(double x, ArrayList<Dot> dots, int k) {
        ArrayList<Dot> mutateDots = new ArrayList<>();
        for (Dot dot : dots) {
            mutateDots.add(new Dot(0, Math.abs(x - dot.x), dot.y));
        }
        mutateDots.sort(Comparator.comparingDouble(o -> o.x));
        List<Dot> a = mutateDots.subList(0, k);
        double curH = 0.6;
        double yw = 0.0;
        double w = 0.0;
        for (Dot dot : a) {
            double wi = fun(dot.x / (curH));
            yw += dot.y * wi;
            w += wi;
        }
        return new Dot(0, x, yw / w);
    }

    private double fun(double in) {
        return Math.pow(2.0 * Math.PI, -0.5) * Math.exp(-in * in / 2);
        /*if (in <= 1) {
            return 1.0 / (2.0 * in);
        } else {
            return 0;
        }*/
        //return Math.exp(-1.0 / 2.0 * in * in);
    }
}
