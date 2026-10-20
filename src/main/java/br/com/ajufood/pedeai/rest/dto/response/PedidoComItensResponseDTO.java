package br.com.ajufood.pedeai.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoComItensResponseDTO {

    private Long id;
    private LocalDateTime dataHora;
    private String status;
    private BigDecimal valorTotal;
    private String bairro;
    private String cidade;
    private List<ItemPedidoResponseDTO> itens;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemPedidoResponseDTO {
        private String nome;
        private int quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subTotal;
    }
}