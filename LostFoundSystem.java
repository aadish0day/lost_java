import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LostFoundSystem {
    static java.util.List<Item> items = new ArrayList<>();
    static final String DATA_FILE = "items.txt";

    public static void main(String[] args) {
        loadData();
        new MainWindow();
    }

    // Load items from file
    static void loadData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATA_FILE));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    items.add(new Item(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
            br.close();
            System.out.println("Loaded " + items.size() + " items");
        } catch (Exception e) {
            System.out.println("No existing data file found");
        }
    }

    // Save items to file
    static void saveData() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE));
            for (Item item : items) {
                bw.write(item.name + "," + item.category + "," + item.location + "," +
                        item.date + "," + item.contact);
                bw.newLine();
            }
            bw.close();
            System.out.println("Data saved");
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    // Check if date is in correct format (dd-mm-yyyy)
    static boolean isValidDate(String date) {
        if (date.length() != 10)
            return false;
        if (date.charAt(2) != '-' || date.charAt(5) != '-')
            return false;

        try {
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6, 10));

            if (day < 1 || day > 31)
                return false;
            if (month < 1 || month > 12)
                return false;
            if (year < 1900 || year > 2100)
                return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if contact has @ (email) or is 10 digits (phone)
    static boolean isValidContact(String contact) {
        if (contact.contains("@"))
            return true; // Simple email check

        String digits = contact.replaceAll("[^0-9]", "");
        return digits.length() == 10; // Phone number check
    }
}

class Item {
    String name, category, location, date, contact;

    Item(String name, String category, String location, String date,
            String contact) {
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

        // Close window properly
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                LostFoundSystem.saveData();
                System.exit(0);
            }
        });

        Label title = new Label("Welcome to Campus Lost & Found", Label.CENTER);
        add(title);

        btnReport = new Button("Report Lost/Found Item");
        btnSearch = new Button("Search Items");
        btnContact = new Button("Where to Claim Items");

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
    Button btnAdd, btnCancel;

    ReportModule() {
        setTitle("Report Lost/Found Item");
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));

        // Close window properly
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

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

        add(new Label("Contact (email/phone):"));
        tfContact = new TextField();
        add(tfContact);

        btnAdd = new Button("Add Item");
        btnCancel = new Button("Cancel");

        btnAdd.addActionListener(this);
        btnCancel.addActionListener(this);

        add(btnAdd);
        add(btnCancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel) {
            dispose();
            return;
        }

        String name = tfName.getText().trim();
        String category = tfCategory.getText().trim();
        String location = tfLocation.getText().trim();
        String date = tfDate.getText().trim();
        String contact = tfContact.getText().trim();

        // Check if all fields are filled
        if (name.isEmpty() || category.isEmpty() || location.isEmpty() ||
                date.isEmpty() || contact.isEmpty()) {
            System.out.println("Please fill all fields!");
            return;
        }

        // Check date format
        if (!LostFoundSystem.isValidDate(date)) {
            System.out.println(
                    "Invalid date format! Use dd-mm-yyyy (example: 25-12-2023)");
            return;
        }

        // Check contact format
        if (!LostFoundSystem.isValidContact(contact)) {
            System.out.println("Invalid contact! Use email or 10-digit phone number");
            return;
        }

        // Add item and save
        LostFoundSystem.items.add(
                new Item(name, category, location, date, contact));
        LostFoundSystem.saveData();
        System.out.println("Item Added Successfully!");
        dispose();
    }
}

// ---------------- SEARCH MODULE ----------------
class SearchModule extends Frame implements ActionListener {
    TextField tfSearch;
    Choice searchType;
    Button btnSearch, btnClose;
    TextArea resultArea;

    SearchModule() {
        setTitle("Search Items");
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Close window properly
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        Panel topPanel = new Panel(new GridLayout(2, 3));

        topPanel.add(new Label("Search by:"));
        searchType = new Choice();
        searchType.add("Category");
        searchType.add("Location");
        searchType.add("Date");
        searchType.add("Name");
        topPanel.add(searchType);

        btnSearch = new Button("Search");
        btnSearch.addActionListener(this);
        topPanel.add(btnSearch);

        topPanel.add(new Label("Search term:"));
        tfSearch = new TextField();
        topPanel.add(tfSearch);

        btnClose = new Button("Close");
        btnClose.addActionListener(this);
        topPanel.add(btnClose);

        add(topPanel, BorderLayout.NORTH);

        resultArea = new TextArea();
        resultArea.setEditable(false);
        add(resultArea, BorderLayout.CENTER);

        // Show all items when opened
        showAllItems();
        setVisible(true);
    }

    void showAllItems() {
        if (LostFoundSystem.items.isEmpty()) {
            resultArea.setText("No items found!");
            return;
        }

        String result = "All Items:\n\n";
        for (int i = 0; i < LostFoundSystem.items.size(); i++) {
            Item item = LostFoundSystem.items.get(i);
            result += "Item " + (i + 1) + ":\n";
            result += "Name: " + item.name + "\n";
            result += "Category: " + item.category + "\n";
            result += "Location: " + item.location + "\n";
            result += "Date: " + item.date + "\n\n";
        }
        resultArea.setText(result);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClose) {
            dispose();
            return;
        }

        String key = tfSearch.getText().toLowerCase().trim();
        String type = searchType.getSelectedItem();

        if (key.isEmpty()) {
            showAllItems();
            return;
        }

        String result = "Search Results:\n\n";
        boolean found = false;

        for (Item item : LostFoundSystem.items) {
            boolean match = false;

            if (type.equals("Category") &&
                    item.category.toLowerCase().contains(key)) {
                match = true;
            } else if (type.equals("Location") &&
                    item.location.toLowerCase().contains(key)) {
                match = true;
            } else if (type.equals("Date") && item.date.contains(key)) {
                match = true;
            } else if (type.equals("Name") && item.name.toLowerCase().contains(key)) {
                match = true;
            }

            if (match) {
                found = true;
                result += "Item: " + item.name + "\n";
                result += "Category: " + item.category + "\n";
                result += "Location: " + item.location + "\n";
                result += "Date: " + item.date + "\n\n";
            }
        }

        if (!found) {
            result += "No items found!";
        }

        resultArea.setText(result);
    }
}

// ---------------- CONTACT MODULE (UPDATED) ----------------
class ContactModule extends Frame implements ActionListener {
    Button btnClose;
    TextArea infoArea;

    ContactModule() {
        setTitle("Where to Claim Your Items");
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Close window properly
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // Title
        Label title = new Label("Lost & Found Claim Information", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Information area
        infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 12));

        String infoText = "HOW TO CLAIM YOUR LOST ITEMS:\n\n"
                + "STEP 1: Search for your item using the 'Search Items' feature\n"
                + "        - Use different keywords (item name, category, location)\n"
                + "        - Check the date when you lost the item\n\n"
                + "STEP 2: If you find your item listed, note down the details\n\n"
                + "STEP 3: Visit one of these locations to claim your item:\n\n"
                + "PRIMARY LOCATIONS:\n"
                + "• Main Campus Security Office\n"
                + "  - Location: Ground Floor, Administration Building\n"
                + "  - Hours: Monday-Friday, 8:00 AM - 6:00 PM\n"
                + "  - Phone: (555) 123-4567\n\n"
                + "• Student Services Center\n"
                + "  - Location: 2nd Floor, Student Union Building\n"
                + "  - Hours: Monday-Friday, 9:00 AM - 5:00 PM\n"
                + "  - Phone: (555) 123-4568\n\n"
                + "SECONDARY LOCATIONS:\n"
                + "• Library Information Desk\n"
                + "  - Hours: Daily, 8:00 AM - 10:00 PM\n\n"
                + "• Dormitory Front Desks (for items found in dorms)\n"
                + "  - Hours: 24/7\n\n"
                + "WHAT TO BRING:\n"
                + "• Valid student/staff ID\n"
                + "• Proof of ownership (if available)\n"
                + "• Description of the item\n\n"
                + "IMPORTANT NOTES:\n"
                + "• Items are held for 30 days before donation\n"
                + "• Some items may require additional verification\n"
                + "• For valuable items, additional ID may be required\n\n"
                + "For questions, email: lostfound@campus.edu";

        infoArea.setText(infoText);
        add(infoArea, BorderLayout.CENTER);

        // Close button
        Panel buttonPanel = new Panel();
        btnClose = new Button("Close");
        btnClose.addActionListener(this);
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnClose) {
            dispose();
        }
    }
}
