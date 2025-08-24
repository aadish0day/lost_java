import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LostFoundSystem {
    static java.util.List<Item> items = new ArrayList<>(); // Store all items

    public static void main(String[] args) {
        new MainWindow();
    }
}

// Item class to store lost/found details
class Item {
    String name, category, location, date, contact;

    Item(String name, String category, String location, String date, String contact) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.date = date;
        this.contact = contact;
    }
}

// ---------------- MAIN WINDOW ----------------
class MainWindow extends Frame implements ActionListener {
    Button btnReport, btnSearch, btnContact;

    MainWindow() {
        setTitle("Campus Lost & Found System");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        Label title = new Label("Welcome to Campus Lost & Found", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title);

        btnReport = new Button("Report Lost/Found Item");
        btnSearch = new Button("Search Items");
        btnContact = new Button("Contact Owner/Finder");

        btnReport.addActionListener(this);
        btnSearch.addActionListener(this);
        btnContact.addActionListener(this);

        add(btnReport);
        add(btnSearch);
        add(btnContact);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnReport) {
            new ReportModule();
        } else if (e.getSource() == btnSearch) {
            new SearchModule();
        } else if (e.getSource() == btnContact) {
            new ContactModule();
        }
    }
}

// ---------------- REPORT MODULE ----------------
class ReportModule extends Frame implements ActionListener {
    TextField tfName, tfCategory, tfLocation, tfDate, tfContact;
    Button btnAdd;

    ReportModule() {
        setTitle("Report Lost/Found Item");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        add(new Label("Item Name:"));
        tfName = new TextField();
        add(tfName);

        add(new Label("Category:"));
        tfCategory = new TextField();
        add(tfCategory);

        add(new Label("Location:"));
        tfLocation = new TextField();
        add(tfLocation);

        add(new Label("Date (dd-mm-yyyy):"));
        tfDate = new TextField();
        add(tfDate);

        add(new Label("Contact Info:"));
        tfContact = new TextField();
        add(tfContact);

        btnAdd = new Button("Add Item");
        btnAdd.addActionListener(this);
        add(btnAdd);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String name = tfName.getText();
        String category = tfCategory.getText();
        String location = tfLocation.getText();
        String date = tfDate.getText();
        String contact = tfContact.getText();

        if (!name.isEmpty() && !category.isEmpty() && !location.isEmpty() && !date.isEmpty() && !contact.isEmpty()) {
            LostFoundSystem.items.add(new Item(name, category, location, date, contact));
            System.out.println("Item Added Successfully!");
            dispose();
        } else {
            System.out.println("Please fill all fields!");
        }
    }
}

// ---------------- SEARCH MODULE ----------------
class SearchModule extends Frame implements ActionListener {
    TextField tfSearch;
    Choice searchType;
    Button btnSearch;
    TextArea resultArea;

    SearchModule() {
        setTitle("Search Items");
        setSize(400, 300);
        setLayout(new BorderLayout());

        Panel topPanel = new Panel(new GridLayout(1, 3));
        searchType = new Choice();
        searchType.add("Category");
        searchType.add("Location");
        searchType.add("Date");
        topPanel.add(searchType);

        tfSearch = new TextField();
        topPanel.add(tfSearch);

        btnSearch = new Button("Search");
        btnSearch.addActionListener(this);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        resultArea = new TextArea();
        add(resultArea, BorderLayout.CENTER);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String key = tfSearch.getText().toLowerCase();
        String type = searchType.getSelectedItem();
        resultArea.setText("");

        for (Item item : LostFoundSystem.items) {
            if ((type.equals("Category") && item.category.toLowerCase().contains(key)) ||
                (type.equals("Location") && item.location.toLowerCase().contains(key)) ||
                (type.equals("Date") && item.date.contains(key))) {
                resultArea.append("Item: " + item.name + "\nCategory: " + item.category +
                        "\nLocation: " + item.location + "\nDate: " + item.date + "\n\n");
            }
        }

        if (resultArea.getText().isEmpty()) {
            resultArea.setText("No items found!");
        }
    }
}

// ---------------- CONTACT MODULE ----------------
class ContactModule extends Frame implements ActionListener {
    TextField tfItemName, tfPassword;
    Button btnShow;
    Label lblResult;

    ContactModule() {
        setTitle("Contact Owner/Finder");
        setSize(400, 200);
        setLayout(new GridLayout(4, 2));

        add(new Label("Enter Item Name:"));
        tfItemName = new TextField();
        add(tfItemName);

        add(new Label("Enter Password (admin):"));
        tfPassword = new TextField();
        tfPassword.setEchoChar('*');
        add(tfPassword);

        btnShow = new Button("Show Contact");
        btnShow.addActionListener(this);
        add(btnShow);

        lblResult = new Label("");
        add(lblResult);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String name = tfItemName.getText();
        String pass = tfPassword.getText();

        if (!pass.equals("admin")) {
            lblResult.setText("Wrong Password!");
            return;
        }

        for (Item item : LostFoundSystem.items) {
            if (item.name.equalsIgnoreCase(name)) {
                lblResult.setText("Contact: " + item.contact);
                return;
            }
        }
        lblResult.setText("Item not found!");
    }
}
