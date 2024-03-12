package com.kiwit.backend.controller;

import com.kiwit.backend.dto.*;
import com.kiwit.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "sign up", description = "sign up with email, password (request body as basic user info)")
    @PostMapping(value = "/sign-up")
    public ResponseEntity<SignInResDTO>
    signUp(@RequestBody SignUpReqDTO signUpReqDTO) {
        SignInResDTO signInResDTO = userService.signUp(signUpReqDTO);
        return ResponseEntity.status(HttpStatus.OK).body(signInResDTO);
    }

    @Operation(summary = "sign in", description = "sign in with email, password")
    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInResDTO>
    signIn(@RequestBody SignInReqDTO signInReqDTO) {
        SignInResDTO signInResDTO = userService.signIn(signInReqDTO);
        return ResponseEntity.status(HttpStatus.OK).body(signInResDTO);
    }

    @Operation(summary = "sign out", description = "remove refresh token")
    @PatchMapping(value = "/sign-out")
    public ResponseEntity<Void>
    signOut(@RequestBody SignInReqDTO signInReqDTO) {
        userService.signOut();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "refresh jwt token", description = "refresh jwt access token & refresh token")
    @PatchMapping("/refresh")
    public ResponseEntity<SignInResDTO>
    refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
//        여기서 fcm refresh도 할까 고민(하면 좋을 것 같음)
        SignInResDTO signInResDTO = userService.refreshToken(refreshTokenDTO);
        return ResponseEntity.status(HttpStatus.OK).body(signInResDTO);
    }

    @Operation(summary = "my info", description = "get my info")
    @GetMapping()
    public ResponseEntity<UserDTO>
    getMyInfo() {
        UserDTO userDTO = userService.myInfo();
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Operation(summary = "edit user", description = "edit my info")
    @PatchMapping()
    public ResponseEntity<UserDTO>
    editUser(@RequestBody UserDTO userDTO) {
        UserDTO resDTO = userService.editUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(resDTO);
    }

    @Operation(summary = "(soft) delete user", description = "soft delete user")
    @DeleteMapping()
    public ResponseEntity<Void>
    withdrawUser() {
        userService.withdrawUser();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "My trophy awarded list.", description = "my trophies")
    @GetMapping("/trophy")
    public ResponseEntity<TrophyDTO>
    getMyTrophyList() {
        TrophyDTO resDTO = userService.getMyTrophyList();
        return ResponseEntity.status(HttpStatus.OK).body(resDTO);
    }

    @Operation(summary = "My trophy awarded most recently.", description = "my latest trophy")
    @GetMapping("/trophy/latest")
    public ResponseEntity<TrophyDTO>
    getMyTrophyLatest() {
        TrophyDTO resDTO = userService.getMyTrophyLatest();
        return ResponseEntity.status(HttpStatus.OK).body(resDTO);
    }

    @Operation(summary = "User Stat", description = "my activity stat")
    @GetMapping("/stat")
    public ResponseEntity<StatDTO>
    getMyStat() {
        StatDTO resDTO = userService.getMyStat();
        return ResponseEntity.status(HttpStatus.OK).body(resDTO);
    }

}
