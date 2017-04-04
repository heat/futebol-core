package filters;

public class Paginacao {

    public final Integer page;
    public final Integer limit;

    public Paginacao(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
    }
}
