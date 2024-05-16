package com.projeto.scribe.service;

import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.model.Cartorio;
import com.projeto.scribe.repository.CartorioRepository;
import com.projeto.scribe.specification.CartorioSpecification;
import com.projeto.scribe.specification.filter.Filtro;
import com.projeto.scribe.util.JsonUtil;
import com.projeto.scribe.util.Mensagens;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartorioService {

    private final CartorioRepository cartorioRepository;

    public List<Cartorio> getCartorios(){
        return cartorioRepository.findAll();
    }

    public Page<Cartorio> findByFilter(Filtro filtro) {
        CartorioSpecification cartorioSpecification = new CartorioSpecification();
        Specification<Cartorio> tranSpec = cartorioSpecification.build(filtro);
        Pageable pageable = cartorioSpecification.buildPageable(filtro);
        return cartorioRepository.findAll(tranSpec, pageable);
    }

    public ResultadoDTO<String> getCartorioById(Integer id){
        Optional<Cartorio> cartorio = cartorioRepository.findById(id);
        if(cartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.CARTORIO_NAO_ENCONTRADO);
        }

        return new ResultadoDTO<>(true, JsonUtil.converteJson(cartorio.get()), null);
    }

    public ResultadoDTO<String> salvarCartorio(Cartorio cartorio){
        String validacao = validacaoCampos(cartorio);
        if(!validacao.equals("Ok")){
            return new ResultadoDTO<>(false, null, validacao);
        }

        Cartorio cartorioSalvo = cartorioRepository.save(cartorio);

        return new ResultadoDTO<>(true, JsonUtil.converteJson(cartorioSalvo), null);
    }


    public ResultadoDTO<String> editarCartorio(Integer id, Cartorio cartorio){
        Optional<Cartorio> cartorioOptional = cartorioRepository.findById(id);
        Cartorio cartorio1;
        if(cartorioOptional.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.CARTORIO_NAO_ENCONTRADO);
        }

        cartorio1 = cartorioOptional.get();

        if(liberadoEditar(cartorio.getNome(), cartorio1.getNome())){
            String idExistente = cartorioRepository.getIdCartorio(cartorio.getNome());
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_DUPLICADO.replace("{id}", idExistente));
        }

        Cartorio cartorioSalvo = cartorioRepository.save(
                Cartorio
                        .builder()
                        .id(cartorio1.getId())
                        .nome(cartorio.getNome())
                        .observacao(Objects.nonNull(cartorio1.getObservacao()) ? cartorio1.getObservacao() : cartorio.getObservacao())
                        .situacaoCartorio(cartorio.getSituacaoCartorio())
                        .build()
        );

        return new ResultadoDTO<>(true, JsonUtil.converteJson(cartorioSalvo), null);
    }

    public ResultadoDTO<String> excluirCartorio(Integer id){
        Optional<Cartorio> cartorio = cartorioRepository.findById(id);
        if(cartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.CARTORIO_NAO_ENCONTRADO);
        }

        if(cartorioRepository.isCartorioVinculado(id)){
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_UTILIZADO);
        }

        cartorio.ifPresent(cartorioRepository::delete);
        return new ResultadoDTO<>(true, Mensagens.REGISTRO_EXCLUIDO.replace("{id}", cartorio.get().getId().toString()), null);
    }


    private String validacaoCampos(Cartorio cartorio){
        if(existsMesmoNome(cartorio.getNome())){
            String id = cartorioRepository.getIdCartorio(cartorio.getNome());
            return Mensagens.REGISTRO_DUPLICADO.replace("{id}", id);
        }

        if(cartorioRepository.existsCartorioById(cartorio.getId())){
            return Mensagens.REGISTRO_JA_CADASTRADO;
        }

        return "Ok";
    }

    private boolean existsMesmoNome(String nome){
        return cartorioRepository.existsCartorioByNome(nome);
    }

    private boolean liberadoEditar(String nome, String nomeOriginal){
        return existsMesmoNome(nome) && !nome.equals(nomeOriginal);
    }

}
