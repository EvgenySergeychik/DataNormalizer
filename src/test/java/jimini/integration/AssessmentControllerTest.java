package jimini.integration;

import jimini.adapter.ProviderAdapter;
import jimini.adapter.ProviderAAdapter;
import jimini.model.Result;
import jimini.model.UnifiedAssessment;
import jimini.svc.AssessmentController;
import jimini.svc.IngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AssessmentControllerTest {

    private IngestionService ingestionService;
    private ProviderAAdapter providerAAdapter;
    private AssessmentController controller;

    @BeforeEach
    void setup() {
        // Mock the service and adapter
        ingestionService = Mockito.mock(IngestionService.class);
        providerAAdapter = Mockito.mock(ProviderAAdapter.class);

        // Set provider name
        when(providerAAdapter.providerName()).thenReturn("provider_a");

        // Create controller manually with list of adapters
        List<ProviderAdapter> adapters = List.of(providerAAdapter);
        controller = new AssessmentController(ingestionService, adapters);
    }

    @Test
    void testIngestEndpointWithValidPayload() {
        String payload = """
                {
                  "patient": {"id": "P123"},
                  "assessment": {"type": "behavioral_screening","scores":{"anxiety":7}}
                }
                """;

        // Create a dummy UnifiedAssessment
        UnifiedAssessment dummyAssessment = new UnifiedAssessment();
        Result successResult = new Result(dummyAssessment, null);

        // Mock the ingestion service to return a successful result
        when(ingestionService.ingest(payload, providerAAdapter))
                .thenReturn(successResult);

        ResponseEntity<?> response = controller.ingest("provider_a", payload);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertSame(dummyAssessment, response.getBody());
    }

    @Test
    void testIngestEndpointWithUnknownProvider() {
        String payload = "{}";

        ResponseEntity<?> response = controller.ingest("unknown_provider", payload);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof java.util.Map);
        assertTrue(((java.util.Map<?, ?>) response.getBody()).containsKey("error"));
    }

    @Test
    void testIngestEndpointWithErrorFromService() {
        String payload = "{}";

        // Mock the ingestion service to return a failure
        Result failureResult = new Result(null, "Some error");
        when(ingestionService.ingest(payload, providerAAdapter))
                .thenReturn(failureResult);

        ResponseEntity<?> response = controller.ingest("provider_a", payload);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Some error", ((java.util.Map<?, ?>) response.getBody()).get("error"));
    }
}
