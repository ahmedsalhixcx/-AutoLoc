package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.MaintenanceDTO;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    private void validateMaintenance(MaintenanceDTO dto) {
        if (dto.getDateDebut() != null && dto.getDateFin() != null
                && dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    @Override
    public MaintenanceDTO create(MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.getIdMaintenance() != null) {
            throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        validateMaintenance(maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);

        if (maintenanceDTO.getIdVehicule() != null) {
            Vehicule vehicule = vehiculeRepository.findById(maintenanceDTO.getIdVehicule())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", maintenanceDTO.getIdVehicule()));
            maintenance.setVehicule(vehicule);
        }

        Maintenance saved = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDTO(saved);
    }

    @Override
    public MaintenanceDTO findById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
        return maintenanceMapper.toDTO(maintenance);
    }

    @Override
    public List<MaintenanceDTO> findAll() {
        return maintenanceMapper.toDTOList(maintenanceRepository.findAll());
    }

    @Override
    public MaintenanceDTO update(Long id, MaintenanceDTO maintenanceDTO) {
        validateMaintenance(maintenanceDTO);
        Maintenance existant = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));

        existant.setDateDebut(maintenanceDTO.getDateDebut());
        existant.setDateFin(maintenanceDTO.getDateFin());
        existant.setDescription(maintenanceDTO.getDescription());

        if (maintenanceDTO.getIdVehicule() != null) {
            Vehicule vehicule = vehiculeRepository.findById(maintenanceDTO.getIdVehicule())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", maintenanceDTO.getIdVehicule()));
            existant.setVehicule(vehicule);
        }

        Maintenance saved = maintenanceRepository.save(existant);
        return maintenanceMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }
}
