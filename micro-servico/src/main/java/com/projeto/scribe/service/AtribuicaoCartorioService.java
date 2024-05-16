package com.projeto.scribe.service;

import com.projeto.scribe.dto.AtribuicaoCartorioDTO;
import com.projeto.scribe.dto.AtribuicaoCartorioPageDTO;
import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.model.AtribuicaoCartorio;
import com.projeto.scribe.repository.AtribuicaoCartorioRepository;
import com.projeto.scribe.specification.AtribuicaoCartorioSpecification;
import com.projeto.scribe.specification.filter.Filtro;
import com.projeto.scribe.util.JsonUtil;
import com.projeto.scribe.util.Mensagens;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AtribuicaoCartorioService {

    private final AtribuicaoCartorioRepository atribuicaoCartorioRepository;

    public List<AtribuicaoCartorio> getAtribuicoes(){
        return atribuicaoCartorioRepository.findAll();
    }

    public Page<AtribuicaoCartorioPageDTO> findByFilter(Filtro filtro) {
        AtribuicaoCartorioSpecification atribuicaoCartorioSpecification = new AtribuicaoCartorioSpecification();
        Specification<AtribuicaoCartorio> tranSpec = atribuicaoCartorioSpecification.build(filtro);
        Pageable pageable = atribuicaoCartorioSpecification.buildPageable(filtro);
        Page<AtribuicaoCartorio> atribuicaoCartoriosPage = atribuicaoCartorioRepository.findAll(tranSpec, pageable);

        List<AtribuicaoCartorioPageDTO> atribuicaoSimplificados = atribuicaoCartoriosPage.map(atribuicaoCartorio -> AtribuicaoCartorioPageDTO
                        .builder()
                        .id(atribuicaoCartorio.getId())
                        .nome(atribuicaoCartorio.getNome())
                        .build())
                .getContent();

        return new PageImpl<>(atribuicaoSimplificados, pageable, atribuicaoCartoriosPage.getTotalElements());
    }

    public ResultadoDTO<String> getAtribuicaoById(String id){
        Optional<AtribuicaoCartorio> atribuicaoCartorio = atribuicaoCartorioRepository.findById(id);
        if(atribuicaoCartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.ATRIBUICAO_NAO_ENCONTRADA);
        }

        return new ResultadoDTO<>(true, JsonUtil.converteJson(atribuicaoCartorio.get()), null);
    }

    @Transactional
    public ResultadoDTO<String> salvarAtribuicao(AtribuicaoCartorio atribuicaoCartorio){
        String validacao = validacaoSalvar(atribuicaoCartorio);
        if(!validacao.equals("Ok")){
            return new ResultadoDTO<>(false, null, validacao);
        }

        if(Objects.isNull(atribuicaoCartorio.getSituacao())){
            atribuicaoCartorio.setSituacao(true);
        }

        AtribuicaoCartorio atribuicaoCartorioSalvo = atribuicaoCartorioRepository.save(atribuicaoCartorio);

        return new ResultadoDTO<>(true, JsonUtil.converteJson(atribuicaoCartorioSalvo), null);
    }

    @Transactional
    public ResultadoDTO<String> editarAtribuicao(String id, AtribuicaoCartorioDTO atribuicaoCartorioDTO){
        Optional<AtribuicaoCartorio> atribuicaoCartorioOptional = atribuicaoCartorioRepository.findById(id);
        AtribuicaoCartorio atribuicaoCartorioOriginal;
        if(atribuicaoCartorioOptional.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.ATRIBUICAO_NAO_ENCONTRADA);
        }

        atribuicaoCartorioOriginal = atribuicaoCartorioOptional.get();

        if(liberadoEditar(atribuicaoCartorioDTO.getNome(), atribuicaoCartorioOriginal.getNome())){
            String idExistente = atribuicaoCartorioRepository.getIdAtribuicao(atribuicaoCartorioDTO.getNome());
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_DUPLICADO.replace("{id}", idExistente));
        }

        AtribuicaoCartorio atribuicaoCartorioSalvo = atribuicaoCartorioRepository.save(
                AtribuicaoCartorio
                        .builder()
                        .id(atribuicaoCartorioOriginal.getId())
                        .nome(atribuicaoCartorioDTO.getNome())
                        .situacao(Objects.nonNull(atribuicaoCartorioDTO.getSituacao())
                                ? atribuicaoCartorioDTO.getSituacao()
                                : atribuicaoCartorioOriginal.getSituacao())
                        .build()
        );

        return new ResultadoDTO<>(true, JsonUtil.converteJson(atribuicaoCartorioSalvo), null);
    }

    public ResultadoDTO<String> excluirAtribuicao(String id){
        Optional<AtribuicaoCartorio> atribuicaoCartorio = atribuicaoCartorioRepository.findById(id);
        if(atribuicaoCartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.ATRIBUICAO_NAO_ENCONTRADA);
        }

        if(atribuicaoCartorioRepository.isAtribuicaoVinculada(id)){
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_UTILIZADO);
        }

        atribuicaoCartorio.ifPresent(atribuicaoCartorioRepository::delete);
        return new ResultadoDTO<>(true, Mensagens.REGISTRO_EXCLUIDO.replace("{id}", atribuicaoCartorio.get().getId()), null);
    }

    private String validacaoSalvar(AtribuicaoCartorio atribuicaoCartorio){
        if(existsMesmoNome(atribuicaoCartorio.getNome())){
            String id = atribuicaoCartorioRepository.getIdAtribuicao(atribuicaoCartorio.getNome());
            return Mensagens.REGISTRO_DUPLICADO.replace("{id}", id);
        }

        if(existsMesmoId(atribuicaoCartorio.getId())){
            return Mensagens.REGISTRO_JA_CADASTRADO;
        }

        return "Ok";
    }

    private boolean existsMesmoNome(String nome){
        return atribuicaoCartorioRepository.existsAtribuicaoCartorioByNome(nome);
    }

    private boolean existsMesmoId(String id) {
        return atribuicaoCartorioRepository.existsAtribuicaoCartorioById(id);
    }

    private boolean liberadoEditar(String nome, String nomeOriginal){
        return existsMesmoNome(nome) && !nome.equals(nomeOriginal);
    }
}
