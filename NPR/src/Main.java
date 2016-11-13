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
        Core core = new Core(dots, true, 2);
        for (double i = 0; i < 60; i += 0.01) {
            line.add(core.produceA(i));
        }
        new Plot("x", "y").addGraphic(dots, "line").addGraphic(line, "line2").show();
    }
}
