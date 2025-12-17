package app.Orders.DTOs;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWhoOrderedDTO {
    private String username;
    private String email;
}
