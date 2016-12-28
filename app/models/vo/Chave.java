package models.vo;

import java.io.Serializable;

public class Chave implements Serializable {

    final Tenant tenant;

    final Long id;

    private Chave(Tenant tenant, Long id) {
        this.tenant = tenant;
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Long getId() {
        return id;
    }

    public static Chave of(Long tenantId, Long id) {
        return of(Tenant.of(tenantId), id);
    }

    public static Chave of(Tenant tenant, Long id) {
        return new Chave(tenant, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chave chave = (Chave) o;

        if (!tenant.equals(chave.tenant)) return false;
        return id.equals(chave.id);

    }

    @Override
    public int hashCode() {
        int result = tenant.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
