package dominio.processadores.usuarios;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Usuario;
import models.vo.Chave;
import repositories.UsuarioRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UsuarioAtualizarProcessador implements Processador<Chave, Usuario> {

    public static final String REGRA = "usuario.atualizar";

    UsuarioRepository repository;

    @Inject
    public UsuarioAtualizarProcessador(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Usuario> executar(Chave chave, Usuario Usuario, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(Usuario);
        }
        try{
            repository.atualizar(chave.getTenant(), chave.getId(), Usuario);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }
        return CompletableFuture.completedFuture(Usuario);
    }
}
