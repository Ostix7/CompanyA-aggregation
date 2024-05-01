package company.a.charlee.utils;

import opennlp.tools.tokenize.TokenizerME;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiLanguageTokenizer {
    private final TokenizerME enTokenizer;


    public MultiLanguageTokenizer(TokenizerME enTokenizer) {
        this.enTokenizer = enTokenizer;
    }

    public String[] tokenize(String text, DetectedLanguage language) {
        String[] tokens;
        switch (language) {
            case ENGLISH:
                tokens = enTokenizer.tokenize(text);
                break;
            case UKRAINIAN:
            case RUSSIAN:
                tokens = tokenizeUkrOrRus(text);
                break;
            default:
                tokens = new String[0];
                break;
        }
        return tokens;
    }

    public String[] tokenizeUkrOrRus(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }

        String pattern = "(\\p{L}+([’']\\p{L}+)*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);

        List<String> tokens = new ArrayList<>();
        while (m.find()) {
            tokens.add(m.group());
        }

        return tokens.toArray(new String[0]);
    }

}
