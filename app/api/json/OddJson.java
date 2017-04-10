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

    public final Long id;
    public final String codigo;
    public final String nome;
    public final String descricao;
    public final String abreviacao;
    public final Boolean favorita;
    public final Boolean visivel;
    public final BigDecimal linha;
    public final Long prioridade;
    public final String mercado;

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
