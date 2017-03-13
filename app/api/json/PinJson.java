package api.json;

import models.bilhetes.Pin;

import java.math.BigDecimal;
import java.util.List;

public class PinJson implements Jsonable, Convertable<Pin> {

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
}
