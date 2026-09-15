

import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ExpenseDAO dao = new ExpenseDAO();

        dao.createTable();

        while (true) {

            System.out.println("\n==============================");
            System.out.println("      EXPENSE TRACKER");
            System.out.println("==============================");

            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Calculate Total");
            System.out.println("6. Search Expense");
            System.out.println("7. Category-wise Total");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.println("\n===== ADD EXPENSE =====");

                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter description: ");
                    String description = sc.nextLine();

                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String date = sc.nextLine();

                    System.out.print("Enter payment method: ");
                    String paymentMethod = sc.nextLine();

                    Expense expense = new Expense(
                        amount,
                        category,
                        description,
                        date,
                        paymentMethod
                    );

                    dao.addExpense(expense);

                    break;

                case 2:

                    dao.viewExpenses();

                    break;

                case 3:

                    System.out.print("Enter expense ID: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new amount: ");
                    double newAmount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter new category: ");
                    String newCategory = sc.nextLine();

                    System.out.print("Enter new description: ");
                    String newDescription = sc.nextLine();

                    System.out.print("Enter new date: ");
                    String newDate = sc.nextLine();

                    System.out.print("Enter new payment method: ");
                    String newPayment = sc.nextLine();

                    Expense updatedExpense = new Expense(
                        updateId,
                        newAmount,
                        newCategory,
                        newDescription,
                        newDate,
                        newPayment
                    );

                    dao.updateExpense(updatedExpense);

                    break;

                case 4:

                    System.out.print("Enter expense ID: ");
                    int deleteId = sc.nextInt();

                    dao.deleteExpense(deleteId);

                    break;

                case 5:

                    dao.calculateTotal();

                    break;

                case 6:

                    System.out.print("Enter category to search: ");
                    String searchCategory = sc.nextLine();

                    dao.searchExpense(searchCategory);

                    break;

                case 7:

                    dao.categoryTotal();

                    break;

                case 8:

                    System.out.println("Thank you for using Expense Tracker!");

                    sc.close();

                    return;

                default:

                    System.out.println("Invalid choice!");
            }
        }
    }
}
