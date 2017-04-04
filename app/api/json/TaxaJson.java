package api.json;

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
    public Boolean visivel;

    public TaxaJson() {
    }

    public TaxaJson(Long id, Long odd, BigDecimal taxa, BigDecimal linha, Long aposta, Boolean visivel) {
        this.id = id;
        this.odd = odd;
        this.taxa = taxa;
        this.linha = linha;
        this.aposta = aposta;
        this.visivel = visivel;
    }

    @Override
    public Taxa to() {
        return new Taxa(id, taxa, aposta, linha, visivel);
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

        return new TaxaJson(taxa.getId(), taxa.getOdd().getId(), taxa.getTaxa(), taxa.getLinha(), aposta, taxa.isVisivel());
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
