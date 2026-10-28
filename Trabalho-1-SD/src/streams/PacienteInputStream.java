package streams;

import model.Paciente;
import java.io.InputStream;
import java.io.IOException;

public class PacienteInputStream extends InputStream {
    private final InputStream in;

    public PacienteInputStream(InputStream source) {
        this.in = source;
    }

    public Paciente[] lerPacientes() throws IOException {
        String qtdStr = lerLinha();
        if (qtdStr == null || qtdStr.trim().isEmpty()) return new Paciente[0];
        
        int qtd = Integer.parseInt(qtdStr.trim());
        Paciente[] pacientes = new Paciente[qtd];

        for (int i = 0; i < qtd; i++) {
            int id = Integer.parseInt(lerLinha().trim());
            String nome = lerLinha().trim();
            String telefone = lerLinha().trim();
            pacientes[i] = new Paciente(id, nome, telefone);
        }
        return pacientes;
    }

    private String lerLinha() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            if (c == '\n') break;
            sb.append((char) c);
        }
        return sb.length() == 0 && c == -1 ? null : sb.toString();
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }
}