package dominio.processadores.financeiro;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.financeiro.DocumentoTransferencia;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TransferenciaProcessador implements Processador<UUID, DocumentoTransferencia> {

    @Override
    public CompletableFuture<DocumentoTransferencia> executar(UUID chave, DocumentoTransferencia documentoTransferencia, List<Validador> validadores) throws ValidadorExcpetion {
        throw new UnsupportedOperationException("Não implementado");
    }
}
