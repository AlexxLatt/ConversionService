package dev.fileconversion.repository;


import dev.fileconversion.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InBoxRepository extends JpaRepository<Inbox, Long> {
}
