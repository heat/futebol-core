package api.json;

import models.bilhetes.Palpite;

import java.io.Serializable;
import java.math.BigDecimal;

public class PalpiteJson implements Serializable, Convertable<Palpite>, Jsonable {

    public static final String TIPO = "palpites";

    public BigDecimal taxa;
    public Long odd;
    public BigDecimal linha;
    public Palpite.Status situacao;
    public Long evento;

    public PalpiteJson(BigDecimal taxa, Long odd, BigDecimal linha, Palpite.Status situacao, Long evento) {
        this.taxa = taxa;
        this.odd = odd;
        this.linha = linha;
        this.situacao = situacao;
        this.evento = evento;
    }

    public static PalpiteJson of(Palpite palpite) {

        return new PalpiteJson(palpite.getValorTaxa(),
                palpite.getTaxa().getOdd().getId(),
                palpite.getTaxa().getLinha(),
                palpite.getStatus(),
                palpite.getTaxa().getEventoAposta());
    }

    @Override
    public Palpite to() {
        return null;
    }

    @Override
    public String type() {
        return TIPO;
    }


}
