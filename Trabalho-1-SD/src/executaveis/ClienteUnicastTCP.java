package executaveis;

import model.Consulta;
import model.Paciente;
import protocol.ReplyMessage;
import protocol.RequestMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteUnicastTCP {
    private static final String HOST = "localhost";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        Paciente paciente = new Paciente(10, "Maria Oliveira", "88988887777");
        RequestMessage req1 = new RequestMessage("CADASTRAR_PACIENTE", paciente);
        enviarEProcessar(req1);

        Consulta consulta = new Consulta(1, 10, "2026-09-25 14:00", "Limpeza e Avaliacao");
        RequestMessage req2 = new RequestMessage("AGENDAR_CONSULTA", consulta);
        enviarEProcessar(req2);
    }

    private static void enviarEProcessar(RequestMessage request) {
        try (Socket socket = new Socket(HOST, PORTA)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("[CLIENTE] Empacotando e enviando request: " + request);
            out.writeObject(request);
            out.flush();

            ReplyMessage reply = (ReplyMessage) in.readObject();
            System.out.println("[CLIENTE] Reply desempacotado do servidor: " + reply);
            System.out.println("------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}