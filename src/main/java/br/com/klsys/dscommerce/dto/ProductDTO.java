package br.com.klsys.dscommerce.dto;

import br.com.klsys.dscommerce.entities.Product;
import jakarta.validation.constraints.*;

public class ProductDTO {

    private Long id;
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, max = 80, message = "Nome deve ter entre 3 e 80 caracteres")
    private String name;
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
    private String description;
    @Positive(message = "O preço deve ser um valor positivo")
    private Double price;
    private String imgUrl;

    public ProductDTO() {
    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
