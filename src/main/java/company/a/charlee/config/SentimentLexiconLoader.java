package company.a.charlee.config;

import company.a.charlee.utils.LanguageProcessingUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.function.Function;

@Configuration
public class SentimentLexiconLoader {

    @Bean
    public Map<String, Integer> uaSentimentBase() throws IOException {
        Map<String, Integer> resMap = new TreeMap<>();
        parseFile(resMap, "sentiment_ua.csv", ";", LanguageProcessingUtils::trimUAEnding,2);
        parseFile(resMap, "tone-dict-uk.tsv", "\t", LanguageProcessingUtils::trimUAEnding,1);
        return resMap;
    }

    @Bean
    public Map<String, Integer> enSentimentBase() throws IOException {
        Map<String, Integer> resMap = new TreeMap<>();
        parseFile(resMap, "AFINN-111.txt", "\t", null, 1);
        parseFile(resMap, "positive-words-1.txt", "\t", null, 1);
        parseFile(resMap, "negative-words-1.txt", "\t", null, 1);
        return resMap;
    }

    @Bean
    public Map<String, Integer> ruSentimentBase() throws IOException {
        Map<String, Integer> resMap = new TreeMap<>();
        parseFile(resMap, "rusentilex_2017-1.txt", ";", LanguageProcessingUtils::trimRUEnding, 1);
        return resMap;
    }

    private static void parseFile(Map<String, Integer> mapToFill,
                                  String fileName,
                                  String delim,
                                  Function<String, String> transformFunc,
                                  int startFromLine) throws IOException {
        File file = ResourceUtils.getFile("classpath:sentiment_db_files/" + fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String currentLine;
            StringTokenizer tk;
            for (int i = 1; i < startFromLine; i++)
                br.readLine();
            while ((currentLine = br.readLine()) != null) {
                tk = new StringTokenizer(currentLine, delim);
                String key = tk.nextToken();
                Integer value = Integer.parseInt(tk.nextToken());
                if (transformFunc != null)
                    key = transformFunc.apply(key);
                mapToFill.put(key, value);
            }
        }
    }
}
