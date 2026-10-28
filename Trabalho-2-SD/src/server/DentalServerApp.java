package server;

import model.Consulta;
import model.Dentista;
import model.Paciente;
import rpc.Message;
import rpc.RPCServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DentalServerApp {
    private static List<Paciente> pacientes = new ArrayList<>();
    private static List<Dentista> dentistas = new ArrayList<>();
    private static List<Consulta> consultas = new ArrayList<>();

    public static void main(String[] args) {
        int port = 9090;
        RPCServer rpcServer = new RPCServer(port);

        System.out.println("[Dental Server] Aguardando chamadas remotas de RPC...");

        while (true) {
            byte[] requestBytes = rpcServer.getRequest();
            if (requestBytes == null) continue;

            Message requestMsg = Message.fromBytes(requestBytes);
            byte[] responseArguments = null;
            String methodId = requestMsg.methodId();

            try {
                Object argsObj = deserializeArgs(requestMsg.getArguments());

                switch (methodId) {
                    case "cadastrarPaciente":
                        Paciente paciente = (Paciente) argsObj;
                        pacientes.add(paciente);
                        System.out.println("[Servidor] Paciente cadastrado: " + paciente.getNome());
                        responseArguments = serializeResponse(true);
                        break;

                    case "cadastrarDentista":
                        Dentista dentista = (Dentista) argsObj;
                        dentistas.add(dentista);
                        System.out.println("[Servidor] Dentista cadastrado: " + dentista.getNome());
                        responseArguments = serializeResponse(true);
                        break;

                    case "agendarConsulta":
                        Consulta consulta = (Consulta) argsObj;
                        consultas.add(consulta);
                        System.out.println("[Servidor] Consulta agendada para: " + consulta.getDataHora());
                        responseArguments = serializeResponse(true);
                        break;

                    case "listarConsultas":
                        responseArguments = serializeResponse(consultas);
                        break;

                    default:
                        System.err.println("[Servidor] Método desconhecido: " + methodId);
                        responseArguments = serializeResponse("Erro: Método não encontrado");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseArguments = serializeResponse("Erro interno no servidor: " + e.getMessage());
            }

            Message replyMsg = new Message(1, requestMsg.getRequestId(), requestMsg.getObjectReference(), methodId, responseArguments);
            rpcServer.sendReply(replyMsg.getBytes(), rpcServer.getLastClientAddress(), rpcServer.getLastClientPort());
        }
    }

    private static byte[] serializeResponse(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object deserializeArgs(byte[] data) {
        if (data == null || data.length == 0) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}