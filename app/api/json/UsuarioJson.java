package api.json;

import models.eventos.Campeonato;
import models.seguranca.Usuario;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioJson implements Serializable, Convertable<Usuario>, Jsonable {

    public static final String TIPO = "usuarios";

    public final String id;
    public final String login;
    public final Usuario.Status status;

    public UsuarioJson(String id, String login, Usuario.Status status) {
        this.id = id;
        this.login = login;
        this.status = status;
    }

    public UsuarioJson() {
        id = null;
        login = null;
        this.status = null;
    }


    @Override
    public String type() {
        return TIPO;
    }

    public static UsuarioJson of(Usuario usuario) {

        return new UsuarioJson(String.valueOf(usuario.getId()), usuario.getLogin(), usuario.getStatus());
    }

    public static List<Jsonable> of(List<Usuario> usuarios) {
        return usuarios.stream().map( c -> UsuarioJson.of(c) ).collect(Collectors.toList());
    }

    @Override
    public Usuario to() {
        return null;
    }
}
