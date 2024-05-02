package company.a.charlee.services.telegram;

import company.a.charlee.entity.TimeFrame;
import company.a.charlee.entity.dto.*;
import company.a.charlee.repository.telegram.TelegramChannelRepository;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    }

    public List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForChannelPostsByDates(
            String telegramChannelTitle,
            TimeFrame timeFrame,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getAverageSentimentValuesForChannelPostsByDates(
                telegramChannelTitle,
                timeFrame.name().toLowerCase(),
                timeFrame.getSecondsFromTimeFrame(),
                startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)

        );
        return results.stream()
                .map(result -> new AveragePostsSentimentValueDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    public List<TotalPostsViewsDTO> getTotalPostsViewsByDates(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getTotalPostsViewsByDates(telegramChannelTitle);
    }

    public List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDates(
            String telegramChannelTitle,
            Integer topicsLimitByDate,
            TimeFrame timeFrame,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostTopicModelingOccurrencesByDates(
                telegramChannelTitle,
                topicsLimitByDate,
                timeFrame.name().toLowerCase(),
                timeFrame.getSecondsFromTimeFrame(),
                startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        );
        return results.stream()
                .map(result -> new PostTopicModelingOccurrencesDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        (String) result[1],
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    public List<PostTopicModelingOccurrencesDTO> getPostPhraseTopicModelingOccurrencesByDates(
            String telegramChannelTitle,
            Integer topicsLimitByDate,
            TimeFrame timeFrame,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostPhraseTopicModelingOccurrencesByDates(
                telegramChannelTitle,
                topicsLimitByDate,
                timeFrame.name().toLowerCase(),
                timeFrame.getSecondsFromTimeFrame(),
                startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        );
        return results.stream()
                .map(result -> new PostTopicModelingOccurrencesDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        (String) result[1],
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    public List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDates(
            String telegramChannelTitle,
            TimeFrame timeFrame,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getCommentsEngagementForPostsByDates(
                telegramChannelTitle,
                timeFrame.name().toLowerCase(),
                timeFrame.getSecondsFromTimeFrame(),
                startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        );
        return results.stream()
                .map(result -> new CommentsEngagementForPostsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            String telegramChannelTitle,
            TimeFrame timeFrame,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getReactionsEngagementForPostsByDates(
                telegramChannelTitle,
                timeFrame.name().toLowerCase(),
                timeFrame.getSecondsFromTimeFrame(),
                startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        );
        return results.stream()
                .map(result -> new ReactionsEngagementForPostsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
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
