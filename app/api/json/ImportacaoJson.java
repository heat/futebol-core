package api.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.Importacao.Importacao;
import models.serializacoes.CalendarDeserializer;
import models.serializacoes.CalendarSerializer;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImportacaoJson implements Jsonable {

    private static final long serialVersionUID = -868655637252381796L;

    private static final String TIPO = "importacaos";

    public UUID id;

    public String chave;

    public BigDecimal variacao;

    @JsonDeserialize(using= CalendarDeserializer.class)
    @JsonSerialize(using = CalendarSerializer.class)
    public Calendar alteradoEm;

    public Importacao.Situacao situacao;

    public ImportacaoJson() {
    }

    public ImportacaoJson(UUID id, String chave, BigDecimal variacao, Calendar alteradoEm, Importacao.Situacao situacao) {
        this.id = id;
        this.chave = chave;
        this.variacao = variacao;
        this.alteradoEm = alteradoEm;
        this.situacao = situacao;
    }

    @Override
    public String type() {
        return TIPO;
    }

    @Override
    public String getContext() {
        return null;
    }

    public static ImportacaoJson to(Importacao r) {
        return new ImportacaoJson(UUID.randomUUID(), r.getChave(), r.getVariacao(), r.getAlteradoEm(), r.getSituacao());
    }
}
