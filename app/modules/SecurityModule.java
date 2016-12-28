package modules;

import authenticators.SysbetDatabaseAuthenticator;
import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.play.ApplicationLogoutController;
import org.pac4j.play.deadbolt2.Pac4jHandlerCache;
import org.pac4j.play.http.DefaultHttpActionAdapter;
import org.pac4j.play.store.PlayCacheStore;
import org.pac4j.play.store.PlaySessionStore;
import play.Configuration;
import play.Environment;
import play.db.jpa.JPAApi;

public class SecurityModule  extends AbstractModule {

    public static final String JWT_SALT = "12345678901234567890123456789012";
    private final Environment environment;
    private final Configuration configuration;

    public SecurityModule(
            Environment environment,
            Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bind(PlaySessionStore.class).to(PlayCacheStore.class);
        //deadbolt configuration
        bind(HandlerCache.class).to(Pac4jHandlerCache.class);


       SysbetDatabaseAuthenticator dbauth = new SysbetDatabaseAuthenticator(getProvider(JPAApi.class));

        final DirectBasicAuthClient directBasicAuthClient =
                new DirectBasicAuthClient(dbauth);

        HeaderClient headerClient = new HeaderClient("Authorization","Bearer ",
                new JwtAuthenticator(new SecretSignatureConfiguration(JWT_SALT),
                        new SecretEncryptionConfiguration(JWT_SALT)));


        ParameterClient parameterClient = new ParameterClient("token",
                new JwtAuthenticator(new SecretSignatureConfiguration(JWT_SALT),
                        new SecretEncryptionConfiguration(JWT_SALT)));

        parameterClient.setSupportGetRequest(true);


        final Clients clients = new Clients(parameterClient, headerClient, directBasicAuthClient);

        final Config config = new Config(clients);
        config.setHttpActionAdapter(new DefaultHttpActionAdapter());

        bind(Config.class).toInstance(config);

        //logout controller
        ApplicationLogoutController logoutController = new ApplicationLogoutController();
        logoutController.setDefaultUrl("/");
        bind(ApplicationLogoutController.class).toInstance(logoutController);
    }
}