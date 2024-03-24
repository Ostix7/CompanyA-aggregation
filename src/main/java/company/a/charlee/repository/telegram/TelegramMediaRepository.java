package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMediaRepository extends JpaRepository<TelegramMedia, Long> {
}