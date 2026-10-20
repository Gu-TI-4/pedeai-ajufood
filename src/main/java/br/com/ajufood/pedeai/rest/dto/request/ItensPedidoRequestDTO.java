package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItensPedidoRequestDTO {

    @Min(
            value = 1,
            message = "A quantidade deve ser maior que zero."
    )
    private int quantidade;

    @DecimalMin(
            value = "0.01",
            message = "O preço unitário deve ser maior que zero."
    )
    private BigDecimal precoUnitario;

    @DecimalMin(
            value = "0.01",
            message = "O subtotal deve ser maior que zero."
    )
    private BigDecimal subTotal;

    @NotNull(message = "O pedido é obrigatório.")
    private Integer pedidoId;

    @NotNull(message = "O produto é obrigatório.")
    private Integer produtoId;
}