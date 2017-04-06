package dominio.processadores.financeiro;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.financeiro.Conta;
import models.financeiro.Saldo;
import models.financeiro.SolicitacaoSaldo;
import models.financeiro.credito.CompraBilheteCredito;
import models.financeiro.debito.AdicionarSaldoDebito;
import models.seguranca.Usuario;
import repositories.ContaRepository;
import repositories.LancamentoRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdicionarSaldoProcessador implements Processador<Conta, SolicitacaoSaldo> {

    public static final String REGRA = "financeiro.saldo";
    LancamentoRepository lancamentoRepository;
    ContaRepository contaRepository;

    @Inject
    public AdicionarSaldoProcessador(LancamentoRepository lancamentoRepository, ContaRepository contaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public CompletableFuture<SolicitacaoSaldo> executar(Conta conta, SolicitacaoSaldo solicitacaoSaldo, List<Validador> validadores) throws ValidadorExcpetion {

        AdicionarSaldoDebito lancamento = new AdicionarSaldoDebito();
        lancamento.setDataLancamento(Calendar.getInstance());
        lancamento.setValor(solicitacaoSaldo.getValor());
        lancamento.setOrigem(solicitacaoSaldo);
        BigDecimal saldoAtual = conta.getSaldo().getSaldo().add(solicitacaoSaldo.getValor());
        lancamento.setSaldo(new Saldo(saldoAtual));
        conta.addLancamento(lancamento);

        contaRepository.inserirSolicitacaoSaldo(solicitacaoSaldo);
        contaRepository.atualizar(conta);

        return CompletableFuture.completedFuture(solicitacaoSaldo);
    }

}
