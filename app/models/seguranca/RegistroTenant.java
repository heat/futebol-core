package models.seguranca;

import models.vo.Tenant;
import models.vo.XFeedKey;

import java.util.Calendar;

public class RegistroTenant {

    private Long id;

    String nome;

    /**
     * Usuario administrador do sistema
     */
    Usuario administrador;

    /**
     * Chave para importacao
     */
    XFeedKey xfeedkey;


    Calendar criadoEm;

    public Tenant getTenant() {
        return Tenant.of(id);
    }
}
