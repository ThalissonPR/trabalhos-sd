package protocol;

import java.io.Serializable;

public class RequestMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operacao;
    private Object parametro;

    public RequestMessage(String operacao, Object parametro) {
        this.operacao = operacao;
        this.parametro = parametro;
    }

    public String getOperacao() { return operacao; }
    public Object getParametro() { return parametro; }

    @Override
    public String toString() {
        return "RequestMessage{operacao='" + operacao + "', parametro=" + parametro + "}";
    }
}