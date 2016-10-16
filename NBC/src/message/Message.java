package message;

import java.util.ArrayList;

/**
 * Created by Ilya239 on 16.10.2016.
 */
public class Message {
    private final String fileName;
    private MessageType type;
    private ArrayList<Integer> subject;
    private ArrayList<Integer> body;

    public Message(String fileName, MessageType type, ArrayList<Integer> subject, ArrayList<Integer> body) {
        this.setType(type);
        this.setSubject(subject);
        this.setBody(body);
        this.fileName = fileName;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<Integer> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<Integer> subject) {
        this.subject = subject;
    }

    public ArrayList<Integer> getBody() {
        return body;
    }

    public void setBody(ArrayList<Integer> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return this.fileName + ":" + this.subject.size() + "/" + this.body.size();
    }

    public String getFileName() {
        return fileName;
    }
}
