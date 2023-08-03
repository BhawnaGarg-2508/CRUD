import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUD {
    private static final String URL = "jdbc:mysql://localhost/crud_example";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Amit@2508";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql:///crud_example","root","Amit@2508");
    }

//    private static void createUser(User user) {
//        String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
//
//        try (Connection connection = getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setString(1, user.getName());
//            preparedStatement.setInt(2, user.getAge());
//
//            preparedStatement.executeUpdate();
//
//            System.out.println("User created: " + user);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    
    private static void createUser(User user) {
    String sql = "INSERT INTO users (name, age) VALUES (?, ?)";

    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        preparedStatement.setString(1, user.getName());
        preparedStatement.setInt(2, user.getAge());

        preparedStatement.executeUpdate();

        // Retrieve the generated ID from the database
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            user.setId(generatedId); // Set the generated ID in the User object
        }

        System.out.println("User created: " + user);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private static User retrieveUser(int id) {
    String sql = "SELECT * FROM users WHERE id = ?";

    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setInt(1, id); // Set parameter index to 1, not 0
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setAge(resultSet.getInt("age"));
            return user;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    private static List<User> retrieveAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setInt(3, user.getId());

            preparedStatement.executeUpdate();

            System.out.println("User updated: " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            System.out.println("User deleted with ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }



private static void createAndShowGUI() {
    JFrame frame = new JFrame("Simple CRUD Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(400, 300));

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(5, 5, 5, 5);

    JLabel idLabel = new JLabel("ID:");
    JTextField idField = new JTextField(5);

    JLabel nameLabel = new JLabel("Name:");
    JTextField nameField = new JTextField(20);

    JLabel ageLabel = new JLabel("Age:");
    JTextField ageField = new JTextField(5);

    JButton createButton = new JButton("Create");
    JButton retrieveButton = new JButton("Retrieve");
    JButton retrieveAllButton = new JButton("Retrieve All");
    JButton updateButton = new JButton("Update");
    JButton deleteButton = new JButton("Delete");

    JTextArea outputArea = new JTextArea(10, 30);
    outputArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(outputArea);

    constraints.gridx = 0;
    constraints.gridy = 0;
    panel.add(idLabel, constraints);

    constraints.gridx = 1;
    panel.add(idField, constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    panel.add(nameLabel, constraints);

    constraints.gridx = 1;
    panel.add(nameField, constraints);

    constraints.gridx = 0;
    constraints.gridy = 2;
    panel.add(ageLabel, constraints);

    constraints.gridx = 1;
    panel.add(ageField, constraints);

    constraints.gridx = 0;
    constraints.gridy = 3;
    panel.add(createButton, constraints);

    constraints.gridx = 1;
    panel.add(retrieveButton, constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    panel.add(retrieveAllButton, constraints);

    constraints.gridx = 1;
    panel.add(updateButton, constraints);

    constraints.gridx = 0;
    constraints.gridy = 5;
    panel.add(deleteButton, constraints);

    constraints.gridwidth = 2;
    constraints.gridx = 0;
    constraints.gridy = 6;
    panel.add(scrollPane, constraints);

//    createButton.addActionListener(e -> {
//        String name = nameField.getText();
//        String ageText = ageField.getText();
//
//        if (isValidAge(ageText)) {
//            int age = Integer.parseInt(ageText);
//            User user = new User();
//            user.setName(name);
//            user.setAge(age);
//            createUser(user);
//            outputArea.append("User created: " + user + "\n");
//        } else {
//            outputArea.append("Invalid age. Please enter a valid integer.\n");
//        }
//    });

createButton.addActionListener(e -> {
    String name = nameField.getText();
    String ageText = ageField.getText();

    if (isValidAge(ageText)) {
        int age = Integer.parseInt(ageText);
        User user = new User();
        user.setId(Integer.parseInt(idField.getText())); // Set the ID from GUI input
        user.setName(name);
        user.setAge(age);
        createUser(user);
        outputArea.append("User created: " + user + "\n");
    } else {
        outputArea.append("Invalid age. Please enter a valid integer.\n");
    }
});


    retrieveButton.addActionListener(e -> {
        int id = Integer.parseInt(idField.getText());
        User retrievedUser = retrieveUser(id);
        if (retrievedUser != null) {
            outputArea.append("Retrieved User: " + retrievedUser + "\n");
        } else {
            outputArea.append("User not found.\n");
        }
    });

    retrieveAllButton.addActionListener(e -> {
        List<User> users = retrieveAllUsers();
        outputArea.append("All Users:\n");
        for (User user : users) {
            outputArea.append(user + "\n");
        }
    });

    updateButton.addActionListener(e -> {
        int id = Integer.parseInt(idField.getText());
        User updateUser = retrieveUser(id);

        if (updateUser != null) {
            String name = nameField.getText();
            String ageText = ageField.getText();

            if (isValidAge(ageText)) {
                int age = Integer.parseInt(ageText);
                updateUser.setName(name);
                updateUser.setAge(age);
                updateUser(updateUser);
                outputArea.append("User updated: " + updateUser + "\n");
            } else {
                outputArea.append("Invalid age. Please enter a valid integer.\n");
            }
        } else {
            outputArea.append("User not found.\n");
        }
    });

    deleteButton.addActionListener(e -> {
        int id = Integer.parseInt(idField.getText());
        deleteUser(id);
        outputArea.append("User deleted with ID: " + id + "\n");
    });

    frame.add(panel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}


    private static boolean isValidAge(String ageText) {
        try {
            int age = Integer.parseInt(ageText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

class User {
    private int id;
    private String name;
    private int age;
        private boolean idSet = false;

     public void setId(int id) {
        if (!idSet) {
            this.id = id;
            idSet = true;
        }
    }


    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}
