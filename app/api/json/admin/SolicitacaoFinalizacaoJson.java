package api.json.admin;


import api.json.Jsonable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.serializacoes.CalendarDeserializer;
import models.serializacoes.CalendarSerializer;

import java.util.Calendar;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitacaoFinalizacaoJson implements Jsonable {

    private static final long serialVersionUID = -3789621936970474759L;

    public static final String TIPO = "solicitacao-finalizacao";

    public String id;
    public Long evento;

    public String autor;

    /**
     * Datahora da realização do processamento
     */
    @JsonProperty("processadoEm")
    @JsonDeserialize(using= CalendarDeserializer.class)
    @JsonSerialize(using = CalendarSerializer.class)
    public Calendar processadoEm;

    @Override
    public String type() {
        return TIPO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SolicitacaoFinalizacaoJson that = (SolicitacaoFinalizacaoJson) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (evento != null ? !evento.equals(that.evento) : that.evento != null) return false;
        if (autor != null ? !autor.equals(that.autor) : that.autor != null) return false;
        return processadoEm != null ? processadoEm.equals(that.processadoEm) : that.processadoEm == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (evento != null ? evento.hashCode() : 0);
        result = 31 * result + (autor != null ? autor.hashCode() : 0);
        result = 31 * result + (processadoEm != null ? processadoEm.hashCode() : 0);
        return result;
    }
}
