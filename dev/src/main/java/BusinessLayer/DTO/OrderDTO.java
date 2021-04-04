package BusinessLayer.DTO;

import BusinessLayer.Order;
import BusinessLayer.ProductInOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    public LocalDateTime dateOfOrder;
    public int orderID;
    public Order.ShipmentStatus shipmentStatus;
    public double priceBeforeDiscount;
    public double priceAfterDiscount;
    public int totalQuantity;
    public List<String> productsInOrder;
    public boolean isFixed;

    public OrderDTO(Order order) {
        dateOfOrder = order.getDateOfOrder();
        orderID = order.getOrderID();
        shipmentStatus = order.getShipmentStatus();
        priceBeforeDiscount = order.getPriceBeforeDiscount();
        priceAfterDiscount = order.getPriceAfterDiscount();
        totalQuantity = order.getTotalQuantity();
        isFixed = order.getisFixed();
        productsInOrder = new ArrayList<>();
        for (ProductInOrder pio : order.getProductsInOrder())
            productsInOrder.add("\nProduct name: " + pio.getProduct().getName() +
                    "\nQuantity: " + pio.getQuantity() +
                    "\nTotal price after discounts: " + pio.getTotalPrice());
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append("Order id: " + orderID);
        builder.append("\nDate of order: " + dateOfOrder.format(DateTimeFormatter.ISO_LOCAL_DATE));
        builder.append("\nShippment status: " + shipmentStatus);
        builder.append("\nPrice before discounts: " + priceBeforeDiscount);
        builder.append("\nPrice after discounts: " + priceAfterDiscount);
        builder.append("\nTotal quantity of items: " + totalQuantity);
        builder.append("\nFixed order: " + isFixed);
        builder.append("\nProducts in the order:");
        for(String product:productsInOrder)
            builder.append("\n" + product);
        builder.append("\n");
        return builder.toString();
    }
}
