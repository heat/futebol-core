package dominio.processadores.usuarios;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Usuario;
import models.vo.Tenant;
import repositories.UsuarioRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class UsuarioInserirProcessador implements Processador<Tenant, Usuario> {

    public static final String REGRA = "Usuario.inserir";

    UsuarioRepository repository;

    @Inject
    public UsuarioInserirProcessador(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Usuario> executar(Tenant tenant, Usuario usuario, List<Validador> validadores) throws ValidadorExcpetion {
        for (Validador validador : validadores) {
            validador.validate(usuario);
        }

        repository.inserir(tenant, usuario);
        return CompletableFuture.completedFuture(usuario);
    }
}
