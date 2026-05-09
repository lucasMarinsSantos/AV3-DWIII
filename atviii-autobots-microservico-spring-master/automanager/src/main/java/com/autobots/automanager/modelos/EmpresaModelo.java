package com.autobots.automanager.modelos;

import org.springframework.hateoas.RepresentationModel;
import com.autobots.automanager.entitades.Empresa;

public class EmpresaModelo extends RepresentationModel<EmpresaModelo> {
    private final Empresa empresa;

    public EmpresaModelo(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
}