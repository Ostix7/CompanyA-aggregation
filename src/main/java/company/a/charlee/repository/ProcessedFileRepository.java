package company.a.charlee.repository;

import company.a.charlee.entity.processed.ProcessedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProcessedFileRepository extends JpaRepository<ProcessedFile, Long> {
    Optional<ProcessedFile> findByBigQueryId(String bigQueryId);

    @Transactional
    @Modifying
    @Query("UPDATE ProcessedFile pf SET pf.isProcessed = TRUE WHERE pf.bigQueryId = :bigQueryId")
    void markAsProcessed(String bigQueryId);
    List<ProcessedFile> findByIsProcessedFalseAndMediaType(String mediaType);
}