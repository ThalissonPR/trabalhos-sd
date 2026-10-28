package model;

import java.io.Serializable;

public class Consulta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String idConsulta;
    private String dataHora;
    private Paciente paciente;
    private Dentista dentista;

    public Consulta(String idConsulta, String dataHora, Paciente paciente, Dentista dentista) {
        this.idConsulta = idConsulta;
        this.dataHora = dataHora;
        this.paciente = paciente;
        this.dentista = dentista;
    }

    public String getIdConsulta() { return idConsulta; }
    public void setIdConsulta(String idConsulta) { this.idConsulta = idConsulta; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Dentista getDentista() { return dentista; }
    public void setDentista(Dentista dentista) { this.dentista = dentista; }
}