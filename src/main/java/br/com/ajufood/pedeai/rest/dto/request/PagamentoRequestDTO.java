package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
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
public class PagamentoRequestDTO {

    @DecimalMin(
            value = "0.01",
            message = "O valor pago deve ser maior que zero."
    )
    private BigDecimal valorPago;

    @NotNull(message = "O pedido é obrigatório.")
    private Integer pedidoId;

    @NotNull(message = "A forma de pagamento é obrigatória.")
    private Integer formaPagamentoId;
}