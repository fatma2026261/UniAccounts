# UniAccounts - TP 1 (Creational patterns)

## Run
    mkdir out
    javac -d out $(find src -name '*.java')
    java -cp out uniaccounts.Main

(Windows PowerShell: `javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`)
Requires JDK 11+. Or: open the folder in VS Code and run `src/uniaccounts/Main.java`.

## Patterns
| Pattern   | Classes | Problem solved |
|-----------|---------|----------------|
| Builder   | `model.StudentAccount.Builder` | 8 fields (3 mandatory, 5 optional): readable code, validation |
| Singleton | `repository.AccountRepository`, `logging.AuditLogger` | exactly one repository and one audit log |
| Prototype | `prototype.Prototype`, `prototype.TemplateRegistry`, `StudentAccount.copy()/copyWithIdentity()` | clone a cohort template, change only the identity |
