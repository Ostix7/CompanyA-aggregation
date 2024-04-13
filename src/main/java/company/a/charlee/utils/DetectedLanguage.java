package company.a.charlee.utils;

public enum DetectedLanguage {
    ENGLISH, UKRAINIAN, RUSSIAN, UNSUPPORTED;

    public static DetectedLanguage getFromString(String lang) {
        if (lang.equals("english") || lang.equals("en") || lang.equals("eng"))
            return ENGLISH;
        if (lang.equals("ukrainian") || lang.equals("ua") || lang.equals("ukr"))
            return UKRAINIAN;
        if (lang.equals("russian") || lang.equals("ru") || lang.equals("rus"))
            return RUSSIAN;
        return UNSUPPORTED;
    }

}
