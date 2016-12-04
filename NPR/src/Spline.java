import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Ilya239 on 20.11.2016.
 */
public class Spline {
    ArrayList<Dot> line;
    private ArrayList<Parameters> parameterses;
    private ArrayList<Dot> dots;

    public Spline(ArrayList<Dot> dots2) {
        this.dots = new ArrayList<>();
        for (Dot dot : dots2) {
            this.dots.add(new Dot(dot));
        }
        preprocess();
        line = new ArrayList<>();
        parameterses = new ArrayList<>();
        for (int i = 1; i < dots.size() - 2; i++) {
            Parameters parameters = getParameters(dots.get(i - 1), dots.get(i), dots.get(i + 1), dots.get(i + 2));
            parameterses.add(parameters);
            for (double j = 0.0; j < 100.0; j++) {
                double t = (j + 1) / 100;
                double a3 = parameters.a3;
                double a2 = parameters.a2;
                double a1 = parameters.a1;
                double a0 = parameters.a0;

                double b3 = parameters.b3;
                double b2 = parameters.b2;
                double b1 = parameters.b1;
                double b0 = parameters.b0;
                line.add(new Dot(0, ((a3 * t + a2) * t + a1) * t + a0, ((b3 * t + b2) * t + b1) * t + b0));
            }
        }
    }

    private void preprocess() {
        ArrayList<Dot> delete = new ArrayList<Dot>();
        for (Dot dot : dots) {
            double x = dot.x;
            double y = 0.0;
            double count = 0.0;
            for (Dot dot2 : dots) {
                if (dot2.x == x) {
                    y += dot2.y;
                    count++;
                }
            }
            if (count > 1) {
                delete.add(new Dot(0, x, y / count));
            }
        }
        for (Dot dot2 : delete) {
            dots.removeIf(dot -> dot2.x == dot.x);
            dots.add(dot2);
        }
        dots.sort(Comparator.comparingDouble(o -> o.x));
        for (int i = 2; i < dots.size(); i++) {
            Dot curr = dots.get(i);
            double alpha = 0.35;
            curr.y = alpha * curr.y + (1 - alpha) * dots.get(i - 1).y;
            //curr.x -=alpha/2;
        }
    }

    public boolean isin(double x) {
        for (Dot dot : line) {
            if (dot.x == x) {
                return true;
            }
        }
        return false;
    }

    public Dot getA(double x) {
        Parameters parameters = null;
        for (int i = 0; i < parameterses.size(); i++) {
            if (x >= parameterses.get(i).x0 && x <= parameterses.get(i).x1) {
                parameters = parameterses.get(i);
                break;
            }
        }
        if (parameters != null) {
            double t = (x - parameters.x0) / (parameters.x1 - parameters.x0);
            return new Dot(0, x, ((parameters.b3 * t + parameters.b2) * t + parameters.b1) * t + parameters.b0);
        } else {
            return new Dot(0, x, 0);
        }

    }

    private Parameters getParameters(Dot... dots) {
        Dot a = dots[0];
        Dot b = dots[1];
        Dot c = dots[2];
        Dot d = dots[3];
        Parameters answer = new Parameters();
        answer.x0 = b.x;
        answer.x1 = c.x;

        answer.a3 = (-a.x + 3.0 * (b.x - c.x) + d.x) / 6.0;
        answer.a2 = (a.x - 2.0 * b.x + c.x) / 2.0;
        answer.a1 = (c.x - a.x) / 2.0;
        answer.a0 = (a.x + 4.0 * b.x + c.x) / 6.0;

        answer.b3 = (-a.y + 3.0 * (b.y - c.y) + d.y) / 6.0;
        answer.b2 = (a.y - 2.0 * b.y + c.y) / 2.0;
        answer.b1 = (c.y - a.y) / 2.0;
        answer.b0 = (a.y + 4.0 * b.y + c.y) / 6.0;

        return answer;
    }

    private class Parameters {
        private double a3;
        private double a2;
        private double a1;
        private double a0;

        private double b3;
        private double b2;
        private double b1;
        private double b0;

        private double x0;
        private double x1;
    }
}
