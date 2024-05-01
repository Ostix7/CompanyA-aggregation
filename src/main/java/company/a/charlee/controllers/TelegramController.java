package company.a.charlee.controllers;

import company.a.charlee.entity.dto.*;
import company.a.charlee.services.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable("telegram_channel_title") String telegramChannelTitle
    ) {
        return telegramService.getAverageSentimentValuesForChannelPostsByDates(telegramChannelTitle);
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
            @PathVariable("topics_limit_by_date") Integer topicsLimitByDate
    ) {
        return telegramService.getPostTopicModelingOccurrencesByDates(telegramChannelTitle, topicsLimitByDate);
    }

    @GetMapping("/post-phrase-topic-occurrences-by-dates/{telegram_channel_title}/{topics_limit_by_date}")
    public List<PostTopicModelingOccurrencesDTO> getPostPhraseTopicModelingOccurrencesByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle,
            @PathVariable("topics_limit_by_date") Integer topicsLimitByDate
    ) {
        return telegramService.getPostPhraseTopicModelingOccurrencesByDates(telegramChannelTitle, topicsLimitByDate);
    }

    @GetMapping("/post-comments-engagement-by-dates/{telegram_channel_title}")
    public List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle
    ) {
        return telegramService.getCommentsEngagementForPostsByDates(telegramChannelTitle);
    }

    @GetMapping("/post-reactions-engagement-by-dates/{telegram_channel_title}")
    public List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            @PathVariable("telegram_channel_title") String telegramChannelTitle
    ) {
        return telegramService.getReactionsEngagementForPostsByDates(telegramChannelTitle);
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
