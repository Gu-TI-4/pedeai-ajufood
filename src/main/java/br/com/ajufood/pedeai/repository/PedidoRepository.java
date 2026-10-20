package br.com.ajufood.pedeai.repository;
import br.com.ajufood.pedeai.repository.projection.VendaCategoriaProjection;
import java.time.LocalDateTime;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {
    @Query("""
       SELECT p
       FROM PedidoModel p
       WHERE p.cliente.id = :clienteId
       AND (:status IS NULL OR LOWER(p.status) = LOWER(:status))
       """)
    Page<PedidoModel> buscarPedidosDoCliente(
            @Param("clienteId") Integer clienteId,
            @Param("status") String status,
            Pageable pageable);
    @Query("""
       SELECT DISTINCT p
       FROM PedidoModel p
       LEFT JOIN FETCH p.enderecoEntrega
       LEFT JOIN FETCH p.itens i
       LEFT JOIN FETCH i.produto
       WHERE p.id IN :ids
       """)
    List<PedidoModel> buscarPedidosComDetalhes(
            @Param("ids") List<Integer> ids);
    @Query("""
    SELECT
        c.nome AS nomeCategoria,
        SUM(i.quantidade) AS totalItens,
        COUNT(DISTINCT p.id) AS totalPedidos,
        SUM(i.subTotal) AS totalFaturado
    FROM PedidoModel p
        JOIN p.itens i
        JOIN i.produto pr
        JOIN pr.categoriaProduto c
    WHERE p.status = 'ENTREGUE'
      AND p.dataHora BETWEEN :dataInicio AND :dataFim
    GROUP BY c.nome
    """)
    List<VendaCategoriaProjection> buscarVendasPorCategoria(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
