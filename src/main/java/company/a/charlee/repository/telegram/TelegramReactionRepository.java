package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramReactionRepository extends JpaRepository<TelegramReaction, Long> {
}