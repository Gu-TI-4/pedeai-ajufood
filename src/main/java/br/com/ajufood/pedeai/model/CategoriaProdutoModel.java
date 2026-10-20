package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoriaProduto")
public class CategoriaProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome da categoria é obrigatório!")
    @Column(name = "nome", nullable = false, length = 128, unique = true)
    @Length(min = 3, max = 128,
            message = "O nome deverá ter no mínimo 3 e no máximo 128 caracteres.")
    private String nome;

    @Column(name = "descricao", length = 256)
    @Length(max = 256,
            message = "A descrição deverá ter no máximo 256 caracteres.")
    private String descricao;

    @OneToMany(mappedBy = "categoriaProduto")
    private List<ProdutoModel> produtos = new ArrayList<>();

    public void addProduto(ProdutoModel produto) {
        this.produtos.add(produto);
        produto.setCategoriaProduto(this);
    }

    public void removeProduto(ProdutoModel produto) {
        this.produtos.remove(produto);
        produto.setCategoriaProduto(null);
    }
}