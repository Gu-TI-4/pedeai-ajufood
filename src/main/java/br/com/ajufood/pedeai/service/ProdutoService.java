package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.rest.dto.request.ProdutoDisponibilidadeRequestDTO;
import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.CategoriaProdutoModel;
import br.com.ajufood.pedeai.model.ProdutoModel;
import br.com.ajufood.pedeai.repository.CategoriaProdutoRepository;
import br.com.ajufood.pedeai.repository.ItensPedidoRepository;
import br.com.ajufood.pedeai.repository.ProdutoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoCategoriaResponseDTO;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProdutoResponseDTO obterPorId(int id) {

        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Produto com ID " + id + " não encontrado."
                ));

        ProdutoResponseDTO dto =
                modelMapper.map(produto, ProdutoResponseDTO.class);

        dto.setCategoriaProdutoId(
                produto.getCategoriaProduto().getId());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> obterTodos() {

        return produtoRepository.findAll()
                .stream()
                .map(produto -> {

                    ProdutoResponseDTO dto =
                            modelMapper.map(produto,
                                    ProdutoResponseDTO.class);

                    dto.setCategoriaProdutoId(
                            produto.getCategoriaProduto().getId());

                    return dto;
                })
                .toList();
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoDTO) {

        try {

            CategoriaProdutoModel categoria =
                    categoriaProdutoRepository.findById(
                                    produtoDTO.getCategoriaProdutoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Categoria não encontrada."
                            ));

            ProdutoModel produto = new ProdutoModel();

            produto.setNome(produtoDTO.getNome());
            produto.setDescricao(produtoDTO.getDescricao());
            produto.setPreco(produtoDTO.getPreco());
            produto.setDisponivel(produtoDTO.isDisponivel());

            produto.setCategoriaProduto(categoria);

            ProdutoModel produtoSalvo =
                    produtoRepository.save(produto);

            ProdutoResponseDTO dto =
                    modelMapper.map(produtoSalvo,
                            ProdutoResponseDTO.class);

            dto.setCategoriaProdutoId(categoria.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar produto.", e
            );
        }
    }

    @Transactional
    public ProdutoResponseDTO atualizar(
            int id,
            ProdutoRequestDTO produtoDTO) {

        try {

            ProdutoModel produtoExistente =
                    produtoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Produto com ID " + id + " não encontrado."
                            ));

            CategoriaProdutoModel categoria =
                    categoriaProdutoRepository.findById(
                                    produtoDTO.getCategoriaProdutoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Categoria não encontrada."
                            ));

            produtoExistente.setNome(produtoDTO.getNome());
            produtoExistente.setDescricao(produtoDTO.getDescricao());
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setDisponivel(produtoDTO.isDisponivel());
            produtoExistente.setCategoriaProduto(categoria);

            ProdutoModel produtoAtualizado =
                    produtoRepository.save(produtoExistente);

            ProdutoResponseDTO dto =
                    modelMapper.map(produtoAtualizado,
                            ProdutoResponseDTO.class);

            dto.setCategoriaProdutoId(categoria.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar produto.", e
            );
        }
    }

    /**
     * Remove um produto da base de dados.
     *
     * Antes de excluir, verifica se existe algum item de pedido vinculado
     * ao produto. Em caso positivo, a exclusão é bloqueada e um erro 409
     * (Conflict) é retornado, evitando delete em cascata indevido.
     *
     * @param id identificador do produto que será removido
     * @throws ObjectNotFoundException quando o produto não for encontrado (404)
     * @throws BusinessRuleException quando o produto possuir pedidos vinculados (409)
     */
    @Transactional
    public void deletar(int id) {

        // Garante 404 caso o produto não exista.
        obterPorId(id);

        // UC-11: bloqueia exclusão se houver itensPedido referenciando o produto.
        if (itensPedidoRepository.existsByProdutoId(id)) {
            throw new BusinessRuleException(
                    "Não é possível excluir o produto de ID " + id +
                            ", pois ele está vinculado a um ou mais pedidos."
            );
        }

        try {

            produtoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir o produto, pois ele possui vínculos.",
                    e
            );
        }
    }
    @Transactional(readOnly = true)
    //*public List<ProdutoResponseDTO> obterDisponiveisPorCategoria(Integer categoriaId) { alterei o dto que ela busca
    // alterei tambem de produto response dto para ProdutoCategoriaResponseDTO no model mapper/*
    public List<ProdutoCategoriaResponseDTO> obterDisponiveisPorCategoria(Integer categoriaId){

        return produtoRepository.buscarDisponiveisPorCategoria(categoriaId)
                .stream()
                .map(produto -> {

                    ProdutoCategoriaResponseDTO dto = modelMapper.map(produto, ProdutoCategoriaResponseDTO.class);

                    dto.setNomeCategoria(produto.getCategoriaProduto().getNome());

                    return dto;
                })
                .toList();
    }
    @Transactional
    public ProdutoResponseDTO atualizarDisponibilidade(
            Integer produtoId,
            ProdutoDisponibilidadeRequestDTO disponibilidadeDTO) {

        ProdutoModel produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Produto com ID " + produtoId + " não encontrado."
                ));

        produto.setDisponivel(disponibilidadeDTO.isDisponivel());

        ProdutoModel produtoAtualizado = produtoRepository.save(produto);

        ProdutoResponseDTO dto = modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
        dto.setCategoriaProdutoId(produtoAtualizado.getCategoriaProduto().getId());
        dto.setNomeCategoria(produtoAtualizado.getCategoriaProduto().getNome());

        return dto;
    }
}