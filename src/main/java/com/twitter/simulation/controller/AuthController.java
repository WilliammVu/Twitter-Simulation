package com.twitter.simulation.controller;

import com.twitter.simulation.dto.ApiResponse;
import com.twitter.simulation.dto.LoginRequest;
import com.twitter.simulation.dto.SignupRequest;
import com.twitter.simulation.dto.UserResponse;
import com.twitter.simulation.models.User;
import com.twitter.simulation.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private TwitterService twitterService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        try {
            boolean success = twitterService.login(request.getUsername(), request.getPassword());

            if (success) {
                User currentUser = twitterService.getCurrentUser();
                UserResponse userResponse = twitterService.convertToUserResponse(currentUser, currentUser);
                return ResponseEntity.ok(ApiResponse.success("Login successful", userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during login"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody SignupRequest request) {
        try {
            boolean success = twitterService.createUser(request.getUsername(), request.getPassword());

            if (success) {
                // Auto-login after successful signup
                twitterService.login(request.getUsername(), request.getPassword());
                User currentUser = twitterService.getCurrentUser();
                UserResponse userResponse = twitterService.convertToUserResponse(currentUser, currentUser);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Account created successfully", userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Username already exists or invalid credentials. Password must be 6-16 characters with at least one digit and special character."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during signup"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        try {
            twitterService.logout();
            return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred during logout"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser != null) {
                UserResponse userResponse = twitterService.convertToUserResponse(currentUser, currentUser);
                return ResponseEntity.ok(ApiResponse.success(userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Not logged in"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred"));
        }
    }
}
