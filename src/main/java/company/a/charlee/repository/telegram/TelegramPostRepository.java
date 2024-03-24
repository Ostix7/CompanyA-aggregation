package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramPostRepository extends JpaRepository<TelegramPost, String> {
    List<TelegramPost> findByProcessedFalse();

}