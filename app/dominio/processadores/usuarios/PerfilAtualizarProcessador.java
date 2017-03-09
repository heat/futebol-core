package dominio.processadores.usuarios;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Perfil;
import models.seguranca.Usuario;
import models.vo.Chave;
import repositories.UsuarioRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PerfilAtualizarProcessador implements Processador<Chave, Perfil> {

    public static final String REGRA = "perfil.atualizar";

    UsuarioRepository repository;

    @Inject
    public PerfilAtualizarProcessador(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Perfil> executar(Chave chave, Perfil perfil, List<Validador> validadores) throws ValidadorExcpetion {
        for (Validador validador : validadores) {
            validador.validate(perfil);
        }
        try{
            Optional<Usuario> us  = repository.buscar(chave.getTenant(), chave.getId());

            Usuario usuario = us.get();
            Perfil p = usuario.getPerfil();

            p.setEmail(perfil.getEmail());
            p.setGenero(perfil.getGenero());
            p.setLocalidade(perfil.getLocalidade());
            p.setLocalizacao(perfil.getLocalizacao());
            p.setNomeExibicao(perfil.getNomeExibicao());
            p.setPrimeiroNome(perfil.getPrimeiroNome());
            p.setSobreNome(perfil.getSobreNome());

        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(perfil);
    }
}
