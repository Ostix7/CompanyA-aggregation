package company.a.charlee.services.telegram;

import company.a.charlee.entity.telegram.TelegramReaction;
import company.a.charlee.repository.telegram.TelegramReactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramReactionService {

    private final TelegramReactionRepository repository;

    public TelegramReactionService(TelegramReactionRepository repository) {
        this.repository = repository;
    }

    public TelegramReaction save(TelegramReaction reaction) {
        return repository.save(reaction);
    }

    public List<TelegramReaction> findAll() {
        return repository.findAll();
    }

    public Optional<TelegramReaction> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}