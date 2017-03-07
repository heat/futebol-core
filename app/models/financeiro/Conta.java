package models.financeiro;

import models.seguranca.Usuario;

import javax.persistence.*;
import java.util.List;

/**
 * Registro de agrega todos os lançamentos financeiros de um usuario
 */
@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @SequenceGenerator(name="contas_conta_id_seq", sequenceName = "contas_conta_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contas_conta_id_seq")
    @Column(name = "conta_id",updatable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario proprietario;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id", nullable = false, updatable = true, insertable = true)
    private List<Lancamento> lancamentos;

    public Conta() {
    }

    public Conta(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public void addLancamento(Lancamento lancamento){
        this.lancamentos.add(lancamento);
    }
}
