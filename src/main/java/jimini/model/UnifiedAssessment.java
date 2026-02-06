package jimini.model;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnifiedAssessment {
    @JsonProperty("patientId")
    public String patientId;

    @JsonProperty("assessmentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    public Instant assessmentDate;

    @JsonProperty("assessmentType")
    public String assessmentType;

    @JsonProperty("scores")
    public List<Score> scores;

    @JsonProperty("metadata")
    public Metadata metadata;
}