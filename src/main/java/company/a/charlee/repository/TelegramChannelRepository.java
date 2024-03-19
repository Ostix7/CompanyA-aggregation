package company.a.charlee.repository;

import company.a.charlee.entity.TelegramChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChannelRepository extends JpaRepository<TelegramChannel, Long> {
}