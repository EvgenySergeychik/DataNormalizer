package jimini.svc;

import jimini.adapter.ProviderAdapter;
import jimini.exception.ValidationException;
import jimini.model.ProviderAssessment;
import jimini.model.Result;
import jimini.model.UnifiedAssessment;
import jimini.normalizer.Normalizer;
import org.springframework.stereotype.Service;

@Service
public class IngestionService {

    private final NormalizerService normalizer;

    public IngestionService(NormalizerService normalizer) {
        this.normalizer = normalizer;
    }

    public Result ingest(String input, ProviderAdapter adapter) {
        try {
            ProviderAssessment pa = adapter.parse(input);
            UnifiedAssessment ua = normalizer.normalize(pa, adapter);
            return Result.success(ua);
        } catch (ValidationException e) {
            return Result.failure(e.getMessage());
        }
    }
}