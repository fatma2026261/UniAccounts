package uniaccounts.prototype;

import java.util.HashMap;
import java.util.Map;
import uniaccounts.model.StudentAccount;

/** Pattern: PROTOTYPE - role: Prototype registry (one ready-made template per cohort). */
public class TemplateRegistry {
    private final Map<String, StudentAccount> templates = new HashMap<>();

    public void register(String key, StudentAccount template) { templates.put(key, template); }

    public StudentAccount create(String key, String id, String fullName, String email) {
        StudentAccount t = templates.get(key);
        if (t == null) throw new IllegalArgumentException("unknown template: " + key);
        return t.copyWithIdentity(id, fullName, email);
    }
}
