package com.twitter.simulation.controller;

import com.twitter.simulation.dto.ApiResponse;
import com.twitter.simulation.dto.TweetResponse;
import com.twitter.simulation.dto.UserResponse;
import com.twitter.simulation.models.Tweet;
import com.twitter.simulation.models.User;
import com.twitter.simulation.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private TwitterService twitterService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<UserResponse>> searchUser(@RequestParam String query) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to search users"));
            }

            User user = twitterService.search(query);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            UserResponse userResponse = twitterService.convertToUserResponse(user, currentUser);
            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while searching"));
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(@PathVariable String username) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to view profiles"));
            }

            User user = twitterService.getUserByUsername(username.toLowerCase());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            UserResponse userResponse = twitterService.convertToUserResponse(user, currentUser);
            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while fetching the profile"));
        }
    }

    @GetMapping("/{username}/tweets")
    public ResponseEntity<ApiResponse<List<TweetResponse>>> getUserTweets(@PathVariable String username) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to view tweets"));
            }

            User user = twitterService.getUserByUsername(username.toLowerCase());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            // Combine original tweets and retweets
            List<TweetResponse> allTweets = twitterService.getUserTweetsWithRetweets(user, currentUser);

            return ResponseEntity.ok(ApiResponse.success(allTweets));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while fetching tweets"));
        }
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ApiResponse<UserResponse>> followUser(@PathVariable String username) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to follow users"));
            }

            User userToFollow = twitterService.getUserByUsername(username.toLowerCase());

            if (userToFollow == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            if (currentUser.equals(userToFollow)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("You cannot follow yourself"));
            }

            twitterService.follow(currentUser, userToFollow);
            UserResponse userResponse = twitterService.convertToUserResponse(userToFollow, currentUser);

            return ResponseEntity.ok(ApiResponse.success("User followed successfully", userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while following the user"));
        }
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ApiResponse<UserResponse>> unfollowUser(@PathVariable String username) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to unfollow users"));
            }

            User userToUnfollow = twitterService.getUserByUsername(username.toLowerCase());

            if (userToUnfollow == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            twitterService.unfollow(currentUser, userToUnfollow);
            UserResponse userResponse = twitterService.convertToUserResponse(userToUnfollow, currentUser);

            return ResponseEntity.ok(ApiResponse.success("User unfollowed successfully", userResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while unfollowing the user"));
        }
    }

    @GetMapping("/suggested")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getSuggestedFriends() {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to get suggestions"));
            }

            User[] suggestedUsers = twitterService.getSuggestedFriends(currentUser);
            List<UserResponse> suggestions = twitterService.convertToUserResponseList(suggestedUsers, currentUser);

            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while fetching suggestions"));
        }
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getFollowers(@PathVariable String username) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to view followers"));
            }

            User user = twitterService.getUserByUsername(username.toLowerCase());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            List<UserResponse> followers = twitterService.convertToUserResponseList(
                user.getFollowers().toArray(new User[0]),
                currentUser
            );

            return ResponseEntity.ok(ApiResponse.success(followers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while fetching followers"));
        }
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getFollowing(@PathVariable String username) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to view following"));
            }

            User user = twitterService.getUserByUsername(username.toLowerCase());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            List<UserResponse> following = twitterService.convertToUserResponseList(
                user.getFollowing().toArray(new User[0]),
                currentUser
            );

            return ResponseEntity.ok(ApiResponse.success(following));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while fetching following"));
        }
    }
}
