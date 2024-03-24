package company.a.charlee.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public List<String> tokenize(String text) {
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
