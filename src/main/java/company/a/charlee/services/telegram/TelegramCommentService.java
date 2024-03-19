package company.a.charlee.services.telegram;

import company.a.charlee.entity.TelegramComment;
import company.a.charlee.repository.TelegramCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramCommentService {

    private final TelegramCommentRepository repository;

    public TelegramCommentService(TelegramCommentRepository repository) {
        this.repository = repository;
    }

    public TelegramComment save(TelegramComment comment) {
        return repository.save(comment);
    }

    public List<TelegramComment> findAll() {
        return repository.findAll();
    }

    public Optional<TelegramComment> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}