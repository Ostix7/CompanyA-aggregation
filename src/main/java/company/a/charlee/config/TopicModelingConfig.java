package company.a.charlee.config;

import company.a.charlee.utils.POSFilter;
import company.a.charlee.utils.Tokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicModelingConfig {

    @Bean
    public Tokenizer tokenizer() {
        return new Tokenizer();
    }

    @Bean
    public POSFilter posFilter() {
        return new POSFilter();
    }

}
