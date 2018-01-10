package com.sh.coffeeshop.model;

import java.io.Serializable;

public class OrderItem implements Serializable {

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

        OrderItem item = (OrderItem) o;

        if (id != null ? !id.equals(item.id) : item.id != null) return false;
        if (coffee != null ? !coffee.equals(item.coffee) : item.coffee != null) return false;
        return quantity != null ? quantity.equals(item.quantity) : item.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (coffee != null ? coffee.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", coffee=" + coffee +
                ", quantity=" + quantity +
                '}';
    }
}
