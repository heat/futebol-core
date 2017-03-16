package authenticators;

import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;

import java.util.Arrays;

public class SysbetAuthCredential extends UsernamePasswordCredentials {

    private static final long serialVersionUID = -3531704623896394233L;

    private String grantType;

    private String[] scope;

    private String pin;

    private String appCode;


    public SysbetAuthCredential(String username,
                                String password,
                                String grantType,
                                String[] scope,
                                String pin,
                                String appCode,
                                String clientName) {
        super(username, password, clientName);
        this.grantType = grantType;
        this.scope = scope;
        this.pin = pin;
        this.appCode = appCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGrantType() {
        return grantType;
    }

    public String[] getScope() {
        return scope;
    }

    public String getPin() {
        return pin;
    }

    public String getAppCode() {
        return appCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysbetAuthCredential that = (SysbetAuthCredential) o;

        String username = getUsername();
        String password = getPassword();

        if (username != null ? !username.equals(that.getUsername()) : that.getUsername() != null) return false;
        if (password != null ? !password.equals(that.getPassword()) : that.getPassword() != null) return false;
        if (grantType != null ? !grantType.equals(that.grantType) : that.grantType != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(scope, that.scope)) return false;
        if (pin != null ? !pin.equals(that.pin) : that.pin != null) return false;
        return appCode != null ? appCode.equals(that.appCode) : that.appCode == null;

    }

    @Override
    public int hashCode() {
        String username = getUsername();
        String password = getPassword();

        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (grantType != null ? grantType.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(scope);
        result = 31 * result + (pin != null ? pin.hashCode() : 0);
        result = 31 * result + (appCode != null ? appCode.hashCode() : 0);
        return result;
    }
}
