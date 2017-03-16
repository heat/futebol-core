package models.seguranca;

public class Token {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String username;
    private String app_session;

    public Token(String access_token, String token_type, Long expires_in, String username, String appSession) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.username = username;
        this.app_session = appSession;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApp_session() {
        return app_session;
    }
}
