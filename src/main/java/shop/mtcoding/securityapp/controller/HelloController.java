
package shop.mtcoding.securityapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.securityapp.core.auth.MyUserDetails;
import shop.mtcoding.securityapp.core.jwt.MyJwtProvider;
import shop.mtcoding.securityapp.dto.ResponseDTO;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.dto.UserResponse;
import shop.mtcoding.securityapp.model.UserRepository;
import shop.mtcoding.securityapp.service.UserService;

/*
 * 로그 레벨 : trace, debug, info, warning, error
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class HelloController {

    private final UserService userService;

    private final AuthenticationManager manager;

    private final UserRepository userRepository;

    @Value("${meta.name}")
    private String name;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDTO loginDTO) {
        String jwt = userService.로그인(loginDTO);
        return ResponseEntity.ok().header(MyJwtProvider.HEADER, jwt).body("로그인 완료");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> userCheck(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        Long id = myUserDetails.getUser().getId();
        String role = myUserDetails.getUser().getRole();
        return ResponseEntity.ok().body(id + " : " + role);
    }

    @GetMapping("/")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok().body(name);
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(UserRequest.JoinDTO joinDTO) {
        // select 됨
        UserResponse.JoinDTO data = userService.회원가입(joinDTO);
        // select 안됨
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(data);
        return ResponseEntity.ok().body(responseDTO);
    }
}