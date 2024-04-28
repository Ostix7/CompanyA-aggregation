package company.a.charlee.services.telegram;

import company.a.charlee.entity.dto.AveragePostsSentimentValueDTO;
import company.a.charlee.entity.dto.CommentsEngagementForPostsDTO;
import company.a.charlee.entity.dto.PostTopicModelingOccurrencesDTO;
import company.a.charlee.entity.dto.ReactionsEngagementForPostsDTO;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramService {

    @Autowired
    private TelegramPostRepository telegramPostRepository;

    public List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForChannelPostsByDates(
            String telegramChannelTitle
    ) {
        return telegramPostRepository.getAverageSentimentValuesForChannelPostsByDates(telegramChannelTitle);
    }

    public List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDates(
            String telegramChannelTitle,
            Integer topicsLimitByDate
    ) {
        return telegramPostRepository.getPostTopicModelingOccurrencesByDates(telegramChannelTitle, topicsLimitByDate);
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

}
