package company.a.charlee.utils;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.tokenize.TokenizerME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiLanguageTokenizer {
    private final TokenizerME enTokenizer;


    public MultiLanguageTokenizer(TokenizerME enTokenizer) {
        this.enTokenizer = enTokenizer;
    }

    public List<String> tokenize(String text, DetectedLanguage language) {
        List<String> tokens;
        switch (language) {
            case ENGLISH:
                tokens = Arrays.asList(enTokenizer.tokenize(text));
                break;
            case UKRAINIAN:
            case RUSSIAN:
                tokens = tokenizeUkrOrRus(text);
                break;
            default:
                tokens = new ArrayList<>();
                break;
        }
        return tokens;
    }

    public List<String> tokenizeUkrOrRus(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        String pattern = "(\\p{L}+([’']\\p{L}+)*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);

        List<String> tokens = new ArrayList<>();
        while (m.find()) {
            tokens.add(m.group());
        }

        return tokens;
    }

}
