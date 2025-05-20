package com.example;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.example.entity.Order;
import com.example.service.OrderService;
import com.example.service.OrderServiceImpl;

public class MainModule {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderService ord = new OrderServiceImpl();

        while (true) {
            System.out.println("\n1. Add Order\n2. Delete Order\n3. View All Orders\n4. Exit");
            int c = sc.nextInt();
            sc.nextLine();  // consume newline after int input

            switch (c) {
                case 1:
                    Order order = new Order();
                    System.out.print("Enter Customer Name: ");
                    order.setCustomerName(sc.nextLine());

                    System.out.print("Enter Order Date (yyyy-MM-dd): ");
                    try {
                        order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").parse(sc.nextLine()));
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                        break;
                    }

                    System.out.print("Enter Total Amount: ");
                    order.setTotalAmount(sc.nextDouble());
                    sc.nextLine();  // consume newline

                    System.out.print("Enter Order Status: ");
                    order.setOrderStatus(sc.nextLine());

                    System.out.println(ord.addOrder(order));
                    break;

                case 2:
                    System.out.print("Enter Order ID to delete: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.println(ord.deleteOrder(id));
                    break;

                case 3:
                    List<Order> orders = ord.getAllOrders();
                    if (orders.isEmpty()) {
                        System.out.println("No orders found.");
                    } else {
                        System.out.println("Orders:");
                        for (Order o : orders) {
                            System.out.println(o.getOrderId() + " - " + o.getCustomerName() + " | Date: " 
                                    + new SimpleDateFormat("yyyy-MM-dd").format(o.getOrderDate()) 
                                    + " | Amount: " + o.getTotalAmount() + " | Status: " + o.getOrderStatus());
                        }
                    }
                    break;

                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
