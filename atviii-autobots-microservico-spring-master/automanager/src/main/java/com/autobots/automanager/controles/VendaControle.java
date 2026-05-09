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

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.VendaModelo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private RepositorioVenda repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<VendaModelo> buscarVenda(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(venda -> {
                    VendaModelo modelo = new VendaModelo(venda);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).buscarVenda(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).listarVendas()).withRel("vendas"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VendaModelo>> listarVendas() {
        List<VendaModelo> modelos = repositorio.findAll().stream()
                .map(venda -> {
                    VendaModelo modelo = new VendaModelo(venda);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).buscarVenda(venda.getId())).withSelfRel());
                    return modelo;
                })
                .collect(Collectors.toList());
        CollectionModel<VendaModelo> colecao = CollectionModel.of(modelos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).listarVendas()).withSelfRel());
        return ResponseEntity.ok(colecao);
    }

    @PostMapping
    public ResponseEntity<VendaModelo> cadastrarVenda(@RequestBody Venda venda) {
        venda.setCadastro(new Date());
        Venda salva = repositorio.save(venda);
        VendaModelo modelo = new VendaModelo(salva);
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).buscarVenda(salva.getId())).withSelfRel());
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).listarVendas()).withRel("vendas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaModelo> atualizarVenda(@PathVariable Long id, @RequestBody Venda dados) {
        return repositorio.findById(id)
                .map(venda -> {
                    if (dados.getIdentificacao() != null) venda.setIdentificacao(dados.getIdentificacao());
                    if (dados.getCliente() != null) venda.setCliente(dados.getCliente());
                    if (dados.getFuncionario() != null) venda.setFuncionario(dados.getFuncionario());
                    if (dados.getVeiculo() != null) venda.setVeiculo(dados.getVeiculo());
                    if (dados.getMercadorias() != null && !dados.getMercadorias().isEmpty())
                        venda.getMercadorias().addAll(dados.getMercadorias());
                    if (dados.getServicos() != null && !dados.getServicos().isEmpty())
                        venda.getServicos().addAll(dados.getServicos());
                    Venda atualizada = repositorio.save(venda);
                    VendaModelo modelo = new VendaModelo(atualizada);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).buscarVenda(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).listarVendas()).withRel("vendas"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return ResponseEntity.notFound().build();
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}