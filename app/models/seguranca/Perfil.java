package models.seguranca;


import models.seguranca.Genero;
import org.pac4j.core.profile.Gender;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "perfis")
public class Perfil {


    @Id
    @SequenceGenerator(name = "perfis_idperfil_seq", sequenceName = "perfis_idperfil_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfis_idperfil_seq")
    @Column(name = "idperfil",updatable = false)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "primeironome")
    private String primeiroNome;

    @Column(name = "sobrenome")
    private String sobrenome;

    /*
    Nome que será exibido no perfil do usuário. Pode ser o nomeUsuario ou primeiroNome + " " + sobrenome.
     */
    @Column(name = "nomeexibicao")
    private String nomeExibicao;

    /*
    Pode ser um usuário específico ou login.
     */
    @Column(name = "nomeusuario")
    private String nomeUsuario;

    @Column(name = "genero")
    private Genero genero;

    @Column(name = "localidade")
    private Locale localidade;

    @Column(name = "imagemurl")
    private String imagemUrl;

    @Column(name = "perfilurl")
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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
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
}