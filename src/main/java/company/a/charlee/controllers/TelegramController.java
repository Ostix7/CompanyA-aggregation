package company.a.charlee.controllers;

import company.a.charlee.entity.TimeUnit;
import company.a.charlee.entity.dto.*;
import company.a.charlee.services.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/telegram/statistics")
public class TelegramController {

    @Autowired
    private TelegramService telegramService;

    @GetMapping("/all-channels")
    public List<TelegramChannelDTO> getAllTelegramChannels() {
        return telegramService.getAllTelegramChannels();
    }

    @GetMapping("/avg-sentiment-values-by-dates/{telegram_channel_title}")
    public List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForChannelPostsByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle,
            @RequestParam(value = "time_unit", required = false) String timeUnit,
            @RequestParam(value = "step") int step,
            @RequestParam(value = "start_date") String startDate,
            @RequestParam(value = "end_date") String endDate
    ) {
        TimeUnit time = timeUnit != null ? TimeUnit.valueOf(timeUnit.toUpperCase()) : TimeUnit.DAY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        return telegramService.getAverageSentimentValuesForChannelPostsByDates(
                telegramChannelTitle, time, step, start, end
        );
    }

    @GetMapping("/total-posts-views-by-dates/{telegram_channel_title}")
    public List<TotalPostsViewsDTO> getTotalPostsViewsByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle
    ) {
        return telegramService.getTotalPostsViewsByDates(telegramChannelTitle);
    }

    @GetMapping("/post-topic-occurrences-by-dates/{telegram_channel_title}/{topics_limit_by_date}")
    public List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle,
            @PathVariable("topics_limit_by_date") Integer topicsLimitByDate,
            @RequestParam(value = "time_unit", required = false) String timeUnit,
            @RequestParam(value = "step") int step,
            @RequestParam(value = "start_date") String startDate,
            @RequestParam(value = "end_date") String endDate
    ) {
        TimeUnit time = timeUnit != null ? TimeUnit.valueOf(timeUnit.toUpperCase()) : TimeUnit.DAY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        return telegramService.getPostTopicModelingOccurrencesByDates(
                telegramChannelTitle, topicsLimitByDate, time, step, start, end
        );
    }

    @GetMapping("/post-phrase-topic-occurrences-by-dates/{telegram_channel_title}/{topics_limit_by_date}")
    public List<PostTopicModelingOccurrencesDTO> getPostPhraseTopicModelingOccurrencesByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle,
            @PathVariable("topics_limit_by_date") Integer topicsLimitByDate,
            @RequestParam(value = "time_unit", required = false) String timeUnit,
            @RequestParam(value = "step") int step,
            @RequestParam(value = "start_date") String startDate,
            @RequestParam(value = "end_date") String endDate
    ) {
        TimeUnit time = timeUnit != null ? TimeUnit.valueOf(timeUnit.toUpperCase()) : TimeUnit.DAY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        return telegramService.getPostPhraseTopicModelingOccurrencesByDates(
                telegramChannelTitle, topicsLimitByDate, time, step, start, end
        );
    }

    @GetMapping("/post-comments-engagement-by-dates/{telegram_channel_title}")
    public List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle,
            @RequestParam(value = "time_unit", required = false) String timeUnit,
            @RequestParam(value = "step") int step,
            @RequestParam(value = "start_date") String startDate,
            @RequestParam(value = "end_date") String endDate
    ) {
        TimeUnit time = timeUnit != null ? TimeUnit.valueOf(timeUnit.toUpperCase()) : TimeUnit.DAY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        return telegramService.getCommentsEngagementForPostsByDates(
                telegramChannelTitle, time, step, start, end
        );
    }

    @GetMapping("/post-reactions-engagement-by-dates/{telegram_channel_title}")
    public List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle,
            @RequestParam(value = "time_unit", required = false) String timeUnit,
            @RequestParam(value = "step") int step,
            @RequestParam(value = "start_date") String startDate,
            @RequestParam(value = "end_date") String endDate
    ) {
        TimeUnit time = timeUnit != null ? TimeUnit.valueOf(timeUnit.toUpperCase()) : TimeUnit.DAY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);
        return telegramService.getReactionsEngagementForPostsByDates(
                telegramChannelTitle, time, step, start, end
        );
    }

    @GetMapping("/all-channel-posts-quantity")
    public List<ChannelPostsQuantityDTO> getAllChannelPostsQuantity() {
        return telegramService.getAllChannelPostsQuantity();
    }

    @GetMapping("/channel-posts-quantity/{telegram_channel_title}")
    public ChannelPostsQuantityDTO getChannelPostsQuantity(
            @PathVariable("telegram_channel_title") String telegramChannelTitle
    ) {
        return telegramService.getChannelPostsQuantity(telegramChannelTitle);
    }

    @GetMapping("/most-viewed-channel")
    public ChannelViewsCountDTO getMostViewedChannel() {
        return telegramService.getMostViewedChannel();
    }

    @GetMapping("/channels-views-count")
    public List<ChannelViewsCountDTO> getChannelsViewsCount() {
        return telegramService.getChannelsViewsCount();
    }

}
