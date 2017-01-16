package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.seguranca.Usuario;
import models.vo.Tenant;
import repositories.UsuarioRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BilheteInserirProcessador implements Processador<Tenant, Usuario>{

    public static final String REGRA = "bilhete.inserir";

    UsuarioRepository usuarioRepository;

    @Inject
    public BilheteInserirProcessador(UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    //TODO: criar gerador que gera codigo do bilhete e coloca na entidade codigo deve ser unico no formato XXX-XXXX-XXX-00
    @Override
    public CompletableFuture<Usuario> executar(Tenant tenant, Usuario usuario, List<Validador> validadores) throws ValidadorExcpetion {

        for(Bilhete bilhete : usuario.getBilhetes()){
            bilhete.setTenant(tenant.get());
        }

        usuarioRepository.atualizarUsuarioBilhete(tenant, usuario.getId(), usuario);
        return CompletableFuture.completedFuture(usuario);
    }
}
