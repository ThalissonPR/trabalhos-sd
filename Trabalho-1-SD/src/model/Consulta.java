package model;

import java.io.Serializable;

public class Consulta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idPaciente;
    private String dataHora;
    private String procedimento;

    public Consulta() {}

    public Consulta(int id, int idPaciente, String dataHora, String procedimento) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.dataHora = dataHora;
        this.procedimento = procedimento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getProcedimento() { return procedimento; }
    public void setProcedimento(String procedimento) { this.procedimento = procedimento; }

    @Override
    public String toString() {
        return "Consulta{id=" + id + ", idPaciente=" + idPaciente + 
               ", dataHora='" + dataHora + "', procedimento='" + procedimento + "'}";
    }
}