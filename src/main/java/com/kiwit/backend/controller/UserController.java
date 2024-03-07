package com.kiwit.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Operation(summary = "sign up", description = "sign up with email, password (request body as basic user info)")
    @PostMapping(value = "/sign-up")
    public void signUp() {}

    @Operation(summary = "sign in", description = "sign in with email, password")
    @PostMapping(value = "/sign-in")
    public void signIn() {}

    @Operation(summary = "refresh jwt token", description = "refresh jwt access token & refresh token")
    @PatchMapping("/refresh")
    public void refreshToken() {
//        여기서 fcm refresh도 할까 고민(하면 좋을 것 같음)
    }

    @Operation(summary = "my info", description = "get my info")
    @GetMapping()
    public void myInfo() {}

    @Operation(summary = "edit user", description = "edit my info")
    @PatchMapping()
    public void editUser() {}

    @Operation(summary = "(soft) delete user", description = "soft delete user")
    @PatchMapping()
    public void withdrawUser() {}

}
