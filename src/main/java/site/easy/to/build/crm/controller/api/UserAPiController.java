package site.easy.to.build.crm.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserAPiController {

    private final UserService userService;

    public UserAPiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<User> login (@RequestParam String email ){
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
