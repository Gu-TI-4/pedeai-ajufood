package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteComEnderecoRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Length(min = 2, max = 128,
            message = "O nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF inválido!")
    @Length(min = 11, max = 11,
            message = "O CPF deverá ter obrigatoriamente 11 dígitos.")
    private String cpf;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido!")
    @Length(min = 3, max = 256,
            message = "O e-mail deverá ter no mínimo 3 caracteres e no máximo 256 caracteres.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Length(min = 10, max = 11,
            message = "O telefone deverá ter entre 10 e 11 dígitos.")
    private String telefone;

    @Valid
    private EnderecoSemClienteRequestDTO endereco;
}