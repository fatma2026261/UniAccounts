package uniaccounts.logging;

import java.time.LocalDateTime;
import java.util.*;

/** Pattern: SINGLETON - role: Singleton (one single audit log for the whole program). */
public final class AuditLogger {

    private static class Holder {
        static final AuditLogger INSTANCE = new AuditLogger();
    }

    private final List<String> entries = new ArrayList<>();

    private AuditLogger() {}

    public static AuditLogger getInstance() { return Holder.INSTANCE; }

    public synchronized void log(String message) {
        entries.add(LocalDateTime.now().withNano(0) + " - " + message);
    }
    public synchronized List<String> getEntries() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }
}
