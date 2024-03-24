package company.a.charlee.services.youtube;

import company.a.charlee.entity.telegram.TelegramComment;
import company.a.charlee.entity.youtube.YoutubeComment;
import company.a.charlee.repository.telegram.TelegramCommentRepository;
import company.a.charlee.repository.youtube.YoutubeCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YoutubeCommentService {

    private final YoutubeCommentRepository repository;

    public YoutubeCommentService(YoutubeCommentRepository repository) {
        this.repository = repository;
    }

    public YoutubeComment save(YoutubeComment comment) {
        return repository.save(comment);
    }

    public List<YoutubeComment> findAll() {
        return repository.findAll();
    }

    public Optional<YoutubeComment> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}