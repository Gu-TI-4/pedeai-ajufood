package br.com.ajufood.pedeai.model;

import br.com.ajufood.pedeai.util.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")

public class PedidoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "dataHora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPedido status;

    @DecimalMin(value = "0.0", inclusive = false, message = "O valor total deve ser maior que zero")
    @Column(name = "valorTotal", nullable = false, precision = 11, scale = 2)
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteID", nullable = false)
    private ClienteModel cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enderecoEntregaID", nullable = false)
    private EnderecoModel enderecoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItensPedidoModel> itens = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private PagamentoModel pagamento;

    public void addItem(ItensPedidoModel item) {
        this.itens.add(item);
        item.setPedido(this);
    }

    public void removeItem(ItensPedidoModel item) {
        this.itens.remove(item);
        item.setPedido(null);
    }



}

