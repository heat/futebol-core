package api.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import play.Configuration;
import play.api.Play;

import java.io.Serializable;

/**
 * Marca uma classe como passível de ser convertida em json
 */
public interface Jsonable extends Serializable {

    String type();

    @JsonIgnore
    default String getContext() {
        return Play.current().injector().instanceOf(Configuration .class).getString("play.http.context");
    }
}
