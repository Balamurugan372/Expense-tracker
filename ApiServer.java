package model;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class ApiServer {

    static String DB_URL = "jdbc:sqlite:expense.db";

    public static void main(String[] args) throws Exception {

        createTable();

        HttpServer server = HttpServer.create(
            new InetSocketAddress(8080), 0
        );

        server.createContext("/api/expenses", new ExpenseHandler());

        server.setExecutor(null);

        System.out.println("================================");
        System.out.println(" Smart Expense Tracker Server");
        System.out.println("================================");
        System.out.println("Server running at:");
        System.out.println("http://localhost:8080");

        server.start();
    }


    // Create database table
    static void createTable() {

        String sql =
            "CREATE TABLE IF NOT EXISTS expenses (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "amount REAL, " +
            "category TEXT, " +
            "description TEXT, " +
            "date TEXT, " +
            "payment_method TEXT)";

        try {

            Connection con = DriverManager.getConnection(DB_URL);

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.executeUpdate();

            ps.close();
            con.close();

            System.out.println("Database ready!");

        } catch (Exception e) {

            System.out.println(
                "Database error: " + e.getMessage()
            );
        }
    }


    // API Handler
    static class ExpenseHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange)
                throws IOException {

            addCorsHeaders(exchange);

            String method = exchange.getRequestMethod();

            try {

                if (method.equals("OPTIONS")) 
                {

              exchange.sendResponseHeaders(204, -1);
                exchange.close();

                }
                else if (method.equals("GET")) {

                    getExpenses(exchange);

                } else if (method.equals("POST")) {

                    addExpense(exchange);

                } else if (method.equals("DELETE")) {

                    deleteExpense(exchange);

                } else {

                    sendResponse(
                        exchange,
                        405,
                        "Method Not Allowed"
                    );
                }

            } catch (Exception e) {

                sendResponse(
                    exchange,
                    500,
                    "Error: " + e.getMessage()
                );
            }
        }
    }


    // GET - View expenses
    static void getExpenses(
            HttpExchange exchange) throws Exception {

        Connection con =
            DriverManager.getConnection(DB_URL);

        Statement st = con.createStatement();

        ResultSet rs =
            st.executeQuery(
                "SELECT * FROM expenses ORDER BY id DESC"
            );

        StringBuilder json =
            new StringBuilder("[");

        boolean first = true;

        while (rs.next()) {

            if (!first) {
                json.append(",");
            }

            json.append("{");

            json.append("\"id\":")
                .append(rs.getInt("id"))
                .append(",");

            json.append("\"amount\":")
                .append(rs.getDouble("amount"))
                .append(",");

            json.append("\"category\":\"")
                .append(escape(rs.getString("category")))
                .append("\",");

            json.append("\"description\":\"")
                .append(escape(rs.getString("description")))
                .append("\",");

            json.append("\"date\":\"")
                .append(escape(rs.getString("date")))
                .append("\",");

            json.append("\"paymentMethod\":\"")
                .append(escape(
                    rs.getString("payment_method")
                ))
                .append("\"");

            json.append("}");

            first = false;
        }

        json.append("]");

        rs.close();
        st.close();
        con.close();

        sendResponse(
            exchange,
            200,
            json.toString()
        );
    }


    // POST - Add expense
    static void addExpense(
            HttpExchange exchange) throws Exception {

        String body = readBody(exchange);

        Map<String, String> data =
            parseFormData(body);

        String sql =
            "INSERT INTO expenses " +
            "(amount, category, description, date, payment_method) " +
            "VALUES (?, ?, ?, ?, ?)";

        Connection con =
            DriverManager.getConnection(DB_URL);

        PreparedStatement ps =
            con.prepareStatement(sql);

        ps.setDouble(
            1,
            Double.parseDouble(data.get("amount"))
        );

        ps.setString(
            2,
            data.get("category")
        );

        ps.setString(
            3,
            data.get("description")
        );

        ps.setString(
            4,
            data.get("date")
        );

        ps.setString(
            5,
            data.get("paymentMethod")
        );

        ps.executeUpdate();

        ps.close();
        con.close();

        sendResponse(
            exchange,
            200,
            "{\"message\":\"Expense added successfully\"}"
        );
    }


    // DELETE - Delete expense
    static void deleteExpense(
            HttpExchange exchange) throws Exception {

        String query =
            exchange.getRequestURI().getQuery();

        Map<String, String> data =
            parseFormData(query);

        int id =
            Integer.parseInt(data.get("id"));

        Connection con =
            DriverManager.getConnection(DB_URL);

        PreparedStatement ps =
            con.prepareStatement(
                "DELETE FROM expenses WHERE id = ?"
            );

        ps.setInt(1, id);

        int result =
            ps.executeUpdate();

        ps.close();
        con.close();

        if (result > 0) {

            sendResponse(
                exchange,
                200,
                "{\"message\":\"Expense deleted successfully\"}"
            );

        } else {

            sendResponse(
                exchange,
                404,
                "{\"message\":\"Expense not found\"}"
            );
        }
    }


    // Read request body
    static String readBody(
            HttpExchange exchange) throws IOException {

        InputStream input =
            exchange.getRequestBody();

        return new String(
            input.readAllBytes(),
            StandardCharsets.UTF_8
        );
    }


    // Convert form data
    static Map<String, String> parseFormData(
            String data) {

        Map<String, String> map =
            new HashMap<>();

        if (data == null || data.isEmpty()) {
            return map;
        }

        String[] pairs =
            data.split("&");

        for (String pair : pairs) {

            String[] parts =
                pair.split("=", 2);

            if (parts.length == 2) {

                String key =
                    URLDecoder.decode(
                        parts[0],
                        StandardCharsets.UTF_8
                    );

                String value =
                    URLDecoder.decode(
                        parts[1],
                        StandardCharsets.UTF_8
                    );

                map.put(key, value);
            }
        }

        return map;
    }


    // JSON special characters
    static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }


    // CORS
   static void addCorsHeaders(
        HttpExchange exchange) {

    exchange.getResponseHeaders().set(
        "Access-Control-Allow-Origin",
        "*"
    );

     exchange.getResponseHeaders().set(
        "Access-Control-Allow-Methods",
        "GET, POST, DELETE, OPTIONS"
    );

    exchange.getResponseHeaders().set(
        "Access-Control-Allow-Headers",
        "Content-Type"
    );
}


    // Send response
    static void sendResponse(
            HttpExchange exchange,
            int status,
            String response) throws IOException {

        byte[] bytes =
            response.getBytes(
                StandardCharsets.UTF_8
            );

        exchange.getResponseHeaders()
            .set(
                "Content-Type",
                "application/json"
            );

        exchange.sendResponseHeaders(
            status,
            bytes.length
        );

        OutputStream output =
            exchange.getResponseBody();

        output.write(bytes);

        output.close();
    }
}