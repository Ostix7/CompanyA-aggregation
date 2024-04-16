package company.a.charlee.utils;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;

public class MultiLanguageDetector {

    private final LanguageDetectorME languageDetector;

    public MultiLanguageDetector(LanguageDetectorME languageDetector) {
        this.languageDetector = languageDetector;
    }

    public DetectedLanguage detectLanguage(String text, String backupLang) {
        String backup = backupLang != null ? backupLang : "";
        Language lang = languageDetector.predictLanguage(text);
        DetectedLanguage language = DetectedLanguage.getFromString(lang.getLang());
        if (language == DetectedLanguage.UNSUPPORTED) {
            language = DetectedLanguage.getFromString(backup);
        }
        return language;
    }

}
