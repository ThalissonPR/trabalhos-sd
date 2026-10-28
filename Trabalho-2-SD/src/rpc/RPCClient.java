package rpc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RPCClient {
    private String serverHost;
    private int serverPort;
    private int requestIdCounter = 0;

    public RPCClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public byte[] doOperation(String objectReference, String methodId, byte[] arguments) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);

            int requestId = ++requestIdCounter;
            Message requestMsg = new Message(0, requestId, objectReference, methodId, arguments);
            byte[] requestData = requestMsg.getBytes();

            InetAddress address = InetAddress.getByName(serverHost);
            DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length, address, serverPort);
            socket.send(sendPacket);

            byte[] buffer = new byte[65535];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            byte[] replyData = new byte[receivePacket.getLength()];
            System.arraycopy(receivePacket.getData(), 0, replyData, 0, receivePacket.getLength());
            
            Message replyMsg = Message.fromBytes(replyData);
            return replyMsg.getArguments();

        } catch (Exception e) {
            System.err.println("[RPC Client Erro] " + e.getMessage());
            return null;
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}