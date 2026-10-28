package model;

public class Paciente extends Pessoa {
    private static final long serialVersionUID = 1L;
    
    private String historicoMedico;

    public Paciente(String nome, String cpf, String historicoMedico) {
        super(nome, cpf);
        this.historicoMedico = historicoMedico;
    }

    public String getHistoricoMedico() { return historicoMedico; }
    public void setHistoricoMedico(String historicoMedico) { this.historicoMedico = historicoMedico; }
}