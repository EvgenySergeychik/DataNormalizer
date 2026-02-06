package jimini.adapter;

import jimini.exception.ValidationException;
import jimini.model.ProviderAssessment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;

@Component
public class ProviderCAdapter implements ProviderAdapter {

    private final static String HEADER = "patient_id,assessment_date,metric_name,metric_value,category";
    @Override
    public ProviderAssessment parse(String rawInput) throws ValidationException {
        String[] lines = rawInput.split("\n");

        String actualHeader = lines[0]; // first line of CSV
        if (!HEADER.equals(actualHeader)) {
            throw new ValidationException("Invalid CSV header");
        }

        ProviderAssessment pa = new ProviderAssessment();
        pa.rawScores = new HashMap<>();
        pa.maxScore = 10;

        for (int i = 1; i < lines.length; i++) {
            String[] cols = lines[i].split(",");

            if (cols.length != 5) {
                throw new ValidationException("Invalid number of columns. Expected 5 actual:" + cols.length);
            }

            pa.patientId = cols[0];
            pa.assessmentDate = LocalDate.parse(cols[1]);
            pa.rawScores.put(cols[2], Integer.parseInt(cols[3]));
            pa.assessmentType = cols[4];
        }

        return pa;
    }

    public String providerName() { return "provider_c"; }
    public String sourceFormat() { return "csv"; }
}
