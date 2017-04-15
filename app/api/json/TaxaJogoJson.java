package api.json;

import models.apostas.Taxa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class TaxaJogoJson implements Jsonable, Convertable<Taxa>{

    public static final String TIPO = "taxas";
    public Long id;
    public Long odd;
    public BigDecimal taxa;
    public BigDecimal linha;
    public Long jogo;
    public Boolean visivel;
    public String mercado;
    public String nome;
    public String abbr;
    public Boolean favorita;
    public Boolean isLinha;

    public TaxaJogoJson() {
    }

    public TaxaJogoJson(Long id, Long odd, BigDecimal taxa, BigDecimal linha, Long jogo, Boolean visivel, String mercado, String nome, String abbr, Boolean favorita, Boolean isLinha) {
        this.id = id;
        this.odd = odd;
        this.taxa = taxa;
        this.linha = linha;
        this.jogo = jogo;
        this.visivel = visivel;
        this.mercado = mercado;
        this.nome = nome;
        this.abbr = abbr;
        this.favorita = favorita;
        this.isLinha = isLinha;
    }

    @Override
    public Taxa to() {
        return new Taxa(id, taxa, jogo, linha, visivel);
    }

    @Override
    public String type() {
        return TIPO;
    }

    @Override
    public String toString() {
        return "TaxaJson{" +
                "odd='" + odd + '\'' +
                ", taxa=" + taxa +
                ", linha=" + linha +
                ", aposta='" + jogo + '\'' +
                '}';
    }

    public static TaxaJogoJson of(Taxa taxa, Long aposta) {

        return new TaxaJogoJson(taxa.getId(), taxa.getOdd().getId(), taxa.getTaxa(), taxa.getLinha(), aposta,
                taxa.isVisivel(), taxa.getOdd().getMercado().getNome(), taxa.getOdd().getNome(), taxa.getOdd().getAbreviacao(),
                taxa.getOddConfiguracao().getFavorita(), taxa.getOdd().getMercado().isLinha());
    }

    public static List<Jsonable> of(List<Taxa> taxas, Long aposta) {
        return taxas.stream().map( c -> TaxaJogoJson.of(c, aposta) ).collect(Collectors.toList());
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
