import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by shambala on 17/09/16.
 */

public class Reader {
    public static void main(String[] args) {
        ArrayList<Dot> dots = new Reader().read("non-parametric.csv");
    }

    ArrayList<Dot> read(String file) {
        ArrayList<Dot> dots = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] spl = line.split(";");
                dots.add(new Dot(Integer.parseInt(spl[0]), Double.parseDouble(spl[1]), Double.parseDouble(spl[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dots;
    }
}

