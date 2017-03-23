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

    public static final String REGRA = "financeiro.bilhete";
    LancamentoRepository lancamentoRepository;
    ContaRepository contaRepository;

    @Inject
    public LancarVendaBilheteProcessador(LancamentoRepository lancamentoRepository, ContaRepository contaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public CompletableFuture<Bilhete> executar(Conta conta, Bilhete bilhete, List<Validador> validadores) throws ValidadorExcpetion {

        CompraBilheteCredito lancamento = new CompraBilheteCredito();
        lancamento.setDataLancamento(Calendar.getInstance());
        lancamento.setValor(bilhete.getValorAposta());
        lancamento.setOrigemBilhete(bilhete);
        BigDecimal saldoAtual = conta.getSaldo().getSaldo().subtract(bilhete.getValorAposta());
        lancamento.setSaldo(new Saldo(saldoAtual));
        conta.addLancamento(lancamento);

        contaRepository.atualizar(conta);

        return CompletableFuture.completedFuture(bilhete);
    }

}
