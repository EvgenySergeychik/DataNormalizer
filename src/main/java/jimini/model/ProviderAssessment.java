package jimini.model;

import java.time.LocalDate;
import java.util.Map;

public class ProviderAssessment {
    public String patientId;
    public LocalDate assessmentDate;
    public String assessmentType;
    public Map<String, Integer> rawScores; // provider scale
    public int maxScore;                   // needed for normalization
}