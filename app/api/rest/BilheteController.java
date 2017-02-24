package api.rest;

import api.json.BilheteJson;
import api.json.ObjectJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.PagarComissaoProcessador;
import dominio.processadores.bilhetes.BilheteAtualizarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import filters.FiltroBilhete;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
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
import repositories.BilheteRepository;
import repositories.TaxaRepository;
import repositories.UsuarioRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class BilheteController extends ApplicationController {

    BilheteRepository bilheteRepository;
    UsuarioRepository usuarioRepository;
    BilheteInserirProcessador inserirProcessador;
    BilheteAtualizarProcessador atualizarProcessador;
    PagarComissaoProcessador pagarComissaoProcessador;
    ValidadorRepository validadorRepository;
    TaxaRepository taxaRepository;

    @Inject
    public BilheteController(PlaySessionStore playSessionStore, BilheteRepository bilheteRepository, UsuarioRepository usuarioRepository,
                             BilheteInserirProcessador inserirProcessador, BilheteAtualizarProcessador atualizarProcessador,
                             PagarComissaoProcessador pagarComissaoProcessador, ValidadorRepository validadorRepository, TaxaRepository taxaRepository) {
        super(playSessionStore);
        this.bilheteRepository = bilheteRepository;
        this.usuarioRepository = usuarioRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.pagarComissaoProcessador = pagarComissaoProcessador;
        this.validadorRepository = validadorRepository;
        this.taxaRepository = taxaRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode json = request().body().asJson();
        BilheteJson bilheteJson = Json.fromJson(json.get("bilhetes"), BilheteJson.class);

        CommonProfile profile = getProfile().get();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(getTenant(), Long.parseLong(profile.getId()));
        if (!usuarioOptional.isPresent())
            return notFound("Usuário não encontrado!");
        final Usuario usuario = usuarioOptional.get();

        List<Taxa> taxas = taxaRepository.buscar(getTenant(), bilheteJson.palpites);
        List<Palpite> palpites = new ArrayList<>();
        BigDecimal valorPremio = bilheteJson.valorAposta;
        taxas.forEach(t -> {
            valorPremio.multiply(t.getTaxa());
            Palpite palpite = new Palpite(getTenant().get(), t, t.getTaxa(), Palpite.Status.A);
            palpites.add(palpite);
        });

        Bilhete bilhete = new Bilhete();
        bilhete.setSituacao(Bilhete.Situacao.A);
        bilhete.setCriadoEm(Calendar.getInstance());
        bilhete.setAlteradoEm(Calendar.getInstance());
        bilhete.setUsuario(usuario);
        bilhete.setValorAposta(bilheteJson.valorAposta);
        bilhete.setValorPremio(valorPremio);
        bilhete.setPalpites(palpites);
        bilhete.setCliente(bilheteJson.cliente);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), bilhete, validadores);
/*            bilhete = inserirProcessador.executar(getTenant(), bilhete, validadores)
                    .thenCompose(b -> {
                        return pagarComissaoProcessador.executar(getTenant().get(), b, Collections.emptyList());
                    } ).get();*/
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
/*            catch (InterruptedException e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        }*/
        ObjectJson.JsonBuilder<BilheteJson> builder = ObjectJson.build(BilheteJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
        builder.comEntidade(BilheteJson.of(bilhete, profile.getUsername()));
        JsonNode retorno = builder.build();
        return created(retorno);

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        JsonNode json = request().body().asJson();
        String cliente = json.findPath("cliente").asText();
        BigDecimal valorAposta = json.findPath("valorAposta").decimalValue();
        BigDecimal valorPremio = json.findPath("valorPremio").decimalValue();
        String situacao = json.findPath("situacao").asText();
        Bilhete bilhete = new Bilhete();
        bilhete.setSituacao(Bilhete.Situacao.valueOf(situacao));
        bilhete.setCliente(cliente);
        bilhete.setValorAposta(valorAposta);
        bilhete.setValorPremio(valorPremio);
        bilhete.setCriadoEm(Calendar.getInstance());
        bilhete.setAlteradoEm(Calendar.getInstance());
        CommonProfile profile = getProfile().get();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(getTenant(), Long.parseLong(profile.getId()));
        if (!usuarioOptional.isPresent())
            return notFound("Usuário não encontrado!");
        final Usuario usuario = usuarioOptional.get();
        bilhete.setUsuario(usuario);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, bilhete, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        ObjectJson.JsonBuilder<BilheteJson> builder = ObjectJson.build(BilheteJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
        builder.comEntidade(BilheteJson.of(bilhete));
        JsonNode retorno = builder.build();

        return ok(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos(String inicio, String termino, String aposta, String dono) {

        CommonProfile profile = getProfile().get();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(getTenant(), Long.parseLong(profile.getId()));
        if (!usuarioOptional.isPresent())
            return notFound("Usuário não encontrado!");
        final Usuario usuario = usuarioOptional.get();

        FiltroBilhete filtro = new FiltroBilhete(inicio, termino, aposta, dono);

        List<Bilhete> bilhetes = bilheteRepository.todos(getTenant(), usuario, filtro);
        ObjectJson.JsonBuilder<BilheteJson> builder = ObjectJson.build(BilheteJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        bilhetes.forEach(bilhete ->{
            builder.comEntidade(BilheteJson.of(bilhete, profile.getUsername()));
        });
        JsonNode retorno = builder.build();
        return ok(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(String codigo) {

        Optional<Bilhete> bilheteOptional = bilheteRepository.buscar(getTenant(), codigo);

        if (!bilheteOptional.isPresent()) {
            return notFound("Bilhete não encontrado!");
        }
        ObjectJson.JsonBuilder<BilheteJson> builder = ObjectJson.build(BilheteJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
        builder.comEntidade(BilheteJson.of(bilheteOptional.get()));
        JsonNode retorno = builder.build();

        return ok(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result cancelar(Long id) {
        try {
            bilheteRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }


    @Transactional
    public Result imprimir(String codigo) {
        Bilhete bilhete = new Bilhete();
        bilhete.setCodigo("TESTE");
        bilhete.setSituacao(Bilhete.Situacao.A);
        bilhete.setAlteradoEm(Calendar.getInstance());
        bilhete.setCliente("TESTE CLIENTE");
        bilhete.setCriadoEm(Calendar.getInstance());
        bilhete.setPalpites(
                Lists.newArrayList(new Palpite(Tenant.SYSBET.get(),
                        null,
                        BigDecimal.TEN,
                        Palpite.Status.A )));
        bilhete.setTenant(Tenant.SYSBET.get());
        bilhete.setUsuario(new Usuario());
        bilhete.setValorAposta(BigDecimal.TEN);
        bilhete.setValorPremio(BigDecimal.TEN.multiply(BigDecimal.TEN));
        return ok(views.txt.bilhetes.render(bilhete));
    }


}
