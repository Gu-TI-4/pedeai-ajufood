package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ItensPedidoModel;
import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.model.ProdutoModel;
import br.com.ajufood.pedeai.repository.ItensPedidoRepository;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.repository.ProdutoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ItensPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItensPedidoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItensPedidoService {

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ItensPedidoResponseDTO obterPorId(long id) {

        ItensPedidoModel item =
                itensPedidoRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(
                                "Item do pedido com ID " + id + " não encontrado."
                        ));

        ItensPedidoResponseDTO dto =
                modelMapper.map(item,
                        ItensPedidoResponseDTO.class);

        dto.setPedidoId(item.getPedido().getId());
        dto.setProdutoId(item.getProduto().getId());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<ItensPedidoResponseDTO> obterTodos() {

        return itensPedidoRepository.findAll()
                .stream()
                .map(item -> {

                    ItensPedidoResponseDTO dto =
                            modelMapper.map(item,
                                    ItensPedidoResponseDTO.class);

                    dto.setPedidoId(item.getPedido().getId());
                    dto.setProdutoId(item.getProduto().getId());

                    return dto;
                })
                .toList();
    }

    @Transactional
    public ItensPedidoResponseDTO salvar(
            ItensPedidoRequestDTO itemDTO) {

        try {

            PedidoModel pedido =
                    pedidoRepository.findById(
                                    itemDTO.getPedidoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Pedido não encontrado."
                            ));

            ProdutoModel produto =
                    produtoRepository.findById(
                                    itemDTO.getProdutoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Produto não encontrado."
                            ));

            // Criar manualmente para evitar ambiguidade do ModelMapper
            ItensPedidoModel item = new ItensPedidoModel();
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            item.setSubTotal(itemDTO.getSubTotal());
            item.setPedido(pedido);
            item.setProduto(produto);

            ItensPedidoModel itemSalvo =
                    itensPedidoRepository.save(item);

            ItensPedidoResponseDTO dto =
                    modelMapper.map(
                            itemSalvo,
                            ItensPedidoResponseDTO.class
                    );

            dto.setPedidoId(pedido.getId());
            dto.setProdutoId(produto.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar item do pedido.",
                    e
            );
        }
    }

    @Transactional
    public ItensPedidoResponseDTO atualizar(
            long id,
            ItensPedidoRequestDTO itemDTO) {

        try {

            ItensPedidoModel itemExistente =
                    itensPedidoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Item do pedido com ID " + id + " não encontrado."
                            ));

            PedidoModel pedido =
                    pedidoRepository.findById(
                                    itemDTO.getPedidoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Pedido não encontrado."
                            ));

            ProdutoModel produto =
                    produtoRepository.findById(
                                    itemDTO.getProdutoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Produto não encontrado."
                            ));

            // Atualizar manualmente os campos
            itemExistente.setQuantidade(itemDTO.getQuantidade());
            itemExistente.setPrecoUnitario(itemDTO.getPrecoUnitario());
            itemExistente.setSubTotal(itemDTO.getSubTotal());
            itemExistente.setPedido(pedido);
            itemExistente.setProduto(produto);

            ItensPedidoModel itemAtualizado =
                    itensPedidoRepository.save(itemExistente);

            ItensPedidoResponseDTO dto = new ItensPedidoResponseDTO();
            dto.setId(itemAtualizado.getId());
            dto.setQuantidade(itemAtualizado.getQuantidade());
            dto.setPrecoUnitario(itemAtualizado.getPrecoUnitario());
            dto.setSubTotal(itemAtualizado.getSubTotal());
            dto.setPedidoId(pedido.getId());
            dto.setProdutoId(produto.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar item do pedido.",
                    e
            );
        }
    }

    @Transactional
    public void deletar(long id) {

        try {

            obterPorId(id);

            itensPedidoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir o item do pedido, pois ele possui vínculos.",
                    e
            );
        }
    }
}