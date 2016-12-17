import io.Utils;
import utils.Image;
import utils.Label;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Ilya239.
 *         Created on 17.12.2016.
 */
public class NNMain {

    public static void main(String[] args) {
        try {
            Path dir = Paths.get(System.getProperty("user.dir"));

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    System.out.println(file.toString());
                }
            }
            ArrayList<Image> images = Utils.readImages("NN/src/resources/t10k-images.idx3-ubyte");
            ArrayList<Label> labels = Utils.readLabels("NN/src/resources/t10k-labels.idx1-ubyte");
            for (int i = 0; i < images.size(); i++) {
                System.out.println("/test/" + labels.get(i).label + "_" + i + ".png");
                Utils.printImage("./test/" + labels.get(i).label + "_" + i + ".png", images.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
