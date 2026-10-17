package dev.fileconversion.repository;


import dev.fileconversion.entity.InBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InBoxRepository extends JpaRepository<InBox, Long> {
}
