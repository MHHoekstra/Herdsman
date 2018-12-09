package br.uepg.projeto.herdsman.objetos;

public class MensagemPendente {
    long id;
    String text;
    String numero;

    public MensagemPendente(long id, String text, String numero) {
        this.id = id;
        this.text = text;
        this.numero = numero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
