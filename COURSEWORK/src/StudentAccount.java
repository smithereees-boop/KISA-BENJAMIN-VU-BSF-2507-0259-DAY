package src;

// File: src/StudentAccount.java
public class StudentAccount extends Account {
    public StudentAccount(String f, String l, String n, String e, String p, int y, int m, int d, String b, double dep) {
        super(f, l, n, e, p, y, m, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 10000.0; }
    @Override public String getAccountTypeName() { return "Student"; }
}