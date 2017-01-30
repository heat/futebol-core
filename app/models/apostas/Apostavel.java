package models.apostas;

import java.util.List;

/**
 * Toda entidade que pode ser apostada
 */
public interface Apostavel<E> {

    public enum Situacao {
        /**
         * Situacao em que o apostavel aceita apostas
         */
        A("ABERTO"),
        /**
         * Apostavel é valido porém temporariamente não aceita apostas
         */
        F("FECHADO"),
        /**
         * Já encerrado o apostavél e não é mais possível realizar apostas
         */
        E("ENCERRADO"),
        /**
         * Apostável foi cancelado pela adminsitracao
         */
        C("CANCELADO");

        private String situacao;

        Situacao(String situacao) {

            this.situacao = situacao;
        }
    }

    List<Taxa> getTaxas();
}
