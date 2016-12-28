package dominio.processadores;

import models.vo.Tenant;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProcessadorInserir<E> {

    public CompletableFuture<E> executar(Tenant tenant, E e, List<Validador> validadores) throws ValidadorExcpetion;

}
