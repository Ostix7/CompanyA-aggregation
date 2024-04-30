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
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class TopicModelingConfig {

    @Bean
    public TokenizerME tokenizer() throws IOException {
        InputStream tokenModelIn = new ClassPathResource("models/en-token.bin").getInputStream();
        TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
        return new TokenizerME(tokenModel);
    }

    @Bean
    public POSTaggerME posTagger() throws IOException {
        InputStream posModelIn = new ClassPathResource("models/en-pos.bin").getInputStream();
        POSModel posModel = new POSModel(posModelIn);
        return new POSTaggerME(posModel);
    }

    @Bean
    public LanguageDetectorME languageDetector() throws IOException {
        InputStream ldModelIn = new ClassPathResource("models/langdetect.bin").getInputStream();
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
