package com.sh.coffeeshop.model;

public class OrderItem {

    private Long id;
    private Coffee coffee;
    private Order order;
    private Integer quantity;

    public OrderItem() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coffee getCoffee() {
        return coffee;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (id != null ? !id.equals(orderItem.id) : orderItem.id != null) return false;
        if (coffee != null ? !coffee.equals(orderItem.coffee) : orderItem.coffee != null) return false;
        if (order != null ? !order.equals(orderItem.order) : orderItem.order != null) return false;
        return quantity != null ? quantity.equals(orderItem.quantity) : orderItem.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (coffee != null ? coffee.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", coffee=" + coffee +
                ", order=" + order +
                ", quantity=" + quantity +
                '}';
    }
}
