import java.util.ArrayList;
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
        mutateDots.sort((o1, o2) -> Double.compare(o1.x, o2.x));
        List<Dot> a = mutateDots.subList(0, k);
        Core tmp = new Core(new ArrayList<Dot>(a), 0.6);
        return new Dot(0, x, tmp.produceA(0.0).y);
    }
}
