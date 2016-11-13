import java.util.ArrayList;

/**
 * Created by Ilya239 on 13.11.2016.
 */
public class Core {
    private double h;
    private ArrayList<Dot> dots;
    private int k;

    public Core(ArrayList<Dot> dots, boolean optimize, int k) {
        this.dots = dots;
        this.k = k;
        /*if (optimize) {
            optimizeH();
        } else {
            this.h = h;
        }*/
    }

    /*private void optimizeH() {
        double minH = 0;
        double minLOO = 100000000.0;
        for (double i = 0.01; i<60; i+=0.01) {
            for (Dot dot : dots) {
                ArrayList<Dot> dotsMutated = new ArrayList<>();
                dotsMutated.addAll(dots);
                dotsMutated.remove(dot);
                Core core = new Core(dotsMutated, false, i);
                Dot a = core.produceA(dot.x);
                double LOO = 0;
                for (Dot dot2 : dots) {
                    LOO += (a.y-dot2.y)*(a.y-dot2.y);
                }
                if (LOO < minLOO) {
                    minLOO = LOO;
                    minH = i;
                }
            }
        }
        this.h = minH;
        System.out.println(this.h);
    }*/

    /*private double estimateH(double x) {
        ArrayList<Dot> dotsMutated = new ArrayList<>();
        dotsMutated.addAll(dots);
        dotsMutated.add(new Dot(0,x,0));
        dotsMutated.sort(new Comparator<Dot>() {
            @Override
            public int compare(Dot o1, Dot o2) {
                return Double.compare(o1.x,o2.x);
            }
        });
        for (int i = 0; i<dotsMutated.size(); i++) {
            Dot dot = dotsMutated.get(i);
            if (dot.x == x && dot.id == -1.0) {
                if (i + k < dotsMutated.size()) {
                    return distance(dot.x, dotsMutated.get(i+k).x);
                } else {
                    return distance(dot.x, dotsMutated.get(dotsMutated.size()-1).x);
                }
            }
        }
        return 2;
    }*/

    public Dot produceA(double x) {
        this.h = 2;
        for (Dot dot : dots) {
            dot.id = coreFunction(x, dot.x);
        }
        double a = 0;
        double b = 0;
        for (Dot dot : dots) {
            a += dot.y * dot.id;
            b += dot.id;
        }
        return new Dot(0, x, a / b);
    }

    private double coreFunction(double x, double xi) {
        double r = distance(x, xi) / h;
        double integral = 0;
        for (Dot dot : dots) {
            if (distance(x, dot.x) / h < 1) {
                integral += distance(x, dot.x) / h;
            }
        }
        //return (15.0/16.0)*(1-r*r)*(1-r*r)*integral; // reccommended k = 37
        return Math.exp(-1.0 / 2.0 * r * r); // reccomended k = 2
    }

    private double distance(double x, double xi) {
        return Math.abs(x - xi);
    }
}
