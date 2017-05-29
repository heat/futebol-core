package api.json;

import models.bilhetes.Bilhete;
import models.bilhetes.Pin;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MovimentacaoJson implements Jsonable, Convertable<String> {

    public static final String TIPO = "movimentacaos";

    public String id;
    public Integer bilhetes;
    public Integer abertos;
    public Integer premiados;

    public MovimentacaoJson() {
    }

    public MovimentacaoJson(String id, Integer bilhetes, Integer abertos, Integer premiados) {
        this.id = id;
        this.bilhetes = bilhetes;
        this.abertos = abertos;
        this.premiados = premiados;
    }

    @Override
    public String to() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }

    public static MovimentacaoJson of(List<Bilhete> bilhetes) {

        Integer abertos = 0;
        Integer premiados = 0;

        for (Bilhete bilhete : bilhetes){
            if (bilhete.getSituacao() == Bilhete.Situacao.C)
                premiados += 1;
            else if (bilhete.getSituacao() == Bilhete.Situacao.A)
                abertos += 1;
        }

        return new MovimentacaoJson(
                UUID.randomUUID().toString(),
                bilhetes.size(),
                abertos,
                premiados);
    }
}
