import java.util.ArrayList;

/**
 * Created by Ilya239 on 13.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        ArrayList<Dot> dots = new Reader().read("non-parametric.csv");
        ArrayList<Dot> line = new ArrayList<>();
        Core core = new Core(dots, 1.1191); //1.1191
        for (double i = 0; i < 60; i += 0.01) {
            line.add(core.produceA(i));
        }
        System.out.println("MSE = " + MSE(dots, core));
        new Plot("x", "y").addGraphic(dots, "line").addGraphic(line, "line2").show();
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
}
