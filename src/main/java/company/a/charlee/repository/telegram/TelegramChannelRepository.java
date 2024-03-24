package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChannelRepository extends JpaRepository<TelegramChannel, Long> {
}