package company.a.charlee.services.telegram;

import company.a.charlee.entity.telegram.TelegramPost;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramPostService {

    private final TelegramPostRepository repository;

    @Autowired
    public TelegramPostService(TelegramPostRepository repository) {
        this.repository = repository;
    }

    public TelegramPost save(TelegramPost post) {
        return repository.save(post);
    }

    public List<TelegramPost> findAll() {
        return repository.findAll();
    }

    public Optional<TelegramPost> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}