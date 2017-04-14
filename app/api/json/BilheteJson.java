package api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BilheteJson implements Jsonable, Convertable<Bilhete> {

    public static final String TIPO = "bilhetes";

    public String id;
    public String codigo;
    public Bilhete.Situacao situacao;
    public String cliente;
    public BigDecimal valorPremio;
    public BigDecimal valorAposta;
    public String criadoEm;
    public String login;
    public BigDecimal comissao;
    public List<Long> palpites;

    public BilheteJson() {
    }

    public BilheteJson(String id, String codigo, Bilhete.Situacao situacao, String cliente, BigDecimal valorPremio, BigDecimal valorAposta, String criadoEm, List<Palpite> palpites) {
        this.id = id;
        this.codigo = codigo;
        this.situacao = situacao;
        this.cliente = cliente;
        this.valorPremio = valorPremio;
        this.valorAposta = valorAposta;
        this.palpites = palpites.stream().map(p -> p.getId()).collect(Collectors.toList());
        this.criadoEm = criadoEm;
    }

    public BilheteJson(String id, String codigo, Bilhete.Situacao situacao, String cliente,BigDecimal valorPremio, BigDecimal valorAposta, String criadoEm, BigDecimal comissao, List<Palpite> palpites, String username) {
        this.id = id;
        this.codigo = codigo;
        this.situacao = situacao;
        this.cliente = cliente;
        this.valorPremio = valorPremio;
        this.valorAposta = valorAposta;
        this.palpites = palpites.stream().map(p -> p.getId()).collect(Collectors.toList());
        this.criadoEm = criadoEm;
        this.login = username;
        this.comissao = comissao;
    }

    @Override
    public Bilhete to() {
        return new Bilhete();
    }

    @Override
    public String type() {
        return TIPO;
    }


    public static BilheteJson of(Bilhete bilhete, boolean full) {

        if (full){
            return new BilheteJson(
                    String.valueOf(bilhete.getId()),
                    bilhete.getCodigo(),
                    bilhete.getSituacao(),
                    bilhete.getCliente(),
                    bilhete.getValorPremio(),
                    bilhete.getValorAposta(),
                    calendarToString(bilhete.getCriadoEm()),
                    bilhete.valorComissao(),
                    bilhete.getPalpites(),
                    bilhete.getUsuario().getLogin()
            );
        } else {
            return new BilheteJson(
                    String.valueOf(bilhete.getId()),
                    bilhete.getCodigo(),
                    bilhete.getSituacao(),
                    bilhete.getCliente(),
                    bilhete.getValorPremio(),
                    bilhete.getValorAposta(),
                    calendarToString(bilhete.getCriadoEm()),
                    bilhete.getPalpites()
            );
        }
    }

        public static BilheteJson of(Bilhete bilhete) {
            return of(bilhete, false);
    }

    @Deprecated
    public static BilheteJson of(Bilhete bilhete, String username) {
        return of(bilhete, true);
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
