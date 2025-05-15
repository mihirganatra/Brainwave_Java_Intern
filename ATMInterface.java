import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMInterface extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel mainPanel, loginPanel, atmPanel;
    private JTextField userField;
    private JPasswordField passField;
    private JLabel balanceLabel;
    private double balance = 1000.0; // starting balance
    private final String USERNAME = "mihir";
    private final String PASSWORD = "123";

    public ATMInterface() {
        setTitle("ATM Machine");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Login Panel
        loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        userField = new JTextField();
        passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginBtn);

        // ATM Panel
        atmPanel = new JPanel();
        atmPanel.setLayout(new GridLayout(6, 1, 10, 10));
        atmPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        balanceLabel = new JLabel("Balance: ₹" + balance);
        JButton checkBalanceBtn = new JButton("Check Balance");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton exitBtn = new JButton("Exit");

        checkBalanceBtn.addActionListener(this);
        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        atmPanel.add(balanceLabel);
        atmPanel.add(checkBalanceBtn);
        atmPanel.add(depositBtn);
        atmPanel.add(withdrawBtn);
        atmPanel.add(exitBtn);

        // Add to main panel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(atmPanel, "ATM");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Login":
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                    cardLayout.show(mainPanel, "ATM");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Check Balance":
                balanceLabel.setText("Balance: ₹" + balance);
                break;

            case "Deposit":
                String depositStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
                try {
                    double depositAmt = Double.parseDouble(depositStr);
                    if (depositAmt <= 0) throw new NumberFormatException();
                    balance += depositAmt;
                    JOptionPane.showMessageDialog(this, "Deposited ₹" + depositAmt);
                    balanceLabel.setText("Balance: ₹" + balance);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Withdraw":
                String withdrawStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
                try {
                    double withdrawAmt = Double.parseDouble(withdrawStr);
                    if (withdrawAmt <= 0 || withdrawAmt > balance) throw new NumberFormatException();
                    balance -= withdrawAmt;
                    JOptionPane.showMessageDialog(this, "Withdrew ₹" + withdrawAmt);
                    balanceLabel.setText("Balance: ₹" + balance);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid or insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Exit":
                int confirm = JOptionPane.showConfirmDialog(this, "Do you really want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }
    }

    public static void main(String[] args) {
        new ATMInterface();
    }
}
