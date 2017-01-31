package api.json;

import models.apostas.Odd;
import models.apostas.Taxa;

import java.math.BigDecimal;

public class TaxaJson implements Jsonable, Convertable<Taxa>{

    public static final String TIPO = "taxas";

    public Long odd;

    public Float taxa;

    public Float linha;

    public Long aposta;

    @Override
    public Taxa to() {
        Odd _odd = Odd.ref(odd);
        return new Taxa(null, _odd, BigDecimal.valueOf(taxa), BigDecimal.valueOf(linha), aposta);
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
}
