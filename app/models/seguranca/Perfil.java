package models.seguranca;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "perfis")
public class Perfil {

    public enum Genero {
        MASCULINO, FEMININO
    }

    @Id
    @Column(name = "usuario_id",updatable = false, insertable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @Column(name = "email")
    private String email;

    @Column(name = "primeiro_nome")
    private String primeiroNome;

    @Column(name = "sobre_nome")
    private String sobrenome;

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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
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
}