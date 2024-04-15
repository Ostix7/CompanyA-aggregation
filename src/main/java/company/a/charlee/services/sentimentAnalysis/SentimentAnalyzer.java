package company.a.charlee.services.sentimentAnalysis;

import company.a.charlee.entity.generic.SentimentValuedEntity;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.LanguageProcessingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SentimentAnalyzer {

    private final Map<String, Integer> uaSentimentBase;

    private final Map<String, Integer> enSentimentBase;
    
    private final Map<String, Integer> ruSentimentBase;

    public void analyseEntity(SentimentValuedEntity entity, List<String> tokens, DetectedLanguage lang) {
        if (tokens.isEmpty())
            throw new RuntimeException("Error: cannot tokenize the text of the post");

        Function<String, String> transformFunc = null;
        Map<String, Integer> searchMap;

        switch (lang) {
            case UKRAINIAN:
                transformFunc = LanguageProcessingUtils::trimUAEnding;
                searchMap = uaSentimentBase;
                break;
            case ENGLISH:
                searchMap = enSentimentBase;
                break;
            case RUSSIAN:
                transformFunc = LanguageProcessingUtils::trimRUEnding;
                searchMap = ruSentimentBase;
                break;
            default:
                throw new UnsupportedOperationException("Sentiment analysis for this language is currently not supported");
        }

        int sentimentVal;
        int accumulator = 0;
        int absentWordsCount = 0;

        for (String token : tokens) {
            if (transformFunc != null)
                token = transformFunc.apply(token);
            sentimentVal = searchMap.getOrDefault(token, 0);
            if (sentimentVal == 0)
                absentWordsCount++;
            accumulator += sentimentVal;
        }

        double sentimentValue = (tokens.size() == absentWordsCount) ? 0.0 :
                1.0 * accumulator / (tokens.size() - absentWordsCount);
        entity.setSentimentValue(sentimentValue);
    }

}