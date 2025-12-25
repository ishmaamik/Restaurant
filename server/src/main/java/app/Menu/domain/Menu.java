package app.Menu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue
    private UUID menuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageURL;

    private boolean active;

    private String category;

    public void activate(){
        this.active= true;
    }

    public void deactivate(){
        this.active= false;
    }

    public void changePrice(BigDecimal price){
        if(price.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalStateException("Price must be positive");
        }
        this.price= price;
    }
}
