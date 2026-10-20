package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import br.com.ajufood.pedeai.model.PagamentoModel;
import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.repository.FormaPagamentoRepository;
import br.com.ajufood.pedeai.repository.PagamentoRepository;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.PagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PagamentoResponseDTO obterPorId(int id) {

        PagamentoModel pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Pagamento com ID " + id + " não encontrado."
                ));

        PagamentoResponseDTO dto =
                modelMapper.map(pagamento, PagamentoResponseDTO.class);

        dto.setPedidoId(pagamento.getPedido().getId());
        dto.setFormaPagamentoId(
                pagamento.getFormaPagamento().getId());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> obterTodos() {

        return pagamentoRepository.findAll()
                .stream()
                .map(pagamento -> {

                    PagamentoResponseDTO dto =
                            modelMapper.map(
                                    pagamento,
                                    PagamentoResponseDTO.class
                            );

                    dto.setPedidoId(
                            pagamento.getPedido().getId());

                    dto.setFormaPagamentoId(
                            pagamento.getFormaPagamento().getId());

                    return dto;
                })
                .toList();
    }

    @Transactional
    public PagamentoResponseDTO salvar(
            PagamentoRequestDTO pagamentoDTO) {

        try {

            PedidoModel pedido =
                    pedidoRepository.findById(
                                    pagamentoDTO.getPedidoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Pedido não encontrado."
                            ));

            FormaPagamentoModel formaPagamento =
                    formaPagamentoRepository.findById(
                                    pagamentoDTO.getFormaPagamentoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Forma de pagamento não encontrada."
                            ));

            PagamentoModel pagamento =
                    modelMapper.map(
                            pagamentoDTO,
                            PagamentoModel.class
                    );

            pagamento.setPedido(pedido);
            pagamento.setFormaPagamento(formaPagamento);
            pagamento.setDataHora(LocalDateTime.now());

            PagamentoModel pagamentoSalvo =
                    pagamentoRepository.save(pagamento);

            PagamentoResponseDTO dto =
                    modelMapper.map(
                            pagamentoSalvo,
                            PagamentoResponseDTO.class
                    );

            dto.setPedidoId(pedido.getId());
            dto.setFormaPagamentoId(
                    formaPagamento.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar pagamento.", e
            );
        }
    }

    @Transactional
    public PagamentoResponseDTO atualizar(
            int id,
            PagamentoRequestDTO pagamentoDTO) {

        try {

            PagamentoModel pagamentoExistente =
                    pagamentoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Pagamento com ID " + id + " não encontrado."
                            ));

            PedidoModel pedido =
                    pedidoRepository.findById(
                                    pagamentoDTO.getPedidoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Pedido não encontrado."
                            ));

            FormaPagamentoModel formaPagamento =
                    formaPagamentoRepository.findById(
                                    pagamentoDTO.getFormaPagamentoId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Forma de pagamento não encontrada."
                            ));

            pagamentoExistente.setValorPago(
                    pagamentoDTO.getValorPago());

            pagamentoExistente.setPedido(pedido);

            pagamentoExistente.setFormaPagamento(
                    formaPagamento);

            PagamentoModel pagamentoAtualizado =
                    pagamentoRepository.save(pagamentoExistente);

            PagamentoResponseDTO dto =
                    modelMapper.map(
                            pagamentoAtualizado,
                            PagamentoResponseDTO.class
                    );

            dto.setPedidoId(pedido.getId());

            dto.setFormaPagamentoId(
                    formaPagamento.getId());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar pagamento.", e
            );
        }
    }

    @Transactional
    public void deletar(int id) {

        try {

            obterPorId(id);

            pagamentoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir o pagamento, pois ele possui vínculos.",
                    e
            );
        }
    }
}