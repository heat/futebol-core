package models.bilhetes;

import models.apostas.EventoAposta;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PalpitePK implements Serializable{

    @Column(name = "bilhete_id")
    private Bilhete bilhete;

    @Column(name = "evento_aposta_id")
    private EventoAposta eventoAposta;

    public PalpitePK() {
    }

    public Bilhete getBilhete() {
        return bilhete;
    }

    public void setBilhete(Bilhete bilhete) {
        this.bilhete = bilhete;
    }

    public EventoAposta getEventoAposta() {
        return eventoAposta;
    }

    public void setEventoAposta(EventoAposta eventoAposta) {
        this.eventoAposta = eventoAposta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PalpitePK palpitePK = (PalpitePK) o;

        if (bilhete != null ? !bilhete.equals(palpitePK.bilhete) : palpitePK.bilhete != null) return false;
        return eventoAposta != null ? eventoAposta.equals(palpitePK.eventoAposta) : palpitePK.eventoAposta == null;
    }

    @Override
    public int hashCode() {
        int result = bilhete != null ? bilhete.hashCode() : 0;
        result = 31 * result + (eventoAposta != null ? eventoAposta.hashCode() : 0);
        return result;
    }


}
