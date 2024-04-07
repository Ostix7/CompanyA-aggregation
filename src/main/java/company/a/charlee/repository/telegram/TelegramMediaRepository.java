package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramMediaRepository extends JpaRepository<TelegramMedia, String> {
    @Query("SELECT tm.mediaType, COUNT(tm) FROM TelegramMedia tm GROUP BY tm.mediaType")
    List<Object[]> countMediaTypeDistribution();
}