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
import models.financeiro.credito.EmprestimoSaldoCredito;
import models.financeiro.debito.AdicionarSaldoDebito;
import models.seguranca.Usuario;
import models.vo.Chave;
import models.vo.Tenant;
import repositories.ContaRepository;
import repositories.LancamentoRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AdicionarSaldoProcessador implements Processador<Chave, SolicitacaoSaldo> {

    public static final String REGRA = "financeiro.saldo";
    LancamentoRepository lancamentoRepository;
    ContaRepository contaRepository;

    @Inject
    public AdicionarSaldoProcessador(LancamentoRepository lancamentoRepository, ContaRepository contaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public CompletableFuture<SolicitacaoSaldo> executar(Chave chave, SolicitacaoSaldo solicitacaoSaldo, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(solicitacaoSaldo);
        }

        Optional<Conta> contaOptional = contaRepository.buscar(chave.getTenant(), solicitacaoSaldo.getSolicitante());

        if (!contaOptional.isPresent()){
            throw new ValidadorExcpetion("Conta não encontrada.");
        }

        Conta conta = contaOptional.get();

        solicitacaoSaldo.setConta(conta);

        AdicionarSaldoDebito lancamentoAdicionarSaldo = new AdicionarSaldoDebito();
        lancamentoAdicionarSaldo.setDataLancamento(Calendar.getInstance());
        lancamentoAdicionarSaldo.setValor(solicitacaoSaldo.getValor());
        lancamentoAdicionarSaldo.setOrigem(solicitacaoSaldo);
        BigDecimal saldoAtual = conta.getSaldo().getSaldo().add(solicitacaoSaldo.getValor());
        lancamentoAdicionarSaldo.setSaldo(new Saldo(saldoAtual));

        conta.addLancamento(lancamentoAdicionarSaldo);

        if (solicitacaoSaldo.getTipo() == SolicitacaoSaldo.TipoSolicitacaoSaldo.E){
            EmprestimoSaldoCredito lancamentoEmprestimo = new EmprestimoSaldoCredito();
            lancamentoEmprestimo.setDataLancamento(Calendar.getInstance());
            lancamentoEmprestimo.setValor(solicitacaoSaldo.getValor());
            lancamentoEmprestimo.setOrigem(solicitacaoSaldo);
            Saldo saldo = new Saldo();
            saldo.setEmprestimo(conta.getSaldo().getEmprestimo().add(solicitacaoSaldo.getValor()));
            lancamentoEmprestimo.setSaldo(saldo);
            conta.addLancamento(lancamentoAdicionarSaldo);
        }

        contaRepository.inserirSolicitacaoSaldo(solicitacaoSaldo);
        contaRepository.atualizar(conta);

        return CompletableFuture.completedFuture(solicitacaoSaldo);
    }

}
