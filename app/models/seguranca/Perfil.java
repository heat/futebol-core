package models.seguranca;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Locale;

@Entity
@Table(name = "perfis")
public class Perfil implements Serializable {

    private static final long serialVersionUID = 8456064568826763970L;

    public enum Genero {
        MASCULINO, FEMININO
    }

    @Id
    @Column(name = "usuario_id",updatable = false, insertable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name="usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "email")
    private String email;

    @Column(name = "primeiro_nome")
    private String primeiroNome;

    @Column(name = "sobre_nome")
    private String sobreNome;

    @Column(name = "exibicao_nome")
    private String nomeExibicao;

    @Column(name = "genero")
    private Genero genero;

    @Column(name = "localidade")
    private Locale localidade;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name = "perfil_url")
    private String perfilUrl;

    @Column(name = "localizacao")
    private String localizacao;

    public Perfil() {

    }

    public Perfil(Long id, Usuario usuario, String email, String primeiroNome, String sobreNome, String nomeExibicao,
                  Genero genero, Locale localidade, String imagemUrl, String perfilUrl, String localizacao) {

        this.id = id;
        this.usuario = usuario;
        this.email = email;
        this.primeiroNome = primeiroNome;
        this.sobreNome = sobreNome;
        this.nomeExibicao = nomeExibicao;
        this.genero = genero;
        this.localidade = localidade;
        this.imagemUrl = imagemUrl;
        this.perfilUrl = perfilUrl;
        this.localizacao = localizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Locale getLocalidade() {
        return localidade;
    }

    public void setLocalidade(Locale localidade) {
        this.localidade = localidade;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getPerfilUrl() {
        return perfilUrl;
    }

    public void setPerfilUrl(String perfilUrl) {
        this.perfilUrl = perfilUrl;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin(){
        return this.usuario.getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Perfil perfil = (Perfil) o;

        if (id != null ? !id.equals(perfil.id) : perfil.id != null) return false;
        return usuario != null ? usuario.equals(perfil.usuario) : perfil.usuario == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        return result;
    }
}