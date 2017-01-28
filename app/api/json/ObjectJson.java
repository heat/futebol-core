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

    /**
     * Recebe duas listas, uma principal e uma adicional.
     * A informação com o total de entidades considera a lista 1 passada como parâmetro
     */
    public static JsonNode toJson(String tipo1, List<Jsonable> entidades1, String tipo2, List<Jsonable> entidades2) {
        ObjectNode root = Json.newObject();
        root.set(tipo1, Json.toJson(entidades1));
        root.set(tipo2, Json.toJson(entidades2));
        JsonNode meta = root.set("meta", Json.newObject().set("total", Json.toJson(entidades1.size())));
        return root;
    }
}
