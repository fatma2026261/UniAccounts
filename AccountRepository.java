package uniaccounts.repository;

import java.util.*;
import uniaccounts.model.StudentAccount;

/**
 * Pattern: SINGLETON - role: Singleton.
 * One single central repository (holder idiom: lazy and thread-safe, no explicit locking).
 */
public final class AccountRepository {

    private static class Holder {
        static final AccountRepository INSTANCE = new AccountRepository();
    }

    private final Map<String, StudentAccount> accounts = new LinkedHashMap<>();

    private AccountRepository() {}                       // nobody else can create one

    public static AccountRepository getInstance() { return Holder.INSTANCE; }

    /** The id must be unique in the repository. */
    public synchronized void save(StudentAccount a) {
        if (accounts.containsKey(a.getId()))
            throw new IllegalArgumentException("duplicate id: " + a.getId());
        accounts.put(a.getId(), a);
    }
    public synchronized Optional<StudentAccount> findById(String id) { return Optional.ofNullable(accounts.get(id)); }
    public synchronized List<StudentAccount> findAll() { return new ArrayList<>(accounts.values()); }
    public synchronized int size() { return accounts.size(); }
    public synchronized void clear() { accounts.clear(); }
}
