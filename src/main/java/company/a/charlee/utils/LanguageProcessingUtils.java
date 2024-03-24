package company.a.charlee.utils;

public class LanguageProcessingUtils {

    private static final String[] uaEndings = {
            "а", "ня", "я", "у", "о", "ю", "ою", "ею", "и", "еві",
            "ові", "ів", "ев", "ом", "ем", "им", "і", "ий", "ій",
            "єй", "е", "ує", "є", "ього", "ьому", "ьої", "ьий",
            "ість", "исть", "ти", "ити", "іти", "чи", "еш",
            "иш", "ить", "жу", "ете", "есь", "ите", "уть", "ують",
            "ять", "ний", "лий", "учий", "ючий", "тимий",
            "ятимий", "увати", "ювати", "ці", "ться", "ись"
    };

    private static final String uaEndingRegex = generateLangEndingsRegex(uaEndings);

    public static String trimUAEnding(String uaWord) {
        String res = uaWord.replaceAll(uaEndingRegex, "");
        return res.isEmpty() ? uaWord : res;
    }

    private static String generateLangEndingsRegex(String[] endings) {
        StringBuilder regexBuilder = new StringBuilder("(");

        for(int i = 0; i < endings.length - 1; i++)
            regexBuilder.append(endings[i]).append('|');

        regexBuilder.append(endings[endings.length - 1]).append(")$");
        return regexBuilder.toString();
    }

}
