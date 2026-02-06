# Project Name

## Overview
NormalizerSvc is a Spring Boot REST service that ingests patient assessment data from multiple external providers, 
normalizes it into a unified schema, and returns validated, standardized results. 
It is designed to handle heterogeneous input formats (JSON, CSV) and supports easy extension for additional providers.
High level flow:

---

## Adding a New Adapter

### 1. Create Adapter Class
- Implement the `Adapter` interface.
- Implement `fetchData()` and `processData()` methods.

```java
@Component
public class MyAdapter implements Adapter {
    @Override
    public ProviderAssessment parse(String rawInput)  {
        // fetch logic here
        return null;
    }
}
```

### 2. Add Validation Logic
- Implement validation in validate(Data data) method or use existing Validator utility.
```java
public boolean validate(Data data) {
    return data.getField() != null && data.isValid();
}
```

### 3. Schema Version 
-  Migrations – Include version in metadata; normalization pipeline adapts to schema changes for backward compatibility.

### 4. Custom Provider Rules 
–  Each ProviderAdapter can define applyCustomRules() for provider-specific transformations before standard normalization.

### 4. Batch Processing 
–  Stream or chunk large datasets, optionally process in parallel; supports batch API or queue-based ingestion for scalable handling.

### 5. Run the service.
It is a Spring boot application
#### 5.1 build:
mvn clean package
#### 5.2 run:
java -jar target/data-normalizer-0.0.1-SNAPSHOT.jar
#### 5.3 Invoke the api
curl -X POST 'http://localhost:8080/api/assessments?provider=provider_a' \
     -H "Content-Type: application/json" \
     --data @src/test/resources/providerA.json
{"patientId":"P123","assessmentDate":"2026-02-06T00:00:00Z","assessmentType":"behavioral_screening","scores":[{"dimension":"anxiety","value":7,"scale":"0-100"},{"dimension":"social","value":4,"scale":"0-100"},{"dimension":"attention","value":6,"scale":"0-100"}],"metadata":{"sourceProvider":"provider_a","sourceFormat":"nested_json","ingestedAt":"2026-02-06T22:35:55.219207Z","version":"1.0"}}
