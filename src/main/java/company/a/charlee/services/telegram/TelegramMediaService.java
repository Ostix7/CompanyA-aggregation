package company.a.charlee.services.telegram;

import company.a.charlee.entity.telegram.TelegramMedia;
import company.a.charlee.repository.telegram.TelegramMediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramMediaService {

    private final TelegramMediaRepository repository;

    public TelegramMediaService(TelegramMediaRepository repository) {
        this.repository = repository;
    }

    public TelegramMedia save(TelegramMedia media) {
        return repository.save(media);
    }

    public List<TelegramMedia> findAll() {
        return repository.findAll();
    }

    public Optional<TelegramMedia> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}