package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
@Entity
@Table(name = "produto")
public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome do produto é obrigatório!")
    @Column(name = "nome", nullable = false, length = 128)
    @Length(min = 3, max = 128)
    private String nome;

    @Column(name = "descricao", length = 256)
    @Length(max = 256)
    private String descricao;

    @DecimalMin(value = "0.01",
            message = "O preço deve ser maior que zero!")
    @Column(name = "preco", nullable = false, precision = 11, scale = 2)
    private BigDecimal preco;

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaProdutoID", nullable = false)
    private CategoriaProdutoModel categoriaProduto;
}