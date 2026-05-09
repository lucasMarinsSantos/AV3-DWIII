package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.UsuarioModelo;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private RepositorioUsuario repositorio;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModelo> buscarUsuario(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(usuario -> {
                    UsuarioModelo modelo = new UsuarioModelo(usuario);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).buscarUsuario(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).listarUsuarios()).withRel("usuarios"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UsuarioModelo>> listarUsuarios() {
        List<UsuarioModelo> modelos = repositorio.findAll().stream()
                .map(usuario -> {
                    UsuarioModelo modelo = new UsuarioModelo(usuario);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).buscarUsuario(usuario.getId())).withSelfRel());
                    return modelo;
                })
                .collect(Collectors.toList());
        CollectionModel<UsuarioModelo> colecao = CollectionModel.of(modelos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).listarUsuarios()).withSelfRel());
        return ResponseEntity.ok(colecao);
    }

    @PostMapping
    public ResponseEntity<UsuarioModelo> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario salvo = repositorio.save(usuario);
        UsuarioModelo modelo = new UsuarioModelo(salvo);
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).buscarUsuario(salvo.getId())).withSelfRel());
        modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).listarUsuarios()).withRel("usuarios"));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModelo> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario dados) {
        return repositorio.findById(id)
                .map(usuario -> {
                    if (dados.getNome() != null) usuario.setNome(dados.getNome());
                    if (dados.getNomeSocial() != null) usuario.setNomeSocial(dados.getNomeSocial());
                    if (dados.getPerfis() != null && !dados.getPerfis().isEmpty()) usuario.getPerfis().addAll(dados.getPerfis());
                    if (dados.getEndereco() != null) usuario.setEndereco(dados.getEndereco());
                    if (dados.getTelefones() != null && !dados.getTelefones().isEmpty()) usuario.getTelefones().addAll(dados.getTelefones());
                    if (dados.getDocumentos() != null && !dados.getDocumentos().isEmpty()) usuario.getDocumentos().addAll(dados.getDocumentos());
                    if (dados.getEmails() != null && !dados.getEmails().isEmpty()) usuario.getEmails().addAll(dados.getEmails());
                    if (dados.getCredenciais() != null && !dados.getCredenciais().isEmpty()) usuario.getCredenciais().addAll(dados.getCredenciais());
                    Usuario atualizado = repositorio.save(usuario);
                    UsuarioModelo modelo = new UsuarioModelo(atualizado);
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).buscarUsuario(id)).withSelfRel());
                    modelo.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).listarUsuarios()).withRel("usuarios"));
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return ResponseEntity.notFound().build();
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}