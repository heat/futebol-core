package models.financeiro.comissao;

import models.bilhetes.Bilhete;
import models.vo.Parametro;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representa um plano de pagamento de comissao gerador a partir de um evento específico.
 */
@Entity
@Table(name = "plano_comissao")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PlanoComissao {

    public enum EVENTO_COMISSAO {
        VENDA_BILHETE, PAGAMENTO_PREMIO, FECHAMENTO_CAIXA
    }

    @Id
    @SequenceGenerator(name="planos_comissao_id_seq", sequenceName = "planos_comissao_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planos_comissao_id_seq")
    @Column(name = "plano_comissao_id",updatable = false)
    private Long id;

    private String nome;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "plano_comissao_id", nullable = false, updatable = false, insertable = false)
    private List<ParametroComissao> parametros = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ParametroComissao> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametroComissao> parametros) {
        this.parametros = parametros;
    }

    public void addParametros(ParametroComissao parametro){
        this.parametros.add(parametro);
    }

    public abstract Optional<Comissao> calcular(Comissionavel<Bilhete> b, EVENTO_COMISSAO evento);
}