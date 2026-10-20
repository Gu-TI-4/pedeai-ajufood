package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO {

    @NotBlank(message = "O status é obrigatório.")
    @Length(min = 3, max = 128,
            message = "O status deverá ter no mínimo 3 e no máximo 128 caracteres.")
    private String status;

    @DecimalMin(value = "0.0",
            inclusive = false,
            message = "O valor total deve ser maior que zero.")
    private BigDecimal valorTotal;

    @NotNull(message = "O cliente é obrigatório.")
    private Integer clienteId;

    @NotNull(message = "O endereço de entrega é obrigatório.")
    private Integer enderecoEntregaId;
}