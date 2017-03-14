package api.json;

import models.bilhetes.Pin;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PinJson implements Jsonable, Convertable<Pin> {

    public static final String TIPO = "pins";

    public String cliente;
    public BigDecimal valorAposta;
    public List<Long> palpites;

    public PinJson() {
    }

    public PinJson(String cliente, BigDecimal valorAposta, List<Long> palpites) {
        this.cliente = cliente;
        this.valorAposta = valorAposta;
        this.palpites = palpites;
    }

    @Override
    public Pin to() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }

    public static PinJson of(Pin pin) {

        List<Long> palpites = pin.getPalpitesPin().stream().map(p -> p.getTaxa()).collect(Collectors.toList());

        return new PinJson(
                pin.getCliente(),
                pin.getValorAposta(),
                palpites);
    }
}
