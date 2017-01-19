package models.bilhetes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.seguranca.Usuario;
import models.serializacoes.CalendarDeserializer;
import models.serializacoes.CalendarSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "bilhetes")
public class Bilhete implements Serializable{


    public enum Situacao {

        /**
         * Situacao em que o bilhete ainda não foi atualizado após o fim da partida
         */
        A("ABERTO"),
        /**
         * Todos os palpites do bilhete estão corretos - [G]anhou
         */
        G("PREMIADO"),
        /**
         * Pelo menos um palpite do bilhete está errado
         */
        E("ERRADO"),
        /**
         * O prêmio do bilhete foi pago
         */
        P("PAGO"),
        /**
         * Desistiram do bilhete antes do início (?) da partida
         */
        C("CANCELADO");

        private String situacao;

        Situacao(String situacao) {

            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="bilhetes_bilhete_id_seq", sequenceName = "bilhetes_bilhete_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bilhetes_bilhete_id_seq")
    @Column(name = "bilhete_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @Column(name = "codigo")
    private String codigo;

    @OneToOne( cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING )
    @Column(name = "situacao")
    private Situacao situacao;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "valor_aposta")
    private BigDecimal valorAposta;

    @Column(name = "valor_premio")
    private BigDecimal valorPremio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alterado_em")
    private Calendar alteradoEm;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bilhete_id", nullable = false, updatable = false, insertable = false)
    private List<Palpite> palpites;

    public Bilhete() {

    }

    public Bilhete(Long tenant, String codigo, Usuario usuario, Situacao situacao, String cliente, BigDecimal valorAposta,
                   BigDecimal valorPremio, Calendar criadoEm, Calendar alteradoEm, List<Palpite> palpites) {
        this.tenant = tenant;
        this.codigo = codigo;
        this.usuario = usuario;
        this.situacao = situacao;
        this.cliente = cliente;
        this.valorAposta = valorAposta;
        this.valorPremio = valorPremio;
        this.criadoEm = criadoEm;
        this.alteradoEm = alteradoEm;
        this.palpites = palpites;
    }

    public Long getId() {
        return id;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValorAposta() {
        return valorAposta;
    }

    public void setValorAposta(BigDecimal valorAposta) {
        this.valorAposta = valorAposta;
    }

    public BigDecimal getValorPremio() {
        return valorPremio;
    }

    public void setValorPremio(BigDecimal valorPremio) {
        this.valorPremio = valorPremio;
    }

    public List<Palpite> getPalpites() {
        return palpites;
    }

    public void setPalpites(List<Palpite> palpites) {
        this.palpites = palpites;
    }

    @JsonDeserialize(using= CalendarDeserializer.class)
    public Calendar getCriadoEm() {
        return criadoEm;
    }

    @JsonSerialize(using= CalendarSerializer.class)
    public void setCriadoEm(Calendar criadoEm) {
        this.criadoEm = criadoEm;
    }

    @JsonDeserialize(using= CalendarDeserializer.class)
    public Calendar getAlteradoEm() {
        return alteradoEm;
    }

    @JsonSerialize(using= CalendarSerializer.class)
    public void setAlteradoEm(Calendar alteradoEm) {
        this.alteradoEm = alteradoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bilhete bilhete = (Bilhete) o;

        if (id != null ? !id.equals(bilhete.id) : bilhete.id != null) return false;
        return tenant != null ? tenant.equals(bilhete.tenant) : bilhete.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
