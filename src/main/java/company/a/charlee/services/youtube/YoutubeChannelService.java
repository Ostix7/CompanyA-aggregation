package company.a.charlee.services.youtube;

import company.a.charlee.entity.telegram.TelegramChannel;
import company.a.charlee.entity.youtube.YoutubeChannel;
import company.a.charlee.repository.telegram.TelegramChannelRepository;
import company.a.charlee.repository.youtube.YoutubeCaptionRepository;
import company.a.charlee.repository.youtube.YoutubeChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YoutubeChannelService {

    private final YoutubeChannelRepository repository;

    public YoutubeChannelService(YoutubeChannelRepository repository) {
        this.repository = repository;
    }

    public YoutubeChannel save(YoutubeChannel channel) {
        return repository.save(channel);
    }

    public List<YoutubeChannel> findAll() {
        return repository.findAll();
    }

    public Optional<YoutubeChannel> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    YoutubeChannel findByChannelId(String id) {
        return repository.findByYoutubeChannelId(id);
    }

    public Optional<YoutubeChannel> findLatestByChannelId(String youtubeChannelId) {
        return repository.findLatestByChannelId(youtubeChannelId);
    }
}