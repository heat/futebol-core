package dominio.processadores;

import models.vo.Tenant;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProcessadorAtualizar<E> {

    public CompletableFuture<E> executar(Tenant tenant, E e, List<Validador> validators, Long idEntidade) throws ValidadorExcpetion;

}
