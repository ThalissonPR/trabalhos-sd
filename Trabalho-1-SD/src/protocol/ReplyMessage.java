package protocol;

import java.io.Serializable;

public class ReplyMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean sucesso;
    private String mensagem;
    private Object dados;

    public ReplyMessage(boolean sucesso, String mensagem, Object dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public Object getDados() { return dados; }

    @Override
    public String toString() {
        return "ReplyMessage{sucesso=" + sucesso + ", mensagem='" + mensagem + "', dados=" + dados + "}";
    }
}