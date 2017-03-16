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
import java.util.Base64;
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


        Optional<String> appKeyOptional = Optional.ofNullable(ctx.request().getHeader(RegistroAplicativo.HEADER_NAME));

        if (!appKeyOptional.isPresent()){
        //TODO: Retornar bad request
        }

        JPAApi jpaApi = jpaApiProvider.get();

        Optional<RegistroAplicativo> aplicativo = jpaApi.withTransaction( em -> {

            TenantRepository tenantRepository = new TenantRepository(jpaApi);
            return tenantRepository.buscar(appKeyOptional.get());
        });

        if(!aplicativo.isPresent())
        {
            //TODO: Retornar bad request
        }

        RegistroAplicativo aplicativoIdentificado = aplicativo.get();

        Tenant tenant;
        if(aplicativoIdentificado.isSessao()) {
            // busca tenant da sessao
            Optional<String> appSession = Optional.ofNullable(ctx.request().getHeader(RegistroAplicativo.HEADER_SESSION));
            if(!appSession.isPresent())
            {
                //TODO: Retornar bad request
            }
            tenant = extractFrom(appSession.get());
        } else {
            tenant = Tenant.of(aplicativoIdentificado.getTenant());
        }

        ctx.args.put("tenant", tenant);

        return  delegate.call(ctx);
    }

    private Tenant extractFrom(String s) {
        //TODO desacoplar codigo criando um extrator de sessao
        String code = new String(Base64.getDecoder().decode(s.getBytes()));
        String t = code.split(":")[1];
        Long tenantId = Long.parseLong(t);
        return Tenant.of(tenantId);
    }
}
