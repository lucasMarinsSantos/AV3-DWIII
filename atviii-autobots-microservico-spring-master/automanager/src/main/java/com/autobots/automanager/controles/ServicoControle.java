package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.modelos.ServicoModelo;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private RepositorioServico repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<ServicoModelo> buscarServico(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(servico -> {
                    ServicoModelo modelo = new ServicoModelo(servico);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).buscarServico(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).listarServicos()).withRel("servicos"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ServicoModelo>> listarServicos() {
        List<ServicoModelo> modelos = repositorio.findAll().stream()
                .map(servico -> {
                    ServicoModelo modelo = new ServicoModelo(servico);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).buscarServico(servico.getId())).withSelfRel());
                    return modelo;
                })
                .collect(Collectors.toList());
        CollectionModel<ServicoModelo> colecao = CollectionModel.of(modelos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).listarServicos()).withSelfRel());
        return ResponseEntity.ok(colecao);
    }

    @PostMapping
    public ResponseEntity<ServicoModelo> cadastrarServico(@RequestBody Servico servico) {
        Servico salvo = repositorio.save(servico);
        ServicoModelo modelo = new ServicoModelo(salvo);
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).buscarServico(salvo.getId())).withSelfRel());
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).listarServicos()).withRel("servicos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoModelo> atualizarServico(@PathVariable Long id, @RequestBody Servico dados) {
        return repositorio.findById(id)
                .map(servico -> {
                    if (dados.getNome() != null) servico.setNome(dados.getNome());
                    if (dados.getDescricao() != null) servico.setDescricao(dados.getDescricao());
                    if (dados.getValor() > 0) servico.setValor(dados.getValor());
                    Servico atualizado = repositorio.save(servico);
                    ServicoModelo modelo = new ServicoModelo(atualizado);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).buscarServico(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).listarServicos()).withRel("servicos"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return ResponseEntity.notFound().build();
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}