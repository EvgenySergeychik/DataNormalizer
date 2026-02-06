package jimini.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import jimini.exception.ValidationException;
import jimini.model.ProviderAssessment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@Component
public class ProviderAAdapter implements ProviderAdapter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ProviderAssessment parse(String rawInput) throws ValidationException {
        try {
            JsonNode root = mapper.readTree(rawInput);

            ProviderAssessment pa = new ProviderAssessment();
            pa.patientId = root.path("patient").path("id").asText(null);
            pa.assessmentType = root.path("assessment").path("type").asText(null);

            if (pa.patientId == null || pa.assessmentType == null) {
                throw new ValidationException("Missing required fields");
            }

            pa.assessmentDate = LocalDate.now(); // mock
            pa.maxScore = 100;

            pa.rawScores = new HashMap<>();
            JsonNode scores = root.path("assessment").path("scores");
            scores.fields().forEachRemaining(e ->
                    pa.rawScores.put(e.getKey(), e.getValue().asInt())
            );

            return pa;
        } catch (IOException e) {
            throw new ValidationException("Invalid JSON", e);
        }
    }

    public String providerName() { return "provider_a"; }
    public String sourceFormat() { return "nested_json"; }
}