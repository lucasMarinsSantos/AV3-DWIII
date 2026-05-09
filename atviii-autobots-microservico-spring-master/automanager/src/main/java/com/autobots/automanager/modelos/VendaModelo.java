package com.autobots.automanager.modelos;

import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.entitades.Venda;

public class VendaModelo extends RepresentationModel<VendaModelo> {
    private final Venda venda;

    public VendaModelo(Venda venda) {
        this.venda = venda;
    }

    public Venda getVenda() {
        return venda;
    }
}