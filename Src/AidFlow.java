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

    private static void showLoginScreen() {
        frame = new JFrame("NGO Aid Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600); // Increase frame size
    
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        userText.setPreferredSize(new Dimension(200, 24)); // Set preferred size for text field
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        passText.setPreferredSize(new Dimension(200, 24)); // Set preferred size for password field
    
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
    
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
    
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
    
        loginButton.addActionListener(e -> handleLogin(userText.getText(), new String(passText.getPassword())));
        registerButton.addActionListener(e -> showRegistrationScreen());
    
        frame.setVisible(true);
    }
    
    private static void handleLogin(String username, String password) {
        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            JOptionPane.showMessageDialog(frame, "You have successfully logged in");
            frame.dispose();
            showUserInterface(username);
        } else {
            JOptionPane.showMessageDialog(frame, "Please check your information");
        }
    }

    private static void showRegistrationScreen() {
        JFrame registerFrame = new JFrame("User Registration");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(600, 400); // Increase frame size
    
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        userText.setPreferredSize(new Dimension(200, 24)); // Set preferred size for text field
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        passText.setPreferredSize(new Dimension(200, 24)); // Set preferred size for password field
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
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(registerFrame, "Username and Password cannot be empty");
            }
        });
    
        goBackButton.addActionListener(e -> {
            registerFrame.dispose();
            showLoginScreen();
        });
    
        buttonPanel.add(registerButton);
        buttonPanel.add(goBackButton);
    
        registerFrame.getContentPane().add(BorderLayout.CENTER, panel);
        registerFrame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
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
        donorFrame.setSize(400, 300);

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
        donateFrame.setSize(600, 600); // Increase frame size
    
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
        JLabel amountLabel = new JLabel("Donation Amount (if applicable):");
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
            String amount = amountText.getText();
            String ccInfo = ccInfoText.getText();
            String billingAddress = billingAddressText.getText();
            
            if (!name.isEmpty() && !contact.isEmpty() && !ccInfo.isEmpty() && !billingAddress.isEmpty()) {
                saveDonation(username, name, contact, type, description, amount, ccInfo, billingAddress);
                JOptionPane.showMessageDialog(donateFrame, "Thank you for your donation!");
                donateFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(donateFrame, "Full Name, Contact Information, Credit Card Information, and Billing Address are required.");
            }
        });
    
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(donateButton);
    
        donateFrame.getContentPane().add(BorderLayout.CENTER, panel);
        donateFrame.setVisible(true);
    }
    
    private static void saveDonation(String username, String name, String contact, String type, String description, String amount, String ccInfo, String billingAddress) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(username + "_donations.txt", true))) {
            bw.write(name + "," + contact + "," + type + "," + description + "," + amount + "," + ccInfo + "," + billingAddress);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
    private static void viewDonationHistory(String username) {
        JFrame historyFrame = new JFrame("Donation History");
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(600, 400);
    
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setText("Donation history for " + username + ":\n\n");
    
        try (BufferedReader br = new BufferedReader(new FileReader(username + "_donations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {  // Ensure the line has the correct number of fields
                    String formattedDonation = String.format(
                        "Full Name: %s\nContact Information: %s\nType of Donation: %s\nDescription of Donation: %s\n" +
                        "Donation Amount: %s\nCredit Card Information: %s\nBilling Address: %s\n\n",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]
                    );
                    textArea.append(formattedDonation);
                    textArea.append("--------------------------------------------------------\n");
                } else {
                    System.out.println("Invalid line format: " + line);
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
        indigentFrame.setSize(400, 300);
    
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
        statusFrame.setSize(600, 400);
    
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setText("Aid Request Status for " + username + ":\n\n");
    
        try (BufferedReader br = new BufferedReader(new FileReader("aid_requests.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(username + ",")) {
                    textArea.append(line + "\n");
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
        aidFrame.setSize(400, 400);

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
        coordinatorFrame.setSize(400, 300);

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
        scheduleFrame.setSize(600, 400);
    
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
                String[] parts = line.split(",");
                if (parts.length == 10 && parts[9].equals("Approved")) {  // Ensure the line has the correct number of fields and is approved
                    JPanel requestPanel = new JPanel(new BorderLayout());
    
                    String formattedRequest = String.format(
                        "<html><b>Username:</b> %s<br><b>Name:</b> %s<br><b>Surname:</b> %s<br><b>Age:</b> %s<br>" +
                        "<b>Profession:</b> %s<br><b>Monthly Income:</b> %s<br><b>Household Size:</b> %s<br>" +
                        "<b>Number of Children:</b> %s<br><b>Education Level:</b> %s<br><b>Status:</b> %s</html>",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9]
                    );
    
                    JLabel requestLabel = new JLabel(formattedRequest);
                    JTextField dateField = new JTextField("Enter date (YYYY-MM-DD)");
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
        backlogFrame.setSize(600, 400);
    
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
                String[] parts = line.split(",");
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
        submitButton.addActionListener(e -> updateAidRequestStatus(requestLines, approveCheckBoxes, rejectCheckBoxes));
    
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
        adminFrame.setSize(400, 300);

        JButton manageUsersButton = new JButton("Manage Users");
        JButton viewStatsButton = new JButton("View Dashboard Statistics");
        JButton goBackButton = new JButton("Go Back");

        manageUsersButton.addActionListener(e -> manageUsers(username));
        viewStatsButton.addActionListener(e -> viewDashboardStatistics(username));
        goBackButton.addActionListener(e -> {
            adminFrame.dispose();
            showLoginScreen();
        });

        JPanel panel = new JPanel();
        panel.add(manageUsersButton);
        panel.add(viewStatsButton);
        panel.add(goBackButton);

        adminFrame.getContentPane().add(BorderLayout.CENTER, panel);
        adminFrame.setVisible(true);
    }

    private static void manageUsers(String username) {
        JFrame manageFrame = new JFrame("Manage Users");
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        manageFrame.setSize(800, 600);
    
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
        addUserButton.addActionListener(e -> showRegistrationScreen());
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
        passwordFrame.setSize(300, 200);
    
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
        panel.add(new JLabel()); // Empty label for alignment
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
        statsFrame.setSize(400, 300);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setText("Viewing Dashboard Statistics for " + username + ":\n\n");  // Placeholder for actual data

        statsFrame.getContentPane().add(new JScrollPane(textArea));
        statsFrame.setVisible(true);
    }
}

