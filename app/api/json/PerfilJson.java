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
    public String sobrenome;
    public String nomeExibicao;
    public Perfil.Genero genero;
    public Locale localidade;
    public String localizacao;
    public String nomeUsuario;

    public PerfilJson() {
    }

    public PerfilJson(String email, String primeiroNome, String sobrenome, String nomeExibicao, Perfil.Genero genero, Locale localidade, String localizacao, String nomeUsuario) {
        this.id = "0";
        this.email = email;
        this.primeiroNome = primeiroNome;
        this.sobrenome = sobrenome;
        this.nomeExibicao = nomeExibicao;
        this.genero = genero;
        this.localidade = localidade;
        this.localizacao = localizacao;
        this.nomeUsuario = nomeUsuario;
    }

    public static PerfilJson of(Perfil perfil) {

        return new PerfilJson(perfil.getEmail(), perfil.getPrimeiroNome(),
                perfil.getSobrenome(), perfil.getNomeExibicao(), perfil.getGenero(), perfil.getLocalidade(),
                perfil.getLocalizacao(), perfil.getLogin());
    }

    @Override
    public Perfil to() {
        return new Perfil(null, null, email, primeiroNome, sobrenome, nomeExibicao, genero, localidade, null, null, localizacao);
    }

    @Override
    public String type() {
        return null;
    }
}
