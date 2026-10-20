package br.com.ajufood.pedeai.rest.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ItensPedidoResumoResponseDTO {
    private String nomeProduto;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subTotal;


}
