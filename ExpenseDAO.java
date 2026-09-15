package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExpenseDAO {

    public void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "amount REAL, " +
                     "category TEXT, " +
                     "description TEXT, " +
                     "date TEXT, " +
                     "payment_method TEXT)";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addExpense(Expense expense) {

        String sql = "INSERT INTO expenses " +
                     "(amount, category, description, date, payment_method) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, expense.getAmount());
            ps.setString(2, expense.getCategory());
            ps.setString(3, expense.getDescription());
            ps.setString(4, expense.getDate());
            ps.setString(5, expense.getPaymentMethod());

            ps.executeUpdate();

            System.out.println("Expense added successfully!");

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewExpenses() {

        String sql = "SELECT * FROM expenses";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== ALL EXPENSES =====");

            while (rs.next()) {

                System.out.println("ID             : " + rs.getInt("id"));
                System.out.println("Amount         : ₹" + rs.getDouble("amount"));
                System.out.println("Category       : " + rs.getString("category"));
                System.out.println("Description    : " + rs.getString("description"));
                System.out.println("Date           : " + rs.getString("date"));
                System.out.println("Payment Method : " + rs.getString("payment_method"));
                System.out.println("-----------------------------");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateExpense(Expense expense) {

        String sql = "UPDATE expenses SET " +
                     "amount = ?, category = ?, description = ?, " +
                     "date = ?, payment_method = ? WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, expense.getAmount());
            ps.setString(2, expense.getCategory());
            ps.setString(3, expense.getDescription());
            ps.setString(4, expense.getDate());
            ps.setString(5, expense.getPaymentMethod());
            ps.setInt(6, expense.getId());

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Expense updated successfully!");
            } else {
                System.out.println("Expense ID not found.");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteExpense(int id) {

        String sql = "DELETE FROM expenses WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Expense deleted successfully!");
            } else {
                System.out.println("Expense ID not found.");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void calculateTotal() {

        String sql = "SELECT SUM(amount) AS total FROM expenses";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            double total = rs.getDouble("total");

            System.out.println("\n===== EXPENSE SUMMARY =====");
            System.out.println("Total Expense: ₹" + total);

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchExpense(String category) {

        String sql = "SELECT * FROM expenses WHERE category = ?";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== SEARCH RESULT =====");

            while (rs.next()) {

                System.out.println("ID          : " + rs.getInt("id"));
                System.out.println("Amount      : ₹" + rs.getDouble("amount"));
                System.out.println("Category    : " + rs.getString("category"));
                System.out.println("Description : " + rs.getString("description"));
                System.out.println("Date        : " + rs.getString("date"));
                System.out.println("-----------------------------");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void categoryTotal() {

        String sql = "SELECT category, SUM(amount) AS total " +
                     "FROM expenses GROUP BY category";

        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== CATEGORY-WISE TOTAL =====");

            while (rs.next()) {

                System.out.println(
                    rs.getString("category") +
                    " : ₹" +
                    rs.getDouble("total")
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}