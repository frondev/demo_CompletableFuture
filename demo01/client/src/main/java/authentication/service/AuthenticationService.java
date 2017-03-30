package authentication.service;

import authentication.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by fabian.frontzek on 30.03.2017.
 */
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public static Set<UserDto> authenticateSequence() {
        Set<UserDto> users = new HashSet<UserDto>();
        users.add(authenticate("http://auth01:8080/authenticate/"));
        users.add(authenticate("http://auth02:8080/authenticate/"));
        users.add(authenticate("http://auth03:8080/authenticate/"));
        return users;
    }

    public static Set<UserDto> authenticateFuture() {
        Set<UserDto> users = new HashSet<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<UserDto> users01 = executorService.submit(() -> authenticate("http://auth01:8080/authenticate/"));
        Future<UserDto> users02 = executorService.submit(() -> authenticate("http://auth02:8080/authenticate/"));
        Future<UserDto> users03 = executorService.submit(() -> authenticate("http://auth03:8080/authenticate/"));

        try {
            users.add(users01.get());
            users.add(users02.get());
            users.add(users03.get());
        } catch (InterruptedException|ExecutionException e) {
            LOGGER.warn("Could not authenticate!");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Thrown Exception", e);
            }
        }

        return users;
    }

    private static UserDto authenticate(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, UserDto.class);
    }

}
