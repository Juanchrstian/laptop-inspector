package backend.controller;

import backend.entity.Employee;
import backend.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repository;

    // =========================================
    // GET ALL EMPLOYEES
    // =========================================

    @GetMapping
    public List<Employee> getAll() {

        return repository.findAll();
    }

    // =========================================
    // GET EMPLOYEE BY NAME
    // =========================================

    @GetMapping("/name/{fullName}")
    public ResponseEntity<Employee> getByName(
            @PathVariable String fullName
    ) {

        return repository
                .findByFullName(fullName)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

}