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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.modelos.EmpresaModelo;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private RepositorioEmpresa repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaModelo> buscarEmpresa(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(empresa -> {
                    EmpresaModelo modelo = new EmpresaModelo(empresa);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).buscarEmpresa(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).listarEmpresas()).withRel("empresas"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EmpresaModelo>> listarEmpresas() {
        List<EmpresaModelo> modelos = repositorio.findAll().stream()
                .map(empresa -> {
                    EmpresaModelo modelo = new EmpresaModelo(empresa);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).buscarEmpresa(empresa.getId())).withSelfRel());
                    return modelo;
                })
                .collect(Collectors.toList());
        CollectionModel<EmpresaModelo> colecao = CollectionModel.of(modelos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).listarEmpresas()).withSelfRel());
        return ResponseEntity.ok(colecao);
    }

    @PostMapping
    public ResponseEntity<EmpresaModelo> cadastrarEmpresa(@RequestBody Empresa empresa) {
        empresa.setCadastro(new Date());
        Empresa salva = repositorio.save(empresa);
        EmpresaModelo modelo = new EmpresaModelo(salva);
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).buscarEmpresa(salva.getId())).withSelfRel());
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).listarEmpresas()).withRel("empresas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaModelo> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa dados) {
        return repositorio.findById(id)
                .map(empresa -> {
                    if (dados.getRazaoSocial() != null) empresa.setRazaoSocial(dados.getRazaoSocial());
                    if (dados.getNomeFantasia() != null) empresa.setNomeFantasia(dados.getNomeFantasia());
                    if (dados.getEndereco() != null) empresa.setEndereco(dados.getEndereco());
                    if (dados.getTelefones() != null && !dados.getTelefones().isEmpty())
                        empresa.getTelefones().addAll(dados.getTelefones());
                    Empresa atualizada = repositorio.save(empresa);
                    EmpresaModelo modelo = new EmpresaModelo(atualizada);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).buscarEmpresa(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).listarEmpresas()).withRel("empresas"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return ResponseEntity.notFound().build();
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{empresaId}/usuario/{usuarioId}")
    public ResponseEntity<EmpresaModelo> associarUsuario(
            @PathVariable Long empresaId, @PathVariable Long usuarioId) {
        return repositorio.findById(empresaId)
                .map(empresa -> repositorioUsuario.findById(usuarioId)
                        .map(usuario -> {
                            empresa.getUsuarios().add(usuario);
                            Empresa salva = repositorio.save(empresa);
                            EmpresaModelo modelo = new EmpresaModelo(salva);
                            modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).buscarEmpresa(empresaId)).withSelfRel());
                            modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).listarEmpresas()).withRel("empresas"));
                            return ResponseEntity.ok(modelo);
                        })
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }
}