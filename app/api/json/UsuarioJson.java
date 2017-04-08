package api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.eventos.Campeonato;
import models.seguranca.Papel;
import models.seguranca.Perfil;
import models.seguranca.Usuario;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioJson implements Serializable, Convertable<Usuario>, Jsonable {

    public static final String TIPO = "usuarios";

    public final String id;
    public final String login;
    public final String senha;
    public final String papel;
    public final Usuario.Status status;
    public final String primeiroNome;
    public final String segundoNome;
    public final String nomeExibicao;
    public final Perfil.Genero genero;
    public final Locale localidade;
    public final String localizacao;
    public final String email;

    public UsuarioJson(String id, String login, String senha, String papel, Usuario.Status status, String primeiroNome, String segundoNome, String nomeExibicao, Perfil.Genero genero, Locale localidade, String localizacao, String email) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.papel = papel;
        this.status = status;
        this.primeiroNome = primeiroNome;
        this.segundoNome = segundoNome;
        this.nomeExibicao = nomeExibicao;
        this.genero = genero;
        this.localidade = localidade;
        this.localizacao = localizacao;
        this.email = email;
    }

    public UsuarioJson() {
        this.id = null;
        this.login = null;
        this.status = null;
        this.senha = null;
        this.papel = null;
        this.primeiroNome = null;
        this.segundoNome = null;
        this.nomeExibicao = null;
        this.genero = null;
        this.localidade = null;
        this.localizacao = null;
        this.email = null;
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static UsuarioJson of(Usuario usuario) {

        return new UsuarioJson(String.valueOf(usuario.getId()), usuario.getLogin(), usuario.getSenha(),
                usuario.getPapel().getNome(), usuario.getStatus(), usuario.getPerfil().getPrimeiroNome(),
                usuario.getPerfil().getSobreNome(), usuario.getPerfil().getNomeExibicao(), usuario.getPerfil().getGenero(),
                usuario.getPerfil().getLocalidade(), usuario.getPerfil().getLocalizacao(), usuario.getPerfil().getEmail());
    }

    public static List<Jsonable> of(List<Usuario> usuarios) {
        return usuarios.stream().map( c -> UsuarioJson.of(c) ).collect(Collectors.toList());
    }

    @Override
    public Usuario to() {

        Perfil perfil = new Perfil(email, primeiroNome, segundoNome, nomeExibicao, genero, localidade, localizacao);

        Usuario usuario = new Usuario(login, senha, new Papel(papel));
        usuario.setPerfil(perfil);

        return usuario;
    }
}
