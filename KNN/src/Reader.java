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
        ArrayList<Dot> dots = new Reader().read("chips.txt");
    }

    ArrayList<Dot> read(String file) {
        ArrayList<Dot> dots = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] spl = line.split(",");
                int type = Integer.parseInt(spl[2]);
                if (type == 0) {
                    type = -1;
                }
                dots.add(new Dot(Double.parseDouble(spl[0]), Double.parseDouble(spl[1]), type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dots;
    }
}

