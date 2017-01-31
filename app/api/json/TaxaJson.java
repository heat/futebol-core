package api.json;

import models.apostas.Taxa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaxaJson implements Serializable, Convertable<Taxa>, Jsonable {

    public static final String TIPO = "taxas";
    public final String idOdd;
    public final BigDecimal taxa;
    public final BigDecimal linha;

    public TaxaJson(String idOdd, BigDecimal taxa, BigDecimal linha ) {

        this.idOdd = idOdd;
        this.taxa = taxa;
        this.linha = linha;
    }

    public static TaxaJson of(Taxa taxa) {
        return new TaxaJson(
                String.valueOf(taxa.getOdd().getId()),
                taxa.getTaxa(),
                taxa.getLinha()
        );
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }

    @Override
    public Taxa to() {

        return new Taxa(0L, null, BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), null, null);
    }

    @Override
    public String type() {
        return null;
    }
}
