package com.projeto.scribe.controller;


import com.projeto.scribe.dto.ResultadoDTO;
import com.projeto.scribe.model.Cartorio;
import com.projeto.scribe.service.CartorioService;
import com.projeto.scribe.specification.filter.Filtro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartorio")
@RequiredArgsConstructor
public class CartorioController {

    private final CartorioService cartorioService;

    @GetMapping
    public ResponseEntity<List<Cartorio>> listaCartorios() {
        return ResponseEntity.ok(cartorioService.getCartorios());
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filtro(@RequestBody Filtro filtro) {
        return ResponseEntity.ok(cartorioService.findByFilter(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> cartorioById(@PathVariable Integer id) {
        ResultadoDTO<String> resultado = cartorioService.getCartorioById(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PostMapping
    public ResponseEntity<String> registrarCartorio(@RequestBody Cartorio cartorio) {
        ResultadoDTO<String> resultado = cartorioService.salvarCartorio(cartorio);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCartorio(@PathVariable Integer id, @RequestBody Cartorio cartorio) {
        ResultadoDTO<String> resultado = cartorioService.editarCartorio(id, cartorio);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirCartorio(@PathVariable Integer id) {
        ResultadoDTO<String> resultado = cartorioService.excluirCartorio(id);
        return !resultado.isSucesso() ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado.getMensagemErro())
                : ResponseEntity.ok(resultado.getResultado());
    }

}
