package authentication.resources;

import authentication.dto.UserDto;
import authentication.service.AuthenticationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Set;

@RestController
public class AuthenticationResource {

    @RequestMapping("/authenticate/sequence/")
    @ResponseBody
    public Set<UserDto> authenticatSequence() {
        return AuthenticationService.authenticateSequence();
    }

    @RequestMapping("/authenticate/future/")
    @ResponseBody
    public Set<UserDto> authenticateFuture() {
        return AuthenticationService.authenticateFuture();
    }

    @RequestMapping("/authenticate/completablefuture/")
    @ResponseBody
    public String authenticateCompletableFuture() {
        throw new NotImplementedException();
    }

}
