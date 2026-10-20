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
public class ProdutoCategoriaResponseDTO {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String nomeCategoria;
}
