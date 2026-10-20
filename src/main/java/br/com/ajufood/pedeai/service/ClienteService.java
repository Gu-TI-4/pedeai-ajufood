package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.repository.EnderecoRepository;
import br.com.ajufood.pedeai.model.EnderecoModel;
import br.com.ajufood.pedeai.rest.dto.request.ClienteComEnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ClientePatchRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.EnderecoSemClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos clientes.
 *
 * Esta classe faz a ligação entre o Controller e o Repository, centralizando
 * validações, regras de negócio e operações de persistência.
 *
 * Exemplos:
 * clienteService.obterPorId(1);
 * clienteService.salvar(clienteModel);
 */
@Service
public class ClienteService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    /**
     * Repositório responsável pelas operações de acesso ao banco de dados.
     */
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Objeto responsável por converter Model em DTO e DTO em Model.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca um cliente pelo ID.
     *
     * @param id identificador do cliente
     * @return DTO com os dados do cliente encontrado
     * @throws ObjectNotFoundException quando o cliente não for encontrado
     */
    @Transactional(readOnly = true)
    public ClienteResponseDTO obterPorId(int id) {
        ClienteModel cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Cliente com ID " + id + " não encontrado."
                ));

        ClienteResponseDTO dto = modelMapper.map(cliente, ClienteResponseDTO.class);
        dto.setCpf(mascaraCPF(cliente.getCpf()));  // ✅ Mascara aqui!
        return dto;
    }

    /**
     * Busca todos os clientes cadastrados.
     *
     * @return lista de clientes em formato DTO
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> obterTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .toList();
    }

    /**
     * Salva um novo cliente na base de dados.
     *
     * Antes de salvar, verifica se já existe cliente cadastrado
     * com o mesmo CPF ou e-mail.
     *
     * @param clienteNovoDTO objeto contendo os dados do novo cliente
     * @return DTO com os dados do cliente salvo
     * @throws ConstraintException quando CPF ou e-mail já estiverem cadastrados
     * @throws DataIntegrityException quando ocorrer violação de integridade no banco
     */
    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO clienteNovoDTO) {
        try {

            ClienteModel clienteNovoModel = modelMapper.map(clienteNovoDTO, ClienteModel.class);
            validarCpfEmailParaCadastro(clienteNovoModel);
            ClienteModel clienteSalvo = clienteRepository.save(clienteNovoModel);

            return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao salvar o cliente " + clienteNovoDTO.getNome() + ".", e
            );
        }
    }

    /**
     * Atualiza os dados de um cliente existente.
     *
     * Antes de atualizar, verifica se o cliente existe e se o CPF ou e-mail
     * informados não pertencem a outro cliente.
     *
     * @param id identificador do cliente que será atualizado
     * @param clienteAtualizadoDTO objeto com os novos dados do cliente
     * @return DTO com os dados atualizados
     * @throws ObjectNotFoundException quando o cliente não for encontrado
     * @throws ConstraintException quando CPF ou e-mail já pertencerem a outro cliente
     * @throws DataIntegrityException quando ocorrer violação de integridade no banco
     */
    @Transactional
    public ClienteResponseDTO atualizar(int id, ClienteRequestDTO clienteAtualizadoDTO) {
        try {

            ClienteModel clienteAtualizadoModel = modelMapper.map(clienteAtualizadoDTO, ClienteModel.class);
            ClienteModel clienteExistenteModel = clienteRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente com ID " + id + " não encontrado."
                    ));
            validarCpfEmailParaAtualizacao(id, clienteAtualizadoModel);

            clienteExistenteModel.setNome(clienteAtualizadoModel.getNome());
            clienteExistenteModel.setCpf(clienteAtualizadoModel.getCpf());
            clienteExistenteModel.setEmail(clienteAtualizadoModel.getEmail());
            clienteExistenteModel.setTelefone(clienteAtualizadoModel.getTelefone());

            ClienteModel clienteSalvo = clienteRepository.save(clienteExistenteModel);

            return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar o cliente " + clienteAtualizadoDTO.getNome() + ".", e
            );
        }
    }

    /**
     * Remove um cliente da base de dados.
     *
     * @param id identificador do cliente que será removido
     * @throws ObjectNotFoundException quando o cliente não for encontrado
     * @throws DataIntegrityException quando o cliente não puder ser removido por possuir vínculos
     */
    @Transactional
    public void deletar(int id) {

        try {
            obterPorId(id);
            clienteRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir o cliente, pois ele possui vínculos com outros registros.", e
            );
        }
    }

    /**
     * Atualiza parcialmente um cliente (PATCH).
     * Apenas campos não nulos são atualizados.
     *
     * @param id identificador do cliente
     * @param clientePatchDTO DTO com campos opcionais
     * @return DTO com dados atualizados
     * @throws ObjectNotFoundException quando cliente não existe
     * @throws BusinessRuleException quando novo email já existe em outro cliente
     */
    @Transactional
    public ClienteResponseDTO atualizarParcial(int id, ClientePatchRequestDTO clientePatchDTO) {
        try {
            ClienteModel clienteExistenteModel = clienteRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente com ID " + id + " não encontrado."
                    ));

            // Validar novo email se informado
            if (clientePatchDTO.getEmail() != null &&
                    !clientePatchDTO.getEmail().equals(clienteExistenteModel.getEmail())) {

                if (clienteRepository.existsByEmail(clientePatchDTO.getEmail())) {
                    throw new BusinessRuleException(
                            "Já existe um cliente cadastrado com este e-mail."
                    );
                }
                clienteExistenteModel.setEmail(clientePatchDTO.getEmail());
            }

            // Aplicar apenas campos não nulos
            if (clientePatchDTO.getNome() != null) {
                clienteExistenteModel.setNome(clientePatchDTO.getNome());
            }

            if (clientePatchDTO.getTelefone() != null) {
                clienteExistenteModel.setTelefone(clientePatchDTO.getTelefone());
            }

            ClienteModel clienteSalvo = clienteRepository.save(clienteExistenteModel);

            ClienteResponseDTO dto = new ClienteResponseDTO();
            dto.setId(clienteSalvo.getId());
            dto.setNome(clienteSalvo.getNome());
            dto.setCpf(mascaraCPF(clienteSalvo.getCpf()));
            dto.setEmail(clienteSalvo.getEmail());
            dto.setTelefone(clienteSalvo.getTelefone());
            dto.setEnderecos(clienteSalvo.getEnderecos().stream()
                    .map(end -> {
                        EnderecoResponseDTO endDTO = modelMapper.map(end, EnderecoResponseDTO.class);
                        endDTO.setClienteId(end.getCliente().getId());
                        endDTO.setPadrao(end.getPadrao());
                        return endDTO;
                    })
                    .toList());

            return dto;

        } catch (BusinessRuleException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar o cliente.", e
            );
        }
    }

    /**
     * Valida se CPF ou e-mail já existem antes de cadastrar um novo cliente.
     *
     * @param cliente cliente que será cadastrado
     * @throws ConstraintException quando CPF ou e-mail já estiverem cadastrados
     */
    private void validarCpfEmailParaCadastro(ClienteModel cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o CPF " + cliente.getCpf() + "."
            );
        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o e-mail " + cliente.getEmail() + "."
            );
        }
    }

    /**
     * Valida se CPF ou e-mail pertencem a outro cliente durante a atualização.
     *
     * @param id id do cliente que está sendo atualizado
     * @param cliente cliente com os dados atualizados
     * @throws ConstraintException quando CPF ou e-mail já pertencerem a outro cliente
     */
    private void validarCpfEmailParaAtualizacao(int id, ClienteModel cliente) {
        clienteRepository.findByCpf(cliente.getCpf())
                .filter(clienteEncontrado -> clienteEncontrado.getId() != id)
                .ifPresent(clienteEncontrado -> {
                    throw new ConstraintException(
                            "Já existe outro cliente cadastrado com o CPF " + cliente.getCpf() + "."
                    );
                });

        clienteRepository.findByEmail(cliente.getEmail())
                .filter(clienteEncontrado -> clienteEncontrado.getId() != id)
                .ifPresent(clienteEncontrado -> {
                    throw new ConstraintException(
                            "Já existe outro cliente cadastrado com o e-mail " + cliente.getEmail() + "."
                    );
                });
    }
    /*//essquei retorna tipo xxx.543.456-xx (importante)
    private String mascaraCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return "xxx." + cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-xx";
    }
    */

    // esse aqui é se quiser com tudo xxx.xxx.xxx-xx(importanmte para outros projetos)
    private String mascaraCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return "xxx.xxx.xxx-xx";
    }


    @Transactional
    public ClienteResponseDTO salvarComEndereco(ClienteComEnderecoRequestDTO clienteEnderecoDTO) {

        try {

            // Validar unicidade de CPF e Email
            if (clienteRepository.existsByCpf(clienteEnderecoDTO.getCpf())) {
                throw new BusinessRuleException(
                        "Já existe um cliente cadastrado com este CPF.");
            }

            if (clienteRepository.existsByEmail(clienteEnderecoDTO.getEmail())) {
                throw new BusinessRuleException(
                        "Já existe um cliente cadastrado com este e-mail.");
            }

            // Criar cliente
            ClienteModel cliente = new ClienteModel();
            cliente.setNome(clienteEnderecoDTO.getNome());
            cliente.setCpf(clienteEnderecoDTO.getCpf());
            cliente.setEmail(clienteEnderecoDTO.getEmail());
            cliente.setTelefone(clienteEnderecoDTO.getTelefone());

            // Salvar cliente primeiro
            ClienteModel clienteSalvo = clienteRepository.save(cliente);

            // Criar endereco vinculado ao cliente
            EnderecoModel endereco = new EnderecoModel();
            endereco.setEndereco(clienteEnderecoDTO.getEndereco().getEndereco());
            endereco.setNumero(clienteEnderecoDTO.getEndereco().getNumero());
            endereco.setComplemento(clienteEnderecoDTO.getEndereco().getComplemento());
            endereco.setBairro(clienteEnderecoDTO.getEndereco().getBairro());
            endereco.setCidade(clienteEnderecoDTO.getEndereco().getCidade());
            endereco.setEstado(clienteEnderecoDTO.getEndereco().getEstado());
            endereco.setCep(clienteEnderecoDTO.getEndereco().getCep());
            endereco.setCliente(clienteSalvo);

            // Salvar endereco
            EnderecoModel enderecoSalvo = enderecoRepository.save(endereco);

            // Adicionar endereço à coleção do cliente para garantir que a relação está populada
            clienteSalvo.addEndereco(enderecoSalvo);

            // Buscar cliente novamente com enderecos (JPA carrega a collection)
            ClienteModel clienteComEnderecos = clienteRepository.findById(clienteSalvo.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado."));

            // Montar DTO de resposta
            ClienteResponseDTO dto = new ClienteResponseDTO();
            dto.setId(clienteComEnderecos.getId());
            dto.setNome(clienteComEnderecos.getNome());
            dto.setCpf(mascaraCPF(clienteComEnderecos.getCpf()));
            dto.setEmail(clienteComEnderecos.getEmail());
            dto.setTelefone(clienteComEnderecos.getTelefone());

            // Converter enderecos para DTOs
            List<EnderecoResponseDTO> enderecosDTO = clienteComEnderecos.getEnderecos()
                    .stream()
                    .map(end -> {
                        EnderecoResponseDTO endDTO = new EnderecoResponseDTO();
                        endDTO.setId(end.getId());
                        endDTO.setEndereco(end.getEndereco());
                        endDTO.setNumero(end.getNumero());
                        endDTO.setComplemento(end.getComplemento());
                        endDTO.setBairro(end.getBairro());
                        endDTO.setCidade(end.getCidade());
                        endDTO.setEstado(end.getEstado());
                        endDTO.setCep(end.getCep());
                        endDTO.setClienteId(end.getCliente().getId());
                        endDTO.setPadrao(end.getPadrao());
                        return endDTO;
                    })
                    .toList();

            dto.setEnderecos(enderecosDTO);

            return dto;

        } catch (BusinessRuleException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao salvar cliente e endereço.", e
            );
        }
    }
    @Transactional
    public ClienteResponseDTO adicionarEndereco(int clienteId, EnderecoSemClienteRequestDTO enderecoDTO) {
        try {
            // Validar se cliente existe
            ClienteModel cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente com ID " + clienteId + " não encontrado."
                    ));

            // Validar máximo de 5 endereços
            long qtdEnderecos = enderecoRepository.countByClienteId(clienteId);
            if (qtdEnderecos >= 5) {
                throw new BusinessRuleException(
                        "Máximo de 5 endereços por cliente. Cliente já possui " + qtdEnderecos + " endereço(s)."
                );
            }

            // Validar duplicidade de endereço (mesma rua, número, CEP)
            boolean enderecoJaExiste = enderecoRepository
                    .existsByClienteIdAndEnderecoAndNumeroAndCep(
                            clienteId,
                            enderecoDTO.getEndereco(),
                            enderecoDTO.getNumero(),
                            enderecoDTO.getCep()
                    );

            if (enderecoJaExiste) {
                throw new ConstraintException(
                        "Já existe um endereço idêntico para este cliente (mesma rua, número e CEP)."
                );
            }

            // Se novo endereço for padrão, desmarcar outros como padrão
            if (enderecoDTO.getPadrao() != null && enderecoDTO.getPadrao()) {
                EnderecoModel enderecoPadraoAtual = enderecoRepository.findPadraoByClienteId(clienteId);
                if (enderecoPadraoAtual != null) {
                    enderecoPadraoAtual.setPadrao(false);
                    enderecoRepository.save(enderecoPadraoAtual);
                }
            }

            // Criar e salvar novo endereço
            EnderecoModel novoEndereco = new EnderecoModel();
            novoEndereco.setEndereco(enderecoDTO.getEndereco());
            novoEndereco.setNumero(enderecoDTO.getNumero());
            novoEndereco.setComplemento(enderecoDTO.getComplemento());
            novoEndereco.setBairro(enderecoDTO.getBairro());
            novoEndereco.setCidade(enderecoDTO.getCidade());
            novoEndereco.setEstado(enderecoDTO.getEstado());
            novoEndereco.setCep(enderecoDTO.getCep());
            novoEndereco.setCliente(cliente);
            novoEndereco.setPadrao(enderecoDTO.getPadrao() != null && enderecoDTO.getPadrao());

            enderecoRepository.save(novoEndereco);

            // Buscar cliente atualizado com todos os endereços
            ClienteModel clienteAtualizado = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente não encontrado."
                    ));

            // Montar resposta com lista de endereços atualizada
            ClienteResponseDTO dto = new ClienteResponseDTO();
            dto.setId(clienteAtualizado.getId());
            dto.setNome(clienteAtualizado.getNome());
            dto.setCpf(mascaraCPF(clienteAtualizado.getCpf()));
            dto.setEmail(clienteAtualizado.getEmail());
            dto.setTelefone(clienteAtualizado.getTelefone());

            List<EnderecoResponseDTO> enderecosDTO = clienteAtualizado.getEnderecos()
                    .stream()
                    .map(end -> {
                        EnderecoResponseDTO endDTO = new EnderecoResponseDTO();
                        endDTO.setId(end.getId());
                        endDTO.setEndereco(end.getEndereco());
                        endDTO.setNumero(end.getNumero());
                        endDTO.setComplemento(end.getComplemento());
                        endDTO.setBairro(end.getBairro());
                        endDTO.setCidade(end.getCidade());
                        endDTO.setEstado(end.getEstado());
                        endDTO.setCep(end.getCep());
                        endDTO.setClienteId(end.getCliente().getId());
                        endDTO.setPadrao(end.getPadrao());
                        return endDTO;
                    })
                    .toList();

            dto.setEnderecos(enderecosDTO);

            return dto;

        } catch (BusinessRuleException | ConstraintException | ObjectNotFoundException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao adicionar endereço.", e
            );
        }
    }

}
