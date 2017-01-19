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
        ABERTO,
        /**
         * Apostavel é valido porém temporariamente não aceita apostas
         */
        FECHADO,
        /**
         * Já encerrado o apostavél e não é mais possível realizar apostas
         */
        ENCERRADO,
        /**
         * Apostável foi cancelado pela adminsitracao
         */
        CANCELADO
    }

    List<Taxa> getTaxas();
}
