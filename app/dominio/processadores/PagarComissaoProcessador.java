package dominio.processadores;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PagarComissaoProcessador implements Processador<Long, Bilhete> {

    @Override
    public CompletableFuture<Bilhete> executar(Long chave, Bilhete bilhete, List<Validador> validadores) throws ValidadorExcpetion {
        return CompletableFuture.completedFuture(bilhete);
    }
}
