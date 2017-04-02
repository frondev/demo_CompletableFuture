package authentication.service;

import authentication.dto.UserDto;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private static final String URL_AUTH01 = "http://auth01:8080/authenticate/";
    private static final String URL_AUTH02 = "http://auth02:8080/authenticate/";
    private static final String URL_AUTH03 = "http://auth03:8080/authenticate/";
    private static List<String> urls;

    static {
        urls = new ArrayList<>();
        urls.add(URL_AUTH01);
        urls.add(URL_AUTH02);
        urls.add(URL_AUTH03);
    }

    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("AuthenticateThread-%40d")
            .setDaemon(true)
            .build();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(400, threadFactory);

    private AuthenticationService() {

    }

    public static Collection<UserDto> authenticateSequence() {
        logRuntime();
        List<UserDto> users = new ArrayList<>();
        users.add(authenticate(URL_AUTH01));
        users.add(authenticate(URL_AUTH02));
        users.add(authenticate(URL_AUTH03));
        return users;
    }

    public static Collection<UserDto> authenticateFuture() {
        logRuntime();
        List<Future<UserDto>> users = urls.parallelStream()
                .map(url -> executorService.submit(() -> authenticate(URL_AUTH01)))
                .collect(Collectors.toList());

        return users.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException|ExecutionException e) {
                LOGGER.debug("thrown exception", e);
            }
            return null;
        }).collect(Collectors.toList());
    }

    public static CompletableFuture authenticateCompletableFuture() {
        List<CompletableFuture<UserDto>> futureUsers = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> AuthenticationService.authenticate(url), executorService))
                .collect(Collectors.toList());
        logRuntime();
        return sequence(futureUsers);

    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.<T>toList())
        );
    }

    private static UserDto authenticate(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, UserDto.class);
    }

    private static void logRuntime() {
        LOGGER.info(executorService.toString());

        int size = 40;
        long memory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().totalMemory();
        long stepSize = maxMemory / size;
        long steps = memory / stepSize;

        StringBuilder s = new StringBuilder("[");
        for (int i=0; i<size; i++) {
            s.append((i < steps) ? "#" : "-");
        }
        s.append("]");

        LOGGER.info("Memory: {}", s.toString());
    }

}
