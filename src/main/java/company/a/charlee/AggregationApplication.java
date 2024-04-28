package company.a.charlee;

import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguagePOSFilter;
import company.a.charlee.utils.MultiLanguageTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class AggregationApplication implements CommandLineRunner {

    @Autowired
    private MultiLanguageDetector languageDetector;

    @Autowired
    private MultiLanguageTokenizer tokenizer;

    @Autowired
    private MultiLanguagePOSFilter posFilter;

    public static void main(String[] args) {
        SpringApplication.run(AggregationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String postText = "але звісно державна комунікація це шось неймовірне просто\n" +
                "реально в підручники з історії війде\n" +
                "Всі вимагають демобілізації\n" +
                "Багато хто розуміє шо гайки закручувать будуть, мобілізація розкручуватись буде, бусики будуть масштабуватись, але демобілізації масової не буде до припинення вогню, бо якщо зараз величезні проблеми з ОС, то через рік вони будуть гірше ніж зараз, через два - гірше ніж через рік, а через 5 - гірше ніж через 2, і якщо зараз нікого списать не можуть, то далі буде гірше, і тим більше 400к обстріляних людей разово звільнить не зможуть (ну або зможуть і на цьому все закінчиться в принципі, але не зовсім так як всі очікують)\n" +
                "Шо робить шановна влада?\n" +
                "Каже шо питання демобілізації це ключове питання\n" +
                "Каже шо треба відновити справедливість\n" +
                "Каже шо це обовʼязок \n" +
                "10 разів ту демобілізацію обіцяють\n" +
                "вносять 100500 правок шо вона буде\n" +
                "Додають в закон\n" +
                "—\n" +
                "за пару днів до голосування мовчки забирають це з закону і поширують листа від сирського до він просить це зробити\n" +
                "без жодної комунікації\n" +
                "Просто ідеально\n" +
                "Якщо це бляха не диверсія то я просто не знаю шо це\n" +
                "І шо особливо удівітєльно, це ж бляха 95 квартал\n" +
                "це ж єдине в чому у них є взагалі якась експертиза, в тому як навалювать в уха людям\n" +
                "І отаке\n" +
                "просто поразительно";
        DetectedLanguage language = languageDetector.detectLanguage(postText, "");
        List<String> tokens = tokenizer.tokenize(postText, language);
        List<String> filteredTokens = posFilter.filterSignificantPOS(tokens, language);
        Map<String, Integer> significantWordOccurrences = new HashMap<>();
        for (String filteredToken : filteredTokens) {
            significantWordOccurrences.put(filteredToken, significantWordOccurrences.getOrDefault(filteredToken, 0) + 1);
        }
        List<String> topFiveWords = significantWordOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("------------------------------" + Arrays.toString(topFiveWords.toArray()));
    }

}
