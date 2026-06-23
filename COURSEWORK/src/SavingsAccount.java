package src;

// File: src/SavingsAccount.java
public class SavingsAccount extends Account {
    public SavingsAccount(String f, String l, String n, String e, String p, int y, int m, int d, String b, double dep) {
        super(f, l, n, e, p, y, m, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 50000.0; }
    @Override public String getAccountTypeName() { return "Savings"; }
}