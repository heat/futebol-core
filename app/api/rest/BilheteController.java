package api.rest;

import api.json.BilheteJson;
import api.json.ObjectJson;
import api.json.OddJson;
import api.json.PalpiteJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.financeiro.LancarVendaBilheteProcessador;
import dominio.processadores.financeiro.PagarComissaoProcessador;
import dominio.processadores.bilhetes.BilheteAtualizarProcessador;
import dominio.processadores.bilhetes.BilheteCancelarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import filters.FiltroBilhete;
import models.financeiro.comissao.Comissao;
import models.apostas.EventoAposta;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.financeiro.Conta;
import models.financeiro.comissao.Comissionavel;
import models.financeiro.comissao.PlanoComissao;
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
import repositories.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BilheteController extends ApplicationController {

    BilheteRepository bilheteRepository;
    UsuarioRepository usuarioRepository;
    BilheteInserirProcessador inserirProcessador;
    BilheteAtualizarProcessador atualizarProcessador;
    BilheteCancelarProcessador cancelarProcessador;
    PagarComissaoProcessador pagarComissaoProcessador;
    ValidadorRepository validadorRepository;
    TaxaRepository taxaRepository;
    EventoApostaRepository eventoApostaRepository;
    LancarVendaBilheteProcessador lancarVendaBilheteProcessador;
    ContaRepository contaRepository;

    @Inject
    public BilheteController(PlaySessionStore playSessionStore, BilheteRepository bilheteRepository,
                             UsuarioRepository usuarioRepository, BilheteInserirProcessador inserirProcessador,
                             BilheteAtualizarProcessador atualizarProcessador, BilheteCancelarProcessador cancelarProcessador,
                             PagarComissaoProcessador pagarComissaoProcessador, ValidadorRepository validadorRepository,
                             TaxaRepository taxaRepository, EventoApostaRepository eventoApostaRepository,
                             LancarVendaBilheteProcessador lancarVendaBilheteProcessador,
                             ContaRepository contaRepository) {
        super(playSessionStore);
        this.bilheteRepository = bilheteRepository;
        this.usuarioRepository = usuarioRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.cancelarProcessador = cancelarProcessador;
        this.pagarComissaoProcessador = pagarComissaoProcessador;
        this.validadorRepository = validadorRepository;
        this.taxaRepository = taxaRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.lancarVendaBilheteProcessador = lancarVendaBilheteProcessador;
        this.contaRepository = contaRepository;
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

        List<Long> idsEventoAposta = palpites.stream().map(p -> p.getTaxa().getEventoAposta()).collect(Collectors.toList());
        List<EventoAposta> eventosAposta = eventoApostaRepository.buscar(getTenant().get(), idsEventoAposta);

        Bilhete bilhete = new Bilhete();
        bilhete.setSituacao(Bilhete.Situacao.A);
        bilhete.setCriadoEm(Calendar.getInstance());
        bilhete.setAlteradoEm(Calendar.getInstance());
        bilhete.setUsuario(usuario);
        bilhete.setValorAposta(bilheteJson.valorAposta);
        bilhete.setValorPremio(valorPremio);
        bilhete.setPalpites(palpites);
        bilhete.setCliente(bilheteJson.cliente);
        bilhete.setEventosAposta(eventosAposta);



        try {
            Tenant tenant = getTenant();

            List<Validador> validadores = validadorRepository.todos(tenant, BilheteInserirProcessador.REGRA);
            Optional<Conta> contaOptional = contaRepository.buscar(tenant, bilhete.getUsuario().getId());

            if (!contaOptional.isPresent()){
                return notFound("Usuário sem conta.");
            }

            Conta conta = contaOptional.get();
            bilhete = inserirProcessador.executar(getTenant(), bilhete, validadores)
                   .thenCompose((b) -> {
                       // lancamento de venda
                       List<Validador> _validadores = validadorRepository.todos(tenant, LancarVendaBilheteProcessador.REGRA);
                     return lancarVendaBilheteProcessador.executar(conta, b, _validadores);
                   }).thenApply( b -> {
                       // pagamento de comissao
                        List<Validador> _validadores = validadorRepository.todos(tenant, PagarComissaoProcessador.REGRA);

                        PlanoComissao planoComissao = usuario.getPlanoComissao();
                        // transforma o bilhete em um comissionavel
                        Comissionavel<Bilhete> comissaoBilhete = Comissionavel.aposta(b);
                        // calcula comissao
                        Optional<Comissao> comissao = planoComissao.calcular(comissaoBilhete, PlanoComissao.EVENTO_COMISSAO.VENDA_BILHETE);
                        if(comissao.isPresent()) {
                            // salva comissao somente se tiver comissao a pagar
                            pagarComissaoProcessador.executar(conta, comissao.get(), _validadores);
                        }
                      return b;
            }).get();

        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        } catch (Exception ex){
            return status(Http.Status.UNPROCESSABLE_ENTITY, ex.getMessage());
        }

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

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteCancelarProcessador.REGRA);
        Optional<Bilhete> bilhete = bilheteRepository.buscar(getTenant(), id);

        if (!bilhete.isPresent()) {
            return notFound("Bilhete não encontrado!");
        }

        try {
            Chave chave = Chave.of(getTenant(), id);
            cancelarProcessador.executar(chave, bilhete.get(), validadores);
            return noContent();
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
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

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscarPalpites(String codigo) {

        Optional<Bilhete> bilheteOptional = bilheteRepository.buscar(getTenant(), codigo);

        if (!bilheteOptional.isPresent()) {
            return notFound("Bilhete não encontrado!");
        }

        List<Palpite> palpites = bilheteOptional.get().getPalpites();
        List<Odd> odds = new ArrayList<>();

        ObjectJson.JsonBuilder<PalpiteJson> builder = ObjectJson.build(PalpiteJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        palpites.forEach(palpite ->{
            builder.comEntidade(PalpiteJson.of(palpite));
            odds.add(palpite.getTaxa().getOdd());
        });

        odds.forEach(odd -> builder.comRelacionamento(OddJson.TIPO, OddJson.of(odd)));

        return ok(builder.build());
    }


}
