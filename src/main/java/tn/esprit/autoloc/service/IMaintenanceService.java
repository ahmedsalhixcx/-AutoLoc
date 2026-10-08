package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    Maintenance addMaintenance(Maintenance maintenance);

    Maintenance updateMaintenance(Maintenance maintenance);

    Maintenance retrieveMaintenance(Long id);

    List<Maintenance> retrieveAllMaintenances();

    void removeMaintenance(Long id);
}
