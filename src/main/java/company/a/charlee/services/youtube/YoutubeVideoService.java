package company.a.charlee.services.youtube;

import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.repository.youtube.YoutubeVideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YoutubeVideoService {

    private final YoutubeVideoRepository repository;

    public YoutubeVideoService(YoutubeVideoRepository repository) {
        this.repository = repository;
    }

    public YoutubeVideo save(YoutubeVideo comment) {
        return repository.save(comment);
    }

    public List<YoutubeVideo> findAll() {
        return repository.findAll();
    }

    public Optional<YoutubeVideo> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    YoutubeVideo findByYoutubeVideoId(String id) {
        return repository.findByYoutubeVideoId(id);
    }
}