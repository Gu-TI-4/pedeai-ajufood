package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pagamento")

public class PagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @DecimalMin(value = "0.01", message = "O valor pago deve ser maior que zero!")
    @Column(name = "valorPago", nullable = false, precision = 11, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "dataHora", nullable = false)
    private LocalDateTime dataHora;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoID", nullable = false)
    private PedidoModel pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formaPagamentoID", nullable = false)
    private FormaPagamentoModel formaPagamento;
}