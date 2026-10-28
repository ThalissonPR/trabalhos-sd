package streams;

import model.Paciente;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacienteOutputStream extends OutputStream {
    private final OutputStream out;

    public PacienteOutputStream(OutputStream target, Paciente[] pacientes, int qtd) throws IOException {
        this.out = target;
        escreverString(qtd + "\n");
        for (int i = 0; i < qtd; i++) {
            Paciente p = pacientes[i];
            escreverString(p.getId() + "\n");
            escreverString(p.getNome() + "\n");
            escreverString(p.getTelefone() + "\n");
        }
        out.flush();
    }

    private void escreverString(String str) throws IOException {
        out.write(str.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}