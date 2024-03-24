package company.a.charlee.services.telegram;

import company.a.charlee.entity.telegram.TelegramChannel;
import company.a.charlee.repository.telegram.TelegramChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramChannelService {

    private final TelegramChannelRepository repository;

    public TelegramChannelService(TelegramChannelRepository repository) {
        this.repository = repository;
    }

    public TelegramChannel save(TelegramChannel channel) {
        return repository.save(channel);
    }

    public List<TelegramChannel> findAll() {
        return repository.findAll();
    }

    public Optional<TelegramChannel> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}