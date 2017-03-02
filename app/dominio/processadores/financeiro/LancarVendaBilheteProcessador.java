package dominio.processadores.financeiro;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.financeiro.Conta;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LancarVendaBilheteProcessador implements Processador<Conta, Bilhete> {
    @Override
    public CompletableFuture<Bilhete> executar(Conta chave, Bilhete bilhete, List<Validador> validadores) throws ValidadorExcpetion {
        throw new UnsupportedOperationException("Não implementado");
    }
}
