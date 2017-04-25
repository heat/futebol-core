package api.json;

import models.apostas.EventoAposta;
import models.bilhetes.Palpite;
import services.DataService;

import java.io.Serializable;
import java.math.BigDecimal;

public class PalpiteDetalheJson implements Serializable, Convertable<Palpite>, Jsonable {

    public static final String TIPO = "palpites";

    public final String id;
    public final String campeonato;
    public final String casa;
    public final String fora;
    public final String dataEvento;
    public final String criadoEm;
    public final BigDecimal taxa;
    public final Palpite.Status situacao;
    public final Long odd;
    public final Long resultadoCasaIntervalo;
    public final Long resultadoCasaFinal;
    public final Long resultadoForaIntervalo;
    public final Long resultadoForaFinal;

    public PalpiteDetalheJson(String id, String campeonato, String casa, String fora, String dataEvento, String criadoEm, BigDecimal taxa, Palpite.Status situacao, Long odd, Long resultadoCasaIntervalo, Long resultadoCasaFinal, Long resultadoForaIntervalo, Long resultadoForaFinal) {
        this.id = id;
        this.campeonato = campeonato;
        this.casa = casa;
        this.fora = fora;
        this.dataEvento = dataEvento;
        this.criadoEm = criadoEm;
        this.taxa = taxa;
        this.situacao = situacao;
        this.odd = odd;
        this.resultadoCasaIntervalo = resultadoCasaIntervalo;
        this.resultadoCasaFinal = resultadoCasaFinal;
        this.resultadoForaIntervalo = resultadoForaIntervalo;
        this.resultadoForaFinal = resultadoForaFinal;
    }

    public static PalpiteDetalheJson of(EventoAposta eventoAposta, Palpite palpite) {

        return new PalpiteDetalheJson(palpite.getId().toString(),
                eventoAposta.getEvento().getCampeonato().getNome(),
                eventoAposta.getEvento().getCasa().getNome(),
                eventoAposta.getEvento().getFora().getNome(),
                DataService.toString(eventoAposta.getEvento().getDataEvento()),
                DataService.toString(eventoAposta.getEvento().getDataEvento()),
                palpite.getValorTaxa(),
                palpite.getStatus(),
                palpite.getTaxa().getOdd().getId(),
                eventoAposta.getEvento().getResultadoFutebol().casaPrimeiroTempo.getPontos(),
                eventoAposta.getEvento().getResultadoFutebol().casaSegundoTempo.getPontos(),
                eventoAposta.getEvento().getResultadoFutebol().foraPrimeiroTempo.getPontos(),
                eventoAposta.getEvento().getResultadoFutebol().foraSegundoTempo.getPontos());
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
