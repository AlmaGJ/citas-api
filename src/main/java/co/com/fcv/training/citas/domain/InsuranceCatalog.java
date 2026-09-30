package co.com.fcv.training.citas.domain;

/** Framework-free vocabulary for admin-managed EPS/plan catalogs. */
public final class InsuranceCatalog {
    private InsuranceCatalog() {}
    public record Eps(long id, String code, String name, boolean active) {}
    public record Plan(long id, long epsId, long regimeId, String code, String name, boolean active) {}
    public record Regime(long id, String code, String name) {}
}
