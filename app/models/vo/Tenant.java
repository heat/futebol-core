package models.vo;

public class Tenant {

    public static final Tenant SYSBET = of(1L);
    public static final String NAME = "TENANT";
    final Long id;

    public Tenant(Long id) {
        this.id = id;
    }

    public Long  get() {
        return this.id;
    }

    public static Tenant of(Long id) {
        return new Tenant(id);
    }
}
