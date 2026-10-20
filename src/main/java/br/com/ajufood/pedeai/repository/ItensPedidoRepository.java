package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ItensPedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensPedidoRepository
        extends JpaRepository<ItensPedidoModel, Long> {

    /**
     * Verifica se existe algum item de pedido vinculado ao produto informado.
     * Usado para impedir a exclusão de produtos que já foram pedidos
     * (UC-11 - Remover Produto com Validação de Integridade).
     *
     * @param produtoId identificador do produto
     * @return true se houver ao menos um item de pedido referenciando o produto
     */
    boolean existsByProdutoId(int produtoId);
}