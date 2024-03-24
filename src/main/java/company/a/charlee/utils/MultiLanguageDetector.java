package company.a.charlee.utils;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;

public class MultiLanguageDetector {

    private final LanguageDetectorME languageDetector;

    public MultiLanguageDetector(LanguageDetectorME languageDetector) {
        this.languageDetector = languageDetector;
    }

    public DetectedLanguage detectLanguage(String text) {
        Language lang = languageDetector.predictLanguage(text);
        DetectedLanguage language;
        switch (lang.getLang()) {
            case "eng":
                language = DetectedLanguage.ENGLISH;
                break;
            case "ukr":
                language = DetectedLanguage.UKRAINIAN;
                break;
            case "rus":
                language = DetectedLanguage.RUSSIAN;
                break;
            default:
                language = DetectedLanguage.UNSUPPORTED;
                break;
        }
        return language;
    }

}
