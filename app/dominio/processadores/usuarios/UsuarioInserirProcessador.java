package dominio.processadores.usuarios;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Papel;
import models.seguranca.Usuario;
import models.vo.Tenant;
import repositories.PapelRepository;
import repositories.UsuarioRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class UsuarioInserirProcessador implements Processador<Tenant, Usuario> {

    public static final String REGRA = "Usuario.inserir";

    UsuarioRepository repository;
    PapelRepository papelRepository;

    @Inject
    public UsuarioInserirProcessador(UsuarioRepository repository, PapelRepository papelRepository) {
        this.repository = repository;
        this.papelRepository = papelRepository;
    }

    @Override
    public CompletableFuture<Usuario> executar(Tenant tenant, Usuario usuario, List<Validador> validadores) throws ValidadorExcpetion {
        for (Validador validador : validadores) {
            validador.validate(usuario);
        }

        Optional<Papel> papelOptional = papelRepository.buscar(tenant, usuario.getPapel().getNome());

        if (!papelOptional.isPresent()){
            throw new ValidadorExcpetion("Papel não existe");
        }
        usuario.setPapel(papelOptional.get());
        repository.inserir(tenant, usuario);
        return CompletableFuture.completedFuture(usuario);
    }
}
