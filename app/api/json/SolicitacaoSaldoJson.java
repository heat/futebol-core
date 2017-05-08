package api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.financeiro.DocumentoTransferencia;
import models.financeiro.SolicitacaoSaldo;
import services.DataService;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitacaoSaldoJson implements Convertable<SolicitacaoSaldo>, Jsonable {

    public static final String TIPO = "saldos";

    public String id;
    public Long solicitante;
    public String dataSolicitacao;
    public BigDecimal valor;
    public SolicitacaoSaldo.TipoSolicitacaoSaldo tipo;

    public SolicitacaoSaldoJson() {
    }

    public SolicitacaoSaldoJson(String id, Long solicitante, BigDecimal valor, String dataSolicitacao, SolicitacaoSaldo.TipoSolicitacaoSaldo tipo) {
        this.id = id;
        this.solicitante = solicitante;
        this.valor = valor;
        this.dataSolicitacao = dataSolicitacao;
        this.tipo = tipo;
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static SolicitacaoSaldoJson of(SolicitacaoSaldo solicitacaoSaldo) {

        return new SolicitacaoSaldoJson(String.valueOf(solicitacaoSaldo.getId()), solicitacaoSaldo.getSolicitante(),
                solicitacaoSaldo.getValor(),
                DataService.toString(solicitacaoSaldo.getCriadoEm()), solicitacaoSaldo.getTipo());
    }

    @Override
    public SolicitacaoSaldo to() {
        return null;
    }

    public SolicitacaoSaldo to(Long idConta) {
        return new SolicitacaoSaldo(idConta, valor, tipo);
    }
}
