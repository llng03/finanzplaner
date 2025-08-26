package de.ftracker.services.pots;

import de.ftracker.model.pots.PotSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PotSummaryRepository extends JpaRepository<PotSummary, Long> {
}
