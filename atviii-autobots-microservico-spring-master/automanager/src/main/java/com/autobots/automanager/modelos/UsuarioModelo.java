package com.autobots.automanager.modelos;

import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.entitades.Usuario;

public class UsuarioModelo extends RepresentationModel<UsuarioModelo> {
    private final Usuario usuario;

    public UsuarioModelo(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}