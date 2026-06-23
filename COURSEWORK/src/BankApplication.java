package src;

// File: src/BankApplication.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

public class BankApplication extends JFrame {
    // GUI Input Components
    private JTextField txtFirst, txtLast, txtNIN, txtSecNIN, txtEmail, txtConfEmail, txtPhone, txtDeposit;
    private JPasswordField txtPIN, txtConfPIN;
    private JComboBox<Integer> cmbYear, cmbDay;
    private JComboBox<String> cmbMonth, cmbType, cmbBranch;
    private JTextArea txtSummary;
    
    // Inline Error Validation Labels
    private JLabel errFirst, errLast, errNIN, errSecNIN, errEmail, errPhone, errPIN, errDeposit;

    private final String[] months = {"January", "February", "March", "April", "May", "June", 
                                     "July", "August", "September", "October", "November", "December"};

    public BankApplication() {
        setTitle("First Bank Uganda - New Account Opening Form");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields Initialization
        txtFirst = new JTextField(15); 
        txtLast = new JTextField(15);
        txtNIN = new JTextField(15); 
        txtSecNIN = new JTextField(15); 
        txtSecNIN.setEnabled(false); // Disabled by default until 'Joint' is picked
        txtEmail = new JTextField(15); 
        txtConfEmail = new JTextField(15);
        txtPhone = new JTextField(15); 
        txtDeposit = new JTextField(15);
        txtPIN = new JPasswordField(15); 
        txtConfPIN = new JPasswordField(15);

        // Error message labels initialization
        errFirst = createErrLabel(); 
        errLast = createErrLabel(); 
        errNIN = createErrLabel();
        errSecNIN = createErrLabel(); 
        errEmail = createErrLabel(); 
        errPhone = createErrLabel();
        errPIN = createErrLabel(); 
        errDeposit = createErrLabel();

        // DOB ComboBoxes Configuration
        cmbYear = new JComboBox<>();
        for (int y = 2026; y >= 1940; y--) cmbYear.addItem(y);
        cmbMonth = new JComboBox<>(months);
        cmbDay = new JComboBox<>();
        updateDays(); // Initial day population

        // Auto-update days when Month or Year selections shift
        ItemListener dobUpdater = e -> { if (e.getStateChange() == ItemEvent.SELECTED) updateDays(); };
        cmbYear.addItemListener(dobUpdater);
        cmbMonth.addItemListener(dobUpdater);

        // Dropdown Lists
        cmbType = new JComboBox<>(new String[]{"Savings", "Current", "Fixed Deposit", "Student", "Joint"});
        cmbBranch = new JComboBox<>(new String[]{"Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"});

        // Enable secondary NIN field only if Joint Account is selected
        cmbType.addActionListener(e -> txtSecNIN.setEnabled("Joint".equals(cmbType.getSelectedItem())));

        // Grid Layout Grid Mapping
        int row = 0;
        addFormRow(formPanel, gbc, "First Name:", txtFirst, errFirst, row++);
        addFormRow(formPanel, gbc, "Last Name:", txtLast, errLast, row++);
        addFormRow(formPanel, gbc, "National ID (NIN):", txtNIN, errNIN, row++);
        addFormRow(formPanel, gbc, "Secondary NIN (Joint Only):", txtSecNIN, errSecNIN, row++);
        addFormRow(formPanel, gbc, "Email Address:", txtEmail, null, row++);
        addFormRow(formPanel, gbc, "Confirm Email:", txtConfEmail, errEmail, row++);
        addFormRow(formPanel, gbc, "Phone Number (+256...):", txtPhone, errPhone, row++);
        addFormRow(formPanel, gbc, "4-6 Digit Security PIN:", txtPIN, null, row++);
        addFormRow(formPanel, gbc, "Confirm PIN:", txtConfPIN, errPIN, row++);
        
        // Custom DOB Row Setup
        gbc.gridy = row; gbc.gridx = 0; formPanel.add(new JLabel("Date of Birth:"), gbc);
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        dobPanel.add(cmbYear); dobPanel.add(cmbMonth); dobPanel.add(cmbDay);
        gbc.gridx = 1; formPanel.add(dobPanel, gbc); row++;

        addFormRow(formPanel, gbc, "Account Type:", cmbType, null, row++);
        addFormRow(formPanel, gbc, "Target Branch:", cmbBranch, null, row++);
        addFormRow(formPanel, gbc, "Opening Deposit (UGX):", txtDeposit, errDeposit, row++);

        // Action Buttons Setup
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSubmit = new JButton("Submit Registration");
        JButton btnReset = new JButton("Reset Form");
        btnPanel.add(btnSubmit); btnPanel.add(btnReset);

        btnSubmit.addActionListener(e -> processSubmission());
        btnReset.addActionListener(e -> resetFormFields());

        // Summary Text Area Layout Setup
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBorder(BorderFactory.createTitledBorder("Account Summary is Below:"));
        txtSummary = new JTextArea(4, 60);
        txtSummary.setEditable(false);
        txtSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        southPanel.add(new JScrollPane(txtSummary), BorderLayout.CENTER);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JLabel createErrLabel() {
        JLabel lbl = new JLabel("");
        lbl.setForeground(Color.RED);
        lbl.setFont(new Font("Arial", Font.ITALIC, 11));
        return lbl;
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, String labelText, Component comp, Component err, int r) {
        gbc.gridy = r; gbc.gridx = 0; p.add(new JLabel(labelText), gbc);
        gbc.gridx = 1; p.add(comp, gbc);
        if (err != null) { gbc.gridx = 2; p.add(err, gbc); }
    }

    private void updateDays() {
        if (cmbYear.getSelectedItem() == null || cmbMonth.getSelectedItem() == null) return;
        int year = (int) cmbYear.getSelectedItem();
        int monthIdx = cmbMonth.getSelectedIndex();
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthIdx);
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int currentSelection = (cmbDay.getSelectedItem() != null) ? (int) cmbDay.getSelectedItem() : 1;
        cmbDay.removeAllItems();
        for (int d = 1; d <= maxDays; d++) cmbDay.addItem(d);
        if (currentSelection <= maxDays) cmbDay.setSelectedItem(currentSelection);
    }

    private void resetFormFields() {
        txtFirst.setText(""); txtLast.setText(""); txtNIN.setText(""); txtSecNIN.setText("");
        txtEmail.setText(""); txtConfEmail.setText(""); txtPhone.setText(""); txtDeposit.setText("");
        txtPIN.setText(""); txtConfPIN.setText("");
        cmbYear.setSelectedIndex(0); cmbMonth.setSelectedIndex(0); updateDays();
        cmbType.setSelectedIndex(0); cmbBranch.setSelectedIndex(0);
        txtSummary.setText("");
        
        // Clear all inline errors cleanly
        errFirst.setText(""); errLast.setText(""); errNIN.setText(""); errSecNIN.setText("");
        errEmail.setText(""); errPhone.setText(""); errPIN.setText(""); errDeposit.setText("");
    }

    private void processSubmission() {
        // Clear out old error warning fields
        errFirst.setText(""); errLast.setText(""); errNIN.setText(""); errSecNIN.setText("");
        errEmail.setText(""); errPhone.setText(""); errPIN.setText(""); errDeposit.setText("");

        StringBuilder errorsSummary = new StringBuilder();
        boolean valid = true;

        // 1. Core Field Input Pattern Validation Rules
        String fName = txtFirst.getText().trim();
        if (!fName.matches("[A-Za-z]{2,30}")) {
            errFirst.setText("Letters only (2-30 chars).");
            errorsSummary.append("- Invalid First Name format.\n"); valid = false;
        }

        String lName = txtLast.getText().trim();
        if (!lName.matches("[A-Za-z]{2,30}")) {
            errLast.setText("Letters only (2-30 chars).");
            errorsSummary.append("- Invalid Last Name format.\n"); valid = false;
        }

        String nin = txtNIN.getText().trim().toUpperCase();
        if (!nin.matches("[A-Z0-9]{14}")) {
            errNIN.setText("Must be 14 Upper Alphanumeric characters.");
            errorsSummary.append("- Base National ID (NIN) format mismatch.\n"); valid = false;
        }

        String type = (String) cmbType.getSelectedItem();
        String secNIN = txtSecNIN.getText().trim().toUpperCase();
        if ("Joint".equals(type) && !secNIN.matches("[A-Z0-9]{14}")) {
            errSecNIN.setText("Required 14 Alpha-num for Joint applicant.");
            errorsSummary.append("- Secondary Joint Partner NIN validation error.\n"); valid = false;
        }

        String email = txtEmail.getText().trim();
        String confEmail = txtConfEmail.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$") || !email.equalsIgnoreCase(confEmail)) {
            errEmail.setText("Valid emails mismatch.");
            errorsSummary.append("- Mail or Confirmation string error.\n"); valid = false;
        }

        String phone = txtPhone.getText().trim();
        if (!phone.matches("\\+256\\d{9}")) {
            errPhone.setText("Must match format +256XXXXXXXXX.");
            errorsSummary.append("- Contact number prefix missing (+256).\n"); valid = false;
        }

        String pin = new String(txtPIN.getPassword());
        String confPin = new String(txtConfPIN.getPassword());
        if (!pin.matches("\\d{4,6}") || !pin.equals(confPin) || pin.matches("^(\\d)\\1+$")) {
            errPIN.setText("4-6 Digits. Must match. Can't be identical digits.");
            errorsSummary.append("- Security PIN parameters constraint error.\n"); valid = false;
        }

        int birthY = (int) cmbYear.getSelectedItem();
        int birthM = cmbMonth.getSelectedIndex() + 1;
        int birthD = (int) cmbDay.getSelectedItem();
        int derivedAge = 2026 - birthY;

        // Core Age Limits Rule Execution
        if (derivedAge < 18 || derivedAge > 75) {
            errorsSummary.append("- General banking age policy restricted (18-75).\n"); valid = false;
        }
        if ("Student".equals(type) && (derivedAge < 18 || derivedAge > 25)) {
            errorsSummary.append("- Student accounts require student age limits (18-25).\n"); valid = false;
        }

        double deposit = 0;
        try {
            deposit = Double.parseDouble(txtDeposit.getText().trim());
        } catch (NumberFormatException nfe) {
            errDeposit.setText("Numeric value required.");
            errorsSummary.append("- Opening deposit field parsing format error.\n"); valid = false;
        }

        // If any initial rules fail, block operation and display error modal dialog box
        if (!valid) {
            JOptionPane.showMessageDialog(this, errorsSummary.toString(), "Validation Core Matrix Alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Polymorphic Model Instantiation
        Account newClientAccount = switch (type) {
            case "Savings" -> new SavingsAccount(fName, lName, nin, email, phone, birthY, birthM, birthD, (String) cmbBranch.getSelectedItem(), deposit);
            case "Current" -> new CurrentAccount(fName, lName, nin, email, phone, birthY, birthM, birthD, (String) cmbBranch.getSelectedItem(), deposit);
            case "Fixed Deposit" -> new FixedDepositAccount(fName, lName, nin, email, phone, birthY, birthM, birthD, (String) cmbBranch.getSelectedItem(), deposit);
            case "Student" -> new StudentAccount(fName, lName, nin, email, phone, birthY, birthM, birthD, (String) cmbBranch.getSelectedItem(), deposit);
            case "Joint" -> new JointAccount(fName, lName, nin, secNIN, email, phone, birthY, birthM, birthD, (String) cmbBranch.getSelectedItem(), deposit);
            default -> null;
        };

        // 3. Dynamic Deposit Limit Checking via Polymorphism
        if (newClientAccount != null && !newClientAccount.isValidDeposit()) {
            errDeposit.setText("Below Min Limit: " + String.format("%,.0f", newClientAccount.getMinimumDeposit()) + " UGX");
            JOptionPane.showMessageDialog(this, "Deposit amount fails minimum financial rules assigned for: " + type, "Limits Exception Fail", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Generate Account Number sequence & Save to MS Access Database
        String genAccNo = DatabaseManager.generateAccountNumber(newClientAccount.getBranch());
        newClientAccount.setAccountNumber(genAccNo);

        boolean saved = DatabaseManager.saveAccount(newClientAccount);
        if (saved) {
            // 1. Your formatted output summary layout string
            String outputStr = String.format("ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s\n",
                    newClientAccount.getAccountNumber(), newClientAccount.getLastName(), newClientAccount.getFirstName(),
                    newClientAccount.getAccountTypeName(), newClientAccount.getBranch(), newClientAccount.getDOBString(),
                    newClientAccount.getPhoneNumber(), newClientAccount.getOpeningDeposit(), newClientAccount.getEmail());
            
            // 2. CHANGE THIS LINE: Use append() instead of setText() so it accumulates down the list
            txtSummary.append(outputStr);
            
            JOptionPane.showMessageDialog(this, "Account provisioned and verified completely in DB record!", "Success Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error writing data to database pipeline connection runtime layer.", "DB Storage Write Exception Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankApplication().setVisible(true));
    }
}