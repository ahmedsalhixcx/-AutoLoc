package tn.esprit.autoloc.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe addEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    @Override
    public Employe updateEmploye(Employe employe) {
        Long id = employe.getIdEmploye();
        if (id == null || !employeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employe introuvable : " + id);
        }
        return employeRepository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public Employe retrieveEmploye(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employe introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> retrieveAllEmployes() {
        return employeRepository.findAll();
    }

    @Override
    public void removeEmploye(Long id) {
        employeRepository.deleteById(id);
    }
}
