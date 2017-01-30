package api.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectJson {

    public enum JsonBuilderPolicy {
        OBJECT, COLLECTION
    }

    public static <T> JsonBuilder<T> build(String tipo, JsonBuilderPolicy policy) {
        switch (policy) {
            case OBJECT:
                return new ObjectJsonBuilder(tipo);
            case COLLECTION:
                return new CollectionJsonBuilder(tipo);
        }
        return new EmptyJsonBuilder<T>(tipo);
    }

    public static class EmptyJsonBuilder<T> extends JsonBuilder<T> {
        public EmptyJsonBuilder(String tipo) {
            super(tipo);
        }

        @Override
        public JsonBuilder<T> comEntidade(T entidade) {
            return this;
        }

        @Override
        public JsonNode build() {
            return Json.newObject();
        }
    }

    public static class ObjectJsonBuilder<T> extends JsonBuilder<T> {

        T entidade;

        public ObjectJsonBuilder(String tipo) {
            super(tipo);
        }

        @Override
        public JsonBuilder<T> comEntidade(T entidade) {
            this.entidade = entidade;
            return this;
        }

        @Override
        public JsonNode build() {
            ObjectNode root = Json.newObject();
            root.set(tipo, Json.toJson(entidade));
            root.set("meta", Json.toJson(metas));
            return root;
        }
    }

    public static class CollectionJsonBuilder<T> extends JsonBuilder<T> {


        private List<T> entidades = new ArrayList<T>();

        public CollectionJsonBuilder(String tipo) {
            super(tipo);
        }

        @Override
        public JsonBuilder<T> comEntidade(T entidade) {
            this.entidades.add(entidade);
            return this;
        }

        @Override
        public JsonNode build() {
            this.comMetaData("total", this.entidades.size());
            ObjectNode root = Json.newObject();

            root.set(tipo, Json.toJson(entidades));
            root.set("meta", Json.toJson(metas));
            return root;
        }
    }

    public static abstract class JsonBuilder<T> {

        protected final String tipo;

        protected Map<String, Object> metas = new HashMap<>();

        public JsonBuilder(String tipo) {
            this.tipo = tipo;
        }

        private void incluiMeta(String nome, Object valor) {
            this.metas.put(nome, valor);
        }
        public JsonBuilder<T> comMetaData(String nome, Integer valor) {
            incluiMeta(nome, valor);
            return this;
        }

        public JsonBuilder<T> comMetaData(String nome, String valor) {
            incluiMeta(nome, valor);
            return this;
        }

        public JsonBuilder<T> comMetaData(String nome, Boolean valor) {
            incluiMeta(nome, valor);
            return this;
        }

        public JsonBuilder<T> comMetaData(String nome, JsonNode valor) {
            incluiMeta(nome, valor);
            return this;
        }

        public abstract JsonBuilder<T> comEntidade(T entidade);

        public abstract JsonNode build();
    }
}
