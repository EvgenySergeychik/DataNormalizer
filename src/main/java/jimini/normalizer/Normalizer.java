package jimini.normalizer;

import jimini.adapter.ProviderAdapter;
import jimini.exception.ValidationException;
import jimini.model.Metadata;
import jimini.model.ProviderAssessment;
import jimini.model.Score;
import jimini.model.UnifiedAssessment;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;


public class Normalizer {

    public UnifiedAssessment normalize(
            ProviderAssessment pa,
            ProviderAdapter adapter
    ) throws ValidationException {

        UnifiedAssessment ua = new UnifiedAssessment();
        ua.patientId = pa.patientId;
        ua.assessmentType = pa.assessmentType;
        ua.assessmentDate = pa.assessmentDate.atStartOfDay(ZoneOffset.UTC).toInstant();

        ua.scores = pa.rawScores.entrySet().stream()
                .map(e -> normalizeScore(e, pa.maxScore))
                .toList();

        ua.metadata = new Metadata();
        ua.metadata.sourceProvider = adapter.providerName();
        ua.metadata.sourceFormat = adapter.sourceFormat();
        ua.metadata.ingestedAt = Instant.now();
        ua.metadata.version = "1.0";

        validateUnified(ua);
        return ua;
    }

    private Score normalizeScore(Map.Entry<String, Integer> e, int max) {
        Score s = new Score();
        s.dimension = e.getKey();
        s.value = (int) Math.round((e.getValue() * 100.0) / max);
        s.scale = "0-100";
        return s;
    }

    private void validateUnified(UnifiedAssessment ua) {
        for (Score s : ua.scores) {
            if (s.value < 0 || s.value > 100) {
                throw new ValidationException("Invalid normalized score");
            }
        }
    }
}