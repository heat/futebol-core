package api.json;

import models.eventos.Time;
import models.financeiro.DocumentoTransferencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class TransferenciaJson implements Convertable<DocumentoTransferencia>, Jsonable {

    public static final String TIPO = "transferencias";

    public Long origem;

    public Long destino;

    public BigDecimal valor;

    public TransferenciaJson() {
    }

    public TransferenciaJson(Long origem, Long destino, BigDecimal valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static TransferenciaJson of(DocumentoTransferencia documentoTransferencia) {

        return new TransferenciaJson(documentoTransferencia.getOrigem(), documentoTransferencia.getDestino(), documentoTransferencia.getValor());
    }

    @Override
    public DocumentoTransferencia to() {
        return new DocumentoTransferencia(origem, destino, valor);
    }
}
