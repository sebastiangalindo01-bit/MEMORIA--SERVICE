package com.gestormemoria.memoriaservice.mapa;

import com.gestormemoria.memoriaservice.mapa.dto.BloqueRequest;
import com.gestormemoria.memoriaservice.mapa.dto.MapaRequest;
import com.gestormemoria.memoriaservice.mapa.dto.MapaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapaService {

    private final MapaRepository repo;

    @Transactional
    public MapaResponse crear(MapaRequest req) {
        validarSinSolapamiento(req.bloques());

        MapaMemoria mapa = new MapaMemoria();
        mapa.setNombre(req.nombre().trim());
        for (BloqueRequest b : req.bloques()) {
            Bloque bloque = new Bloque();
            bloque.setInicio(b.inicio());
            bloque.setTamanoKb(b.tamanoKb());
            mapa.agregarBloque(bloque);
        }
        return MapaResponse.de(repo.save(mapa));
    }

    @Transactional(readOnly = true)
    public List<MapaResponse> listar() {
        return repo.findAll().stream().map(MapaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public MapaResponse obtener(Long id) {
        return repo.findById(id)
                .map(MapaResponse::de)
                .orElseThrow(() -> new MapaNoEncontradoException(id));
    }

    /** Dos bloques no pueden ocupar las mismas direcciones. */
    private void validarSinSolapamiento(List<BloqueRequest> bloques) {
        List<BloqueRequest> ordenados = bloques.stream()
                .sorted(Comparator.comparingInt(BloqueRequest::inicio))
                .toList();
        for (int i = 1; i < ordenados.size(); i++) {
            BloqueRequest anterior = ordenados.get(i - 1);
            BloqueRequest actual = ordenados.get(i);
            int finAnterior = anterior.inicio() + anterior.tamanoKb();
            if (finAnterior > actual.inicio()) {
                throw new MapaInvalidoException(
                        "El bloque que empieza en " + actual.inicio()
                                + " se solapa con el bloque que empieza en " + anterior.inicio()
                                + ", que termina en " + finAnterior + ".");
            }
        }
    }
}