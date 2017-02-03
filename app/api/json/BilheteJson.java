package api.json;

import models.bilhetes.Bilhete;

import java.math.BigDecimal;

public class BilheteJson implements Jsonable, Convertable<Bilhete> {

    public static final String TIPO = "bilhetes";

    public String id;
    public String codigo;
    public Bilhete.Situacao situacao;
    public String cliente;
    public BigDecimal valorPremio;
    public BigDecimal valorAposta;

    public BilheteJson(String id, String codigo, Bilhete.Situacao situacao, String cliente,BigDecimal valorPremio, BigDecimal valorAposta) {
        this.id = id;
        this.codigo = codigo;
        this.situacao = situacao;
        this.cliente = cliente;
        this.valorPremio = valorPremio;
        this.valorAposta = valorAposta;
    }

    @Override
    public Bilhete to() {
        return new Bilhete();
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static BilheteJson of(Bilhete bilhete) {
        return new BilheteJson(
                String.valueOf(bilhete.getId()),
                bilhete.getCodigo(),
                bilhete.getSituacao(),
                bilhete.getCliente(),
                bilhete.getValorPremio(),
                bilhete.getValorAposta());
    }
}
