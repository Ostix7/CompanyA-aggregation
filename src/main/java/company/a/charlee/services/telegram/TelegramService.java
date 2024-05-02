package company.a.charlee.services.telegram;

import company.a.charlee.entity.Segment;
import company.a.charlee.entity.TimeUnit;
import company.a.charlee.entity.dto.*;
import company.a.charlee.repository.telegram.TelegramChannelRepository;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
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
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getAverageSentimentValuesForChannelPostsByDates(
                telegramChannelTitle,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new AveragePostsSentimentValueDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    public List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForSegmentPostsByDates(
            Segment segment,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getAverageSentimentValuesForSegmentPostsByDates(
                segment.getSegmentAsString(),
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new AveragePostsSentimentValueDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).doubleValue()))
                .collect(Collectors.toList());
    }

    public List<TotalPostsViewsDTO> getTotalPostsViewsByDates(
            String telegramChannelTitle,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getTotalPostsViewsByDates(
                telegramChannelTitle,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new TotalPostsViewsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<TotalPostsViewsDTO> getTotalPostsViewsByDatesForSegment(
            Segment segment,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getTotalPostsViewsByDatesForSegment(
                segment.getSegmentAsString(),
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new TotalPostsViewsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDates(
            String telegramChannelTitle,
            Integer topicsLimitByDate,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostTopicModelingOccurrencesByDates(
                telegramChannelTitle,
                topicsLimitByDate,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new PostTopicModelingOccurrencesDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        (String) result[1],
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    public List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDatesForSegment(
            Segment segment,
            Integer topicsLimitByDate,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostTopicModelingOccurrencesByDatesForSegment(
                segment.getSegmentAsString(),
                topicsLimitByDate,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
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
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostPhraseTopicModelingOccurrencesByDates(
                telegramChannelTitle,
                topicsLimitByDate,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new PostTopicModelingOccurrencesDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        (String) result[1],
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());
    }

    public List<PostTopicModelingOccurrencesDTO> getPostPhraseTopicModelingOccurrencesByDatesForSegment(
            Segment segment,
            Integer topicsLimitByDate,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getPostPhraseTopicModelingOccurrencesByDatesForSegment(
                segment.getSegmentAsString(),
                topicsLimitByDate,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
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
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getCommentsEngagementForPostsByDates(
                telegramChannelTitle,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new CommentsEngagementForPostsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDatesForSegment(
            Segment segment,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getCommentsEngagementForPostsByDatesForSegment(
                segment.getSegmentAsString(),
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new CommentsEngagementForPostsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            String telegramChannelTitle,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getReactionsEngagementForPostsByDates(
                telegramChannelTitle,
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
        return results.stream()
                .map(result -> new ReactionsEngagementForPostsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDatesForSegment(
            Segment segment,
            TimeUnit timeUnit,
            int step,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Object[]> results = telegramPostRepository.getReactionsEngagementForPostsByDatesForSegment(
                segment.getSegmentAsString(),
                timeUnit.name().toLowerCase(),
                timeUnit.getSecondsFromTimeFrame() * (long) step,
                startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond(),
                endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond()
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
