package models.vo;

public class Tenant {

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
