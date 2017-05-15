package api.json;

import models.financeiro.Lancamento;
import models.seguranca.Perfil;
import services.DataService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

public class FechamentoJson implements Serializable, Convertable<Lancamento>, Jsonable {

    public static final String TIPO = "fechamentos";

    public String id;
    public BigDecimal valor;
    public String dataFechamento;

    public FechamentoJson() {
    }

    public FechamentoJson(String id, BigDecimal valor, String dataFechamento) {
        this.id = id;
        this.valor = valor;
        this.dataFechamento = dataFechamento;
    }

    public static FechamentoJson of(Lancamento lancamento) {

        return new FechamentoJson(lancamento.getId().toString(), lancamento.getValor(), DataService.toString(lancamento.getDataLancamento()));
    }

    @Override
    public Lancamento to() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }
}
