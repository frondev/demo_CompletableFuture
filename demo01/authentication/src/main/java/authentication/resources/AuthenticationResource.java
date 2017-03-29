package authentication.resources;

import authentication.dto.UserDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationResource {

    @RequestMapping("/authenticate/")
    @ResponseBody
    public UserDto authenticate() throws InterruptedException {
        Thread.sleep(500L);
        return new UserDto("fabian.frontzek", "Fabian", "Frontzek");
    }

}
