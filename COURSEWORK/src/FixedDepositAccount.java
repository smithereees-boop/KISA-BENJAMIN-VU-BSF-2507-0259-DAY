package src;

// File: src/FixedDepositAccount.java
public class FixedDepositAccount extends Account {
    public FixedDepositAccount(String f, String l, String n, String e, String p, int y, int m, int d, String b, double dep) {
        super(f, l, n, e, p, y, m, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 1000000.0; }
    @Override public String getAccountTypeName() { return "Fixed Deposit"; }
}