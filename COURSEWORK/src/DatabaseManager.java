package src;

// File: src/DatabaseManager.java
import java.sql.*;

public class DatabaseManager {
    // This is the missing connection string pointing directly to your BankDB.accdb file
    private static final String DB_URL = "jdbc:ucanaccess://BankDB.accdb";

    /**
     * Generates a unique, sequential account number matching the pattern: BRANCHCODE-YYYY-xxxxxx
     */
    public static synchronized String generateAccountNumber(String branchName) {
        String branchCode = switch (branchName) {
            case "Kampala" -> "KLA";
            case "Gulu" -> "GUL";
            case "Mbarara" -> "MBR";
            case "Jinja" -> "JNJ";
            case "Mbale" -> "MBL";
            default -> "GEN";
        };
        
        int currentYear = 2026;
        int nextSequence = 1;

        // Query the highest existing sequential index directly from your Accounts table
        String checkSQL = "SELECT accountNumber FROM Accounts WHERE accountNumber LIKE '" + branchCode + "-" + currentYear + "-%' ORDER BY accountNumber DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSQL)) {
            
            if (rs.next()) {
                String highestAcc = rs.getString("accountNumber"); // e.g., "KLA-2026-000001"
                // Split the string by hyphens to grab the final numerical suffix
                String[] parts = highestAcc.split("-");
                if (parts.length == 3) {
                    nextSequence = Integer.parseInt(parts[2]) + 1; // Auto-increment safely by 1
                }
            }
        } catch (Exception ex) {
            System.err.println("Sequence Generation Notice: " + ex.getMessage());
        }

        return String.format("%s-%d-%06d", branchCode, currentYear, nextSequence);
    }

    /**
     * Inserts the account details directly into the MS Access Accounts Table
     */
    public static boolean saveAccount(Account account) {
        // Explicit column names mapped to prevent indexing structural shifts
        String insertSQL = "INSERT INTO Accounts (accountNumber, firstName, lastName, accountType, branch, dob, phoneNumbe, openingDep, email, nin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getFirstName());
            pstmt.setString(3, account.getLastName());
            pstmt.setString(4, account.getAccountTypeName());
            pstmt.setString(5, account.getBranch());
            pstmt.setString(6, account.getDOBString());
            pstmt.setString(7, account.getPhoneNumber());
            pstmt.setDouble(8, account.getOpeningDeposit()); // Double mapping matches your DB design change
            pstmt.setString(9, account.getEmail());
            pstmt.setString(10, account.getNin());
            
            pstmt.executeUpdate();
            return true; 
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}