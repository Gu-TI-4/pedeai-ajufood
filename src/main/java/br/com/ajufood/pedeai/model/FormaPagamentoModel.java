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
@Table(name = "formaPagamento")

public class FormaPagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "O nome da forma de pagamento é obrigatório!")
    @Column(name = "nome", nullable = false, length = 128, unique = true)
    @Length(min = 3, max = 128,
            message = "O nome deverá ter no mínimo 3 caracteres e no máximo 128!")
    private String nome;

    @Column(name = "descricao", length = 256)
    @Length(max = 256,
            message = "A descrição deverá ter no máximo 256 caracteres!")
    private String descricao;
    @OneToMany(mappedBy = "formaPagamento")
    private List<PagamentoModel> pagamentos = new ArrayList<>();

    public void addPagamento(PagamentoModel pagamento) {
        this.pagamentos.add(pagamento);
        pagamento.setFormaPagamento(this);
    }

    public void removePagamento(PagamentoModel pagamento) {
        this.pagamentos.remove(pagamento);
        pagamento.setFormaPagamento(null);
    }
}
