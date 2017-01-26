package api.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.List;

public class ObjectJson {

    public static JsonNode toJson(Jsonable entidade) {
        ObjectNode root = Json.newObject();
        root.set(entidade.type(), Json.toJson(entidade));
        return root;
    }

    public static JsonNode toJson(String tipo, List<Jsonable> entidades) {
        ObjectNode root = Json.newObject();
        root.set(tipo, Json.toJson(entidades));
        JsonNode meta = root.set("meta", Json.newObject().set("total", Json.toJson(entidades.size())));
        return root;
    }
}
