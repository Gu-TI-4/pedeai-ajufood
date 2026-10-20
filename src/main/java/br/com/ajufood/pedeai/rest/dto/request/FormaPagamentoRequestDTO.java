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
public class FormaPagamentoRequestDTO {

    @NotBlank(message = "O nome da forma de pagamento é obrigatório.")
    @Length(min = 3, max = 128,
            message = "O nome deverá ter no mínimo 3 caracteres e no máximo 128.")
    private String nome;

    @Length(max = 256,
            message = "A descrição deverá ter no máximo 256 caracteres.")
    private String descricao;
}