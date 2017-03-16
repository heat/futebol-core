package api.json;

import models.seguranca.Token;

public class TokenJson implements Convertable<Token>, Jsonable {

    public final String access_token;
    public final String token_type;
    public final Long expires_in;
    public final String username;
    public final String app_session;

    public TokenJson(String access_token, String token_type, Long expires_in, String username, String app_session) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.username = username;
        this.app_session = app_session;
    }

    @Override
    public Token to() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }

    public static TokenJson of(Token token) {
        return new TokenJson(token.getAccess_token(), token.getToken_type(), token.getExpires_in(), token.getUsername(), token.getApp_session());
    }
}
