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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SenhaAtualizarProcessador implements Processador<Chave, String> {

    public static final String REGRA = "senha.atualizar";

    UsuarioRepository repository;

    @Inject
    public SenhaAtualizarProcessador(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<String> executar(Chave chave, String senha, List<Validador> validadores) throws ValidadorExcpetion {
        for (Validador validador : validadores) {
            validador.validate(senha);
        }
        try{
            Optional<Usuario> us  = repository.buscar(chave.getTenant(), chave.getId());

            Usuario usuario = us.get();
            usuario.setSenha(senha);

        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(senha);
    }
}
