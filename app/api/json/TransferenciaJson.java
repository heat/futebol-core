package api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.financeiro.Conta;
import models.financeiro.DocumentoTransferencia;
import services.DataService;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferenciaJson implements Convertable<DocumentoTransferencia>, Jsonable {

    public static final String TIPO = "transferencias";

    public String id;

    public Long origem;

    public Long destino;

    public String dataTransferencia;

    public BigDecimal valor;

    public TransferenciaJson() {
    }

    public TransferenciaJson(String id, Long origem, Long destino, BigDecimal valor, String dataTransferencia) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
        this.dataTransferencia = dataTransferencia;
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static TransferenciaJson of(DocumentoTransferencia documentoTransferencia) {

        return new TransferenciaJson(String.valueOf(documentoTransferencia.getId()), documentoTransferencia.getOrigem(),
                documentoTransferencia.getDestino(), documentoTransferencia.getValor(),
                DataService.toString(documentoTransferencia.getCriadoEm()));
    }

    @Override
    public DocumentoTransferencia to() {
        return new DocumentoTransferencia(origem, destino, valor);
    }

    public DocumentoTransferencia to(Conta origem, Conta destino) {
        return new DocumentoTransferencia(origem.getId(), destino.getId(), valor);
    }
}
