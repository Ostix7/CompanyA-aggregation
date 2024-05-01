package company.a.charlee.services.telegram;

import company.a.charlee.entity.dto.*;
import company.a.charlee.repository.telegram.TelegramChannelRepository;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelegramService {

    @Autowired
    private TelegramPostRepository telegramPostRepository;

    @Autowired
    private TelegramChannelRepository telegramChannelRepository;

    public List<TelegramChannelDTO> getAllTelegramChannels() {
        return telegramChannelRepository.getAll();
//        return telegramChannelRepository.getAll(limit).stream().map(channel ->
//                new TelegramChannelDTO((String) channel)
//        ).collect(Collectors.toList());
    }

    public List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForChannelPostsByDates(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getAverageSentimentValuesForChannelPostsByDates(telegramChannelTitle);
    }

    public List<TotalPostsViewsDTO> getTotalPostsViewsByDates(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getTotalPostsViewsByDates(telegramChannelTitle);
    }

    public List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDates(
            String telegramChannelTitle,
            Integer topicsLimitByDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostTopicModelingOccurrencesByDates(telegramChannelTitle, topicsLimitByDate);
        return results.stream()
                .map(result -> new PostTopicModelingOccurrencesDTO(
                        ((Date) result[0]).toLocalDate(),
                        (String) result[1],
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    public List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDates(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getCommentsEngagementForPostsByDates(telegramChannelTitle);
    }

    public List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getReactionsEngagementForPostsByDates(telegramChannelTitle);
    }

    public List<ChannelPostsQuantityDTO> getAllChannelPostsQuantity() {
        return telegramPostRepository.getAllChannelPostsQuantity();
    }

    public ChannelPostsQuantityDTO getChannelPostsQuantity(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getChannelPostsQuantity(telegramChannelTitle);
    }

    public ChannelViewsCountDTO getMostViewedChannel() {
        List<ChannelViewsCountDTO> result = telegramChannelRepository.getChannelsViewsCount();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<ChannelViewsCountDTO> getChannelsViewsCount() {
        return telegramChannelRepository.getChannelsViewsCount();
    }

}
