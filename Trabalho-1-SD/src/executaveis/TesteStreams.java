package executaveis;

import model.Paciente;
import streams.PacienteInputStream;
import streams.PacienteOutputStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TesteStreams {
    public static void main(String[] args) throws Exception {
        Paciente[] lista = {
            new Paciente(1, "Ana Silva", "88999991111"),
            new Paciente(2, "Carlos Souza", "88999992222")
        };

        System.out.println("=== TESTE 1a e 2b: System.out / ByteArrayStream ===");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new PacienteOutputStream(baos, lista, lista.length);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        PacienteInputStream pisConsole = new PacienteInputStream(bais);
        for (Paciente p : pisConsole.lerPacientes()) {
            System.out.println("Lido do Stream: " + p);
        }

        System.out.println("\n=== TESTE 1c e 2c: Arquivo (FileOutputStream/FileInputStream) ===");
        try (FileOutputStream fos = new FileOutputStream("pacientes.bin")) {
            new PacienteOutputStream(fos, lista, lista.length);
        }

        try (FileInputStream fis = new FileInputStream("pacientes.bin")) {
            PacienteInputStream pisArq = new PacienteInputStream(fis);
            for (Paciente p : pisArq.lerPacientes()) {
                System.out.println("Lido do Arquivo: " + p);
            }
        }

        System.out.println("\n=== TESTE 1d e 2d: Servidor e Cliente TCP ===");
        Thread servidor = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(12345);
                 Socket socket = server.accept();
                 PacienteInputStream pisNet = new PacienteInputStream(socket.getInputStream())) {
                
                Paciente[] recebidos = pisNet.lerPacientes();
                for (Paciente p : recebidos) {
                    System.out.println("[SERVIDOR TCP] Recebido via socket: " + p);
                }
            } catch (Exception e) { e.printStackTrace(); }
        });
        servidor.start();

        Thread.sleep(500);

        try (Socket socket = new Socket("localhost", 12345)) {
            new PacienteOutputStream(socket.getOutputStream(), lista, lista.length);
            System.out.println("[CLIENTE TCP] Dados enviados com sucesso via Socket!");
        }

        servidor.join();
    }
}