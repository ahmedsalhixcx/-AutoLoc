package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    public Maintenance addMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance updateMaintenance(Maintenance maintenance) {
        Long id = maintenance.getIdMaintenance();
        if (id == null || !maintenanceRepository.existsById(id)) {
            throw new EntityNotFoundException("Maintenance introuvable : " + id);
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance retrieveMaintenance(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> retrieveAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    @Override
    public void removeMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
