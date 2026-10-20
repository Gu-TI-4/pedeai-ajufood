package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import br.com.ajufood.pedeai.repository.FormaPagamentoRepository;
import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public FormaPagamentoResponseDTO obterPorId(int id) {

        FormaPagamentoModel formaPagamento =
                formaPagamentoRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException(
                                "Forma de pagamento com ID " + id + " não encontrada."
                        ));

        return modelMapper.map(
                formaPagamento,
                FormaPagamentoResponseDTO.class
        );
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoResponseDTO> obterTodos() {

        return formaPagamentoRepository.findAll()
                .stream()
                .map(formaPagamento ->
                        modelMapper.map(
                                formaPagamento,
                                FormaPagamentoResponseDTO.class))
                .toList();
    }

    @Transactional
    public FormaPagamentoResponseDTO salvar(
            FormaPagamentoRequestDTO formaPagamentoDTO) {

        if (formaPagamentoRepository.existsByNomeIgnoreCase(formaPagamentoDTO.getNome())) {
            throw new BusinessRuleException(
                    "Já existe uma forma de pagamento cadastrada com o nome '" + formaPagamentoDTO.getNome() + "'.");
        }

        try {

            FormaPagamentoModel formaPagamento =
                    modelMapper.map(
                            formaPagamentoDTO,
                            FormaPagamentoModel.class
                    );

            FormaPagamentoModel formaPagamentoSalva =
                    formaPagamentoRepository.save(formaPagamento);

            return modelMapper.map(
                    formaPagamentoSalva,
                    FormaPagamentoResponseDTO.class
            );

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar forma de pagamento.", e
            );
        }
    }

    @Transactional
    public FormaPagamentoResponseDTO atualizar(
            int id,
            FormaPagamentoRequestDTO formaPagamentoDTO) {

        try {

            FormaPagamentoModel formaPagamentoExistente =
                    formaPagamentoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Forma de pagamento com ID " + id + " não encontrada."
                            ));

            formaPagamentoExistente.setNome(formaPagamentoDTO.getNome());
            formaPagamentoExistente.setDescricao(
                    formaPagamentoDTO.getDescricao());

            FormaPagamentoModel formaPagamentoAtualizada =
                    formaPagamentoRepository.save(formaPagamentoExistente);

            return modelMapper.map(
                    formaPagamentoAtualizada,
                    FormaPagamentoResponseDTO.class
            );

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar forma de pagamento.", e
            );
        }
    }

    @Transactional
    public void deletar(int id) {

        try {

            obterPorId(id);

            formaPagamentoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir a forma de pagamento, pois ela possui vínculos.",
                    e
            );
        }
    }
}