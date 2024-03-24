package company.a.charlee.services.sentimentAnalysis;

import company.a.charlee.entity.TelegramPost;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.LanguageProcessingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentimentAnalyzer {

    private final Map<String, Integer> uaSentimentBase;

    private final Map<String, Integer> enSentimentBase;

    public void analysePost(TelegramPost post, List<String> tokens, DetectedLanguage lang) {
        if (tokens.isEmpty())
            throw new RuntimeException("Error: cannot tokenize telegram post text");

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
            default:
                throw new UnsupportedOperationException("Sentiment analysis for this language is currently not supported");
        }

        Map<String, Integer> index = new TreeMap<>();

        List<String> modifiedTokens;

        if (transformFunc != null)
            modifiedTokens = tokens.stream().map(transformFunc).collect(Collectors.toList());
        else
            modifiedTokens = new ArrayList<>(tokens);

        Collections.sort(modifiedTokens);

        buildFullTextIndex(index, modifiedTokens);

        int sentimentVal;
        int accumulator = 0;
        int absentWordsCount = 0;

        for (Map.Entry<String, Integer> entry : index.entrySet()) {
            sentimentVal = searchMap.getOrDefault(entry.getKey(), 0);
            if (sentimentVal == 0)
                absentWordsCount++;
            accumulator += sentimentVal * entry.getValue();
        }

        double sentimentValue = (modifiedTokens.size() == absentWordsCount) ? 0.0 :
                1.0 * accumulator / (modifiedTokens.size() - absentWordsCount);
        post.setSentimentValue(sentimentValue);
    }


    private void buildFullTextIndex(Map<String, Integer> indexToFill, List<String> sortedTokens) {
        String prevToken = null;
        int tokenCnt = 1;
        for (String token : sortedTokens) {
            if(token.equals(prevToken))
                tokenCnt++;
            else if (prevToken != null) {
                indexToFill.put(prevToken, tokenCnt);
                tokenCnt = 1;
            }
            prevToken = token;
        }
        indexToFill.put(prevToken, tokenCnt);
    }

}