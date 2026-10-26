
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class User {
    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(2, 1, 10, 10));

        Label welcome = new Label(
            "Hello, " + user.getUsername(),
            Label.CENTER
        );

        welcome.setFont(new Font("SansSerif", Font.BOLD, 20));

        Label role = new Label(
            "Your role is: " + user.getRole(),
            Label.CENTER
        );

        role.setFont(new Font("SansSerif", Font.PLAIN, 16));

        add(welcome);
        add(role);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 180);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {

    private ArrayList<User> users = new ArrayList<>();
    private int wrongPasswordAttempts = 0;

    public LoginFormTask() {
        super("Login");

        loadUsers();

        setLayout(new GridBagLayout());

        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));

        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(
            new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        Button cancel = new Button("Cancel");
        Button login = new Button("Login");

        buttons.add(cancel);
        buttons.add(login);

        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        // Login logic
        login.addActionListener(e -> {

            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            String selectedRole = role.getSelectedItem();

            // Check empty fields
            if (enteredUsername.isEmpty()
                    || enteredPassword.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Find the user
            User user = findUser(enteredUsername);

            if (user == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Username does not exist.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Check password
            if (!user.getPassword().equals(enteredPassword)) {

                wrongPasswordAttempts++;

                if (wrongPasswordAttempts >= 3) {
                    login.setEnabled(false);

                    JOptionPane.showMessageDialog(
                        this,
                        "Too many wrong password attempts. Login is disabled.",
                        "Account Locked",
                        JOptionPane.ERROR_MESSAGE
                    );

                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Incorrect password. Attempts remaining: "
                            + (3 - wrongPasswordAttempts),
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

                return;
            }

            // Check role
            if (!user.getRole().equals(selectedRole)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Incorrect role selected.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Login successful
            JOptionPane.showMessageDialog(
                this,
                "Login successful!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            WelcomePage welcomePage = new WelcomePage(user);
            welcomePage.setVisible(true);

            dispose();
        });

        // Cancel logic
        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    // Add hard-coded users
    private void loadUsers() {
        users.add(new User("ahmed", "1234", "Student"));
        users.add(new User("omar", "9876", "TA"));
        users.add(new User("mohamed", "abcd", "Professor"));
    }

    // Search for a user by username
    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
