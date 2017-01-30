package api.json;

import models.bilhetes.Bilhete;

public class BilheteJson implements Jsonable, Convertable<Bilhete> {

    public static final String TIPO = "bilhetes";

    @Override
    public Bilhete to() {
        return new Bilhete();
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static BilheteJson of(Bilhete bilhete) {
        return new BilheteJson();
    }
}
