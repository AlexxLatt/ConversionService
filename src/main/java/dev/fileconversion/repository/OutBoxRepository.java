package dev.fileconversion.repository;

import dev.fileconversion.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OutBoxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
}
