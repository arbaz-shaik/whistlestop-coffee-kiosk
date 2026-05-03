package uk.ac.ncl.csc8019backend.business.order;

public class OrderItemRequest {
    private Long menuItemId;
    private Integer quantity;
    private String size;

    public OrderItemRequest() {
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
