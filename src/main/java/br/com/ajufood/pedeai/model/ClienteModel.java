package br.com.ajufood.pedeai.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class ClienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "O nome é obrigatório!")
    @Column(name = "nome", nullable = false, length = 128)
    @Length(min = 2, max = 128, message = "O nome deverá ter no mínimo 3 caracteres e no máximo 256")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório!")
    @CPF(message = "CPF inválido!")
    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    @Length(min = 11, max = 11, message = "O CPF deverá ter obrigatoriamente 11 digitos")
    private String cpf;

    @NotBlank(message = "O Email é obrigatório!")
    @Email(message= "Email inválido!")
    @Length(min = 3, max = 256, message = "O Email deverá ter no mínimo 3 caracteres e no máximo 256")
    @Column(name = "email", nullable = false, length = 256, unique = true)
    private String email;

    @NotBlank(message = "O telefone é obrigatório!")
    @Column(name = "telefone", nullable = false, length = 11)
    @Length(min = 10, max = 11, message = "O telefone deverá ter obrigatoriamente entre 10 e 11 digitos")

    private String telefone;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnderecoModel> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    private List<PedidoModel> pedidos = new ArrayList<>();

    public void addEndereco(EnderecoModel endereco) {
        this.enderecos.add(endereco);
        endereco.setCliente(this);
    }

    public void removeEndereco(EnderecoModel endereco) {
        this.enderecos.remove(endereco);
        endereco.setCliente(null);
    }

    public void addPedido(PedidoModel pedido) {
        this.pedidos.add(pedido);
        pedido.setCliente(this);
    }

    public void removePedido(PedidoModel pedido) {
        this.pedidos.remove(pedido);
        pedido.setCliente(null);
    }

}
