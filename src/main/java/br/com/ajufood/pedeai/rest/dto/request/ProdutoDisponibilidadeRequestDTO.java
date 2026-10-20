package br.com.ajufood.pedeai.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDisponibilidadeRequestDTO {

    private boolean disponivel;
}