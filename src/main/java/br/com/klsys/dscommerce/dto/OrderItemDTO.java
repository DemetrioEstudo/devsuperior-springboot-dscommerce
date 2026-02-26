package br.com.klsys.dscommerce.dto;

import br.com.klsys.dscommerce.entities.OrderItem;

public class OrderItemDTO {

    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;
    private String imageUrl;


    public OrderItemDTO(Long productId, String name, Double price, Integer quantity, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem entity) {
         this.productId = entity.getProduct().getId();
         this.name = entity.getProduct().getName();
         this.price = entity.getPrice();
         this.quantity = entity.getQuantity();
         this.imageUrl = entity.getProduct().getImgUrl();
   }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubTotal(){
        return price * quantity;
    }

    public String getImageUrl(){
        return imageUrl;
    }


}
