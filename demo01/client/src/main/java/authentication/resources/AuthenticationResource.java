package authentication.resources;

import authentication.dto.UserDto;
import authentication.service.AuthenticationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@RestController
public class AuthenticationResource {

    @RequestMapping("/authenticate/sequence/")
    @ResponseBody
    public Collection<UserDto> authenticatSequence() {
        return AuthenticationService.authenticateSequence();
    }

    @RequestMapping("/authenticate/future/")
    @ResponseBody
    public Collection<UserDto> authenticateFuture() {
        return AuthenticationService.authenticateFuture();
    }

    @RequestMapping("/authenticate/completablefuture/")
    @ResponseBody
    public CompletableFuture<UserDto> authenticateCompletableFuture() {
        return AuthenticationService.authenticateCompletableFuture();
    }
}
