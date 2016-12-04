import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Ilya239 on 13.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        ArrayList<Dot> dots = new Reader().read("non-parametric.csv");
        dots.sort(new Comparator<Dot>() {
            @Override
            public int compare(Dot o1, Dot o2) {
                return Double.compare(o1.x, o2.x);
            }
        });
        ArrayList<Dot> line = new ArrayList<>();
        ArrayList<Dot> line2 = new ArrayList<>();
        Core core = new Core(dots, 1.1191); //1.1191
        //KNN knn = new KNN();
        Spline spline = new Spline(dots);
        for (double i = 0; i < 60; i += 0.01) {
            line.add(core.produceA(i));
            //line2.add(spline.getA(i));
        }
        System.out.println("MSE_Core = " + MSE(dots, core));
        System.out.println("MSE_Spline = " + MSE(dots, spline.line));
        new Plot("x", "y").addGraphic(dots, "dots").addGraphic(line, "Core").addGraphic(spline.line, "Spline").show();
    }

    private double MSE(ArrayList<Dot> dots, ArrayList<Dot> line) {
        double MSE = 0;
        double n = 0;
        for (Dot dot : dots) {
            Dot a = new Dot(0, 0, 0);
            for (Dot dot2 : line) {
                if (dot2.x >= dot.x) {
                    a = dot2;
                    break;
                }
            }
            double LOO = (dot.y - a.y) * (dot.y - a.y);
            n++;
            MSE += LOO;
        }
        return MSE / n;
    }

    private double MSE(ArrayList<Dot> dots, Core core1) {
        double MSE = 0;
        double n = 0;
        for (Dot dot : dots) {
            ArrayList<Dot> dotsMutated = new ArrayList<>();
            dotsMutated.addAll(dots);
            dotsMutated.remove(dot);
            Core core = new Core(dotsMutated, core1.h);
            Dot a = core.produceA(dot.x);
            double LOO = (dot.y - a.y) * (dot.y - a.y);
            n++;
            MSE += LOO;
        }
        return MSE / n;
    }

    private double MSE(ArrayList<Dot> dots, KNN knn1) {
        double MSE = 0;
        double n = 0;
        for (Dot dot : dots) {
            ArrayList<Dot> dotsMutated = new ArrayList<>();
            dotsMutated.addAll(dots);
            dotsMutated.remove(dot);
            KNN knn = new KNN();
            Dot a = knn.getA(dot.x, dotsMutated, 50);
            double LOO = (dot.y - a.y) * (dot.y - a.y);
            n++;
            MSE += LOO;
        }
        return MSE / n;
    }
}
