package jimini.adapter;

import jimini.exception.ValidationException;
import jimini.model.ProviderAssessment;

public interface ProviderAdapter {
    ProviderAssessment parse(String rawInput) throws ValidationException;
    String providerName();
    String sourceFormat();
}