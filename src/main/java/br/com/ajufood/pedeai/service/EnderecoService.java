package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.EnderecoModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.repository.EnderecoRepository;
import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public EnderecoResponseDTO obterPorId(int id) {

        EnderecoModel endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Endereço com ID " + id + " não encontrado."
                ));

        EnderecoResponseDTO dto =
                modelMapper.map(endereco, EnderecoResponseDTO.class);

        dto.setClienteId(endereco.getCliente().getId());
        dto.setPadrao(endereco.getPadrao());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> obterTodos() {

        return enderecoRepository.findAll()
                .stream()
                .map(endereco -> {

                    EnderecoResponseDTO dto =
                            modelMapper.map(endereco,
                                    EnderecoResponseDTO.class);

                    dto.setClienteId(endereco.getCliente().getId());
                    dto.setPadrao(endereco.getPadrao());

                    return dto;
                })
                .toList();
    }

    @Transactional
    public EnderecoResponseDTO salvar(
            EnderecoRequestDTO enderecoDTO) {

        try {

            ClienteModel cliente =
                    clienteRepository.findById(
                                    enderecoDTO.getClienteId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Cliente não encontrado."
                            ));

            EnderecoModel endereco =
                    modelMapper.map(enderecoDTO, EnderecoModel.class);
            endereco.setId(0);
            endereco.setPadrao(false);

            endereco.setCliente(cliente);

            EnderecoModel enderecoSalvo =
                    enderecoRepository.save(endereco);

            EnderecoResponseDTO dto =
                    modelMapper.map(enderecoSalvo,
                            EnderecoResponseDTO.class);

            dto.setClienteId(cliente.getId());
            dto.setPadrao(enderecoSalvo.getPadrao());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar endereço.", e
            );
        }
    }

    @Transactional
    public EnderecoResponseDTO atualizar(
            int id,
            EnderecoRequestDTO enderecoDTO) {

        try {

            EnderecoModel enderecoExistente =
                    enderecoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Endereço com ID " + id + " não encontrado."
                            ));

            ClienteModel cliente =
                    clienteRepository.findById(
                                    enderecoDTO.getClienteId())
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Cliente não encontrado."
                            ));

            enderecoExistente.setEndereco(enderecoDTO.getEndereco());
            enderecoExistente.setNumero(enderecoDTO.getNumero());
            enderecoExistente.setComplemento(enderecoDTO.getComplemento());
            enderecoExistente.setBairro(enderecoDTO.getBairro());
            enderecoExistente.setCidade(enderecoDTO.getCidade());
            enderecoExistente.setEstado(enderecoDTO.getEstado());
            enderecoExistente.setCep(enderecoDTO.getCep());
            enderecoExistente.setCliente(cliente);

            EnderecoModel enderecoAtualizado =
                    enderecoRepository.save(enderecoExistente);

            EnderecoResponseDTO dto =
                    modelMapper.map(enderecoAtualizado,
                            EnderecoResponseDTO.class);

            dto.setClienteId(cliente.getId());
            dto.setPadrao(enderecoAtualizado.getPadrao());

            return dto;

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar endereço.", e
            );
        }
    }

    @Transactional
    public void deletar(int id) {

        try {

            obterPorId(id);

            enderecoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir o endereço, pois ele possui vínculos.",
                    e
            );
        }
    }
}