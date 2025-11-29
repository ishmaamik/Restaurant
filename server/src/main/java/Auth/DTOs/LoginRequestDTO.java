package Auth.DTOs;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public record LoginRequestDTO(String username, String password) {

}
