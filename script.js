const API_URL = "http://localhost:8080/api/expenses";


// Load expenses when page opens

window.onload = function () {

    document.getElementById("today").innerText =
        new Date().toLocaleDateString();

    loadExpenses();
};


// Add Expense

document.getElementById("expenseForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const amount =
            document.getElementById("amount").value;

        const category =
            document.getElementById("category").value;

        const description =
            document.getElementById("description").value;

        const date =
            document.getElementById("date").value;

        const paymentMethod =
            document.getElementById("paymentMethod").value;


        const data =
            new URLSearchParams();

        data.append("amount", amount);
        data.append("category", category);
        data.append("description", description);
        data.append("date", date);
        data.append("paymentMethod", paymentMethod);


        try {

            const response = await fetch(
                API_URL,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/x-www-form-urlencoded"
                    },

                    body: data
                }
            );


            if (response.ok) {

                alert(
                    "Expense added successfully!"
                );

                document
                    .getElementById("expenseForm")
                    .reset();

                loadExpenses();

            } else {

                alert("Failed to add expense.");

            }

        } catch (error) {

            alert(
                "Cannot connect to Java server."
            );

            console.log(error);
        }

    });


// Get expenses from Java

async function loadExpenses() {

    try {

        const response =
            await fetch(API_URL);

        const expenses =
            await response.json();

        displayExpenses(expenses);

        updateDashboard(expenses);

    } catch (error) {

        console.log(error);

        document.getElementById(
            "expenseTable"
        ).innerHTML = `
            <tr>
                <td colspan="7">
                    Start the Java server first.
                </td>
            </tr>
        `;
    }
}


// Display expenses

function displayExpenses(expenses) {

    const table =
        document.getElementById(
            "expenseTable"
        );

    table.innerHTML = "";


    expenses.forEach(function (expense) {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>${expense.id}</td>

            <td>
                ₹${expense.amount.toFixed(2)}
            </td>

            <td>
                ${expense.category}
            </td>

            <td>
                ${expense.description}
            </td>

            <td>
                ${expense.date}
            </td>

            <td>
                ${expense.paymentMethod}
            </td>

            <td>

                <button
                    class="delete-btn"
                    onclick="deleteExpense(${expense.id})">

                    Delete

                </button>

            </td>

        `;


        table.appendChild(row);

    });

}


// Delete Expense

async function deleteExpense(id) {

    if (!confirm(
        "Are you sure you want to delete this expense?"
    )) {

        return;
    }


    try {

        const response =
            await fetch(
                API_URL + "?id=" + id,
                {
                    method: "DELETE"
                }
            );


        if (response.ok) {

            alert(
                "Expense deleted successfully!"
            );

            loadExpenses();

        } else {

            alert("Failed to delete expense.");

        }

    } catch (error) {

        alert(
            "Cannot connect to Java server."
        );
    }
}


// Update dashboard

function updateDashboard(expenses) {

    let total = 0;


    expenses.forEach(function (expense) {

        total += expense.amount;

    });


    document.getElementById(
        "totalExpense"
    ).innerText =
        "₹" + total.toFixed(2);


    document.getElementById(
        "totalTransactions"
    ).innerText =
        expenses.length;
}


// Search

function searchExpenses() {

    const searchText =
        document.getElementById(
            "search"
        ).value.toLowerCase();


    const rows =
        document.querySelectorAll(
            "#expenseTable tr"
        );


    rows.forEach(function (row) {

        const text =
            row.innerText.toLowerCase();


        if (text.includes(searchText)) {

            row.style.display = "";

        } else {

            row.style.display = "none";

        }

    });
}


// Scroll to Add Expense

function showAddExpense() {

    document
        .getElementById("add-expense")
        .scrollIntoView({
            behavior: "smooth"
        });
}