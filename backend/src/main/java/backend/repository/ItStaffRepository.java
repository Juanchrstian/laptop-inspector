package backend.repository;

import backend.entity.ItStaff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItStaffRepository
        extends JpaRepository<ItStaff, Long> {
}