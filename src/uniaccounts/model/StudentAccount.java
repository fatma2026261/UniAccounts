package uniaccounts.model;

import java.util.*;
import uniaccounts.prototype.Prototype;

/**
 * Pattern: BUILDER   - role: Product (can only be created through StudentAccount.Builder).
 * Pattern: PROTOTYPE - role: ConcretePrototype (copy / copyWithIdentity).
 */
public class StudentAccount implements Prototype<StudentAccount> {

    public static final List<String> LEVELS = List.of("L1", "L2", "L3", "M1", "M2");

    private final String id;
    private final String fullName;
    private final String email;
    private final String phone;
    private final String level;
    private final String major;
    private final boolean scholarship;
    private final List<String> courses;

    // private: the only way in is the Builder
    private StudentAccount(Builder b) {
        this.id = b.id;
        this.fullName = b.fullName;
        this.email = b.email;
        this.phone = b.phone;
        this.level = b.level;
        this.major = b.major;
        this.scholarship = b.scholarship;
        this.courses = new ArrayList<>(b.courses);   // own copy of the list
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLevel() { return level; }
    public String getMajor() { return major; }
    public boolean hasScholarship() { return scholarship; }
    public List<String> getCourses() { return Collections.unmodifiableList(courses); }

    // ---------------- PROTOTYPE ----------------
    /** Exact deep copy (the courses list is duplicated, not shared). */
    @Override
    public StudentAccount copy() {
        return new Builder(id, fullName, email).phone(phone).level(level)
                .major(major).scholarship(scholarship).courses(courses).build();
    }

    /** Clone a template and change only the student's identity (id, name, email). */
    public StudentAccount copyWithIdentity(String newId, String newName, String newEmail) {
        return new Builder(newId, newName, newEmail).level(level)
                .major(major).scholarship(scholarship).courses(courses).build();
    }

    @Override
    public String toString() {
        return "StudentAccount{id=" + id + ", name=" + fullName + ", email=" + email
                + ", phone=" + phone + ", level=" + level + ", major=" + major
                + ", scholarship=" + scholarship + ", courses=" + courses + "}";
    }

    // =====================================================================
    /** Pattern: BUILDER - role: Builder (fluent API, validation in build()). */
    public static class Builder {
        // mandatory fields: required by the constructor
        private final String id, fullName, email;
        // optional fields: with their default values
        private String phone = null;
        private String level = "L1";
        private String major = null;
        private boolean scholarship = false;
        private final List<String> courses = new ArrayList<>();

        public Builder(String id, String fullName, String email) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
        }
        public Builder phone(String v)        { this.phone = v; return this; }
        public Builder level(String v)        { this.level = v; return this; }
        public Builder major(String v)        { this.major = v; return this; }
        public Builder scholarship(boolean v) { this.scholarship = v; return this; }
        public Builder addCourse(String c)    { this.courses.add(c); return this; }
        public Builder courses(Collection<String> cs) {
            this.courses.clear();
            this.courses.addAll(cs);
            return this;
        }

        public StudentAccount build() {
            if (id == null || id.isBlank())
                throw new IllegalArgumentException("id is required and must not be empty");
            if (fullName == null || fullName.isBlank())
                throw new IllegalArgumentException("fullName is required");
            if (email == null || !email.contains("@"))
                throw new IllegalArgumentException("email must contain '@': " + email);
            if (!LEVELS.contains(level))
                throw new IllegalArgumentException("invalid level (L1,L2,L3,M1,M2): " + level);
            return new StudentAccount(this);
        }
    }
}
