package executaveis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ServidorMulticast {
    private static final int PORTA_TCP_AUTH = 12346;
    private static final String IP_MULTICAST = "230.0.0.1";
    private static final int PORTA_MULTICAST = 4446;

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR MULTICAST + AUTENTICACAO TCP ===");

        new Thread(ServidorMulticast::iniciarServidorAutenticacaoTCP).start();
        new Thread(ServidorMulticast::iniciarEnvioMulticastUDP).start();
    }

    private static void iniciarServidorAutenticacaoTCP() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA_TCP_AUTH)) {
            System.out.println("[SERVIDOR TCP] Aguardando autenticacoes na porta " + PORTA_TCP_AUTH + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new AuthClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class AuthClientHandler implements Runnable {
        private Socket socket;

        public AuthClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String credencial = in.readLine();
                System.out.println("[SERVIDOR TCP] Tentativa de autenticacao do cliente: " + socket.getInetAddress() + " (" + credencial + ")");

                if (credencial != null && credencial.startsWith("AUTH:")) {
                    out.println("200_OK_AUTENTICADO");
                    System.out.println("[SERVIDOR TCP] Cliente autenticado com sucesso!");
                } else {
                    out.println("401_NEGADO");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void iniciarEnvioMulticastUDP() {
        try (DatagramSocket socketUDP = new DatagramSocket()) {
            InetAddress group = InetAddress.getByName(IP_MULTICAST);
            Scanner scanner = new Scanner(System.in);

            System.out.println("[SERVIDOR MULTICAST] Pronto para transmissao no IP " + IP_MULTICAST + ":" + PORTA_MULTICAST);
            System.out.println("Digite o texto do alerta e pressione ENTER para disparar a todos:");

            while (true) {
                System.out.print("> ");
                String texto = scanner.nextLine();
                long timestamp = System.currentTimeMillis();

                String jsonMensagem = String.format(
                    "{\"tipo\": \"ALERTA\", \"mensagem\": \"%s\", \"timestamp\": %d}",
                    texto, timestamp
                );

                byte[] buffer = jsonMensagem.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORTA_MULTICAST);
                
                socketUDP.send(packet);
                System.out.println("[SERVIDOR MULTICAST] Notificacao enviada para o grupo!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}