package message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class MessagesReader {
    public ArrayList<Message> read(String directoryPath) {
        ArrayList<Path> messagesPaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    messagesPaths.add(filePath);
                }
            });
            return readMessages(messagesPaths);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Message> readMessages(ArrayList<Path> messagesPaths) throws IOException {
        ArrayList<Message> messages = new ArrayList<>();
        for (Path path : messagesPaths) {
            Scanner scanner = new Scanner(path);
            String subject = scanner.nextLine();
            scanner.nextLine();
            String body = scanner.nextLine();
            messages.add(new Message(path.getFileName().toString(), getTypeFromPath(path), subjectParser(subject), bodyParser(body)));
        }
        return messages;
    }

    private ArrayList<Integer> subjectParser(String subject) {
        ArrayList<Integer> answer = new ArrayList<>();
        String[] subjectPatrs = subject.split(" ");
        for (String subjectPart : subjectPatrs) {
            try {
                Integer a = Integer.parseInt(subjectPart);
                answer.add(a);
            } catch (Exception e) {
            }
        }
        return answer;
    }

    private ArrayList<Integer> bodyParser(String body) {
        ArrayList<Integer> answer = new ArrayList<>();
        String[] bodyParts = body.split(" ");
        for (String bodyPart : bodyParts) {
            answer.add(Integer.parseInt(bodyPart));
        }
        return answer;
    }

    private MessageType getTypeFromPath(Path path) {
        if (path.getFileName().toString().contains("legit")) {
            return MessageType.LEGIT;
        } else {
            return MessageType.SPAM;
        }
    }
}
