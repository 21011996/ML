import message.Message;
import message.MessageType;
import message.MessagesReader;

import java.util.ArrayList;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        double accuracy = 0;
        for (int i = 1; i<=10; i++) {
            NBC nbc = new NBC().train(new MessagesReader().read("Bayes/pu1/part" + i));
            int count = 0;
            int correct = 0;
            for (int j = 1; j<=10; j++) {
                if (i != j) {
                    ArrayList<Message> toClassify = new MessagesReader().read("Bayes/pu1/part" + j);
                    ArrayList<MessageType> classified = nbc.classify(toClassify);
                    for (int k = 0; k<toClassify.size(); k++) {
                        count++;
                        if (toClassify.get(k).getType().equals(classified.get(k))) {
                            correct++;
                        }
                    }
                }
            }
            accuracy += (double) correct / count;
        }
        System.out.println("Accuracy: " + accuracy*10 + "%");
    }
}
