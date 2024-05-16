package com.projeto.scribe.service;

import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.dto.SituacaoCartorioDTO;
import com.projeto.scribe.model.SituacaoCartorio;
import com.projeto.scribe.repository.CartorioRepository;
import com.projeto.scribe.repository.SituacaoCartorioRepository;
import com.projeto.scribe.specification.SituacaoCartorioSpecification;
import com.projeto.scribe.specification.filter.Filtro;
import com.projeto.scribe.util.JsonUtil;
import com.projeto.scribe.util.Mensagens;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SituacaoCartorioService {

    private final SituacaoCartorioRepository situacaoCartorioRepository;

    private final CartorioRepository cartorioRepository;

    public List<SituacaoCartorio> getSituacoes(){
        return situacaoCartorioRepository.findAll();
    }

    public Page<SituacaoCartorio> findByFilter(Filtro filtro) {
        SituacaoCartorioSpecification situacaoCartorioSpecification = new SituacaoCartorioSpecification();
        Specification<SituacaoCartorio> tranSpec = situacaoCartorioSpecification.build(filtro);
        Pageable pageable = situacaoCartorioSpecification.buildPageable(filtro);
        return situacaoCartorioRepository.findAll(tranSpec, pageable);
    }

    public ResultadoDTO<String> getSituacaoById(String id){
        Optional<SituacaoCartorio> situacaoCartorio = situacaoCartorioRepository.findById(id);
        if(situacaoCartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.SITUACAO_NAO_ENCONTRADA);
        }

        return new ResultadoDTO<>(true, JsonUtil.converteJson(situacaoCartorio.get()), null);
    }

    @Transactional
    public ResultadoDTO<String> salvarSituacao(SituacaoCartorio situacaoCartorio){
        String validacao = validaCampos(situacaoCartorio);
        if(!validacao.equals("Ok")){
            return new ResultadoDTO<>(false, null, validacao);
        }

        SituacaoCartorio situacaoCartorioSalvo = situacaoCartorioRepository.save(situacaoCartorio);

        return new ResultadoDTO<>(true, JsonUtil.converteJson(situacaoCartorioSalvo), null);
    }

    @Transactional
    public ResultadoDTO<String> editarSituacao(String id, SituacaoCartorioDTO situacaoCartorioDTO){
        Optional<SituacaoCartorio> situacaoCartorioOptional = situacaoCartorioRepository.findById(id);
        SituacaoCartorio situacaoCartorio;
        if(situacaoCartorioOptional.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.SITUACAO_NAO_ENCONTRADA);
        }

        situacaoCartorio = situacaoCartorioOptional.get();

        if(liberadoEditar(situacaoCartorioDTO.getNome(), situacaoCartorio.getNome())){
            String idExistente = situacaoCartorioRepository.getIdSituacaoCartorio(situacaoCartorioDTO.getNome());
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_DUPLICADO.replace("{id}", idExistente));
        }

        SituacaoCartorio situacaoCartorioSalvo = situacaoCartorioRepository.save(
                SituacaoCartorio
                        .builder()
                        .id(situacaoCartorio.getId())
                        .nome(situacaoCartorioDTO.getNome())
                        .build()
        );

        return new ResultadoDTO<>(true, JsonUtil.converteJson(situacaoCartorioSalvo), null);
    }

    public ResultadoDTO<String> excluirSituacao(String id){
        Optional<SituacaoCartorio> situacaoCartorio = situacaoCartorioRepository.findById(id);
        if(situacaoCartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.SITUACAO_NAO_ENCONTRADA);
        }

        if(cartorioRepository.isSituacaoVinculada(id)){
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_UTILIZADO);
        }

        situacaoCartorio.ifPresent(situacaoCartorioRepository::delete);
        return new ResultadoDTO<>(true, Mensagens.REGISTRO_EXCLUIDO.replace("{id}", situacaoCartorio.get().getId()), null);
    }

    private String validaCampos(SituacaoCartorio situacaoCartorio){
        if(existsMesmoNome(situacaoCartorio.getNome())){
            String id = situacaoCartorioRepository.getIdSituacaoCartorio(situacaoCartorio.getNome());
            return Mensagens.REGISTRO_DUPLICADO.replace("{id}", id);
        }

        if(situacaoCartorioRepository.existsSituacaoCartorioById(situacaoCartorio.getId())){
            return Mensagens.REGISTRO_JA_CADASTRADO;
        }

        return "Ok";
    }

    private boolean existsMesmoNome(String nome){
        return situacaoCartorioRepository.existsSituacaoCartorioByNome(nome);
    }

    private boolean liberadoEditar(String nome, String nomeOriginal){
        return existsMesmoNome(nome) && !nome.equals(nomeOriginal);
    }

}
