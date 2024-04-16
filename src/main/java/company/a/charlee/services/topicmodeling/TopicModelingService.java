package company.a.charlee.services.topicmodeling;

import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguagePOSFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicModelingService {

    private final MultiLanguagePOSFilter posFilter;

    public List<String> findTopics(List<String> tokens, DetectedLanguage lang) {
        if (tokens.isEmpty())
            throw new RuntimeException("Error: cannot tokenize the text of the post");
        List<String> filteredTokens = posFilter.filterSignificantPOS(tokens, lang);
        Map<String, Integer> significantWordOccurrences = new HashMap<>();
        for (String filteredToken : filteredTokens) {
            significantWordOccurrences.put(filteredToken, significantWordOccurrences.getOrDefault(filteredToken, 0) + 1);
        }
        return significantWordOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
