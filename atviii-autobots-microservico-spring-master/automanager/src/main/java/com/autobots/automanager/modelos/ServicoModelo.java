package com.autobots.automanager.modelos;

import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.entitades.Servico;

public class ServicoModelo extends RepresentationModel<ServicoModelo> {
    private final Servico servico;

    public ServicoModelo(Servico servico) {
        this.servico = servico;
    }

    public Servico getServico() {
        return servico;
    }
}