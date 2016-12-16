package repositories;

import models.vo.Confirmacao;
import models.vo.Tenant;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository <K, E> {

    List<E> todos(Tenant tenant);

    Optional<E> buscar(Tenant tenant, K id);

    CompletableFuture<E> atualizar(Tenant tenant, K id, E updetable);

    CompletableFuture<E> inserir(Tenant tenant, E novo);

    CompletableFuture<Confirmacao> excluir(Tenant tenant, K id);
}
