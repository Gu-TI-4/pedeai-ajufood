package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoRequestDTO {
    @NotNull(message = "O ID do produto é obrigatório.")
    private Long produtoID;

    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private int quantidade;
}
