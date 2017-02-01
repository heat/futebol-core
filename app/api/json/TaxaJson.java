package api.json;

import models.apostas.Odd;
import models.apostas.Taxa;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class TaxaJson implements Jsonable, Convertable<Taxa>{

    public static final String TIPO = "taxas";

    public Long odd;

    public BigDecimal taxa;

    public BigDecimal linha;

    public Long aposta;

    public TaxaJson() {
    }

    public TaxaJson(Long odd, BigDecimal taxa, BigDecimal linha, Long aposta) {

        this.odd = odd;
        this.taxa = taxa;
        this.linha = linha;
        this.aposta = aposta;
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

        return new TaxaJson( taxa.getOdd().getId(), taxa.getTaxa(), taxa.getLinha(), aposta);
    }

    public static List<Jsonable> of(List<Taxa> taxas, Long aposta) {
        return taxas.stream().map( c -> TaxaJson.of(c, aposta) ).collect(Collectors.toList());
    }
}
