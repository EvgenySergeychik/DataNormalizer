package jimini.svc;

import jimini.model.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import jimini.adapter.ProviderAdapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private IngestionService ingestionService;
    private Map<String, ProviderAdapter> adapters;

    public AssessmentController(IngestionService ingestionService,
                                List<ProviderAdapter> adapterList) {
        this.ingestionService = ingestionService;
        // Map provider name -> adapter
        this.adapters = adapterList.stream()
                .collect(Collectors.toMap(ProviderAdapter::providerName, a -> a));
    }

    @PostMapping
    public ResponseEntity<?> ingest(
            @RequestParam("provider") String provider,
            @RequestBody String payload) {

        ProviderAdapter adapter = adapters.get(provider.toLowerCase());
        if (adapter == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unknown provider: " + provider));
        }

        Result result = ingestionService.ingest(payload, adapter);
        if (result.data() != null)
            return ResponseEntity.ok(result.data());
        else
            return ResponseEntity.badRequest().body(Map.of("error", result.error()));
    }
}