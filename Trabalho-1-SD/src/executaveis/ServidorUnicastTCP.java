package executaveis;

import model.Consulta;
import model.Paciente;
import protocol.ReplyMessage;
import protocol.RequestMessage;
import service.AgendamentoService;
import service.ProntuarioService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorUnicastTCP {
    private static final int PORTA = 12345;
    private static AgendamentoService agendamentoService = new AgendamentoService();
    private static ProntuarioService prontuarioService = new ProntuarioService();

    public static void main(String[] args) {
        System.out.println("[SERVIDOR] Iniciando servidor TCP na porta " + PORTA + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n[SERVIDOR] Cliente conectado: " + clientSocket.getInetAddress());

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                RequestMessage request = (RequestMessage) in.readObject();
                System.out.println("[SERVIDOR] Request desempacotado: " + request);

                ReplyMessage reply = processarRequisicao(request);

                System.out.println("[SERVIDOR] Empacotando e enviando reply: " + reply);
                out.writeObject(reply);
                out.flush();

                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ReplyMessage processarRequisicao(RequestMessage request) {
        switch (request.getOperacao()) {
            case "CADASTRAR_PACIENTE":
                Paciente p = (Paciente) request.getParametro();
                prontuarioService.cadastrarPaciente(p);
                return new ReplyMessage(true, "Paciente " + p.getNome() + " cadastrado com sucesso!", p);

            case "AGENDAR_CONSULTA":
                Consulta c = (Consulta) request.getParametro();
                agendamentoService.agendarConsulta(c);
                return new ReplyMessage(true, "Consulta agendada para " + c.getDataHora(), c);

            case "LISTAR_CONSULTAS":
                return new ReplyMessage(true, "Consultas recuperadas", agendamentoService.listarConsultas());

            default:
                return new ReplyMessage(false, "Operacao nao reconhecida", null);
        }
    }
}