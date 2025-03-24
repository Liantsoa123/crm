package site.easy.to.build.crm.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    public UserAPiController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public ResponseEntity<User> login (@RequestParam String email, @RequestParam String password ){
        User user = userService.findByEmail(email);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(!matches) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
