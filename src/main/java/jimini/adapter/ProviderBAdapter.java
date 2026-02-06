package jimini.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jimini.exception.ValidationException;
import jimini.model.ProviderAssessment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@Component
public class ProviderBAdapter implements ProviderAdapter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ProviderAssessment parse(String rawInput) throws ValidationException {
        try {
            JsonNode root = mapper.readTree(rawInput);

            ProviderAssessment pa = new ProviderAssessment();
            pa.patientId = root.path("patient_id").asText(null);
            pa.assessmentType = root.path("assessment_type").asText(null);
            pa.assessmentDate = LocalDate.now();
            pa.maxScore = 100;

            pa.rawScores = new HashMap<>();
            root.fields().forEachRemaining(e -> {
                if (e.getKey().startsWith("score_")) {
                    pa.rawScores.put(
                            e.getKey().substring(6),
                            e.getValue().asInt()
                    );
                }
            });

            return pa;
        } catch (IOException e) {
            throw new ValidationException("Invalid JSON", e);
        }
    }

    public String providerName() { return "provider_b"; }
    public String sourceFormat() { return "flat_json"; }
}