package models.apostas;

import java.math.BigDecimal;

/**
 * A configuracao representa o Paramatro da banca para cada Odd. É necessário que a banca tenha uma configuracao para
 * cada odd
 */
public class OddConfiguracao {

    enum Situacao {
        A("ATIVA");

        private final String situacao;

        Situacao(String situacao) {
            this.situacao = situacao;
        }
    }

    Long id;

    Long tenant;

    Odd odd;

    Boolean favorita;

    Situacao situacao;

    /**
     * Odd do tipo linha deve ter específicado qual a linha favorita
     */
    BigDecimal linhaFavorita;

    Long prioridade;

    public Long getIdOdd() {
        return this.odd.getId();
    }

    public Odd get() {
        return this.odd;
    }

}
