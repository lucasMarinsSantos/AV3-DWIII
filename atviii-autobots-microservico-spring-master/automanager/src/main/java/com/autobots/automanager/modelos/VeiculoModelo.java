package com.autobots.automanager.modelos;

import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.entitades.Veiculo;

public class VeiculoModelo extends RepresentationModel<VeiculoModelo> {
    private final Veiculo veiculo;

    public VeiculoModelo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }
}