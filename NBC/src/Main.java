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
        MessagesReader reader = new MessagesReader();
        for (int i = 1; i <= 10; i++) {
            ArrayList<Message> test = reader.read("Bayes/pu1/part" + i);
            ArrayList<Message> train = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                if (i != j) {
                    train.addAll(reader.read("Bayes/pu1/part" + j));
                }
            }
            NBC nbc = new NBC().train(train);
            ArrayList<MessageType> classified = nbc.classify(test);
            int correct = 0;
            int count = 0;
            for (int j = 0; j < test.size(); j++) {
                count++;
                if (test.get(j).getType().equals(classified.get(j))) {
                    correct++;
                }
            }
            accuracy += (double) correct / count;
        }
        System.out.println("Accuracy: " + accuracy * 10 + "%");
    }
}
