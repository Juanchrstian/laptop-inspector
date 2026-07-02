package backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "it_staff")
@Data
public class ItStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String staffCode;

    @Column(nullable = false)
    private String fullName;
}