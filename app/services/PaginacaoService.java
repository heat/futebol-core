package services;

import filters.Paginacao;

import javax.persistence.Query;

public class PaginacaoService {

    public static Query incluiPaginacao(Query query, Paginacao paginacao){

        if (paginacao.limit == 0){
            return query;
        } else if (paginacao.page == 0) {
            return query;
        } else {
            return query.setFirstResult((paginacao.page - 1) * paginacao.limit).setMaxResults(paginacao.limit);
        }
    }
}
