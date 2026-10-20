package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.CategoriaProdutoModel;
import br.com.ajufood.pedeai.repository.CategoriaProdutoRepository;
import br.com.ajufood.pedeai.rest.dto.request.CategoriaProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.CategoriaProdutoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaProdutoService {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public CategoriaProdutoResponseDTO obterPorId(int id) {

        CategoriaProdutoModel categoria = categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Categoria com ID " + id + " não encontrada."
                ));

        return modelMapper.map(categoria, CategoriaProdutoResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<CategoriaProdutoResponseDTO> obterTodos() {

        return categoriaProdutoRepository.findAll()
                .stream()
                .map(categoria ->
                        modelMapper.map(categoria, CategoriaProdutoResponseDTO.class))
                .toList();
    }

    @Transactional
    public CategoriaProdutoResponseDTO salvar(
            CategoriaProdutoRequestDTO categoriaDTO) {

        if (categoriaProdutoRepository.existsByNomeIgnoreCase(categoriaDTO.getNome())) {
            throw new BusinessRuleException(
                    "Já existe uma categoria cadastrada com o nome '" + categoriaDTO.getNome() + "'.");
        }



        try {

            CategoriaProdutoModel categoria =
                    modelMapper.map(categoriaDTO, CategoriaProdutoModel.class);

            CategoriaProdutoModel categoriaSalva =
                    categoriaProdutoRepository.save(categoria);

            return modelMapper.map(
                    categoriaSalva,
                    CategoriaProdutoResponseDTO.class
            );

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao salvar categoria.", e
            );
        }
    }

    @Transactional
    public CategoriaProdutoResponseDTO atualizar(
            int id,
            CategoriaProdutoRequestDTO categoriaDTO) {

        try {

            CategoriaProdutoModel categoriaExistente =
                    categoriaProdutoRepository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException(
                                    "Categoria com ID " + id + " não encontrada."
                            ));

            categoriaExistente.setNome(categoriaDTO.getNome());
            categoriaExistente.setDescricao(categoriaDTO.getDescricao());

            CategoriaProdutoModel categoriaAtualizada =
                    categoriaProdutoRepository.save(categoriaExistente);

            return modelMapper.map(
                    categoriaAtualizada,
                    CategoriaProdutoResponseDTO.class
            );

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar categoria.", e
            );
        }
    }

    @Transactional
    public void deletar(int id) {

        try {

            obterPorId(id);

            categoriaProdutoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataIntegrityException(
                    "Não foi possível excluir a categoria, pois ela possui vínculos.",
                    e
            );
        }
    }
}