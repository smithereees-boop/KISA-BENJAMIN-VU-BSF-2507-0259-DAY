package src;

// File: src/Account.java
public abstract class Account {
    // Common state attributes required by the coursework paper
    private String firstName;
    private String lastName;
    private String nin;
    private String email;
    private String phoneNumber;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String branch;
    private double openingDeposit;
    private String accountNumber;

    // Constructor to clean and map all incoming form values
    public Account(String firstName, String lastName, String nin, String email, 
                   String phoneNumber, int birthYear, int birthMonth, int birthDay, 
                   String branch, double openingDeposit) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.nin = nin.trim().toUpperCase();
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
    }

    // Abstract methods to be overridden by subclasses (Polymorphism)
    public abstract double getMinimumDeposit();
    public abstract String getAccountTypeName();

    // The missing method your GUI needs to check if the deposit is valid
    public boolean isValidDeposit() {
        return this.openingDeposit >= getMinimumDeposit();
    }

    // Computes age relative to the coursework session year (2026)
    public int getDerivedAge() {
        return 2026 - birthYear;
    }

    // Standard Getters and Setters for data extraction and persistence
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNin() { return nin; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDOBString() { return String.format("%04d-%02d-%02d", birthYear, birthMonth, birthDay); }
    public String getBranch() { return branch; }
    public double getOpeningDeposit() { return openingDeposit; }
    public String getAccountNumber() { return accountNumber; }
    
    public void setAccountNumber(String accountNumber) { 
        this.accountNumber = accountNumber; 
    }
}