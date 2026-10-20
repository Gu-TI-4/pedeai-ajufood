package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.util.StatusPedido;
import br.com.ajufood.pedeai.rest.dto.request.PedidoStatusPatchRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoStatusResponseDTO;
import br.com.ajufood.pedeai.exception.InvalidTransitionException;

import br.com.ajufood.pedeai.repository.projection.VendaCategoriaProjection;
import br.com.ajufood.pedeai.rest.dto.response.VendaCategoriaResponseDTO;
import java.util.Comparator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import br.com.ajufood.pedeai.model.ItensPedidoModel;
import br.com.ajufood.pedeai.rest.dto.response.ItensPedidoResumoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Collectors;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.model.ItensPedidoModel;
import br.com.ajufood.pedeai.model.ProdutoModel;
import br.com.ajufood.pedeai.repository.ProdutoRepository;
import br.com.ajufood.pedeai.rest.dto.request.PedidoComItensRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ItemPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoComItensResponseDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.EnderecoModel;
import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.repository.EnderecoRepository;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PedidoResponseDTO obterPorId(int id) {

        PedidoModel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Pedido com ID " + id + " não encontrado."
                ));

        PedidoResponseDTO dto =
                modelMapper.map(pedido, PedidoResponseDTO.class);

        dto.setClienteId(pedido.getCliente().getId());
        dto.setEnderecoEntregaId(
                pedido.getEnderecoEntrega().getId());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obterTodos() {

        return pedidoRepository.findAll()
                .stream()
                .map(pedido -> {

                    PedidoResponseDTO dto =
                            modelMapper.map(
                                    pedido,
                                    PedidoResponseDTO.class
                            );

                    dto.setClienteId(
                            pedido.getCliente().getId());

                    dto.setEnderecoEntregaId(
                            pedido.getEnderecoEntrega().getId());

                    return dto;
                })
                .toList();
    }

    @Transactional
    public PedidoResponseDTO salvar(PedidoRequestDTO pedidoDTO) {

        try {

            ClienteModel cliente =
                    clienteRepository.findById(
                                    pedidoDTO.getClienteId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Cliente não encontrado."
                            ));

            EnderecoModel endereco =
                    enderecoRepository.findById(
                                    pedidoDTO.getEnderecoEntregaId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Endereço não encontrado."
                            ));

            PedidoModel pedido = new PedidoModel();

            pedido.setStatus(StatusPedido.valueOf(pedidoDTO.getStatus().toUpperCase()));
            pedido.setValorTotal(pedidoDTO.getValorTotal());

            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(endereco);
            pedido.setDataHora(LocalDateTime.now());

            PedidoModel pedidoSalvo =
                    pedidoRepository.save(pedido);

            PedidoResponseDTO dto =
                    modelMapper.map(
                            pedidoSalvo,
                            PedidoResponseDTO.class
                    );

            dto.setClienteId(cliente.getId());
            dto.setEnderecoEntregaId(endereco.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar pedido.", e
            );
        }
    }

    @Transactional
    public PedidoResponseDTO atualizar(
            int id,
            PedidoRequestDTO pedidoDTO) {

        try {

            PedidoModel pedidoExistente =
                    pedidoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Pedido com ID " + id + " não encontrado."
                            ));

            ClienteModel cliente =
                    clienteRepository.findById(
                                    pedidoDTO.getClienteId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Cliente não encontrado."
                            ));

            EnderecoModel endereco =
                    enderecoRepository.findById(
                                    pedidoDTO.getEnderecoEntregaId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Endereço não encontrado."
                            ));

            pedidoExistente.setStatus(StatusPedido.valueOf(pedidoDTO.getStatus().toUpperCase()));
            pedidoExistente.setValorTotal(
                    pedidoDTO.getValorTotal());
            pedidoExistente.setCliente(cliente);
            pedidoExistente.setEnderecoEntrega(endereco);

            PedidoModel pedidoAtualizado =
                    pedidoRepository.save(pedidoExistente);

            PedidoResponseDTO dto =
                    modelMapper.map(
                            pedidoAtualizado,
                            PedidoResponseDTO.class
                    );

            dto.setClienteId(cliente.getId());
            dto.setEnderecoEntregaId(endereco.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar pedido.", e
            );
        }
    }

    @Transactional
    public void deletar(int id) {

        try {

            obterPorId(id);

            pedidoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir o pedido, pois ele possui vínculos.",
                    e
            );
        }
    }
    @Transactional(readOnly = true)
    public Page<PedidoResumoResponseDTO> obterHistoricoPedidos(
            Integer clienteId,
            String status,
            int pagina,
            int tamanho) {

        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Cliente com ID " + clienteId + " não encontrado."
                ));

        Pageable pageable = PageRequest.of(
                pagina,
                tamanho,
                Sort.by("dataHora").descending());

        Page<PedidoModel> paginaPedidos =
                pedidoRepository.buscarPedidosDoCliente(
                        clienteId,
                        status,
                        pageable);

        List<Integer> ids = paginaPedidos.getContent()
                .stream()
                .map(PedidoModel::getId)
                .toList();

        if (ids.isEmpty()) {
            return paginaPedidos.map(p -> new PedidoResumoResponseDTO());
        }

        List<PedidoModel> pedidos =
                pedidoRepository.buscarPedidosComDetalhes(ids);

        Map<Integer, PedidoModel> pedidosMap =
                pedidos.stream()
                        .collect(Collectors.toMap(
                                PedidoModel::getId,
                                p -> p));

        return paginaPedidos.map(p -> {

            PedidoModel pedido = pedidosMap.get(p.getId());

            PedidoResumoResponseDTO dto =
                    new PedidoResumoResponseDTO();

            dto.setId(pedido.getId());
            dto.setDataHora(pedido.getDataHora());
            dto.setStatus(pedido.getStatus().name());
            dto.setValorTotal(pedido.getValorTotal());

            dto.setEndereco(
                    pedido.getEnderecoEntrega().getEndereco());
            dto.setNumero(
                    pedido.getEnderecoEntrega().getNumero());
            dto.setBairro(
                    pedido.getEnderecoEntrega().getBairro());
            dto.setCidade(
                    pedido.getEnderecoEntrega().getCidade());

            List<ItensPedidoResumoResponseDTO> itens =
                    pedido.getItens()
                            .stream()
                            .map(item -> {

                                ItensPedidoResumoResponseDTO itemDTO =
                                        new ItensPedidoResumoResponseDTO();

                                itemDTO.setNomeProduto(
                                        item.getProduto().getNome());

                                itemDTO.setQuantidade(
                                        item.getQuantidade());

                                itemDTO.setPrecoUnitario(
                                        item.getPrecoUnitario());

                                itemDTO.setSubTotal(
                                        item.getSubTotal());

                                return itemDTO;
                            })
                            .toList();

            dto.setItens(itens);

            return dto;
        });
    }
    @Transactional(readOnly = true)
    public List<VendaCategoriaResponseDTO> obterRelatorioVendasPorCategoria(
            LocalDate dataInicio,
            LocalDate dataFim) {

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException(
                    "A data inicial não pode ser posterior à data final.");
        }

        LocalDateTime inicio = dataInicio.atStartOfDay();

        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        List<VendaCategoriaProjection> vendas =
                pedidoRepository.buscarVendasPorCategoria(
                        inicio,
                        fim);

        List<VendaCategoriaResponseDTO> relatorio =
                new ArrayList<>(
                        vendas.stream()
                                .map(venda -> {

                                    VendaCategoriaResponseDTO dto =
                                            new VendaCategoriaResponseDTO();

                                    dto.setNomeCategoria(venda.getNomeCategoria());

                                    dto.setTotalItens(venda.getTotalItens());

                                    dto.setTotalPedidos(venda.getTotalPedidos());

                                    dto.setTotalFaturado(venda.getTotalFaturado());

                                    dto.setTicketMedio(
                                            venda.getTotalFaturado().divide(
                                                    BigDecimal.valueOf(venda.getTotalPedidos()),
                                                    2,
                                                    RoundingMode.HALF_UP));

                                    return dto;
                                })
                                .toList()
                );

        relatorio.sort(
                Comparator.comparing(
                                VendaCategoriaResponseDTO::getTotalFaturado)
                        .reversed());
        return relatorio;
    }
    @Transactional
    public PedidoComItensResponseDTO salvarComItens(
            PedidoComItensRequestDTO pedidoComItensDTO) {

        try {
            // Validar Cliente existe
            ClienteModel cliente = clienteRepository.findById(
                            pedidoComItensDTO.getClienteId().intValue())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente com ID " + pedidoComItensDTO.getClienteId() +
                                    " não encontrado."
                    ));

            //  Validar Endereço existe
            EnderecoModel endereco = enderecoRepository.findById(
                            pedidoComItensDTO.getEnderecoEntregaId().intValue())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Endereço com ID " + pedidoComItensDTO.getEnderecoEntregaId() +
                                    " não encontrado."
                    ));

            //  Validar Endereço pertence ao Cliente
            if (endereco.getCliente().getId() != cliente.getId()) {
                throw new BusinessRuleException(
                        "O endereço informado não pertence ao cliente."
                );
            }

            //  Validar itens não vazios
            if (pedidoComItensDTO.getItens().isEmpty()) {
                throw new BusinessRuleException(
                        "O pedido deve conter pelo menos um item."
                );
            }

            // Validar produtos e calcular valor total
            List<Long> produtoIds = new ArrayList<>();
            BigDecimal valorTotal = BigDecimal.ZERO;

            for (ItemPedidoRequestDTO itemRequest : pedidoComItensDTO.getItens()) {
                // Verificar se o produto ta duplicado
                if (produtoIds.contains(itemRequest.getProdutoID())) {
                    throw new BusinessRuleException(
                            "Produto duplicado na lista de itens."
                    );
                }
                produtoIds.add(itemRequest.getProdutoID());

                // Buscar produto
                ProdutoModel produto = produtoRepository.findById(
                                itemRequest.getProdutoID().intValue())
                        .orElseThrow(() -> new BusinessRuleException(
                                "Produto com ID " + itemRequest.getProdutoID() +
                                        " não encontrado."
                        ));

                // Valida se ta disponibilidade
                if (!produto.isDisponivel()) {
                    throw new BusinessRuleException(
                            "Produto '" + produto.getNome() +
                                    "' não está disponível."
                    );
                }

                // Calcular subtotal
                BigDecimal subtotal = produto.getPreco()
                        .multiply(BigDecimal.valueOf(itemRequest.getQuantidade()));
                valorTotal = valorTotal.add(subtotal);
            }

            //  Criar Pedido
            PedidoModel pedido = new PedidoModel();
            pedido.setStatus(StatusPedido.AGUARDANDO_CONFIRMACAO);
            pedido.setValorTotal(valorTotal);
            pedido.setDataHora(LocalDateTime.now());
            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(endereco);

            PedidoModel pedidoSalvo = pedidoRepository.save(pedido);

            // Criar os itens e adicionar itens
            for (ItemPedidoRequestDTO itemRequest : pedidoComItensDTO.getItens()) {
                ProdutoModel produto = produtoRepository.findById(
                                itemRequest.getProdutoID().intValue())
                        .orElseThrow();

                ItensPedidoModel item = new ItensPedidoModel();
                item.setQuantidade(itemRequest.getQuantidade());
                item.setPrecoUnitario(produto.getPreco());
                item.setSubTotal(produto.getPreco()
                        .multiply(BigDecimal.valueOf(itemRequest.getQuantidade())));
                item.setPedido(pedidoSalvo);
                item.setProduto(produto);

                pedidoSalvo.addItem(item);
            }

            // Salvarrrr com itens
            PedidoModel pedidoComItens = pedidoRepository.save(pedidoSalvo);

            // 8. Montar resposta saida da resposta
            PedidoComItensResponseDTO response = new PedidoComItensResponseDTO();
            response.setId((long) pedidoComItens.getId());
            response.setDataHora(pedidoComItens.getDataHora());
            response.setStatus(pedidoComItens.getStatus().name());
            response.setValorTotal(pedidoComItens.getValorTotal());
            response.setBairro(pedidoComItens.getEnderecoEntrega().getBairro());
            response.setCidade(pedidoComItens.getEnderecoEntrega().getCidade());

            List<PedidoComItensResponseDTO.ItemPedidoResponseDTO> itensResponse =
                    pedidoComItens.getItens().stream()
                            .map(item -> new PedidoComItensResponseDTO.ItemPedidoResponseDTO(
                                    item.getProduto().getNome(),
                                    item.getQuantidade(),
                                    item.getPrecoUnitario(),
                                    item.getSubTotal()
                            ))
                            .toList();

            response.setItens(itensResponse);
            return response;

        } catch (BusinessRuleException e) {
            throw e;
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao salvar pedido com itens.", e
            );
        }
    }
    /**
     * Atualiza o status de um pedido seguindo a máquina de estados.
     *
     * @param id identificador do pedido
     * @param statusPatchDTO DTO com novo status
     * @return DTO com status anterior, novo status e data/hora
     * @throws ObjectNotFoundException quando pedido não existe
     * @throws InvalidTransitionException quando transição é inválida
     */
    @Transactional
    public PedidoStatusResponseDTO atualizarStatus(
            int id,
            PedidoStatusPatchRequestDTO statusPatchDTO) {

        //  Buscar  o pedido
        PedidoModel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Pedido com ID " + id + " não encontrado."
                ));

        //  Validar o novo status (se existe no enum?)
        StatusPedido novoStatus;
        try {
            novoStatus = StatusPedido.valueOf(statusPatchDTO.getNovoStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTransitionException(
                    "Status inválido: " + statusPatchDTO.getNovoStatus() +
                            ". Valores válidos: " + String.join(", ",
                            java.util.Arrays.stream(StatusPedido.values())
                                    .map(StatusPedido::name)
                                    .toArray(String[]::new))
            );
        }

        //  Guardar o status anterior
        StatusPedido statusAnterior = pedido.getStatus();

        //  Validar a  transição vendo se ta invalida
        if (!statusAnterior.podeTransicionar(novoStatus)) {
            throw new InvalidTransitionException(
                    "Transição inválida de " + statusAnterior + " para " + novoStatus + "."
            );
        }

        //  Atualizar o status
        pedido.setStatus(novoStatus);
        PedidoModel pedidoAtualizado = pedidoRepository.save(pedido);

        //  Montar a resposta
        PedidoStatusResponseDTO dto = new PedidoStatusResponseDTO();
        dto.setId(pedidoAtualizado.getId());
        dto.setStatusAnterior(statusAnterior.name());
        dto.setStatusAtual(novoStatus.name());
        dto.setDataHora(LocalDateTime.now());

        return dto;
    }
}