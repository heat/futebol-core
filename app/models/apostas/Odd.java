package models.apostas;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table( name = "odds")
public class Odd implements Serializable{

    @Id
    @SequenceGenerator(name="odds_odd_id_seq", sequenceName = "odds_odd_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "odds_odd_id_seq")
    @Column(name = "odds_odd_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @Column(name = "nome")
    private String nome;

    @Column(name = "mercado")
    private String mercado;

    @Column(name = "tipo_linha")
    private char tipoLinha;

    @Column(name = "abreviacao")
    private String abreviacao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "prioridade")
    private Long prioridade;

    @Column(name = "posicao")
    private Long posicao;

    @OneToMany
    @JoinColumn(name = "odd_id")
    private List<Taxa> taxas;







}
