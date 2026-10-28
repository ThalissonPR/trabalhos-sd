package client;

import model.Consulta;
import model.Dentista;
import model.Paciente;
import rpc.RPCClient;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class DentalClientApp {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 9090;
        
        RPCClient rpcClient = new RPCClient(serverHost, serverPort);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CLÍNICA ODONTOLÓGICA (RPC Client) ===");
            System.out.println("1. Cadastrar Paciente");
            System.out.println("2. Cadastrar Dentista");
            System.out.println("3. Agendar Consulta");
            System.out.println("4. Listar Consultas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            try {
                if (opcao == 1) {
                    System.out.print("Nome do Paciente: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Histórico Médico: ");
                    String historico = scanner.nextLine();

                    Paciente p = new Paciente(nome, cpf, historico);
                    byte[] argsBytes = serialize(p);

                    rpcClient.doOperation("DentalService", "cadastrarPaciente", argsBytes);
                    System.out.println(">> Resposta do Servidor: Sucesso ao cadastrar paciente!");

                } else if (opcao == 2) {
                    System.out.print("Nome do Dentista: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("CRO: ");
                    String cro = scanner.nextLine();

                    Dentista d = new Dentista(nome, cpf, cro);
                    byte[] argsBytes = serialize(d);

                    rpcClient.doOperation("DentalService", "cadastrarDentista", argsBytes);
                    System.out.println(">> Resposta do Servidor: Sucesso ao cadastrar dentista!");

                } else if (opcao == 3) {
                    System.out.print("ID da Consulta: ");
                    String id = scanner.nextLine();
                    System.out.print("Data e Hora (ex: 20/06/2026 14:00): ");
                    String dataHora = scanner.nextLine();

                    System.out.print("Nome do Paciente associado: ");
                    String nomePac = scanner.nextLine();
                    Paciente p = new Paciente(nomePac, "000.000.000-00", "Rotina");

                    System.out.print("Nome do Dentista associado: ");
                    String nomeDent = scanner.nextLine();
                    Dentista d = new Dentista(nomeDent, "111.111.111-11", "CRO-CE 1234");

                    Consulta c = new Consulta(id, dataHora, p, d);
                    byte[] argsBytes = serialize(c);

                    rpcClient.doOperation("DentalService", "agendarConsulta", argsBytes);
                    System.out.println(">> Resposta do Servidor: Consulta agendada com sucesso!");

                } else if (opcao == 4) {
                    byte[] response = rpcClient.doOperation("DentalService", "listarConsultas", new byte[0]);
                    @SuppressWarnings("unchecked")
                    List<Consulta> lista = (List<Consulta>) deserialize(response);
                    
                    System.out.println("\n--- LISTA DE CONSULTAS REMOTAS ---");
                    if (lista != null && !lista.isEmpty()) {
                        for (Consulta c : lista) {
                            System.out.println("ID: " + c.getIdConsulta() + " | Data: " + c.getDataHora() + 
                                               " | Paciente: " + c.getPaciente().getNome() + 
                                               " | Dentista: " + c.getDentista().getNome());
                        }
                    } else {
                        System.out.println("Nenhuma consulta cadastrada.");
                    }

                } else if (opcao == 0) {
                    System.out.println("Encerrando cliente...");
                    break;
                }
            } catch (Exception e) {
                System.err.println("Erro na operação remota: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static byte[] serialize(Serializable obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object deserialize(byte[] data) {
        if (data == null || data.length == 0) return null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}