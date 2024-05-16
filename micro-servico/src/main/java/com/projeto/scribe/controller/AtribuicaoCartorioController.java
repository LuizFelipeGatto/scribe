package com.projeto.scribe.controller;

import com.projeto.scribe.dto.AtribuicaoCartorioDTO;
import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.model.AtribuicaoCartorio;
import com.projeto.scribe.service.AtribuicaoCartorioService;
import com.projeto.scribe.specification.filter.Filtro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atribuicao")
@RequiredArgsConstructor
public class AtribuicaoCartorioController {

    private final AtribuicaoCartorioService atribuicaoCartorioService;

    @GetMapping
    public ResponseEntity<List<AtribuicaoCartorio>> listaAtribuicoes() {
        return ResponseEntity.ok(atribuicaoCartorioService.getAtribuicoes());
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filtro(@RequestBody Filtro filtro) {
        return ResponseEntity.ok(atribuicaoCartorioService.findByFilter(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> atribuicaoById(@PathVariable String id) {
        ResultadoDTO<String> resultado = atribuicaoCartorioService.getAtribuicaoById(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PostMapping
    public ResponseEntity<String> registrarAtribuicao(@RequestBody AtribuicaoCartorio atribuicaoCartorio) {
        ResultadoDTO<String> resultado = atribuicaoCartorioService.salvarAtribuicao(atribuicaoCartorio);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarAtribuicao(@PathVariable String id, @RequestBody AtribuicaoCartorioDTO atribuicaoCartorioDTO) {
        ResultadoDTO<String> resultado = atribuicaoCartorioService.editarAtribuicao(id, atribuicaoCartorioDTO);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirAtribuicao(@PathVariable String id) {
        ResultadoDTO<String> resultado = atribuicaoCartorioService.excluirAtribuicao(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

}
