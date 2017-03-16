package authenticators;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.pac4j.core.exception.HttpAction;

import java.util.Optional;

public class SysbetExtractor implements CredentialsExtractor<SysbetAuthCredential> {

    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String GRANT_TYPE_PARAMETER = "grant_type";
    private static final String SCOPE_PARAMETER = "scope";
    private static final String PIN_PARAMETER = "pin";
    private static final String APP_CODE_PARAMETER = "X-AppCode";

    private final String clientName;

    public SysbetExtractor(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public SysbetAuthCredential extract(WebContext context) throws HttpAction {
        final Optional<String> username = Optional.ofNullable(context.getRequestParameter(USERNAME_PARAMETER));

        final Optional<String> password = Optional.ofNullable(context.getRequestParameter(PASSWORD_PARAMETER));

        final Optional<String> grantType = Optional.ofNullable(context.getRequestParameter(GRANT_TYPE_PARAMETER));

        final Optional<String> scope = Optional.ofNullable(context.getRequestParameter(SCOPE_PARAMETER));

        final Optional<String> pin = Optional.ofNullable(context.getRequestParameter(PIN_PARAMETER));

        final Optional<String> appCode = Optional.ofNullable(context.getRequestHeader(APP_CODE_PARAMETER));

        if(!appCode.isPresent() || !username.isPresent() || !password.isPresent())
            return null;

        return new SysbetAuthCredential(username.get(),
                password.get(),
                grantType.orElse("password"),
                scope.orElse("basic").split(" "),
                pin.orElse(""),
                appCode.get(),
                this.clientName);
    }
}
