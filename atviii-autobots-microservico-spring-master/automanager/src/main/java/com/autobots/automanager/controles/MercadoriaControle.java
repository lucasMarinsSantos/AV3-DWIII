package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.modelos.MercadoriaModelo;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

    @Autowired
    private RepositorioMercadoria repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<MercadoriaModelo> buscarMercadoria(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(mercadoria -> {
                    MercadoriaModelo modelo = new MercadoriaModelo(mercadoria);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).buscarMercadoria(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).listarMercadorias()).withRel("mercadorias"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MercadoriaModelo>> listarMercadorias() {
        List<MercadoriaModelo> modelos = repositorio.findAll().stream()
                .map(mercadoria -> {
                    MercadoriaModelo modelo = new MercadoriaModelo(mercadoria);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).buscarMercadoria(mercadoria.getId())).withSelfRel());
                    return modelo;
                })
                .collect(Collectors.toList());
        CollectionModel<MercadoriaModelo> colecao = CollectionModel.of(modelos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).listarMercadorias()).withSelfRel());
        return ResponseEntity.ok(colecao);
    }

    @PostMapping
    public ResponseEntity<MercadoriaModelo> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        mercadoria.setCadastro(new Date());
        Mercadoria salva = repositorio.save(mercadoria);
        MercadoriaModelo modelo = new MercadoriaModelo(salva);
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).buscarMercadoria(salva.getId())).withSelfRel());
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).listarMercadorias()).withRel("mercadorias"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MercadoriaModelo> atualizarMercadoria(@PathVariable Long id, @RequestBody Mercadoria dados) {
        return repositorio.findById(id)
                .map(mercadoria -> {
                    if (dados.getNome() != null) mercadoria.setNome(dados.getNome());
                    if (dados.getDescricao() != null) mercadoria.setDescricao(dados.getDescricao());
                    if (dados.getValor() > 0) mercadoria.setValor(dados.getValor());
                    if (dados.getQuantidade() > 0) mercadoria.setQuantidade(dados.getQuantidade());
                    if (dados.getValidade() != null) mercadoria.setValidade(dados.getValidade());
                    if (dados.getFabricao() != null) mercadoria.setFabricao(dados.getFabricao());
                    Mercadoria atualizada = repositorio.save(mercadoria);
                    MercadoriaModelo modelo = new MercadoriaModelo(atualizada);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).buscarMercadoria(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).listarMercadorias()).withRel("mercadorias"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMercadoria(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return ResponseEntity.notFound().build();
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}