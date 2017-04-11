package dominio.processadores.usuarios;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.financeiro.Conta;
import models.seguranca.Papel;
import models.seguranca.Usuario;
import models.vo.Tenant;
import repositories.ContaRepository;
import repositories.PapelRepository;
import repositories.UsuarioRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class UsuarioInserirProcessador implements Processador<Tenant, Usuario> {

    public static final String REGRA = "Usuario.inserir";

    UsuarioRepository repository;
    PapelRepository papelRepository;
    ContaRepository contaRepository;

    @Inject
    public UsuarioInserirProcessador(UsuarioRepository repository, PapelRepository papelRepository, ContaRepository contaRepository) {
        this.repository = repository;
        this.papelRepository = papelRepository;
        this.contaRepository = contaRepository;
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
        try {
            Usuario usu = repository.inserir(tenant, usuario).get();
            Conta conta = new Conta(usu);
            contaRepository.inserir(tenant, conta);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(usuario);
    }
}
