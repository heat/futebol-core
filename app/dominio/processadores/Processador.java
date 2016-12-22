package dominio.processadores;

import models.vo.Tenant;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Processador<E> {

    public CompletableFuture<E> executar(Tenant tenant, E e, List<Validador> validators) throws ValidadorExcpetion;

}
