package api.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.eventos.Evento;
import play.Configuration;
import play.api.Play;
import play.libs.Json;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectJson {

    public enum JsonBuilderPolicy {
        OBJECT, COLLECTION
    }

    public static <T extends Jsonable> JsonBuilder<T> build(String tipo, JsonBuilderPolicy policy) {
        switch (policy) {
            case OBJECT:
                return new ObjectJsonBuilder(tipo);
            case COLLECTION:
                return new CollectionJsonBuilder(tipo);
        }
        return new EmptyJsonBuilder<T>(tipo);
    }

    public static class EmptyJsonBuilder<T  extends Jsonable> extends JsonBuilder<T> {
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

    public static class ObjectJsonBuilder<T  extends Jsonable> extends JsonBuilder<T> {

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
            ObjectNode entidadeNode = Json.toJson(entidade).deepCopy();
            _buildLinks(entidadeNode);
            root.set(tipo, entidadeNode);
            root.set("meta", Json.toJson(metas));

            _buildRelacionamentos(root);
            return root;
        }
    }

    public static class CollectionJsonBuilder<T  extends Jsonable> extends JsonBuilder<T> {

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
            _buildRelacionamentos(root);
            ArrayNode arrayNode = Json.newArray();
            entidades.forEach( entidade -> {
                ObjectNode entidadeNode = Json.toJson(entidade).deepCopy();
                arrayNode.add(entidadeNode);
            });
            root.set(tipo, arrayNode);
            root.set("meta", Json.toJson(metas));
            return root;
        }
    }

    public static abstract class JsonBuilder<T  extends Jsonable> {

        protected final String tipo;

        protected Map<String, Object> metas = new HashMap<>();

        protected Map<String, List<Jsonable>> relacionamentos = new HashMap<>();

        protected List<RelationalJsonBuilder> relacionamentoBuilders = new ArrayList<>();

        protected Map<String, String> links = new HashMap<>();

        public Configuration conf = Play.current().injector().instanceOf(Configuration .class);

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

        protected ObjectNode _buildLinks(ObjectNode  elemento) {
            if(links.isEmpty())
                return elemento;
            ObjectNode _links = Json.newObject();

            String context = conf.getString("play.http.context");
            links.forEach((tipo, link) -> {
                String linkCompleto = context + "/" + link;
                _links.put(tipo, linkCompleto);
            });

            elemento.set("links", _links);
            return elemento;
        }

        protected  ObjectNode _buildRelacionamentos(ObjectNode root) {
                relacionamentos.forEach((tipo, rels) -> {
                    int size = rels.size();
                    if(size == 1) {
                        root.set(tipo, Json.toJson(rels.get(0)));
                    } else if (size > 1) {
                        root.set(tipo, Json.toJson(rels));
                    }
                });

                relacionamentoBuilders.
                        stream().map(this::_build)
                        .forEach(root::setAll);

                return root;
        }

        private ObjectNode _build(RelationalJsonBuilder builder) {
            return builder.build();
        }

        public JsonBuilder<T> comLink(String tipo, String link) {
            links.put(tipo, link);
            return this;
        }

        public <V extends Jsonable> RelationalJsonBuilder<V> comRelacionamento(String tipo) {
            RelationalJsonBuilder<V> rel = new RelationalJsonBuilder<V>(tipo);
            relacionamentoBuilders.add(rel);
            return rel;
        }

        public JsonBuilder<T> comRelacionamento(String tipo, Jsonable rel) {
            if(!relacionamentos.containsKey(tipo)) {
                ArrayList<Jsonable> elementos = new ArrayList<>();
                relacionamentos.put(tipo, elementos);
            }
            relacionamentos.get(tipo).add(rel);
            return this;
        }
    }

    public static class RelationalJsonBuilder<T extends Jsonable> extends JsonBuilder<T> {

        List<T> jsons = new ArrayList<T>();

        public RelationalJsonBuilder(String tipo) {
            super(tipo);
        }

        @Override
        public RelationalJsonBuilder<T> comEntidade(T entidade) {
            jsons.add(entidade);
            return this;
        }

        @Override
        public ObjectNode build() {
            ObjectNode root = Json.newObject();
            ArrayNode nodes = Json.newArray();
            jsons.stream().map(Json::toJson).forEach(nodes::add);
            root.set(tipo, nodes);
            return root;
        }
    }
}
