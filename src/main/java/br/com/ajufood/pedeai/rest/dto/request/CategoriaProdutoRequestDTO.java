package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaProdutoRequestDTO {

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Length(min = 3, max = 128,
            message = "O nome deverá ter no mínimo 3 e no máximo 128 caracteres.")
    private String nome;

    @Length(max = 256,
            message = "A descrição deverá ter no máximo 256 caracteres.")
    private String descricao;
}