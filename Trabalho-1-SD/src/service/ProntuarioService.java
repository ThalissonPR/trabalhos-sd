package service;

import model.Paciente;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioService {
    private List<Paciente> pacientes = new ArrayList<>();

    public void cadastrarPaciente(Paciente p) {
        pacientes.add(p);
        System.out.println("Paciente cadastrado: " + p.getNome());
    }

    public Paciente buscarPorId(int id) {
        return pacientes.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}