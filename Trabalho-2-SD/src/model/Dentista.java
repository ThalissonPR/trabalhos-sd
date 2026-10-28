package model;

public class Dentista extends Funcionario {
    private static final long serialVersionUID = 1L;
    
    private String cro;

    public Dentista(String nome, String cpf, String cro) {
        super(nome, cpf, "Dentista");
        this.cro = cro;
    }

    public String getCro() { return cro; }
    public void setCro(String cro) { this.cro = cro; }
}