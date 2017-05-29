package api.rest.financeiro;

import actions.TenantAction;
import api.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.bilhetes.BilheteAtualizarProcessador;
import dominio.processadores.bilhetes.BilheteCancelarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.processadores.financeiro.LancarVendaBilheteProcessador;
import dominio.processadores.financeiro.PagarComissaoProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import filters.FiltroBilhete;
import models.apostas.EventoAposta;
import models.apostas.OddConfiguracao;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.financeiro.Conta;
import models.financeiro.comissao.Comissao;
import models.financeiro.comissao.Comissionavel;
import models.financeiro.comissao.PlanoComissao;
import models.seguranca.Permissao;
import models.seguranca.Usuario;
import models.vo.Chave;
import models.vo.Tenant;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@With(TenantAction.class)
public class MovimentacaoController extends ApplicationController {

    BilheteRepository bilheteRepository;
    UsuarioRepository usuarioRepository;

    @Inject
    public MovimentacaoController(PlaySessionStore playSessionStore, BilheteRepository bilheteRepository,
                                  UsuarioRepository usuarioRepository, BilheteInserirProcessador inserirProcessador,
                                  BilheteAtualizarProcessador atualizarProcessador, BilheteCancelarProcessador cancelarProcessador,
                                  PagarComissaoProcessador pagarComissaoProcessador, ValidadorRepository validadorRepository,
                                  TaxaRepository taxaRepository, EventoApostaRepository eventoApostaRepository,
                                  LancarVendaBilheteProcessador lancarVendaBilheteProcessador,
                                  ContaRepository contaRepository) {
        super(playSessionStore);
        this.bilheteRepository = bilheteRepository;
        this.usuarioRepository = usuarioRepository;

    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos(String idUsuario) {

        CommonProfile profile = getProfile().get();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(getTenant(), Long.parseLong(idUsuario));
        if (!usuarioOptional.isPresent())
            return notFound("Usuário não encontrado!");
        final Usuario usuario = usuarioOptional.get();

        boolean full = profile.getPermissions().contains(Permissao.BILHETE_DETALHE);

        List<Bilhete> bilhetes = bilheteRepository.todos(getTenant(), usuario);
        ObjectJson.JsonBuilder<MovimentacaoJson> builder = ObjectJson.build(MovimentacaoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
        builder.comEntidade(MovimentacaoJson.of(bilhetes));

        JsonNode retorno = builder.build();
        return ok(retorno);
    }

}
