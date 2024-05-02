package company.a.charlee.controllers;

import company.a.charlee.entity.dto.*;
import company.a.charlee.services.youtube.YoutubeService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/youtube/statistics")
public class YoutubeController {

    @Autowired
    private YoutubeService youtubeService;

    @GetMapping("/videos/mostLiked")
    public List<VideoInfoDTO> getMostLikedVideosInDateRange(
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String channelName,
            @ApiParam(value = "Start date and time as 'yyyy-MM-dd'", example = "2024-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @ApiParam(value = "End date and time as 'yyyy-MM-dd'", example = "2024-01-31", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(defaultValue = "10") int limit) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // Set hour to midnight to cover the whole day
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTimestamp = cal.getTimeInMillis();

        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 23); // Set hour to last of day
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endTimestamp = cal.getTimeInMillis();

        return youtubeService.findMostLikedVideosInDateRange(channelId, channelName, startTimestamp, endTimestamp, limit);
    }

    @GetMapping("/channels/names")
    public List<YoutubeChannelDTO> getAllYouTubeChannelNames() {
        return youtubeService.listAllYouTubeChannelNames();
    }

    @GetMapping("/active-commenters")
    public List<ActiveCommenterDTO> getActiveCommenters(
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String channelName,
            @ApiParam(value = "Start date and time as 'yyyy-MM-dd'", example = "2024-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @ApiParam(value = "End date and time as 'yyyy-MM-dd'", example = "2024-01-31", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(defaultValue = "10") int limit) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // Set hour to midnight to cover the whole day
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTimestamp = cal.getTimeInMillis() * 1000; // to microseconds

        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 23); // Set hour to last of day
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endTimestamp = cal.getTimeInMillis() * 1000; // to microseconds

        return youtubeService.findActiveCommenters(channelId, channelName, startTimestamp, endTimestamp, limit);
    }

    @GetMapping("/average-sentiment")
    public YoutubeAverageSentimentDTO getAverageSentiment(
            @ApiParam(value = "Start date and time as 'yyyy/MM/dd hh:mm:ss a' (12-hour format with AM/PM, UTC expected)", example = "2024/01/01 12:00:00 AM", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss a") Date startDateTime,
            @ApiParam(value = "End date and time as 'yyyy/MM/dd hh:mm:ss a' (12-hour format with AM/PM, UTC expected)", example = "2024/04/25 11:59:00 PM", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss a") Date endDateTime) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(startDateTime);
        long startTimestamp = cal.getTimeInMillis();

        cal.setTime(endDateTime);
        long endTimestamp = cal.getTimeInMillis();

        return youtubeService.calculateAverageSentiment(startTimestamp, endTimestamp);
    }

    @GetMapping("/topics/mostPopular")
    public List<TopicInfoDTO> getMostPopularTopicsInDateRange(
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String channelName,
            @ApiParam(value = "Start date and time as 'yyyy-MM-dd'", example = "2024-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @ApiParam(value = "End date and time as 'yyyy-MM-dd'", example = "2024-01-31", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @ApiParam(allowableValues = "en, uk, ru", example = "en")
            @RequestParam (required = false) String language,
            @RequestParam(defaultValue = "10") int limit) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // Set hour to midnight to cover the whole day
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTimestamp = cal.getTimeInMillis();

        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 23); // Set hour to last of day
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endTimestamp = cal.getTimeInMillis();

        return youtubeService.findMostPopularTopicsInDateRange(channelId, channelName, language, startTimestamp, endTimestamp, limit);
    }

    @GetMapping("/topic-phrases/mostPopular")
    public List<TopicPhraseInfoDTO> getMostPopularTopicPhrasesInDateRange(
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String channelName,
            @ApiParam(value = "Start date and time as 'yyyy-MM-dd'", example = "2024-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @ApiParam(value = "End date and time as 'yyyy-MM-dd'", example = "2024-01-31", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @ApiParam(allowableValues = "en, uk, ru", example = "en")
            @RequestParam (required = false) String language,
            @RequestParam(defaultValue = "10") int limit) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // Set hour to midnight to cover the whole day
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTimestamp = cal.getTimeInMillis();

        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 23); // Set hour to last of day
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endTimestamp = cal.getTimeInMillis();

        return youtubeService.findMostPopularTopicPhrasesInDateRange(channelId, channelName, language, startTimestamp, endTimestamp, limit);
    }

    @GetMapping("/videos/{video_id}/comments/mostLiked")
    public List<YoutubeCommentDTO> getMostLikedCommentsForVideo(
            @ApiParam(value = "Youtube Video Id", example = "RUHDbSIHmDQ")
            @PathVariable("video_id") String videoId,
            @RequestParam(defaultValue = "10") int limit) {

        return youtubeService.findMostLikedCommentsForVideo(videoId, limit);
    }

    @GetMapping("/comments/mostLiked")
    public List<YoutubePopularCommentDTO> getMostLikedCommentsInDateRange(
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String channelName,
            @ApiParam(value = "Start date and time as 'yyyy-MM-dd'", example = "2024-01-01", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @ApiParam(value = "End date and time as 'yyyy-MM-dd'", example = "2024-01-31", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(defaultValue = "10") int limit) {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // Set hour to midnight to cover the whole day
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTimestamp = cal.getTimeInMillis()  * 1000; // to microseconds;

        cal.setTime(endDate);
        cal.set(Calendar.HOUR_OF_DAY, 23); // Set hour to last of day
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endTimestamp = cal.getTimeInMillis()  * 1000; // to microseconds;

        return youtubeService.findMostLikedCommentsInDateRange(channelId, channelName, startTimestamp, endTimestamp, limit);
    }
}
