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
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Length(min = 3, max = 128,
            message = "O nome deverá ter no mínimo 3 e no máximo 128 caracteres.")
    private String nome;

    @Length(max = 256,
            message = "A descrição deverá ter no máximo 256 caracteres.")
    private String descricao;

    @DecimalMin(value = "0.01",
            message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    private boolean disponivel;

    @NotNull(message = "A categoria do produto é obrigatória.")
    private Integer categoriaProdutoId;
}