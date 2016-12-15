package models.seguranca;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity(name="Usuario")
@NamedQuery(
        name="validarLoginSenha",
        query="SELECT us FROM Usuario us WHERE us.login = :login AND us.senha = :senha"
)
@Table(name="usuarios")
public class Usuario {

    @Id
    @SequenceGenerator(name="usuarios_idusuario_seq", sequenceName = "usuarios_idusuario_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_idusuario_seq")
    @Column(name = "idusuario",updatable = false)
    private Long id;

    @Column(name="idtenant")
    private Long idTenant;

    @Column(name="login")
    private String login;

    @Column(name="senha")
    private String senha;

    @OneToOne
    @JoinColumn(name="papeis_idpapel", foreignKey = @ForeignKey(name = "fk_papeis_idpapel"))
    private Papel papel;

    @OneToOne
    @JoinColumn(name="perfis_idperfil", foreignKey = @ForeignKey(name = "fk_perfis_idperfil"))
    private Perfil perfil;

    public Usuario(){

    }

    public Usuario(String login, String senha, Long idTenant) {
        this.login = login;
        this.senha = senha;
        this.idTenant = idTenant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTenant() {
        return idTenant;
    }

    public void setIdTenant(Long idTenant) {
        this.idTenant = idTenant;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
