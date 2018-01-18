package dk.bearsoft.scaries.repository;

import dk.bearsoft.scaries.model.ScaryMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScaryMessageRepository extends JpaRepository<ScaryMessage, Long> {
}
