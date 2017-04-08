package dominio.processadores.financeiro;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.financeiro.Conta;
import models.financeiro.DocumentoTransferencia;
import models.financeiro.Saldo;
import models.financeiro.credito.TransferenciaCredito;
import models.financeiro.debito.TransferenciaDebito;
import models.vo.Chave;
import repositories.ContaRepository;
import repositories.LancamentoRepository;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TransferenciaProcessador implements Processador<Chave, DocumentoTransferencia> {

    public static final String REGRA = "financeiro.transferencia";
    LancamentoRepository lancamentoRepository;
    ContaRepository contaRepository;

    @Inject
    public TransferenciaProcessador(LancamentoRepository lancamentoRepository, ContaRepository contaRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public CompletableFuture<DocumentoTransferencia> executar(Chave chave, DocumentoTransferencia documentoTransferencia, List<Validador> validadores) throws ValidadorExcpetion {
        for (Validador validador : validadores) {
            validador.validate(documentoTransferencia);
        }

        Optional<Conta> contaOrigemOptional = contaRepository.buscarPorId(chave.getTenant(), documentoTransferencia.getOrigem());
        Optional<Conta> contaDestinoOptional = contaRepository.buscarPorId(chave.getTenant(), documentoTransferencia.getDestino());

        if (!contaOrigemOptional.isPresent() || !contaDestinoOptional.isPresent()){
            throw new ValidadorExcpetion("Conta não encontrada.");
        }

        Conta contaOrigem = contaOrigemOptional.get();
        Conta contaDestino = contaDestinoOptional.get();

        // Creditando na conta destino
        TransferenciaCredito credito = new TransferenciaCredito();
        credito.setDataLancamento(Calendar.getInstance());
        credito.setValor(documentoTransferencia.getValor());
        credito.setOrigem(documentoTransferencia);
        BigDecimal s = contaDestino.getSaldo().getSaldo();
        BigDecimal saldoAtualCredito = s.add(documentoTransferencia.getValor());
        credito.setSaldo(new Saldo(saldoAtualCredito));
        contaDestino.addLancamento(credito);

        //Debitando na conta origem
        TransferenciaDebito debito = new TransferenciaDebito();
        debito.setDataLancamento(Calendar.getInstance());
        debito.setValor(documentoTransferencia.getValor());
        debito.setOrigem(documentoTransferencia);
        BigDecimal saldoAtualDebito = contaOrigem.getSaldo().getSaldo().subtract(documentoTransferencia.getValor());
        debito.setSaldo(new Saldo(saldoAtualDebito));
        contaOrigem.addLancamento(debito);

        contaRepository.inserirTransferencia(documentoTransferencia);
        contaRepository.atualizar(contaDestino);
        contaRepository.atualizar(contaOrigem);

        return CompletableFuture.completedFuture(documentoTransferencia);
    }
}
