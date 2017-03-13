package api.json;

import com.sun.org.apache.xpath.internal.operations.Bool;
import models.apostas.Odd;
import models.apostas.Taxa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class TaxaJson implements Jsonable, Convertable<Taxa>{

    public static final String TIPO = "taxas";
    public Long id;
    public Long odd;
    public BigDecimal taxa;
    public BigDecimal linha;
    public Long aposta;
    public String alterado_em;
    public String criado_em;
    public Boolean isLinha;
    public Boolean favorita;
    public String abbr;
    public String nome;
    public String mercado;

    public TaxaJson() {
    }

    public TaxaJson(Long id, Long odd, BigDecimal taxa, BigDecimal linha, Long aposta, String alterado_em, String criado_em, Boolean isLinha, Boolean favorita, String abbr, String nome, String mercado) {
        this.id = id;
        this.odd = odd;
        this.taxa = taxa;
        this.linha = linha;
        this.aposta = aposta;
        this.alterado_em = alterado_em;
        this.criado_em = criado_em;
        this.isLinha = isLinha;
        this.favorita = favorita;
        this.abbr = abbr;
        this.nome = nome;
        this.mercado = mercado;
    }

    @Override
    public Taxa to() {
        Odd _odd = Odd.ref(odd);
        return new Taxa(null, _odd, taxa, linha);
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
                ", aposta='" + aposta + '\'' +
                '}';
    }

    public static TaxaJson of(Taxa taxa, Long aposta) {

        return new TaxaJson(taxa.getId(), taxa.getOdd().getId(), taxa.getTaxa(), taxa.getLinha(), aposta, calendarToString(taxa.getAlteradoEm()),
                calendarToString(taxa.getCriadoEm()), taxa.getOdd().isLinha(), taxa.getOdd().isFavorita(), taxa.getOdd().getAbreviacao(),
                taxa.getOdd().getNome(), taxa.getOdd().getMercado());
    }

    public static List<Jsonable> of(List<Taxa> taxas, Long aposta) {
        return taxas.stream().map( c -> TaxaJson.of(c, aposta) ).collect(Collectors.toList());
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
