package dominio.processadores;

import models.bilhetes.Bilhete;
import models.eventos.Campeonato;
import repositories.CampeonatoRepository;
import validators.Validator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BilheteInserirProcessador implements Processador<Bilhete> {


    public static final String REGRA = "bilhete.inserir";
    List<Validator> validators;

    CampeonatoRepository repository;

    public BilheteInserirProcessador(List<Validator> validators, CampeonatoRepository repository) {
        this.validators = validators;
        this.repository = repository;
    }

    public CompletableFuture<Bilhete> executar(Bilhete bilhete) {
        for (Validator validator: validators) {
            validator.validate(bilhete);
        }

        System.out.println("bilhete validado e agora criado");

        return CompletableFuture.completedFuture(bilhete);
    }

}
