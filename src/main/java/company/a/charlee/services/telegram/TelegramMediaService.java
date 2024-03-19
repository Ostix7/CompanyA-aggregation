package company.a.charlee.services.telegram;

import company.a.charlee.entity.TelegramMedia;
import company.a.charlee.repository.TelegramMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<TelegramMedia> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}