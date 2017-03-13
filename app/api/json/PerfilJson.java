package api.json;

import models.apostas.Odd;
import models.seguranca.Perfil;

import java.io.Serializable;
import java.util.Locale;

public class PerfilJson implements Serializable, Convertable<Perfil>, Jsonable {

    public static final String TIPO = "perfil";

    public String id;
    public String email;
    public String primeiroNome;
    public String sobreNome;
    public String nomeExibicao;
    public Perfil.Genero genero;
    public Locale locale;
    public String localizacao;
    public String nomeUsuario;

    public PerfilJson() {
    }

    public PerfilJson(String email, String primeiroNome, String sobreNome, String nomeExibicao, Perfil.Genero genero, Locale locale, String localizacao, String nomeUsuario) {
        this.id = "0";
        this.email = email;
        this.primeiroNome = primeiroNome;
        this.sobreNome = sobreNome;
        this.nomeExibicao = nomeExibicao;
        this.genero = genero;
        this.locale = locale;
        this.localizacao = localizacao;
        this.nomeUsuario = nomeUsuario;
    }

    public static PerfilJson of(Perfil perfil) {

        return new PerfilJson(perfil.getEmail(), perfil.getPrimeiroNome(),
                perfil.getSobreNome(), perfil.getNomeExibicao(), perfil.getGenero(), perfil.getLocalidade(),
                perfil.getLocalizacao(), perfil.getLogin());
    }

    @Override
    public Perfil to() {
        return new Perfil(null, null, email, primeiroNome, sobreNome, nomeExibicao, genero, locale, null, null, localizacao);
    }

    @Override
    public String type() {
        return null;
    }
}
