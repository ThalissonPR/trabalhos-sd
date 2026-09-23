package executaveis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ClienteMulticast {
    private static final String HOST_TCP = "localhost";
    private static final int PORTA_TCP_AUTH = 12346;
    private static final String IP_MULTICAST = "230.0.0.1";
    private static final int PORTA_MULTICAST = 4446;

    private static volatile boolean executando = true;

    public static void main(String[] args) {
        System.out.println("=== CLIENTE DE NOTIFICACOES EM TEMPO REAL ===");

        if (!autenticarServidorTCP()) {
            System.out.println("[CLIENTE] Falha na autenticacao. Encerrando.");
            return;
        }

        Thread threadMulticast = new Thread(ClienteMulticast::escutarMulticastUDP);
        threadMulticast.start();

        iniciarInteracaoUsuario();
    }

    private static boolean autenticarServidorTCP() {
        try (Socket socket = new Socket(HOST_TCP, PORTA_TCP_AUTH);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("[CLIENTE TCP] Conectando ao servidor para autenticacao...");
            out.println("AUTH:usuario_odonto_1");

            String resposta = in.readLine();
            System.out.println("[CLIENTE TCP] Resposta do servidor: " + resposta);
            return "200_OK_AUTENTICADO".equals(resposta);

        } catch (Exception e) {
            System.err.println("[CLIENTE TCP] Erro ao conectar ao servidor de autenticacao: " + e.getMessage());
            return false;
        }
    }

    private static void escutarMulticastUDP() {
        try {
            MulticastSocket socket = new MulticastSocket(PORTA_MULTICAST);
            InetAddress group = InetAddress.getByName(IP_MULTICAST);
            NetworkInterface netIf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            SocketAddress groupAddress = new InetSocketAddress(group, PORTA_MULTICAST);

            socket.joinGroup(groupAddress, netIf);
            System.out.println("[CLIENTE UDP] Conectado ao grupo Multicast (" + IP_MULTICAST + "). Aguardando mensagens...\n");

            byte[] buffer = new byte[1024];

            while (executando) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String mensagemRecebida = new String(packet.getData(), 0, packet.getLength());
                System.out.println("\n------------------------------------------------");
                System.out.println("[NOTIFICACAO RECEBIDA VIA MULTICAST]:");
                System.out.println(mensagemRecebida);
                System.out.println("------------------------------------------------");
                System.out.print("\nOpcao (1-Perfil, 2-Sair): ");
            }

            socket.leaveGroup(groupAddress, netIf);
            socket.close();

        } catch (Exception e) {
            if (executando) {
                e.printStackTrace();
            }
        }
    }

    private static void iniciarInteracaoUsuario() {
        Scanner scanner = new Scanner(System.in);
        while (executando) {
            System.out.print("Opcao (1-Perfil, 2-Sair): ");
            String opcao = scanner.nextLine();

            if ("1".equals(opcao)) {
                System.out.println("[SISTEMA] Perfil do Paciente: Dr. Odonto - Consultorio Ativo.");
            } else if ("2".equals(opcao)) {
                System.out.println("[SISTEMA] Desconectando e saindo do grupo multicast...");
                executando = false;
                System.exit(0);
            } else {
                System.out.println("[SISTEMA] Opcao invalida.");
            }
        }
    }
}