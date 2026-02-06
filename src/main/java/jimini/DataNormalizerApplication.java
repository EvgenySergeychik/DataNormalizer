package jimini;

import jimini.adapter.ProviderAAdapter;
import jimini.adapter.ProviderAdapter;
import jimini.model.Result;
import jimini.svc.IngestionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class DataNormalizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataNormalizerApplication.class, args);
    }

}
