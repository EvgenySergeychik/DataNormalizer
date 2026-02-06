package jimini.model;

public record Result(UnifiedAssessment data, String error) {
    public static Result success(UnifiedAssessment d) {
        return new Result(d, null);
    }
    public static Result failure(String e) {
        return new Result(null, e);
    }
}