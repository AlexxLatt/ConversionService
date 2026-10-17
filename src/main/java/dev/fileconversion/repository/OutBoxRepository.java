package dev.fileconversion.repository;

import dev.fileconversion.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutBox, Long> {

    List<OutBox> findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
}
