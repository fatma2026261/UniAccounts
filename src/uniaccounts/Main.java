package uniaccounts;

import uniaccounts.logging.AuditLogger;
import uniaccounts.model.StudentAccount;
import uniaccounts.prototype.TemplateRegistry;
import uniaccounts.repository.AccountRepository;

public class Main {
    public static void main(String[] args) {
        AccountRepository repo = AccountRepository.getInstance();
        AuditLogger audit = AuditLogger.getInstance();

        // ---------- 1) BUILDER ----------
        System.out.println("=== 1) Builder ===");
        // mandatory fields only (defaults for the others)
        StudentAccount minimal = new StudentAccount.Builder("2025001", "Amine Khelifi", "amine@univ.dz").build();
        System.out.println(minimal);

        // mandatory + optional fields, readable and order-independent
        StudentAccount full = new StudentAccount.Builder("2025002", "Sara Benali", "sara@univ.dz")
                .phone("0550123456").level("M1").major("Computer Science")
                .scholarship(true).addCourse("Java").addCourse("Databases").build();
        System.out.println(full);

        repo.save(minimal);  audit.log("CREATED account " + minimal.getId());
        repo.save(full);     audit.log("CREATED account " + full.getId());

        // validation errors
        tryBuild("no @ in email",  () -> new StudentAccount.Builder("3", "Bad Mail", "bad-mail").build());
        tryBuild("empty id",       () -> new StudentAccount.Builder("  ", "No Id", "a@b.dz").build());
        tryBuild("invalid level",  () -> new StudentAccount.Builder("4", "Bad Level", "a@b.dz").level("L9").build());

        // ---------- 2) SINGLETON ----------
        System.out.println("\n=== 2) Singleton ===");
        System.out.println("Same repository instance? " + (AccountRepository.getInstance() == repo));
        System.out.println("Same logger instance?     " + (AuditLogger.getInstance() == audit));
        try {
            repo.save(minimal);                           // duplicate id
        } catch (IllegalArgumentException e) {
            System.out.println("Repository refused: " + e.getMessage());
            audit.log("REJECTED duplicate id " + minimal.getId());
        }

        // ---------- 3) PROTOTYPE ----------
        System.out.println("\n=== 3) Prototype ===");
        StudentAccount template = new StudentAccount.Builder("TEMPLATE", "Template", "template@univ.dz")
                .level("L1").major("Computer Science")
                .addCourse("Algorithms").addCourse("Mathematics").addCourse("Java").build();
        TemplateRegistry registry = new TemplateRegistry();
        registry.register("CS-L1-2025", template);

        String[][] cohort = {
            {"2025101", "Yacine Bensalem", "yacine@univ.dz"},
            {"2025102", "Nadia Ouali",     "nadia@univ.dz"},
            {"2025103", "Rida Benbordi",   "rida@univ.dz"}
        };
        for (String[] s : cohort) {
            StudentAccount a = registry.create("CS-L1-2025", s[0], s[1], s[2]);
            repo.save(a);
            audit.log("CREATED (from template) account " + a.getId());
            System.out.println(a);
        }
        StudentAccount clone = repo.findById("2025101").get();
        System.out.println("Clone is a different object than template? " + (clone != template));

        // ---------- result ----------
        System.out.println("\n=== Repository content (" + repo.size() + " accounts) ===");
        repo.findAll().forEach(a -> System.out.println(a.getId() + " | " + a.getFullName() + " | " + a.getLevel()));

        System.out.println("\n=== Audit log ===");
        audit.getEntries().forEach(System.out::println);
    }

    private static void tryBuild(String label, Runnable r) {
        try { r.run(); }
        catch (IllegalArgumentException e) { System.out.println("Builder rejected (" + label + "): " + e.getMessage()); }
    }
}
