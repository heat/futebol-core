package authenticators;

import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;

/**
 * Created by nuvemhost on 13/12/2016.
 */
public class JwtDirectBasicAuthClient extends DirectBasicAuthClient {


    public JwtDirectBasicAuthClient(JwtAuthenticator jwtAuthenticator) {
        super(jwtAuthenticator);
    }

    @Override
    public String getName() {
        return super.getName();
    }
}