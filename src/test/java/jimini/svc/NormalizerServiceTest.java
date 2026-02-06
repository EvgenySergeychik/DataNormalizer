package jimini.svc;

import jimini.adapter.ProviderAAdapter;
import jimini.model.ProviderAssessment;
import jimini.model.UnifiedAssessment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NormalizerServiceTest {

    @Test
    void testNormalizeScores() {
        ProviderAssessment pa = new ProviderAssessment();
        pa.patientId = "P123";
        pa.assessmentType = "behavioral_screening";
        pa.assessmentDate = LocalDate.of(2024, 10, 15);
        pa.rawScores = Map.of( "social", 40, "anxiety", 70);
        pa.maxScore = 100;

        NormalizerService normalizer = new NormalizerService();
        UnifiedAssessment ua = normalizer.normalize(pa, new ProviderAAdapter());

        assertEquals("P123", ua.patientId);

        for (var score: ua.scores) {
            if (score.dimension.equals("social")) {
                assertEquals(40, score.value);
            } else if (score.dimension.equals("anxiety")) {
                assertEquals(70, score.value);
            }
        }

        assertEquals("provider_a", ua.metadata.sourceProvider);
        assertEquals("nested_json", ua.metadata.sourceFormat);
    }

    @Test
    void testInvalidScoreThrows() {
        ProviderAssessment pa = new ProviderAssessment();
        pa.patientId = "P123";
        pa.assessmentType = "behavioral_screening";
        pa.assessmentDate = LocalDate.now();
        pa.rawScores = Map.of("anxiety", 20); // maxScore is 10
        pa.maxScore = 10;

        NormalizerService normalizer = new NormalizerService();
        assertThrows(Exception.class, () -> normalizer.normalize(pa, new ProviderAAdapter()));
    }
}