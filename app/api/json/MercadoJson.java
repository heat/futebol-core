package api.json;

import models.apostas.Odd;
import models.apostas.OddConfiguracao;
import models.apostas.mercado.Mercado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MercadoJson implements Serializable, Jsonable {

    public static final String TIPO = "mercados";

    public final String id;
    public final String nome;
    public final Mercado.TipoMercado tipo;

    public MercadoJson(String id, String nome, Mercado.TipoMercado tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public static MercadoJson of(Mercado mercado) {

        return new MercadoJson(mercado.getId(), mercado.getNome(), mercado.getTipo());
    }

    @Override
    public String type() {
        return TIPO;
    }
}
