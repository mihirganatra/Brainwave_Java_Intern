import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// The full dynamic Hospital Management System with Swing GUI
public class HospitalManagementSystemGUI {

    // --- Entities ---

    static class Patient {
        private static int idCounter = 1;
        private int id;
        private String name;
        private int age;
        private String gender;
        private String contact;

        public Patient(String name, int age, String gender, String contact) {
            this.id = idCounter++;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.contact = contact;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public String getContact() { return contact; }

        @Override
        public String toString() {
            return id + " - " + name;
        }
    }

    static class Appointment {
        private static int idCounter = 1;
        private int id;
        private int patientId;
        private String doctorName;
        private LocalDateTime appointmentDateTime;

        public Appointment(int patientId, String doctorName, LocalDateTime appointmentDateTime) {
            this.id = idCounter++;
            this.patientId = patientId;
            this.doctorName = doctorName;
            this.appointmentDateTime = appointmentDateTime;
        }

        public int getId() { return id; }
        public int getPatientId() { return patientId; }
        public String getDoctorName() { return doctorName; }
        public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }

        @Override
        public String toString() {
            return String.format("%d - PatientID:%d Doctor:%s %s", id, patientId, doctorName,
                    appointmentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
    }

    static class EHR {
        private int patientId;
        private ArrayList<String> records;

        public EHR(int patientId) {
            this.patientId = patientId;
            this.records = new ArrayList<>();
        }

        public int getPatientId() { return patientId; }
        public ArrayList<String> getRecords() { return records; }
        public void addRecord(String record) { records.add(record); }
    }

    static class Billing {
        private static int idCounter = 1;
        private int billId;
        private int patientId;
        private double amount;
        private LocalDateTime billingDate;
        private boolean paid;

        public Billing(int patientId, double amount) {
            this.billId = idCounter++;
            this.patientId = patientId;
            this.amount = amount;
            this.billingDate = LocalDateTime.now();
            this.paid = false;
        }

        public int getBillId() { return billId; }
        public int getPatientId() { return patientId; }
        public double getAmount() { return amount; }
        public LocalDateTime getBillingDate() { return billingDate; }
        public boolean isPaid() { return paid; }
        public void pay() { this.paid = true; }

        @Override
        public String toString() {
            return String.format("%d - PatientID:%d Amount:%.2f Date:%s Paid:%s", billId, patientId, amount,
                    billingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), paid ? "Yes" : "No");
        }
    }

    static class InventoryItem {
        private static int idCounter = 1;
        private int itemId;
        private String name;
        private int quantity;
        private String unit;

        public InventoryItem(String name, int quantity, String unit) {
            this.itemId = idCounter++;
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }

        public int getItemId() { return itemId; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public String getUnit() { return unit; }
        public void addQuantity(int amount) { quantity += amount; }
        public boolean reduceQuantity(int amount) {
            if(amount > quantity) return false;
            quantity -= amount;
            return true;
        }
    }

    static class Staff {
        private static int idCounter = 1;
        private int staffId;
        private String name;
        private String role;
        private String contact;

        public Staff(String name, String role, String contact) {
            this.staffId = idCounter++;
            this.name = name;
            this.role = role;
            this.contact = contact;
        }

        public int getStaffId() { return staffId; }
        public String getName() { return name; }
        public String getRole() { return role; }
        public String getContact() { return contact; }

        @Override
        public String toString() {
            return staffId + " - " + name + " (" + role + ")";
        }
    }

    // --- Managers / Models ---
    private Map<Integer, Patient> patients = new HashMap<>();
    private Map<Integer, Appointment> appointments = new HashMap<>();
    private Map<Integer, EHR> ehrRecords = new HashMap<>();
    private Map<Integer, Billing> bills = new HashMap<>();
    private Map<Integer, InventoryItem> inventoryItems = new HashMap<>();
    private Map<Integer, Staff> staffMembers = new HashMap<>();

    // --- GUI Components ---
    private JFrame frame;

    // References to panels for updating combo boxes
    private AppointmentPanel appointmentPanel;
    private EHRPanel ehrPanel;
    private BillingPanel billingPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HospitalManagementSystemGUI app = new HospitalManagementSystemGUI();
            app.buildGUI();
        });
    }

    private void buildGUI() {
        frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        // Main panel with buttons vertically arranged on left, dynamic content on right
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton patientBtn = new JButton("Patient Registration");
        JButton appointmentBtn = new JButton("Appointment Scheduling");
        JButton ehrBtn = new JButton("Electronic Health Records");
        JButton billingBtn = new JButton("Billing and Invoicing");
        JButton inventoryBtn = new JButton("Inventory Management");
        JButton staffBtn = new JButton("Staff Management");

        menuPanel.add(patientBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(appointmentBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(ehrBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(billingBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(inventoryBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(staffBtn);

        JPanel contentPanel = new JPanel(new CardLayout());

        // Create panels for each module
        PatientPanel patientPanel = new PatientPanel();
        appointmentPanel = new AppointmentPanel();
        ehrPanel = new EHRPanel();
        billingPanel = new BillingPanel();
        InventoryPanel inventoryPanel = new InventoryPanel();
        StaffPanel staffPanel = new StaffPanel();

        contentPanel.add(patientPanel, "Patient");
        contentPanel.add(appointmentPanel, "Appointment");
        contentPanel.add(ehrPanel, "EHR");
        contentPanel.add(billingPanel, "Billing");
        contentPanel.add(inventoryPanel, "Inventory");
        contentPanel.add(staffPanel, "Staff");

        // Button listeners to switch cards
        patientBtn.addActionListener(e -> switchCard(contentPanel, "Patient"));
        appointmentBtn.addActionListener(e -> switchCard(contentPanel, "Appointment"));
        ehrBtn.addActionListener(e -> switchCard(contentPanel, "EHR"));
        billingBtn.addActionListener(e -> switchCard(contentPanel, "Billing"));
        inventoryBtn.addActionListener(e -> switchCard(contentPanel, "Inventory"));
        staffBtn.addActionListener(e -> switchCard(contentPanel, "Staff"));

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private void switchCard(JPanel contentPanel, String cardName) {
        CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, cardName);
        // When switching to Appointment, EHR, or Billing panel, refresh the patients combo box
        if(cardName.equals("Appointment")) {
            appointmentPanel.refreshPatients();
        } else if(cardName.equals("EHR")) {
            ehrPanel.refreshPatients();
        } else if(cardName.equals("Billing")) {
            billingPanel.refreshPatients();
        }
    }

    // Call this after a new patient is added to refresh combos
    private void refreshPatientComboBoxes() {
        if(appointmentPanel != null) appointmentPanel.refreshPatients();
        if(ehrPanel != null) ehrPanel.refreshPatients();
        if(billingPanel != null) billingPanel.refreshPatients();
    }


    // --- Panels ---

    // Patient Registration Panel
    class PatientPanel extends JPanel {
        private JTextField nameField, ageField, genderField, contactField;
        private DefaultTableModel tableModel;

        public PatientPanel() {
            setLayout(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));

            formPanel.add(new JLabel("Name:"));
            nameField = new JTextField();
            formPanel.add(nameField);

            formPanel.add(new JLabel("Age:"));
            ageField = new JTextField();
            formPanel.add(ageField);

            formPanel.add(new JLabel("Gender:"));
            genderField = new JTextField();
            formPanel.add(genderField);

            formPanel.add(new JLabel("Contact:"));
            contactField = new JTextField();
            formPanel.add(contactField);

            JButton registerBtn = new JButton("Register Patient");
            formPanel.add(registerBtn);

            JButton clearBtn = new JButton("Clear");
            formPanel.add(clearBtn);

            add(formPanel, BorderLayout.NORTH);

            // Table to display patients
            tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Age", "Gender", "Contact"}, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            registerBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String ageText = ageField.getText().trim();
                String gender = genderField.getText().trim();
                String contact = contactField.getText().trim();

                if(name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageText);
                    if(age <= 0) throw new NumberFormatException();
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Patient p = new Patient(name, age, gender, contact);
                patients.put(p.getId(), p);
                refreshTable();
                clearForm();
                refreshPatientComboBoxes();
                JOptionPane.showMessageDialog(this, "Patient registered with ID " + p.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
            });

            clearBtn.addActionListener(e -> clearForm());

            refreshTable();
        }

        private void refreshTable() {
            tableModel.setRowCount(0);
            for(Patient p : patients.values()) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(), p.getContact()});
            }
        }

        private void clearForm() {
            nameField.setText("");
            ageField.setText("");
            genderField.setText("");
            contactField.setText("");
        }
    }

    // Appointment Scheduling Panel
    class AppointmentPanel extends JPanel {
        private JComboBox<Patient> patientComboBox;
        private JTextField doctorField;
        private JTextField dateTimeField; // yyyy-MM-dd HH:mm
        private DefaultTableModel tableModel;

        public AppointmentPanel() {
            setLayout(new BorderLayout(10,10));

            JPanel formPanel = new JPanel(new GridLayout(4,2,5,5));

            formPanel.add(new JLabel("Select Patient:"));
            patientComboBox = new JComboBox<>();
            formPanel.add(patientComboBox);

            formPanel.add(new JLabel("Doctor Name:"));
            doctorField = new JTextField();
            formPanel.add(doctorField);

            formPanel.add(new JLabel("Appointment DateTime (yyyy-MM-dd HH:mm):"));
            dateTimeField = new JTextField();
            formPanel.add(dateTimeField);

            JButton scheduleBtn = new JButton("Schedule Appointment");
            formPanel.add(scheduleBtn);

            JButton clearBtn = new JButton("Clear");
            formPanel.add(clearBtn);

            add(formPanel, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Date & Time"},0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            scheduleBtn.addActionListener(e -> {
                Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
                if(selectedPatient == null) {
                    JOptionPane.showMessageDialog(this, "No patient selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String doctor = doctorField.getText().trim();
                String dtStr = dateTimeField.getText().trim();
                if(doctor.isEmpty() || dtStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Doctor and DateTime must be provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDateTime dateTime;
                try {
                    dateTime = LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "DateTime format invalid. Use yyyy-MM-dd HH:mm", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Appointment a = new Appointment(selectedPatient.getId(), doctor, dateTime);
                appointments.put(a.getId(), a);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Appointment scheduled with ID " + a.getId(), "Success", JOptionPane.INFORMATION_MESSAGE);
            });

            clearBtn.addActionListener(e -> clearForm());

            refreshTable();
            refreshPatients();
        }

        public void refreshPatients() {
            patientComboBox.removeAllItems();
            for(Patient p : patients.values()) {
                patientComboBox.addItem(p);
            }
            patientComboBox.setSelectedIndex(-1);
        }

        private void refreshTable() {
            tableModel.setRowCount(0);
            for(Appointment a : appointments.values()) {
                Patient p = patients.get(a.getPatientId());
                String patientName = (p != null) ? p.getName() : "Unknown";
                tableModel.addRow(new Object[]{a.getId(), patientName, a.getDoctorName(),
                        a.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))});
            }
        }

        private void clearForm() {
            doctorField.setText("");
            dateTimeField.setText("");
            patientComboBox.setSelectedIndex(-1);
        }
    }

    // Electronic Health Records Panel
    class EHRPanel extends JPanel {
        private JComboBox<Patient> patientComboBox;
        private JTextArea recordsArea;
        private JTextField newRecordField;
        private DefaultTableModel tableModel;

        public EHRPanel() {
            setLayout(new BorderLayout(10,10));

            JPanel topPanel = new JPanel(new BorderLayout(5,5));
            JPanel formPanel = new JPanel(new BorderLayout(5,5));

            JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            comboPanel.add(new JLabel("Select Patient:"));
            patientComboBox = new JComboBox<>();
            comboPanel.add(patientComboBox);
            JButton loadBtn = new JButton("Load Records");
            comboPanel.add(loadBtn);

            topPanel.add(comboPanel, BorderLayout.NORTH);

            recordsArea = new JTextArea(10, 40);
            recordsArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(recordsArea);
            topPanel.add(scrollPane, BorderLayout.CENTER);

            formPanel.add(new JLabel("Add New Record:"), BorderLayout.NORTH);
            newRecordField = new JTextField();
            formPanel.add(newRecordField, BorderLayout.CENTER);
            JButton addRecordBtn = new JButton("Add Record");
            formPanel.add(addRecordBtn, BorderLayout.EAST);

            add(topPanel, BorderLayout.CENTER);
            add(formPanel, BorderLayout.SOUTH);

            loadBtn.addActionListener(e -> loadRecords());
            addRecordBtn.addActionListener(e -> addRecord());

            refreshPatients();
        }

        public void refreshPatients() {
            patientComboBox.removeAllItems();
            for(Patient p : patients.values()) {
                patientComboBox.addItem(p);
            }
            patientComboBox.setSelectedIndex(-1);
            recordsArea.setText("");
        }

        private void loadRecords() {
            Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
            if(selectedPatient == null) {
                JOptionPane.showMessageDialog(this, "No patient selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            EHR ehr = ehrRecords.get(selectedPatient.getId());
            if(ehr == null) {
                ehr = new EHR(selectedPatient.getId());
                ehrRecords.put(selectedPatient.getId(), ehr);
            }
            StringBuilder sb = new StringBuilder();
            for(String rec : ehr.getRecords()) {
                sb.append("- ").append(rec).append("\n");
            }
            recordsArea.setText(sb.toString());
        }

        private void addRecord() {
            Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
            if(selectedPatient == null) {
                JOptionPane.showMessageDialog(this, "No patient selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String newRec = newRecordField.getText().trim();
            if(newRec.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Record cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            EHR ehr = ehrRecords.get(selectedPatient.getId());
            if(ehr == null) {
                ehr = new EHR(selectedPatient.getId());
                ehrRecords.put(selectedPatient.getId(), ehr);
            }
            ehr.addRecord(newRec);
            newRecordField.setText("");
            loadRecords();
            JOptionPane.showMessageDialog(this, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Billing and Invoicing Panel
    class BillingPanel extends JPanel {
        private JComboBox<Patient> patientComboBox;
        private JTextField amountField;
        private DefaultTableModel tableModel;
        private JTable table;

        public BillingPanel() {
            setLayout(new BorderLayout(10,10));

            JPanel formPanel = new JPanel(new GridLayout(3,2,5,5));

            formPanel.add(new JLabel("Select Patient:"));
            patientComboBox = new JComboBox<>();
            formPanel.add(patientComboBox);

            formPanel.add(new JLabel("Amount:"));
            amountField = new JTextField();
            formPanel.add(amountField);

            JButton addBillBtn = new JButton("Add Bill");
            formPanel.add(addBillBtn);

            JButton clearBtn = new JButton("Clear");
            formPanel.add(clearBtn);

            add(formPanel, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"Bill ID", "Patient", "Amount", "Date", "Paid"}, 0);
            table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            addBillBtn.addActionListener(e -> {
                Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
                if(selectedPatient == null) {
                    JOptionPane.showMessageDialog(this, "No patient selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String amountText = amountField.getText().trim();
                if(amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Amount must be provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double amount;
                try {
                    amount = Double.parseDouble(amountText);
                    if(amount <= 0) throw new NumberFormatException();
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Amount must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Billing bill = new Billing(selectedPatient.getId(), amount);
                bills.put(bill.getBillId(), bill);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Bill added successfully with ID " + bill.getBillId(), "Success", JOptionPane.INFORMATION_MESSAGE);
            });

            clearBtn.addActionListener(e -> clearForm());

            refreshPatients();
            refreshTable();
        }

        public void refreshPatients() {
            patientComboBox.removeAllItems();
            for(Patient p : patients.values()) {
                patientComboBox.addItem(p);
            }
            patientComboBox.setSelectedIndex(-1);
        }

        private void refreshTable() {
            tableModel.setRowCount(0);
            for(Billing b : bills.values()) {
                Patient p = patients.get(b.getPatientId());
                String patientName = (p != null) ? p.getName() : "Unknown";
                tableModel.addRow(new Object[]{
                        b.getBillId(),
                        patientName,
                        String.format("%.2f", b.getAmount()),
                        b.getBillingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        b.isPaid() ? "Yes" : "No"
                });
            }
        }

        private void clearForm() {
            amountField.setText("");
            patientComboBox.setSelectedIndex(-1);
        }
    }

    // Inventory Management Panel
    class InventoryPanel extends JPanel {
        private JTextField nameField, quantityField, unitField;
        private DefaultTableModel tableModel;

        public InventoryPanel() {
            setLayout(new BorderLayout(10,10));

            JPanel formPanel = new JPanel(new GridLayout(4,2,5,5));

            formPanel.add(new JLabel("Item Name:"));
            nameField = new JTextField();
            formPanel.add(nameField);

            formPanel.add(new JLabel("Quantity:"));
            quantityField = new JTextField();
            formPanel.add(quantityField);

            formPanel.add(new JLabel("Unit (e.g., pcs, boxes):"));
            unitField = new JTextField();
            formPanel.add(unitField);

            JButton addItemBtn = new JButton("Add Item");
            formPanel.add(addItemBtn);

            JButton clearBtn = new JButton("Clear");
            formPanel.add(clearBtn);

            add(formPanel, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"Item ID", "Name", "Quantity", "Unit"}, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            addItemBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String quantityText = quantityField.getText().trim();
                String unit = unitField.getText().trim();

                if(name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityText);
                    if(quantity <= 0) throw new NumberFormatException();
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantity must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                InventoryItem item = new InventoryItem(name, quantity, unit);
                inventoryItems.put(item.getItemId(), item);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Inventory item added with ID " + item.getItemId(), "Success", JOptionPane.INFORMATION_MESSAGE);
            });

            clearBtn.addActionListener(e -> clearForm());

            refreshTable();
        }

        private void refreshTable() {
            tableModel.setRowCount(0);
            for(InventoryItem item : inventoryItems.values()) {
                tableModel.addRow(new Object[]{item.getItemId(), item.getName(), item.getQuantity(), item.getUnit()});
            }
        }

        private void clearForm() {
            nameField.setText("");
            quantityField.setText("");
            unitField.setText("");
        }
    }

    // Staff Management Panel
    class StaffPanel extends JPanel {
        private JTextField nameField, roleField, contactField;
        private DefaultTableModel tableModel;

        public StaffPanel() {
            setLayout(new BorderLayout(10,10));

            JPanel formPanel = new JPanel(new GridLayout(4,2,5,5));

            formPanel.add(new JLabel("Name:"));
            nameField = new JTextField();
            formPanel.add(nameField);

            formPanel.add(new JLabel("Role:"));
            roleField = new JTextField();
            formPanel.add(roleField);

            formPanel.add(new JLabel("Contact:"));
            contactField = new JTextField();
            formPanel.add(contactField);

            JButton addStaffBtn = new JButton("Add Staff");
            formPanel.add(addStaffBtn);

            JButton clearBtn = new JButton("Clear");
            formPanel.add(clearBtn);

            add(formPanel, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"Staff ID", "Name", "Role", "Contact"}, 0);
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            addStaffBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String role = roleField.getText().trim();
                String contact = contactField.getText().trim();

                if(name.isEmpty() || role.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Staff s = new Staff(name, role, contact);
                staffMembers.put(s.getStaffId(), s);
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Staff member added with ID " + s.getStaffId(), "Success", JOptionPane.INFORMATION_MESSAGE);
            });

            clearBtn.addActionListener(e -> clearForm());

            refreshTable();
        }

        private void refreshTable() {
            tableModel.setRowCount(0);
            for(Staff s : staffMembers.values()) {
                tableModel.addRow(new Object[]{s.getStaffId(), s.getName(), s.getRole(), s.getContact()});
            }
        }

        private void clearForm() {
            nameField.setText("");
            roleField.setText("");
            contactField.setText("");
        }
    }
}
