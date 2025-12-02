package com.twitter.simulation.controller;

import com.twitter.simulation.dto.ApiResponse;
import com.twitter.simulation.dto.TweetRequest;
import com.twitter.simulation.dto.TweetResponse;
import com.twitter.simulation.models.Tweet;
import com.twitter.simulation.models.User;
import com.twitter.simulation.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    @Autowired
    private TwitterService twitterService;

    @PostMapping
    public ResponseEntity<ApiResponse<TweetResponse>> postTweet(@RequestBody TweetRequest request) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to post a tweet"));
            }

            if (request.getBody() == null || request.getBody().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Tweet body cannot be empty"));
            }

            if (request.getBody().length() > 280) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Tweet cannot exceed 280 characters"));
            }

            Tweet tweet = twitterService.postTweet(currentUser, request.getBody());
            TweetResponse tweetResponse = twitterService.convertToTweetResponse(tweet, currentUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Tweet posted successfully", tweetResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while posting the tweet"));
        }
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<List<TweetResponse>>> getFeed() {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to view your feed"));
            }

            Tweet[] feedTweets = twitterService.getFeed(currentUser);
            List<TweetResponse> feedResponse = twitterService.convertToTweetResponseList(feedTweets, currentUser);

            return ResponseEntity.ok(ApiResponse.success(feedResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while fetching the feed"));
        }
    }

    @PostMapping("/{tweetId}/like")
    public ResponseEntity<ApiResponse<TweetResponse>> likeTweet(@PathVariable int tweetId) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to like a tweet"));
            }

            Tweet tweet = twitterService.getTweet(tweetId);

            if (tweet == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Tweet not found"));
            }

            twitterService.likeTweet(currentUser, tweet);
            TweetResponse tweetResponse = twitterService.convertToTweetResponse(tweet, currentUser);

            return ResponseEntity.ok(ApiResponse.success("Tweet liked", tweetResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while liking the tweet"));
        }
    }

    @DeleteMapping("/{tweetId}/like")
    public ResponseEntity<ApiResponse<TweetResponse>> unlikeTweet(@PathVariable int tweetId) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to unlike a tweet"));
            }

            Tweet tweet = twitterService.getTweet(tweetId);

            if (tweet == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Tweet not found"));
            }

            twitterService.unlikeTweet(currentUser, tweet);
            TweetResponse tweetResponse = twitterService.convertToTweetResponse(tweet, currentUser);

            return ResponseEntity.ok(ApiResponse.success("Tweet unliked", tweetResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while unliking the tweet"));
        }
    }

    @PostMapping("/{tweetId}/retweet")
    public ResponseEntity<ApiResponse<TweetResponse>> retweet(@PathVariable int tweetId) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to retweet"));
            }

            Tweet tweet = twitterService.getTweet(tweetId);

            if (tweet == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Tweet not found"));
            }

            twitterService.retweet(currentUser, tweet);
            TweetResponse tweetResponse = twitterService.convertToTweetResponse(tweet, currentUser);

            return ResponseEntity.ok(ApiResponse.success("Tweet retweeted", tweetResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while retweeting"));
        }
    }

    @DeleteMapping("/{tweetId}/retweet")
    public ResponseEntity<ApiResponse<TweetResponse>> unretweet(@PathVariable int tweetId) {
        try {
            User currentUser = twitterService.getCurrentUser();

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("You must be logged in to unretweet"));
            }

            Tweet tweet = twitterService.getTweet(tweetId);

            if (tweet == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Tweet not found"));
            }

            twitterService.unretweet(currentUser, tweet);
            TweetResponse tweetResponse = twitterService.convertToTweetResponse(tweet, currentUser);

            return ResponseEntity.ok(ApiResponse.success("Tweet unretweeted", tweetResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("An error occurred while unretweeting"));
        }
    }
}
