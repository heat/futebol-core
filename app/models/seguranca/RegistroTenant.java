package models.seguranca;

import models.vo.Tenant;
import models.vo.XFeedKey;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "registro_tenant")
public class RegistroTenant {

    @Id
    @SequenceGenerator(name="registro_tenant_id_seq", sequenceName = "registro_tenant_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registro_tenant_id_seq")
    @Column(name = "registro_tenant_id", updatable = false)
    private Long id;

    private String nome;

    /**
     * Usuario administrador do sistema
     */
    @Column(name = "usuario_id")
    private Usuario administrador;

    /**
     * Chave para importacao
     */
    @Column(name = "feed_key")
    @Embedded
    private XFeedKey xfeedkey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public XFeedKey getXfeedkey() {
        return xfeedkey;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

    public Tenant getTenant() {
        return Tenant.of(id);
    }
}
