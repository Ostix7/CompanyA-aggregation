package company.a.charlee.services.youtube;

import company.a.charlee.entity.telegram.TelegramComment;
import company.a.charlee.entity.youtube.YoutubeCaption;
import company.a.charlee.repository.telegram.TelegramCommentRepository;
import company.a.charlee.repository.youtube.YoutubeCaptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YoutubeCaptionService {

    private final YoutubeCaptionRepository repository;

    public YoutubeCaptionService(YoutubeCaptionRepository repository) {
        this.repository = repository;
    }

    public YoutubeCaption save(YoutubeCaption caption) {
        return repository.save(caption);
    }

    public List<YoutubeCaption> findAll() {
        return repository.findAll();
    }

    public Optional<YoutubeCaption> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    public YoutubeCaption findByYoutubeVideoId(String id){
        return repository.findByYoutubeVideoId(id);
    }
}