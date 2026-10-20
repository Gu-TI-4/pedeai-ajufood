package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@Entity
@Table(name="endereco")

public class EnderecoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @NotBlank(message = "O endereço é obrigatório!")
    @Column(name = "endereco", nullable = false, length = 256)
    @Length(min = 3, max = 256, message = "O endereço deverá ter no mínimo 3 caracteres e no máximo 256")
    private String endereco;

    @Min(value = 1, message = "O número deve ser maior que zero")
    @Column(name = "numero", nullable = false)
    private int numero;

    @Column(name = "complemento", length = 256)
    @Length(max = 256, message = "O complemento deverá ter no máximo 256 caracteres!")
    private String complemento;


    @NotBlank(message = "O bairro é obrigatório!")
    @Column(name = "bairro", nullable = false, length = 128)
    @Length(min = 3, max = 128, message = "O Nome do bairro deverá ter no mínimo 3 caracteres e no máximo 128!")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatoria!")
    @Column(name = "cidade", nullable = false, length = 128)
    @Length(min = 3, max = 128, message = "A cidade deverá ter no mínimo 3 caracteres e no máximo 128!")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório!")
    @Column(name = "estado", nullable = false, length = 2)
    @Length(min = 2, max = 2, message = "A Sigla do estado deverá ter  2 caracteres!")
    private String estado;

    @NotBlank(message = "O CEP é obrigatório!")
    @Column(name = "cep", nullable = false, length = 8)
    @Length(min = 8, max = 8, message = "O CEP deverá ter 8 caracteres!")
    private String cep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteID", nullable = false)
    private ClienteModel cliente;

    @Column(name = "padrao", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean padrao = false;


}
