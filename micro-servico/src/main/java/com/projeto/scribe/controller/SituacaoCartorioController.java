package com.projeto.scribe.controller;

import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.dto.SituacaoCartorioDTO;
import com.projeto.scribe.model.SituacaoCartorio;
import com.projeto.scribe.service.SituacaoCartorioService;
import com.projeto.scribe.specification.filter.Filtro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/situacao")
@RequiredArgsConstructor
public class SituacaoCartorioController {

    private final SituacaoCartorioService situacaoCartorioService;

    @GetMapping
    public ResponseEntity<List<SituacaoCartorio>> listaSituacoes() {
        return ResponseEntity.ok(situacaoCartorioService.getSituacoes());
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filtro(@RequestBody Filtro filtro) {
        return ResponseEntity.ok(situacaoCartorioService.findByFilter(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> situacaoById(@PathVariable String id) {
        ResultadoDTO<String> resultado = situacaoCartorioService.getSituacaoById(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PostMapping
    public ResponseEntity<String> registrarSituacao(@RequestBody SituacaoCartorio situacaoCartorio) {
        ResultadoDTO<String> resultado = situacaoCartorioService.salvarSituacao(situacaoCartorio);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarSituacao(@PathVariable String id, @RequestBody SituacaoCartorioDTO situacaoCartorioDTO) {
        ResultadoDTO<String> resultado = situacaoCartorioService.editarSituacao(id, situacaoCartorioDTO);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirSituacao(@PathVariable String id) {
        ResultadoDTO<String> resultado = situacaoCartorioService.excluirSituacao(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

}
