package src;

// File: src/CurrentAccount.java
public class CurrentAccount extends Account {
    public CurrentAccount(String f, String l, String n, String e, String p, int y, int m, int d, String b, double dep) {
        super(f, l, n, e, p, y, m, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 200000.0; }
    @Override public String getAccountTypeName() { return "Current"; }
}