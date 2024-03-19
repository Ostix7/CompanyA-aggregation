package company.a.charlee.repository;

import company.a.charlee.entity.TelegramReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramReactionRepository extends JpaRepository<TelegramReaction, Long> {
}