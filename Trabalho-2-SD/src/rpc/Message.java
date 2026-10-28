package rpc;

import java.io.*;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private int messageType; // 0 = Request, 1 = Reply
    private int requestId;
    private String objectReference;
    private String methodId;
    private byte[] arguments;

    public Message(int messageType, int requestId, String objectReference, String methodId, byte[] arguments) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.objectReference = objectReference;
        this.methodId = methodId;
        this.arguments = arguments;
    }

    public byte[] getBytes() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Message fromBytes(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Message) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getMessageType() { return messageType; }
    public int getRequestId() { return requestId; }
    public String getObjectReference() { return objectReference; }
    public String methodId() { return methodId; }
    public byte[] getArguments() { return arguments; }
}