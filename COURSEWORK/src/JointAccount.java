package src;

// File: src/JointAccount.java
public class JointAccount extends Account {
    private String secondaryNin;
    
    public JointAccount(String f, String l, String n, String sn, String e, String p, int y, int m, int d, String b, double dep) {
        super(f, l, n, e, p, y, m, d, b, dep);
        this.secondaryNin = sn.trim().toUpperCase();
    }
    @Override public double getMinimumDeposit() { return 100000.0; }
    @Override public String getAccountTypeName() { return "Joint"; }
    public String getSecondaryNin() { return secondaryNin; }
}