package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.EnderecoSemClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ClienteComEnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ClientePatchRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoResponseDTO;
import br.com.ajufood.pedeai.service.PedidoService;
import org.springframework.data.domain.Page;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos clientes.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    /**
     * Instância do serviço de clientes, responsável por encapsular a lógica de negócios
     * e intermediar as operações entre o controlador e o repositório.
     */
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PedidoService pedidoService;

    /**
     * Obtém um cliente pelo ID.
     * Link: http://localhost:8080/ajufood/pedeai/cliente/?
     *
     * @param id ID do cliente.
     * @return clienteResponseDTO representando o cliente encontrado.
     */
    @Operation(summary = "Busca um cliente pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obterPorId(@PathVariable int id) {
        ClienteResponseDTO clienteResponseDTO = clienteService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponseDTO);
    }

    /**
     * Obtém a lista de todos os clientes cadastrados.
     * Link: http://localhost:8080/ajufood/pedeai/cliente
     *
     * @return Lista de clienteResponseDTOS representando os clientes cadastrados.
     */
    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obterTodos() {
        List<ClienteResponseDTO> clienteResponseDTOS = clienteService.obterTodos();
        return ResponseEntity.ok(clienteResponseDTOS);
    }

    /**
     * Atualiza os dados de um cliente existente.
     * Link: http://localhost:8080/ajufood/pedeai/cliente
     *
     * @param clienteRequestDTO contendo os dados atualizados do cliente.
     * @return clienteAtualizadoDTO representando o cliente atualizado.
     */
    @Operation(summary = "Atualiza um cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable int id,
            @RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteAtualizadoDTO = clienteService.atualizar(id, clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizadoDTO);
    }

    @Operation(summary = "Histórico de pedidos do cliente")
    @GetMapping("/{clienteId}/pedidos")
    public ResponseEntity<Page<PedidoResumoResponseDTO>> obterHistoricoPedidos(

            @PathVariable Integer clienteId,

            @RequestParam(required = false)
            String status,

            @RequestParam(defaultValue = "0")
            int pagina,

            @RequestParam(defaultValue = "10")
            int tamanho) {

        Page<PedidoResumoResponseDTO> pedidos =
                pedidoService.obterHistoricoPedidos(
                        clienteId,
                        status,
                        pagina,
                        tamanho);

        return ResponseEntity.ok(pedidos);
    }

    /**
     * Deleta um cliente da base de dados.
     * Link: http://localhost:8080/ajufood/pedeai/cliente
     *
     * @param id contendo o identificador do cliente a ser deletado.
     */
    @Operation(summary = "Remove um cliente pelo id")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cadastra um novo cliente com endereco")
    @ApiResponse(responseCode = "201",
            description = "Cliente criado com sucesso")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvarComEndereco(
            @Valid @RequestBody ClienteComEnderecoRequestDTO clienteEnderecoDTO) {

        ClienteResponseDTO clienteNovo = clienteService.salvarComEndereco(clienteEnderecoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteNovo);
    }

    @Operation(summary = "Atualiza parcialmente um cliente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarParcial(
            @PathVariable int id,
            @RequestBody ClientePatchRequestDTO clientePatchDTO) {

        ClienteResponseDTO clienteAtualizadoDTO = clienteService.atualizarParcial(id, clientePatchDTO);
        return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizadoDTO);
    }
    @Operation(summary = "Adiciona novo endereço ao cliente")
    @ApiResponse(responseCode = "201", description = "Endereço adicionado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "409", description = "Endereço duplicado")
    @ApiResponse(responseCode = "422", description = "Máximo de 5 endereços atingido")
    @PostMapping("/{clienteId}/enderecos")
    public ResponseEntity<ClienteResponseDTO> adicionarEndereco(
            @PathVariable int clienteId,
            @Valid @RequestBody EnderecoSemClienteRequestDTO enderecoDTO) {

        ClienteResponseDTO clienteAtualizado = clienteService.adicionarEndereco(clienteId, enderecoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteAtualizado);
    }



}
