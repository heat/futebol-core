package dominio.processadores.financeiro;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.financeiro.Conta;
import models.financeiro.comissao.Comissao;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PagarComissaoProcessador implements Processador<Conta, Comissao> {

    public static final String REGRA = "financeiro.comissao";

    @Override
    public CompletableFuture<Comissao> executar(Conta conta, Comissao comissao, List<Validador> validadores) throws ValidadorExcpetion {
        //TODO realizar LOG da comissao e persistir o lançamento
        return null;
    }
}
