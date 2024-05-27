// This application, built using Java and JFrame, is designed to manage aid donations and requests for a non-governmental organization (NGO).
// It provides different user interfaces based on user roles such as Donor, Indigent Person, Operation Coordinator, and System Administrator.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NGOAidManagementSystem {
    private static JFrame frame;
    private static HashMap<String, String> userCredentials = new HashMap<>();
    private static HashMap<String, String> userRoles = new HashMap<>();

    public static void main(String[] args) {
        readCredentials();
        showLoginScreen();
    }

    private static void readCredentials() {
        try (BufferedReader br = new BufferedReader(new FileReader("infos.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    userCredentials.put(parts[0], parts[1]);
                    userRoles.put(parts[0], parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveCredentials(String username, String password, String role) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("infos.txt", true))) {
            bw.write(username + "," + password + "," + role);
            bw.newLine();
            userCredentials.put(username, password);
            userRoles.put(username, role);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveCandidate(String username, String password, String role, String fullName, String birthDate, String email) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("candidates.txt", true))) {
            bw.write(username + "," + password + "," + role + "," + fullName + "," + birthDate + "," + email);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void showLoginScreen() {
        frame = new JFrame("AidFlow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); 
    
        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        // Add a label for the title
        JLabel titleLabel = new JLabel("AidFlow", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Create the login form panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JTextField userText = new JTextField();
        userText.setPreferredSize(new Dimension(200, 30));
        userText.setMaximumSize(new Dimension(Integer.MAX_VALUE, userText.getPreferredSize().height));
    
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JPasswordField passText = new JPasswordField();
        passText.setPreferredSize(new Dimension(200, 30));
        passText.setMaximumSize(new Dimension(Integer.MAX_VALUE, passText.getPreferredSize().height));
    
        // Add components to the login panel
        loginPanel.add(userLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between components
        loginPanel.add(userText);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(passLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(passText);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Serif", Font.PLAIN, 20));
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Serif", Font.PLAIN, 20));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
    
        // Add action listeners
        loginButton.addActionListener(e -> handleLogin(userText.getText(), new String(passText.getPassword())));
        registerButton.addActionListener(e -> showRegistrationScreen());
    
        // Add panels to the main panel
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
    

    private static void handleLogin(String username, String password) {
        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            JOptionPane.showMessageDialog(frame, "You have successfully logged in");
            frame.dispose();
            showUserInterface(username);
        } else if (isPendingApproval(username)) {
            JOptionPane.showMessageDialog(frame, "Your account is pending approval by an administrator.");
        } else {
            JOptionPane.showMessageDialog(frame, "Please check your information");
        }
    }
    
    private static boolean isPendingApproval(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("candidates.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    

    private static void showRegistrationScreen() {
        JFrame registerFrame = new JFrame("AidFlow - User Registration");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(1200, 800);
    
        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
    
        // Add a label for the title
        JLabel titleLabel = new JLabel("AidFlow - User Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Create the registration form panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JTextField userText = new JTextField();
        userText.setPreferredSize(new Dimension(200, 30));
        userText.setMaximumSize(new Dimension(Integer.MAX_VALUE, userText.getPreferredSize().height));
    
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JPasswordField passText = new JPasswordField();
        passText.setPreferredSize(new Dimension(200, 30));
        passText.setMaximumSize(new Dimension(Integer.MAX_VALUE, passText.getPreferredSize().height));
    
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JTextField nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(200, 30));
        nameText.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameText.getPreferredSize().height));
    
        JLabel birthDateLabel = new JLabel("Birth Date (DD/MM/YYYY):");
        birthDateLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JTextField birthDateText = new JTextField();
        birthDateText.setPreferredSize(new Dimension(200, 30));
        birthDateText.setMaximumSize(new Dimension(Integer.MAX_VALUE, birthDateText.getPreferredSize().height));
    
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        JTextField emailText = new JTextField();
        emailText.setPreferredSize(new Dimension(200, 30));
        emailText.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailText.getPreferredSize().height));
    
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        String[] roles = {"Donor", "IndigentPerson", "OperationCoordinator", "SystemAdministrator"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(new Dimension(200, 30));
        roleComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, roleComboBox.getPreferredSize().height));
    
        // Add components to the registration panel
        registerPanel.add(userLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between components
        registerPanel.add(userText);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(passLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        registerPanel.add(passText);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(nameLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        registerPanel.add(nameText);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(birthDateLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        registerPanel.add(birthDateText);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(emailLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        registerPanel.add(emailText);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        registerPanel.add(roleLabel);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        registerPanel.add(roleComboBox);
        registerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Serif", Font.PLAIN, 20));
        JButton goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Serif", Font.PLAIN, 20));
        buttonPanel.add(registerButton);
        buttonPanel.add(goBackButton);
    
        // Add action listeners
        registerButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());
            String fullName = nameText.getText();
            String birthDate = birthDateText.getText();
            String email = emailText.getText();
            String role = (String) roleComboBox.getSelectedItem();
            if (!username.isEmpty() && !password.isEmpty() && !fullName.isEmpty() && !birthDate.isEmpty() && !email.isEmpty()) {
                if (role.equals("Donor") || role.equals("OperationCoordinator") || role.equals("SystemAdministrator")) {
                    saveCandidate(username, password, role, fullName, birthDate, email);
                    JOptionPane.showMessageDialog(registerFrame, "Registration successful. Your account is pending approval.");
                } else {
                    saveCredentials(username, password, role);
                    JOptionPane.showMessageDialog(registerFrame, "Registration successful");
                }
                registerFrame.dispose();
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(registerFrame, "All fields must be filled");
            }
        });
    
        goBackButton.addActionListener(e -> {
            registerFrame.dispose();
            showLoginScreen();
        });
    
        // Add panels to the main panel
        mainPanel.add(registerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        registerFrame.getContentPane().add(mainPanel);
        registerFrame.setVisible(true);
    }
    

    private static void showUserInterface(String username) {
        String role = getRole(username);

        switch (role) {
            case "Donor":
                showDonorInterface(username);
                break;
            case "IndigentPerson":
                showIndigentPersonInterface(username);
                break;
            case "OperationCoordinator":
                showOperationCoordinatorInterface(username);
                break;
            case "SystemAdministrator":
                showAdminInterface(username);
                break;
            default:
                JOptionPane.showMessageDialog(frame, "Unknown role.");
                break;
        }
    }

    private static String getRole(String username) {
        return userRoles.getOrDefault(username, "Unknown");
    }

    private static void showDonorInterface(String username) {
        JFrame donorFrame = new JFrame("Donor Interface");
        donorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        donorFrame.setSize(1200, 600);

        JButton donateButton = new JButton("Donate Aid");
        JButton viewHistoryButton = new JButton("View Donation History");
        JButton goBackButton = new JButton("Go Back");

        donateButton.addActionListener(e -> donateAid(username));
        viewHistoryButton.addActionListener(e -> viewDonationHistory(username));
        goBackButton.addActionListener(e -> {
            donorFrame.dispose();
            showLoginScreen();
        });

        JPanel panel = new JPanel();
        panel.add(donateButton);
        panel.add(viewHistoryButton);
        panel.add(goBackButton);

        donorFrame.getContentPane().add(BorderLayout.CENTER, panel);
        donorFrame.setVisible(true);
    }

    private static void donateAid(String username) {
        JFrame donateFrame = new JFrame("Donate Aid");
        donateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        donateFrame.setSize(600, 600);
    
        JPanel panel = new JPanel(new GridLayout(12, 2));
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameText = new JTextField();
        JLabel contactLabel = new JLabel("Contact Information (Email or Phone):");
        JTextField contactText = new JTextField();
        JLabel typeLabel = new JLabel("Type of Donation:");
        String[] donationTypes = {"Financial Donation", "Goods", "Services"};
        JComboBox<String> typeComboBox = new JComboBox<>(donationTypes);
        JLabel descriptionLabel = new JLabel("Description of Donation (if applicable):");
        JTextField descriptionText = new JTextField();
        JLabel amountLabel = new JLabel("Donation Amount $ (if applicable):");
        JTextField amountText = new JTextField();
        JLabel ccInfoLabel = new JLabel("Credit Card Information:");
        JTextField ccInfoText = new JTextField();
        JLabel billingAddressLabel = new JLabel("Billing Address:");
        JTextField billingAddressText = new JTextField();
    
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(contactLabel);
        panel.add(contactText);
        panel.add(typeLabel);
        panel.add(typeComboBox);
        panel.add(descriptionLabel);
        panel.add(descriptionText);
        panel.add(amountLabel);
        panel.add(amountText);
        panel.add(ccInfoLabel);
        panel.add(ccInfoText);
        panel.add(billingAddressLabel);
        panel.add(billingAddressText);
    
        JButton donateButton = new JButton("Donate");
        donateButton.addActionListener(e -> {
            String name = nameText.getText();
            String contact = contactText.getText();
            String type = (String) typeComboBox.getSelectedItem();
            String description = descriptionText.getText();
            String amountStr = amountText.getText();
            String ccInfo = ccInfoText.getText();
            String billingAddress = billingAddressText.getText();
    
            if (!name.isEmpty() && !contact.isEmpty() && !billingAddress.isEmpty()) {
                // Ensure the donation amount is a valid number if provided
                if (!amountStr.isEmpty()) {
                    try {
                        // Remove the dollar sign if present and parse the amount
                        Integer.parseInt(amountStr.replace("$", "").trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(donateFrame, "Donation amount must be a valid number.");
                        return;
                    }
                }
    
                // Check if the donation type is financial and ensure credit card information is provided
                if (type.equals("Financial Donation") && ccInfo.isEmpty()) {
                    JOptionPane.showMessageDialog(donateFrame, "Credit Card Information is required for financial donations.");
                    return;
                }
    
                String amount = amountStr.isEmpty() ? "0" : amountStr.replace("$", "").trim(); // Remove the dollar sign if present
                saveDonation(username, name, contact, type, description, amount, ccInfo, billingAddress);
                JOptionPane.showMessageDialog(donateFrame, "Thank you for your donation!");
                donateFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(donateFrame, "Full Name, Contact Information, and Billing Address are required.");
            }
        });
    
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(donateButton);
    
        donateFrame.getContentPane().add(BorderLayout.CENTER, panel);
        donateFrame.setVisible(true);
    }
    
    
    
    
    private static void saveDonation(String username, String name, String contact, String type, String description, String amount, String ccInfo, String billingAddress) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("donations.txt", true))) {
            bw.write(username + "," + name + "," + contact + "," + type + "," + description + "," + amount + "," + ccInfo + "," + billingAddress);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    
    private static void viewDonationHistory(String username) {
        JFrame historyFrame = new JFrame("Donation History");
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(1200, 600);
    
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setFont(new Font("Serif", Font.PLAIN, 24));
        textArea.setEditable(false);  // Make the text area non-editable
        textArea.setText("Donation history for " + username + ":\n\n");
    
        try (BufferedReader br = new BufferedReader(new FileReader("donations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);  // Include trailing empty strings
                if (parts.length == 8 && parts[0].equals(username)) {  // Ensure the line has the correct number of fields and belongs to the user
                    String formattedDonation = String.format(
                        "Full Name: %s\nContact Information: %s\nType of Donation: %s\nDescription of Donation: %s\n" +
                        "Donation Amount: %s\nCredit Card Information: %s\nBilling Address: %s\n\n",
                        parts[1], parts[2], parts[3], parts[4].isEmpty() ? "N/A" : parts[4],
                        parts[5].isEmpty() ? "N/A" : parts[5], parts[6], parts[7]
                    );
                    textArea.append(formattedDonation);
                    textArea.append("--------------------------------------------------------\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.append("No donation history found.");
        }
    
        historyFrame.getContentPane().add(new JScrollPane(textArea));
        historyFrame.setVisible(true);
    }    
    

    private static void showIndigentPersonInterface(String username) {
        JFrame indigentFrame = new JFrame("Indigent Person Interface");
        indigentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        indigentFrame.setSize(1200, 600);

        JButton requestAidButton = new JButton("Apply to Receive Aid");
        JButton viewStatusButton = new JButton("View Aid Request Status");
        JButton goBackButton = new JButton("Go Back");

        requestAidButton.addActionListener(e -> applyForAid(username));
        viewStatusButton.addActionListener(e -> viewAidRequestStatus(username));
        goBackButton.addActionListener(e -> {
            indigentFrame.dispose();
            showLoginScreen();
        });

        JPanel panel = new JPanel();
        panel.add(requestAidButton);
        panel.add(viewStatusButton);
        panel.add(goBackButton);

        indigentFrame.getContentPane().add(BorderLayout.CENTER, panel);
        indigentFrame.setVisible(true);
    }

    private static void viewAidRequestStatus(String username) {
        JFrame statusFrame = new JFrame("Aid Request Status");
        statusFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statusFrame.setSize(1200, 600);
    
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setFont(new Font("Serif", Font.PLAIN, 24));
        textArea.setEditable(false); 
        textArea.setText("Aid Request Status for " + username + ":\n\n");
    
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    String formattedStatus = String.format(
                        "Username: %s\nName: %s\nSurname: %s\nAge: %s\nProfession: %s\nMonthly Income: %s\n" +
                        "Household Size: %s\nNumber of Children: %s\nEducation Level: %s\nStatus: %s\n\n",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9]
                    );
                    textArea.append(formattedStatus);
                    textArea.append("--------------------------------------------------------\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.append("No aid requests found.");
        }
    
        statusFrame.getContentPane().add(new JScrollPane(textArea));
        statusFrame.setVisible(true);
    }
    

    private static void applyForAid(String username) {
        JFrame aidFrame = new JFrame("Apply to Receive Aid");
        aidFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aidFrame.setSize(1200, 600);

        JPanel panel = new JPanel(new GridLayout(9, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameText = new JTextField();
        JLabel surnameLabel = new JLabel("Surname:");
        JTextField surnameText = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageText = new JTextField();
        JLabel professionLabel = new JLabel("Profession:");
        JTextField professionText = new JTextField();
        JLabel incomeLabel = new JLabel("Monthly Income:");
        JTextField incomeText = new JTextField();
        JLabel householdLabel = new JLabel("Household Size:");
        JTextField householdText = new JTextField();
        JLabel childrenLabel = new JLabel("Number of Children:");
        JTextField childrenText = new JTextField();
        JLabel educationLabel = new JLabel("Education Level:");
        String[] educationLevels = {"No formal education", "Elementary School", "Highschool", "College", "Graduate"};
        JComboBox<String> educationComboBox = new JComboBox<>(educationLevels);

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(surnameLabel);
        panel.add(surnameText);
        panel.add(ageLabel);
        panel.add(ageText);
        panel.add(professionLabel);
        panel.add(professionText);
        panel.add(incomeLabel);
        panel.add(incomeText);
        panel.add(householdLabel);
        panel.add(householdText);
        panel.add(childrenLabel);
        panel.add(childrenText);
        panel.add(educationLabel);
        panel.add(educationComboBox);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            String name = nameText.getText();
            String surname = surnameText.getText();
            String age = ageText.getText();
            String profession = professionText.getText();
            String income = incomeText.getText();
            String household = householdText.getText();
            String children = childrenText.getText();
            String education = (String) educationComboBox.getSelectedItem();
            if (!name.isEmpty() && !surname.isEmpty() && !age.isEmpty() && !profession.isEmpty() &&
                    !income.isEmpty() && !household.isEmpty() && !children.isEmpty() && !education.isEmpty()) {
                saveAidRequest(username, name, surname, age, profession, income, household, children, education);
                JOptionPane.showMessageDialog(aidFrame, "Your help request has been successfully submitted. Our team will review your application and get back to you as soon as possible.");
                aidFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(aidFrame, "All fields must be filled");
            }
        });

        aidFrame.getContentPane().add(BorderLayout.CENTER, panel);
        aidFrame.getContentPane().add(BorderLayout.SOUTH, applyButton);
        aidFrame.setVisible(true);
    }

    private static void saveAidRequest(String username, String name, String surname, String age, String profession,
                                       String income, String household, String children, String education) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("aid_requests.txt", true))) {
            bw.write(username + "," + name + "," + surname + "," + age + "," + profession + "," + income + "," +
                    household + "," + children + "," + education + ",Pending");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showOperationCoordinatorInterface(String username) {
        JFrame coordinatorFrame = new JFrame("Operation Coordinator Interface");
        coordinatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        coordinatorFrame.setSize(1200, 600);

        JButton scheduleButton = new JButton("Schedule Aid Operations");
        JButton reviewBacklogButton = new JButton("Review Aid Request Backlog");
        JButton goBackButton = new JButton("Go Back");

        scheduleButton.addActionListener(e -> scheduleAidOperations(username));
        reviewBacklogButton.addActionListener(e -> reviewAidRequestBacklog(username));
        goBackButton.addActionListener(e -> {
            coordinatorFrame.dispose();
            showLoginScreen();
        });

        JPanel panel = new JPanel();
        panel.add(scheduleButton);
        panel.add(reviewBacklogButton);
        panel.add(goBackButton);

        coordinatorFrame.getContentPane().add(BorderLayout.CENTER, panel);
        coordinatorFrame.setVisible(true);
    }

    private static void scheduleAidOperations(String username) {
        JFrame scheduleFrame = new JFrame("Schedule Aid Operations");
        scheduleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        scheduleFrame.setSize(1200, 600);
    
        JPanel panel = new JPanel(new BorderLayout());
        JPanel requestsPanel = new JPanel();
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
    
        JScrollPane scrollPane = new JScrollPane(requestsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        ArrayList<JTextField> dateFields = new ArrayList<>();
        ArrayList<String> approvedRequests = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);  // Include trailing empty strings
                if (parts.length == 10 && parts[9].startsWith("Approved")) {  // Ensure the line has the correct number of fields and is approved
                    JPanel requestPanel = new JPanel(new BorderLayout());
    
                    String formattedRequest = String.format(
                        "<html><b>Username:</b> %s<br><b>Name:</b> %s<br><b>Surname:</b> %s<br><b>Age:</b> %s<br>" +
                        "<b>Profession:</b> %s<br><b>Monthly Income:</b> %s<br><b>Household Size:</b> %s<br>" +
                        "<b>Number of Children:</b> %s<br><b>Education Level:</b> %s<br><b>Status:</b> %s</html>",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9]
                    );
    
                    JLabel requestLabel = new JLabel(formattedRequest);
                    JTextField dateField = new JTextField("Enter date (DD/MM/YYYY)");
                    JButton scheduleButton = new JButton("Schedule");
    
                    dateFields.add(dateField);
                    approvedRequests.add(line);
    
                    String currentLine = line; // effectively final variable for use in lambda
                    scheduleButton.addActionListener(e -> scheduleAidOperation(currentLine, dateField.getText()));
    
                    JPanel inputPanel = new JPanel();
                    inputPanel.add(dateField);
                    inputPanel.add(scheduleButton);
    
                    requestPanel.add(requestLabel, BorderLayout.CENTER);
                    requestPanel.add(inputPanel, BorderLayout.SOUTH);
                    requestsPanel.add(requestPanel);
    
                    // Add a separator line
                    requestsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        scheduleFrame.getContentPane().add(panel);
        scheduleFrame.setVisible(true);
    }
    
    private static void scheduleAidOperation(String requestLine, String date) {
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            ArrayList<String> requests = new ArrayList<>();
            String line;
    
            while ((line = br.readLine()) != null) {
                if (line.equals(requestLine)) {
                    line = line.replace("Approved", "Scheduled on " + date);
                }
                requests.add(line);
            }
    
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("aid_requests.txt"))) {
                for (String request : requests) {
                    bw.write(request);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void reviewAidRequestBacklog(String username) {
        JFrame backlogFrame = new JFrame("Aid Request Backlog");
        backlogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        backlogFrame.setSize(1200, 600);
    
        JPanel panel = new JPanel(new BorderLayout());
        JPanel requestsPanel = new JPanel();
        requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
    
        JScrollPane scrollPane = new JScrollPane(requestsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        ArrayList<JCheckBox> approveCheckBoxes = new ArrayList<>();
        ArrayList<JCheckBox> rejectCheckBoxes = new ArrayList<>();
        ArrayList<String> requestLines = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);  // Include trailing empty strings
                if (parts.length == 10) {  // Ensure the line has the correct number of fields
                    JPanel requestPanel = new JPanel(new BorderLayout());
    
                    String formattedRequest = String.format(
                        "<html><b>Username:</b> %s<br><b>Name:</b> %s<br><b>Surname:</b> %s<br><b>Age:</b> %s<br>" +
                        "<b>Profession:</b> %s<br><b>Monthly Income:</b> %s<br><b>Household Size:</b> %s<br>" +
                        "<b>Number of Children:</b> %s<br><b>Education Level:</b> %s<br><b>Status:</b> %s</html>",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9]
                    );
    
                    JLabel requestLabel = new JLabel(formattedRequest);
                    JCheckBox approveCheckBox = new JCheckBox("Approve");
                    JCheckBox rejectCheckBox = new JCheckBox("Reject");
    
                    if (!parts[9].equals("Pending")) {
                        approveCheckBox.setVisible(false);
                        rejectCheckBox.setVisible(false);
                    }
    
                    approveCheckBoxes.add(approveCheckBox);
                    rejectCheckBoxes.add(rejectCheckBox);
                    requestLines.add(line);
    
                    JPanel checkBoxPanel = new JPanel();
                    checkBoxPanel.add(approveCheckBox);
                    checkBoxPanel.add(rejectCheckBox);
    
                    requestPanel.add(requestLabel, BorderLayout.CENTER);
                    requestPanel.add(checkBoxPanel, BorderLayout.EAST);
                    requestsPanel.add(requestPanel);
    
                    // Add a separator line
                    requestsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            updateAidRequestStatus(requestLines, approveCheckBoxes, rejectCheckBoxes);
            backlogFrame.dispose();
            reviewAidRequestBacklog(username);
        });
    
        panel.add(submitButton, BorderLayout.SOUTH);
    
        backlogFrame.getContentPane().add(panel);
        backlogFrame.setVisible(true);
    }
    
    
    private static void updateAidRequestStatus(ArrayList<String> requestLines, ArrayList<JCheckBox> approveCheckBoxes, ArrayList<JCheckBox> rejectCheckBoxes) {
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            ArrayList<String> requests = new ArrayList<>();
            String line;
            int index = 0;
    
            while ((line = br.readLine()) != null) {
                if (index < requestLines.size()) {
                    String updatedLine = requestLines.get(index);
                    if (approveCheckBoxes.get(index).isSelected()) {
                        updatedLine = updatedLine.replace("Pending", "Approved");
                    } else if (rejectCheckBoxes.get(index).isSelected()) {
                        updatedLine = updatedLine.replace("Pending", "Rejected");
                    }
                    requests.add(updatedLine);
                    index++;
                } else {
                    requests.add(line);
                }
            }
    
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("aid_requests.txt"))) {
                for (String request : requests) {
                    bw.write(request);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void showAdminInterface(String username) {
        JFrame adminFrame = new JFrame("Admin Interface");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(1200, 600);
    
        JButton manageUsersButton = new JButton("Manage Users");
        JButton manageCandidatesButton = new JButton("Manage User Candidates");
        JButton viewStatsButton = new JButton("View Dashboard Statistics");
        JButton goBackButton = new JButton("Go Back");
    
        manageUsersButton.addActionListener(e -> manageUsers(username));
        manageCandidatesButton.addActionListener(e -> manageUserCandidates());
        viewStatsButton.addActionListener(e -> viewDashboardStatistics(username));
        goBackButton.addActionListener(e -> {
            adminFrame.dispose();
            showLoginScreen();
        });
    
        JPanel panel = new JPanel();
        panel.add(manageUsersButton);
        panel.add(manageCandidatesButton);
        panel.add(viewStatsButton);
        panel.add(goBackButton);
    
        adminFrame.getContentPane().add(BorderLayout.CENTER, panel);
        adminFrame.setVisible(true);
    }
    
    

    private static void showAdminRegistrationScreen(JFrame manageFrame) {
        JFrame registerFrame = new JFrame("Admin User Registration");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(1200, 600);
    
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        userText.setPreferredSize(new Dimension(200, 24));
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        passText.setPreferredSize(new Dimension(200, 24));
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Donor", "IndigentPerson", "OperationCoordinator", "SystemAdministrator"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
    
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
        panel.add(roleLabel);
        panel.add(roleComboBox);
    
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        JButton goBackButton = new JButton("Go Back");
    
        registerButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            if (!username.isEmpty() && !password.isEmpty()) {
                saveCredentials(username, password, role);
                JOptionPane.showMessageDialog(registerFrame, "Registration successful");
                registerFrame.dispose();
                manageFrame.dispose(); // Close the previous manage users frame
                manageUsers(username); // Refresh the manage users frame
            } else {
                JOptionPane.showMessageDialog(registerFrame, "Username and Password cannot be empty");
            }
        });
    
        goBackButton.addActionListener(e -> {
            registerFrame.dispose();
        });
    
        buttonPanel.add(registerButton);
        buttonPanel.add(goBackButton);
    
        registerFrame.getContentPane().add(BorderLayout.CENTER, panel);
        registerFrame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        registerFrame.setVisible(true);
    }
    
    private static void manageUserCandidates() {
        JFrame candidatesFrame = new JFrame("Manage User Candidates");
        candidatesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        candidatesFrame.setSize(1200, 600);
    
        JPanel panel = new JPanel(new BorderLayout());
        JPanel candidatesPanel = new JPanel();
        candidatesPanel.setLayout(new BoxLayout(candidatesPanel, BoxLayout.Y_AXIS));
    
        JScrollPane scrollPane = new JScrollPane(candidatesPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        JPanel headerPanel = new JPanel(new GridLayout(1, 8));
        headerPanel.add(new JLabel("Username"));
        headerPanel.add(new JLabel("Role"));
        headerPanel.add(new JLabel("Full Name"));
        headerPanel.add(new JLabel("Birth Date"));
        headerPanel.add(new JLabel("Email"));
        headerPanel.add(new JLabel("Approve"));
        headerPanel.add(new JLabel("Reject"));
        candidatesPanel.add(headerPanel);
        candidatesPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    
        ArrayList<JButton> approveButtons = new ArrayList<>();
        ArrayList<JButton> rejectButtons = new ArrayList<>();
        ArrayList<String> candidateLines = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader("candidates.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {  // Ensure the line has the correct number of fields
                    JPanel candidatePanel = new JPanel(new GridLayout(1, 8));
    
                    JLabel usernameLabel = new JLabel(parts[0]);
                    JLabel roleLabel = new JLabel(parts[2]);
                    JLabel fullNameLabel = new JLabel(parts[3]);
                    JLabel birthDateLabel = new JLabel(parts[4]);
                    JLabel emailLabel = new JLabel(parts[5]);
                    JButton approveButton = new JButton("Approve");
                    JButton rejectButton = new JButton("Reject");
    
                    approveButtons.add(approveButton);
                    rejectButtons.add(rejectButton);
                    candidateLines.add(line);
    
                    approveButton.addActionListener(e -> approveCandidate(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], candidatesFrame));
                    rejectButton.addActionListener(e -> rejectCandidate(parts[0], candidatesFrame));
    
                    candidatePanel.add(usernameLabel);
                    candidatePanel.add(roleLabel);
                    candidatePanel.add(fullNameLabel);
                    candidatePanel.add(birthDateLabel);
                    candidatePanel.add(emailLabel);
                    candidatePanel.add(approveButton);
                    candidatePanel.add(rejectButton);
    
                    candidatesPanel.add(candidatePanel);
                    candidatesPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        candidatesFrame.getContentPane().add(panel);
        candidatesFrame.setVisible(true);
    }
    
    
    private static void approveCandidate(String username, String password, String role, String fullName, String birthDate, String email, JFrame candidatesFrame) {
        saveCredentials(username, password, role);
        removeCandidate(username);
        candidatesFrame.dispose();
        manageUserCandidates();
    }
    
    
    private static void rejectCandidate(String username, JFrame candidatesFrame) {
        removeCandidate(username);
        candidatesFrame.dispose();
        manageUserCandidates();
    }
    
    private static void removeCandidate(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("candidates.txt"))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[0].equals(username)) {
                    lines.add(line);
                }
            }
    
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("candidates.txt"))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void manageUsers(String username) {
        JFrame manageFrame = new JFrame("Manage Users");
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        manageFrame.setSize(1200, 600);
    
        JPanel panel = new JPanel(new BorderLayout());
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
    
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        JPanel headerPanel = new JPanel(new GridLayout(1, 6));
        headerPanel.add(new JLabel("Username"));
        headerPanel.add(new JLabel("Role"));
        headerPanel.add(new JLabel("Change Role"));
        headerPanel.add(new JLabel("Change Password"));
        headerPanel.add(new JLabel("Remove User"));
        usersPanel.add(headerPanel);
        usersPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    
        ArrayList<JComboBox<String>> roleComboBoxes = new ArrayList<>();
        ArrayList<JButton> passwordButtons = new ArrayList<>();
        ArrayList<JButton> removeButtons = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader("infos.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {  // Ensure the line has the correct number of fields
                    JPanel userPanel = new JPanel(new GridLayout(1, 6));
    
                    JLabel usernameLabel = new JLabel(parts[0]);
                    String[] roles = {"Donor", "IndigentPerson", "OperationCoordinator", "SystemAdministrator"};
                    JComboBox<String> roleComboBox = new JComboBox<>(roles);
                    roleComboBox.setSelectedItem(parts[2]);
                    JButton changeRoleButton = new JButton("Change Role");
                    JButton changePasswordButton = new JButton("Change Password");
                    JButton removeUserButton = new JButton("Remove User");
    
                    roleComboBoxes.add(roleComboBox);
                    passwordButtons.add(changePasswordButton);
                    removeButtons.add(removeUserButton);
                    usernames.add(parts[0]);
    
                    changeRoleButton.addActionListener(e -> changeUserRole(parts[0], (String) roleComboBox.getSelectedItem()));
                    changePasswordButton.addActionListener(e -> changeUserPassword(parts[0]));
                    removeUserButton.addActionListener(e -> removeUser(parts[0], manageFrame));
    
                    userPanel.add(usernameLabel);
                    userPanel.add(new JLabel(parts[2]));  // Display current role
                    userPanel.add(roleComboBox);
                    userPanel.add(changeRoleButton);
                    userPanel.add(changePasswordButton);
                    userPanel.add(removeUserButton);
    
                    usersPanel.add(userPanel);
                    usersPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        JPanel bottomPanel = new JPanel();
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> showAdminRegistrationScreen(manageFrame));
        bottomPanel.add(addUserButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
    
        manageFrame.getContentPane().add(panel);
        manageFrame.setVisible(true);
    }
    

    private static void changeUserRole(String username, String newRole) {
        try (BufferedReader br = new BufferedReader(new FileReader("infos.txt"))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    parts[2] = newRole;
                }
                lines.add(String.join(",", parts));
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("infos.txt"))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void changeUserPassword(String username) {
        JFrame passwordFrame = new JFrame("Change Password");
        passwordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        passwordFrame.setSize(1200, 600);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel passLabel = new JLabel("New Password:");
        JPasswordField passText = new JPasswordField();
        JButton changeButton = new JButton("Change Password");

        changeButton.addActionListener(e -> {
            String newPassword = new String(passText.getPassword());
            if (!newPassword.isEmpty()) {
                updatePassword(username, newPassword);
                JOptionPane.showMessageDialog(passwordFrame, "Password changed successfully");
                passwordFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(passwordFrame, "Password cannot be empty");
            }
        });

        panel.add(passLabel);
        panel.add(passText);
        panel.add(new JLabel());
        panel.add(changeButton);

        passwordFrame.getContentPane().add(panel);
        passwordFrame.setVisible(true);
    }

    private static void updatePassword(String username, String newPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader("infos.txt"))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    parts[1] = newPassword;
                }
                lines.add(String.join(",", parts));
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("infos.txt"))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeUser(String username, JFrame manageFrame) {
        int confirmation = JOptionPane.showConfirmDialog(manageFrame, "Are you sure you want to remove this user?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader("infos.txt"))) {
                ArrayList<String> lines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (!parts[0].equals(username)) {
                        lines.add(line);
                    }
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("infos.txt"))) {
                    for (String l : lines) {
                        bw.write(l);
                        bw.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            manageFrame.dispose();
            manageUsers(username);
        }
    }

    private static void viewDashboardStatistics(String username) {
        JFrame statsFrame = new JFrame("Dashboard Statistics");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setSize(1200, 600);
    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    
        // Donations Section
        JPanel donationsPanel = new JPanel();
        donationsPanel.setLayout(new BoxLayout(donationsPanel, BoxLayout.Y_AXIS));
        donationsPanel.setBorder(BorderFactory.createTitledBorder("Donations"));
    
        int totalDonations = 0;
        HashMap<String, Integer> donationTypes = new HashMap<>();
        int totalDonationAmount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("donations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 8) { // Ensure the line has the correct number of fields
                    totalDonations++;
                    String donationType = parts[3];
                    int amount = 0;
                    if (!parts[5].isEmpty()) {
                        amount = Integer.parseInt(parts[5].replace("$", "").trim()); // Parse amount as an integer after removing the dollar sign
                    }
    
                    donationTypes.put(donationType, donationTypes.getOrDefault(donationType, 0) + 1);
                    totalDonationAmount += amount;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        JLabel totalDonationsLabel = new JLabel("Total Donations: " + totalDonations);
        totalDonationsLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        donationsPanel.add(totalDonationsLabel);
    
        JLabel totalDonationAmountLabel = new JLabel("Total Donation Amount: $" + totalDonationAmount);
        totalDonationAmountLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        donationsPanel.add(totalDonationAmountLabel);
    
        for (String type : donationTypes.keySet()) {
            JLabel donationTypeLabel = new JLabel(type + ": " + donationTypes.get(type));
            donationTypeLabel.setFont(new Font("Serif", Font.PLAIN, 24));
            donationsPanel.add(donationTypeLabel);
        }
    
        mainPanel.add(donationsPanel);
    
        // Aid Requests Section
        JPanel aidRequestsPanel = new JPanel();
        aidRequestsPanel.setLayout(new BoxLayout(aidRequestsPanel, BoxLayout.Y_AXIS));
        aidRequestsPanel.setBorder(BorderFactory.createTitledBorder("Aid Requests"));
    
        int totalAidRequests = 0;
        int pendingAidRequests = 0;
        int approvedAidRequests = 0;
        int rejectedAidRequests = 0;
        int scheduledAidRequests = 0;
    
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 10) { // Ensure the line has the correct number of fields
                    totalAidRequests++;
                    String status = parts[9];
                    switch (status) {
                        case "Pending":
                            pendingAidRequests++;
                            break;
                        case "Approved":
                            approvedAidRequests++;
                            break;
                        case "Rejected":
                            rejectedAidRequests++;
                            break;
                        default:
                            if (status.startsWith("Scheduled")) {
                                scheduledAidRequests++;
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        JLabel totalAidRequestsLabel = new JLabel("Total Aid Requests: " + totalAidRequests);
        totalAidRequestsLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        aidRequestsPanel.add(totalAidRequestsLabel);
    
        JLabel pendingAidRequestsLabel = new JLabel("Pending Aid Requests: " + pendingAidRequests);
        pendingAidRequestsLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        aidRequestsPanel.add(pendingAidRequestsLabel);
    
        JLabel approvedAidRequestsLabel = new JLabel("Approved Aid Requests: " + approvedAidRequests);
        approvedAidRequestsLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        aidRequestsPanel.add(approvedAidRequestsLabel);
    
        JLabel rejectedAidRequestsLabel = new JLabel("Rejected Aid Requests: " + rejectedAidRequests);
        rejectedAidRequestsLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        aidRequestsPanel.add(rejectedAidRequestsLabel);
    
        JLabel scheduledAidRequestsLabel = new JLabel("Scheduled Aid Requests: " + scheduledAidRequests);
        scheduledAidRequestsLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        aidRequestsPanel.add(scheduledAidRequestsLabel);
    
        mainPanel.add(aidRequestsPanel);
    
        statsFrame.getContentPane().add(new JScrollPane(mainPanel));
        statsFrame.setVisible(true);
    }
    
    
}    
