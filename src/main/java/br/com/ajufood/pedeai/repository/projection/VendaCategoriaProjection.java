package br.com.ajufood.pedeai.repository.projection;

import java.math.BigDecimal;

public interface VendaCategoriaProjection {
    String getNomeCategoria();

    Long getTotalItens();

    Long getTotalPedidos();

    BigDecimal getTotalFaturado();

}
