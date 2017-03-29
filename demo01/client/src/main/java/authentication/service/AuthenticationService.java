package authentication.service;

import authentication.dto.UserDto;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fabian.frontzek on 30.03.2017.
 */
public class AuthenticationService {

    public static Set<UserDto> authenticateSequence() {
        Set<UserDto> users = new HashSet<UserDto>();
        users.add(authenticate("http://auth01:9091/authenticate/"));
        users.add(authenticate("http://auth02:9092/authenticate/"));
        users.add(authenticate("http://auth03:9093/authenticate/"));
        return users;
    }

    private static UserDto authenticate(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, UserDto.class);
    }

}
