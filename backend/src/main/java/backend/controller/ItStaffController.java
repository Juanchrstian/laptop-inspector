package backend.controller;

import backend.entity.ItStaff;
import backend.repository.ItStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/it-staff")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ItStaffController {

    private final ItStaffRepository repository;

    @GetMapping
    public List<ItStaff> getAll() {

        return repository.findAll();
    }
}