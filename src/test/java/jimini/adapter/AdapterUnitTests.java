package jimini.adapter;

import jimini.exception.ValidationException;
import jimini.model.ProviderAssessment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterUnitTests {

    // --- Provider A Tests ---

    @Test
    void providerA_validInput_shouldParse() throws ValidationException {
        String jsonA = """
                {
                  "patient": {"id": "P123", "name": "John"},
                  "assessment": {"type": "behavioral_screening", "scores": {"anxiety": 7, "social": 4}}
                }
                """;

        ProviderAAdapter adapter = new ProviderAAdapter();
        ProviderAssessment pa = adapter.parse(jsonA);

        assertEquals("P123", pa.patientId);
        assertEquals("behavioral_screening", pa.assessmentType);
        assertEquals(2, pa.rawScores.size());
    }

    @Test
    void providerA_missingPatientId_shouldThrow() {
        String jsonA = """
                {
                  "patient": {"name": "John"},
                  "assessment": {"type": "behavioral_screening", "scores": {"anxiety": 7}}
                }
                """;

        ProviderAAdapter adapter = new ProviderAAdapter();
        assertThrows(ValidationException.class, () -> adapter.parse(jsonA));
    }

    // --- Provider B Tests ---

    @Test
    void providerB_validInput_shouldParse() throws ValidationException {
        String jsonB = """
                {
                  "patient_id": "P123",
                  "assessment_type": "cognitive",
                  "score_memory": 85,
                  "score_processing": 72
                }
                """;

        ProviderBAdapter adapter = new ProviderBAdapter();
        ProviderAssessment pa = adapter.parse(jsonB);

        assertEquals("P123", pa.patientId);
        assertEquals("cognitive", pa.assessmentType);
        assertEquals(2, pa.rawScores.size());
    }


    // --- Provider C Tests (CSV) ---

    @Test
    void providerC_validCsv_shouldParse() throws ValidationException {
        String csv = """
                patient_id,assessment_date,metric_name,metric_value,category
                P123,2024-10-15,attention_span,6,behavioral
                P123,2024-10-15,social_engagement,4,behavioral
                """;

        ProviderCAdapter adapter = new ProviderCAdapter();
        ProviderAssessment pa = adapter.parse(csv);

        assertEquals("P123", pa.patientId);
        assertEquals("behavioral", pa.assessmentType);
        assertEquals(2, pa.rawScores.size());
        assertEquals(6, pa.rawScores.get("attention_span"));
    }

    @Test
    void providerC_malformedCsv_shouldThrow() {
        String csv = """
                patient_id,assessment_date,metric_name,metric_value,category
                P123,2024-10-15,attention_span
                """;

        ProviderCAdapter adapter = new ProviderCAdapter();
        assertThrows(ValidationException.class, () -> adapter.parse(csv));
    }

    @Test
    void providerC_missingHeader_shouldThrow() {
        String csv = "wrong,header,line\nP123,2024-10-15,attention_span,6,behavioral";
        ProviderCAdapter adapter = new ProviderCAdapter();
        assertThrows(ValidationException.class, () -> adapter.parse(csv));
    }
}