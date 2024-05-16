package com.projeto.scribe.service;

import com.projeto.scribe.dto.CartorioDTO;
import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.model.Cartorio;
import com.projeto.scribe.repository.AtribuicaoCartorioRepository;
import com.projeto.scribe.repository.CartorioRepository;
import com.projeto.scribe.repository.SituacaoCartorioRepository;
import com.projeto.scribe.specification.CartorioSpecification;
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
public class CartorioService {

    private final CartorioRepository cartorioRepository;

    private final AtribuicaoCartorioRepository atribuicaoCartorioRepository;

    private final SituacaoCartorioRepository situacaoCartorioRepository;

    public List<Cartorio> getCartorios(){
        return cartorioRepository.findAll();
    }

    public Page<CartorioDTO> findByFilter(Filtro filtro) {
        CartorioSpecification cartorioSpecification = new CartorioSpecification();
        Specification<Cartorio> tranSpec = cartorioSpecification.build(filtro);
        Pageable pageable = cartorioSpecification.buildPageable(filtro);
        Page<Cartorio> cartoriosPage = cartorioRepository.findAll(tranSpec, pageable);

        List<CartorioDTO> cartoriosSimplificados = cartoriosPage.map(cartorio -> CartorioDTO
                .builder()
                .id(cartorio.getId())
                .nome(cartorio.getNome())
                .build())
                .getContent();

        return new PageImpl<>(cartoriosSimplificados, pageable, cartoriosPage.getTotalElements());
    }


    public ResultadoDTO<String> getCartorioById(Integer id){
        Optional<Cartorio> cartorio = cartorioRepository.findById(id);
        if(cartorio.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.CARTORIO_NAO_ENCONTRADO);
        }

        return new ResultadoDTO<>(true, JsonUtil.converteJson(cartorio.get()), null);
    }

    @Transactional
    public ResultadoDTO<String> salvarCartorio(Cartorio cartorio){
        String validacao = validacaoCampos(cartorio);
        if(!validacao.equals("Ok")){
            return new ResultadoDTO<>(false, null, validacao);
        }

        Cartorio cartorioSalvo = cartorioRepository.save(cartorio);

        return new ResultadoDTO<>(true, JsonUtil.converteJson(cartorioSalvo), null);
    }

    @Transactional
    public ResultadoDTO<String> editarCartorio(Integer id, Cartorio cartorio){
        Optional<Cartorio> cartorioOptional = cartorioRepository.findById(id);
        Cartorio cartorio1;
        if(cartorioOptional.isEmpty()){
            return new ResultadoDTO<>(false, null, Mensagens.CARTORIO_NAO_ENCONTRADO);
        }

        cartorio1 = cartorioOptional.get();

        if(naoLiberadoEditar(cartorio.getNome(), cartorio1.getNome())){
            String idExistente = cartorioRepository.getIdCartorio(cartorio.getNome());
            return new ResultadoDTO<>(false, null, Mensagens.REGISTRO_DUPLICADO.replace("{id}", idExistente));
        }

        String result = validaCamposFaltantes(cartorio);

        if(!result.equals("Ok")){
            return new ResultadoDTO<>(false, null, result);
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

        String result = validaCamposFaltantes(cartorio);

        if(!result.equals("Ok")){
            return result;
        }

        return "Ok";
    }

    private String validaCamposFaltantes(Cartorio cartorio){
        if(Objects.isNull(cartorio.getSituacaoCartorio()) || Objects.isNull(cartorio.getSituacaoCartorio().getId())){
            return Mensagens.SITUACAO_FALTANTE;
        }

        if(situacaoCartorioRepository.findById(cartorio.getSituacaoCartorio().getId()).isEmpty()){
            return Mensagens.SITUACAO_NAO_ENCONTRADA;
        }

        if(Objects.isNull(cartorio.getAtribuicoes()) || cartorio.getAtribuicoes().isEmpty()){
            return Mensagens.ATIBUICAO_FALTANTE;
        }

        boolean todasAsAtribuicoesExistem = cartorio.getAtribuicoes().stream()
                .allMatch(atribuicaoCartorio ->
                        atribuicaoCartorioRepository.findById(atribuicaoCartorio.getId()).isPresent());

        if(!todasAsAtribuicoesExistem){
            return Mensagens.ATRIBUICAO_NAO_ENCONTRADA;
        }

        return "Ok";
    }

    private boolean existsMesmoNome(String nome){
        return cartorioRepository.existsCartorioByNome(nome);
    }

    private boolean naoLiberadoEditar(String nome, String nomeOriginal){
        return existsMesmoNome(nome) && !nome.equals(nomeOriginal);
    }

}
