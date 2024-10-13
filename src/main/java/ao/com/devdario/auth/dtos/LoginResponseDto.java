package ao.com.devdario.auth.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

    private Integer userId;
    private String token;
    private String username;
    private long expiredIn;
}
