package com.autobots.automanager.modelos;

import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.entitades.Mercadoria;

public class MercadoriaModelo extends RepresentationModel<MercadoriaModelo> {
    private final Mercadoria mercadoria;

    public MercadoriaModelo(Mercadoria mercadoria) {
        this.mercadoria = mercadoria;
    }

    public Mercadoria getMercadoria() {
        return mercadoria;
    }
}