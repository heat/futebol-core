package dominio.processadores.financeiro;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.financeiro.Conta;
import models.financeiro.Lancamento;
import models.financeiro.Saldo;
import models.financeiro.credito.CompraBilheteCredito;
import repositories.ContaRepository;
import repositories.LancamentoRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LancarVendaBilheteProcessador implements Processador<Conta, Bilhete> {

    LancamentoRepository lancamentoRepository;
    ContaRepository contaRepository;

    @Inject
    public LancarVendaBilheteProcessador(LancamentoRepository lancamentoRepository, ContaRepository contaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public CompletableFuture<Bilhete> executar(Conta chave, Bilhete bilhete, List<Validador> validadores) throws ValidadorExcpetion {

        CompraBilheteCredito lancamento = new CompraBilheteCredito();
        lancamento.setDataLancamento(Calendar.getInstance());
        lancamento.setValor(bilhete.getValorAposta());
        lancamento.setOrigemBilhete(bilhete);
        //lancamento.setConta(chave.getId());
        Saldo saldo = new Saldo(BigDecimal.TEN);
        lancamento.setSaldo(saldo);

        chave.addLancamento(lancamento);

        contaRepository.atualizar(chave);
        //lancamentoRepository.inserir(lancamento);

        return CompletableFuture.completedFuture(bilhete);
    }

}
