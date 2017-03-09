package models.seguranca;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "registro_aplicativo")
public class RegistroAplicativo {

    @Id
    @SequenceGenerator(name="registro_aplicativo_id_seq", sequenceName = "registro_aplicativo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registro_aplicativo_id_seq")
    @Column(name = "registro_aplicativo_id",updatable = false)
    private Long id;
    @Column(name = "app_key")
    private String appKey;
    private String nome;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm;
    private String autor;
    private String email;
    private String telefone;
    private String descricao;
    @Column(name = "tenant_id")
    private Long tenant;
    private String scope;

    public RegistroAplicativo() {
    }

    public RegistroAplicativo(String appKey, String nome, Calendar criadoEm, String autor, String email, String telefone, String descricao, Long tenant, String scope) {
        appKey = appKey;
        this.nome = nome;
        this.criadoEm = criadoEm;
        this.autor = autor;
        this.email = email;
        this.telefone = telefone;
        this.descricao = descricao;
        this.tenant = tenant;
        this.scope = scope;
    }

    public String getappKey() {
        return appKey;
    }

    public void setappKey(String appKey) {
        appKey = appKey;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Calendar criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
