package com.tuxoftware.ms_padron_unico.controller;

import com.tuxoftware.ms_padron_unico.persistence.entity.CatalogoGiro;
import com.tuxoftware.ms_padron_unico.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping("/giros")
    public ResponseEntity<List<CatalogoGiro>> listarGiros() {
        return ResponseEntity.ok(catalogoService.listarGirosActivos());
    }
}
