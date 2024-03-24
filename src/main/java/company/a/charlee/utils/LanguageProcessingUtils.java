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

    private static final String[] ruEndings = {
            "ом", "ем", "е", "а", "я", "ы", "и", "у",
            "ю", "ый", "ий", "ой", "тся", "ться", "ть",
            "ая", "яя", "ое", "ее", "ые", "ие", "сть"
    };

    private static final String uaEndingRegex = generateLangEndingsRegex(uaEndings);

    private static final String ruEndingRegex = generateLangEndingsRegex(ruEndings);

    public static String trimUAEnding(String uaWord) {
        String res = uaWord.replaceAll(uaEndingRegex, "");
        return res.isEmpty() ? uaWord : res;
    }

    public static String trimRUEnding(String ruWord) {
        String res = ruWord.replaceAll(ruEndingRegex, "");
        return res.isEmpty() ? ruWord : res;
    }

    private static String generateLangEndingsRegex(String[] endings) {
        StringBuilder regexBuilder = new StringBuilder("(");

        for(int i = 0; i < endings.length - 1; i++)
            regexBuilder.append(endings[i]).append('|');

        regexBuilder.append(endings[endings.length - 1]).append(")$");
        return regexBuilder.toString();
    }

}
