package company.a.charlee.repository;

import company.a.charlee.entity.TelegramPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramPostRepository extends JpaRepository<TelegramPost, String> {
}