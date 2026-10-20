package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoComItensRequestDTO {

    @NotNull(message = "O cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "O endereço de entrega é obrigatório.")
    private Long enderecoEntregaId;

    @NotEmpty(message = "A lista de itens não pode estar vazia.")
    @Valid
    private List<ItemPedidoRequestDTO> itens;
}