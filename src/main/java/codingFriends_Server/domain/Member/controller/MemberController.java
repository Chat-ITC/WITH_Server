package codingFriends_Server.domain.Member.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @GetMapping("/hello")
    public String helloController() {
        return "hello";
    }
}
