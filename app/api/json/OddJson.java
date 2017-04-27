package api.json;

import models.apostas.Odd;
import models.apostas.OddConfiguracao;
import models.apostas.mercado.Mercado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OddJson implements Serializable, Convertable<Odd>, Jsonable {

    public static final String TIPO = "odds";

    public Long id;
    public String codigo;
    public String nome;
    public String descricao;
    public String abreviacao;
    public Boolean favorita;
    public Boolean visivel;
    public BigDecimal linha;
    public Long prioridade;
    public String mercado;

    public OddJson() {
    }

    public OddJson(Long id, String nome, String descricao, String abreviacao, Boolean favorita, Boolean visivel,
                   BigDecimal linha, Long prioridade, String mercado, String codigo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.abreviacao = abreviacao;
        this.favorita = favorita;
        this.visivel = visivel;
        this.linha = linha;
        this.prioridade = prioridade;
        this.mercado = mercado;
        this.codigo = codigo;
    }

    public static OddJson of(OddConfiguracao odd) {

        return new OddJson(odd.getIdOdd(), odd.getOdd().getNome(), odd.getOdd().getDescricao(), odd.getOdd().getAbreviacao(), odd.getFavorita(),
                odd.isVisivel(), odd.getLinhaFavorita(), odd.getPrioridade(), odd.getOdd().getMercado().getId(), odd.getOdd().getCodigo());
    }

    public static List<Jsonable> of(List<OddConfiguracao> odds) {
        return odds.stream().map( o -> OddJson.of(o) ).collect(Collectors.toList());
    }

    @Override
    public Odd to() {
        return new OddRef(this.id);
    }

    public OddConfiguracao toConfiguracao() {

        return new OddConfiguracao(favorita,
                (visivel ? OddConfiguracao.Situacao.A : OddConfiguracao.Situacao.I), linha, prioridade);
    }

    @Override
    public String type() {
        return TIPO;
    }

    public class OddRef extends Odd<String> {

        @Override
        public String getNome() {
            return "";
        }

        @Override
        public Mercado getMercado() {
            return null;
        }

        @Override
        public String getAbreviacao() {
            return "";
        }

        @Override
        public String getDescricao() {
            return "";
        }

        public OddRef(Long id) {
            this.id = id;
        }

        @Override
        public String getPosicao() {
            return "REF";
        }
    }
}
