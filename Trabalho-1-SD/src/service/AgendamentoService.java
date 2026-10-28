package service;

import model.Consulta;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoService {
    private List<Consulta> consultas = new ArrayList<>();

    public void agendarConsulta(Consulta c) {
        consultas.add(c);
        System.out.println("Consulta agendada com sucesso: " + c);
    }

    public List<Consulta> listarConsultas() {
        return consultas;
    }
}