package dominio.processadores.financeiro;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.financeiro.Conta;
import models.financeiro.Saldo;
import models.financeiro.comissao.Comissao;
import models.financeiro.debito.PagamentoComissaoDebito;
import repositories.ContaRepository;
import repositories.LancamentoRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PagarComissaoProcessador implements Processador<Conta, Comissao> {

    public static final String REGRA = "financeiro.comissao";
    ContaRepository contaRepository;

    @Inject
    public PagarComissaoProcessador(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Override
    public CompletableFuture<Comissao> executar(Conta conta, Comissao comissao, List<Validador> validadores) throws ValidadorExcpetion {
        //TODO realizar LOG da comissao e persistir o lançamento

        comissao.setDestino(conta);
        contaRepository.inserirComissao(comissao);

        PagamentoComissaoDebito lancamento = new PagamentoComissaoDebito();
        lancamento.setDataLancamento(Calendar.getInstance());
        lancamento.setValor(comissao.getValor());
        lancamento.setOrigemComissao(comissao);
        BigDecimal saldoAtual = conta.getSaldo().getSaldo().add(comissao.getValor());
        lancamento.setSaldo(new Saldo(saldoAtual));
        conta.addLancamento(lancamento);

        contaRepository.atualizar(conta);

        return CompletableFuture.completedFuture(comissao);
    }
}
