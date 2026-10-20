package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itensPedido")

public class ItensPedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Min(value = 1, message = "A quantidade deve ser maior que zero!")
    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero!")
    @Column(name = "precoUnitario", nullable = false, precision = 11, scale = 2)
    private BigDecimal precoUnitario;

    @DecimalMin(value = "0.01", message = "O subtotal deve ser maior que zero!")
    @Column(name = "subTotal", nullable = false, precision = 11, scale = 2)
    private BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoID", nullable = false)
    private PedidoModel pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtoID", nullable = false)
    private ProdutoModel produto;
}