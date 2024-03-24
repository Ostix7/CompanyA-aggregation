package company.a.charlee.config;

import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguagePOSFilter;
import company.a.charlee.utils.MultiLanguageTokenizer;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class TopicModelingConfig {

    @Bean
    public TokenizerME tokenizer() throws IOException, URISyntaxException {
        String tokenModelPath = Paths.get(ClassLoader.getSystemResource("models/en-token.bin").toURI()).toString();
        InputStream tokenModelIn = Files.newInputStream(Paths.get(tokenModelPath));
        TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
        return new TokenizerME(tokenModel);
    }

    @Bean
    public POSTaggerME posTagger() throws IOException, URISyntaxException {
        String posModelPath = Paths.get(ClassLoader.getSystemResource("models/en-pos.bin").toURI()).toString();
        InputStream posModelIn = Files.newInputStream(Paths.get(posModelPath));
        POSModel posModel = new POSModel(posModelIn);
        return new POSTaggerME(posModel);
    }

    @Bean
    public LanguageDetectorME languageDetector() throws IOException, URISyntaxException {
        String ldPath = Paths.get(ClassLoader.getSystemResource("models/langdetect.bin").toURI()).toString();
        InputStream ldModelIn = Files.newInputStream(Paths.get(ldPath));
        LanguageDetectorModel model = new LanguageDetectorModel(ldModelIn);
        return new LanguageDetectorME(model);
    }

    @Bean
    public MultiLanguageDetector multiLanguageDetector(LanguageDetectorME apacheLanguageDetector) {
        return new MultiLanguageDetector(apacheLanguageDetector);
    }

    @Bean
    public MultiLanguageTokenizer enTokenizer(TokenizerME enTokenizer) {
        return new MultiLanguageTokenizer(enTokenizer);
    }

    @Bean
    public MultiLanguagePOSFilter posFilter(POSTaggerME enPOSFilter) {
        return new MultiLanguagePOSFilter(enPOSFilter);
    }

}
