package com.example;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.example.entity.Order;
import com.example.service.OrderService;
import com.example.service.OrderServiceImpl;
import com.example.util.DBConnectionUtil;

public class AppTest {

    private final OrderService orderService = new OrderServiceImpl();

    @Test
    public void test_Util_File_Exist() {
        File file = new File("src/main/java/com/example/util/DBConnectionUtil.java");
        assertTrue(file.exists(), "DBConnectionUtil.java should exist in the util folder.");
    }

    @Test
    public void test_Util_Folder_Exist() {
        File folder = new File("src/main/java/com/example/util");
        assertTrue(folder.exists() && folder.isDirectory(), "The util folder should exist in the specified path.");
    }

    @Test
    public void test_Check_Method_Exist() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = Class.forName("com.example.service.OrderServiceImpl");
        assertNotNull(clazz.getMethod("addOrder", com.example.entity.Order.class));
        assertNotNull(clazz.getMethod("deleteOrder", int.class));
        assertNotNull(clazz.getMethod("getAllOrders"));
    }

    @Test
    public void test_Create_Query_Exist() {
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(new java.util.Date());
        order.setTotalAmount(100.0);
        order.setOrderStatus("NEW");

        orderService.addOrder(order);

        try (Connection conn = DBConnectionUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE customerName = ?");
            stmt.setString(1, "John Doe");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Order should be inserted.");
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void test_Delete_Query_Exist() {
        try (Connection conn = DBConnectionUtil.getConnection()) {
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO orders (customerName, orderDate, totalAmount, orderStatus) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, "Delete Me");
            insertStmt.setDate(2, Date.valueOf(LocalDate.now()));
            insertStmt.setDouble(3,100.00);
            insertStmt.setString(4, "PENDING");
            insertStmt.executeUpdate();

            ResultSet rs = insertStmt.getGeneratedKeys();
            assertTrue(rs.next());
            int id = rs.getInt(1);

            // Now delete it using the service
            orderService.deleteOrder(id);

            // Confirm that the order has been deleted from the database
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM orders WHERE orderId = ?");
            checkStmt.setInt(1, id);
            ResultSet checkRs = checkStmt.executeQuery();
            assertFalse(checkRs.next(), "Order should be deleted.");
        } catch (Exception e) {
            fail("Exception during deletion: " + e.getMessage());
        }
    }
}
