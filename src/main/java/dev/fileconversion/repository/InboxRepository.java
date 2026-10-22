package dev.fileconversion.repository;


import dev.fileconversion.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InboxRepository extends JpaRepository<Inbox, Long> {
}
