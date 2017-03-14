package actions;

import models.seguranca.RegistroAplicativo;
import models.vo.Tenant;
import org.pac4j.core.exception.HttpAction;
import play.db.jpa.JPAApi;
import play.mvc.Http;
import play.mvc.Result;
import repositories.TenantRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class TenantAction extends play.mvc.Action.Simple {

    Provider<JPAApi> jpaApiProvider;

    @Inject
    public TenantAction(Provider<JPAApi> jpaApiProvider) {
        this.jpaApiProvider = jpaApiProvider;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {

        //TODO: Retornar bad request nos comentarios

        Optional<String> appKeyOptional = Optional.ofNullable(ctx.request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
//            return badRequest("Key not found.");
        }

        JPAApi jpaApi = jpaApiProvider.get();
        TenantRepository tenantRepository = new TenantRepository(jpaApi);

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
  //          return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());

        ctx.args.put("tenant", tenant);

        return  delegate.call(ctx);
    }
}
