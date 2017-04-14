package dominio.processadores.Importacao;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.Importacao.Importacao;
import models.bilhetes.Bilhete;
import models.vo.Tenant;
import repositories.BilheteRepository;
import repositories.ImportacaoRepository;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ImportacaoInserirProcessador implements Processador<Tenant, Importacao>{

    public static final String REGRA = "importacao.inserir";

    ImportacaoRepository importacaoRepository;

    @Inject
    public ImportacaoInserirProcessador(ImportacaoRepository importacaoRepository) {

        this.importacaoRepository = importacaoRepository;
    }

    @Override
    public CompletableFuture<Importacao> executar(Tenant tenant, Importacao importacao, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador<Importacao> validador: validadores) {
            validador.validate(importacao);
        }
        if (importacaoRepository.contains(importacao)) {
            importacao.setAlteradoEm(Calendar.getInstance());
            return importacaoRepository.atualizar(tenant, importacao.getId(), importacao);
        }
        return importacaoRepository.inserir(tenant, importacao) ;
    }
}
