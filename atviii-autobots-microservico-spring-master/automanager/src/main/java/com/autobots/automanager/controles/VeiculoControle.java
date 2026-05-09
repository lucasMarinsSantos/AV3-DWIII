package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.modelos.VeiculoModelo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private RepositorioVeiculo repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoModelo> buscarVeiculo(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    VeiculoModelo modelo = new VeiculoModelo(veiculo);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).buscarVeiculo(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).listarVeiculos()).withRel("veiculos"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VeiculoModelo>> listarVeiculos() {
        List<VeiculoModelo> modelos = repositorio.findAll().stream()
                .map(veiculo -> {
                    VeiculoModelo modelo = new VeiculoModelo(veiculo);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).buscarVeiculo(veiculo.getId())).withSelfRel());
                    return modelo;
                })
                .collect(Collectors.toList());
        CollectionModel<VeiculoModelo> colecao = CollectionModel.of(modelos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).listarVeiculos()).withSelfRel());
        return ResponseEntity.ok(colecao);
    }

    @PostMapping
    public ResponseEntity<VeiculoModelo> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        Veiculo salvo = repositorio.save(veiculo);
        VeiculoModelo modelo = new VeiculoModelo(salvo);
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).buscarVeiculo(salvo.getId())).withSelfRel());
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).listarVeiculos()).withRel("veiculos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoModelo> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo dados) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    if (dados.getTipo() != null) veiculo.setTipo(dados.getTipo());
                    if (dados.getModelo() != null) veiculo.setModelo(dados.getModelo());
                    if (dados.getPlaca() != null) veiculo.setPlaca(dados.getPlaca());
                    if (dados.getProprietario() != null) veiculo.setProprietario(dados.getProprietario());
                    Veiculo atualizado = repositorio.save(veiculo);
                    VeiculoModelo modelo = new VeiculoModelo(atualizado);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).buscarVeiculo(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).listarVeiculos()).withRel("veiculos"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return ResponseEntity.notFound().build();
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}