package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.web.dto.EmployeDTO;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    private void validateEmploye(EmployeDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé ne peut pas être vide");
        }
        if (dto.getPrenom() == null || dto.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom de l'employé ne peut pas être vide");
        }
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'employé est obligatoire");
        }
        if (dto.getIdAgence() == null) {
            throw new IllegalArgumentException("L'identifiant de l'agence est obligatoire");
        }
    }

    @Override
    public EmployeDTO create(EmployeDTO employeDTO) {
        if (employeDTO.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant");
        }
        validateEmploye(employeDTO);
        Agence agence = agenceRepository.findById(employeDTO.getIdAgence())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", employeDTO.getIdAgence()));

        Employe employe = employeMapper.toEntity(employeDTO);
        employe.setAgence(agence);
        Employe saved = employeRepository.save(employe);
        return employeMapper.toDTO(saved);
    }

    @Override
    public EmployeDTO findById(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
        return employeMapper.toDTO(employe);
    }

    @Override
    public List<EmployeDTO> findAll() {
        return employeMapper.toDTOList(employeRepository.findAll());
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO employeDTO) {
        validateEmploye(employeDTO);
        Employe existant = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
        Agence agence = agenceRepository.findById(employeDTO.getIdAgence())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", employeDTO.getIdAgence()));

        existant.setNom(employeDTO.getNom());
        existant.setPrenom(employeDTO.getPrenom());
        existant.setRole(employeDTO.getRole());
        existant.setAgence(agence);

        Employe saved = employeRepository.save(existant);
        return employeMapper.toDTO(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }
}
