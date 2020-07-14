package digitalinnovation.example.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

import digitalinnovation.example.controller.request.SoldadoEditRequest;
import digitalinnovation.example.controller.response.SoldadoListResponse;
import digitalinnovation.example.controller.response.SoldadoResponse;
import digitalinnovation.example.dto.Soldado;
import digitalinnovation.example.entity.SoldadoEntity;
import digitalinnovation.example.repository.SoldadoRepository;
import digitalinnovation.example.resource.ResourceSoldado;

@Service
public class SoldadoService {

    private SoldadoRepository soldadoRepository;
    private ObjectMapper objectMapper;
    private ResourceSoldado resourceSoldado;

    public SoldadoService(SoldadoRepository soldadoRepository, ObjectMapper objectMapper, ResourceSoldado resourceSoldado) {
        this.soldadoRepository = soldadoRepository;
        this.objectMapper = objectMapper;
        this.resourceSoldado = resourceSoldado;
    }

    public SoldadoResponse buscarSoldado(Long id) {
        SoldadoEntity soldado = soldadoRepository.findById(id).orElseThrow();
        SoldadoResponse soldadoResponse = resourceSoldado.criarLinkDetalhe(soldado);
        return soldadoResponse;
    }

    public void criarSoldado(Soldado soldado){
        SoldadoEntity soldadoEntity = objectMapper.convertValue(soldado, SoldadoEntity.class);
        soldadoRepository.save(soldadoEntity);
    }

    public void alterarSoldado(Long id, SoldadoEditRequest soldadoEditRequest) {
        SoldadoEntity soldadoEntity = objectMapper.convertValue(soldadoEditRequest, SoldadoEntity.class);
        soldadoEntity.setId(id);
        soldadoRepository.save(soldadoEntity);
    }

    public void deletarSoldado(Long id) {
        SoldadoEntity soldado = soldadoRepository.findById(id).orElseThrow();
        soldadoRepository.delete(soldado);
    }

    public Resources<SoldadoListResponse> buscarSoldados(){
        List<SoldadoEntity> all = soldadoRepository.findAll();
        List<SoldadoListResponse> soldadoStream = all.stream()
                .map(it -> resourceSoldado.criarLink(it))
                .collect(Collectors.toList());
        return new Resources<>(soldadoStream);
    }
}
