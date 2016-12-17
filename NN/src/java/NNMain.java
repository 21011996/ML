import io.Utils;
import network.Network;
import utils.Image;
import utils.Label;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ilya239.
 *         Created on 17.12.2016.
 */
public class NNMain {

    private static final double RATE = 0.07;
    private static final double RED = 0d;

    public static void main(String[] args) {
        try {
            ArrayList<Image> images = Utils.readImages("NN/src/resources/train-images.idx3-ubyte");
            ArrayList<Label> labels = Utils.readLabels("NN/src/resources/train-labels.idx1-ubyte");

            Network network = new Network(28 * 28, 10);
            network.initWeights();
            for (int i = 0; i < images.size(); i++) {
                network.learnStep(images.get(i), labels.get(i), RATE, RED);
            }
            network.learn(images, labels, RATE, 1d / images.size(), RED);
            ArrayList<Image> imagesTest = Utils.readImages("NN/src/resources/t10k-images.idx3-ubyte");
            ArrayList<Label> labelsTest = Utils.readLabels("NN/src/resources/t10k-labels.idx1-ubyte");

            int lul = 0;
            cleanFailed();
            for (int i = 0; i < imagesTest.size(); i++) {
                Label result = network.classify(imagesTest.get(i));
                if (result.label != labelsTest.get(i).label) {
                    lul++;
                    Utils.printImage("./failed/" + i + "_got_" + result.label + "_expected_" + labelsTest.get(i).label + ".png", imagesTest.get(i));
                }
            }
            System.out.println(lul);
            System.out.println(imagesTest.size());
            System.out.println(100 - ((double) lul) / imagesTest.size() * 100);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cleanFailed() {
        File dir = new File("./failed");
        for (File file : dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }
}
