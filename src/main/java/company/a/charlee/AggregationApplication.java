package company.a.charlee;

import company.a.charlee.reader.ParquetReaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregationApplication.class, args);
    }

}
