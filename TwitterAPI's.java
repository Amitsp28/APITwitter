package com.test.API;


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TwitterTestAPI {

   
    private static final String CLIENT_ID = "your_client_id";
    private static final String CLIENT_SECRET = "your_client_secret";
	

    @GetMapping("/userSearch")
    public ResponseEntity<?> userSearch(@RequestHeader("Client-ID") String clientId, @RequestParam String username) {
        if (!clientId.equals(CLIENT_ID)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Client-ID");
        }

        Twitter twitter = new TwitterTemplate(CLIENT_ID, CLIENT_SECRET);
        TwitterProfile profile = twitter.userOperations().getUserProfile(username);

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/getUserTweets")
    public ResponseEntity<?> getUserTweets(@RequestHeader("Client-ID") String clientId, @RequestParam String username) {
        if (!clientId.equals(CLIENT_ID)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Client-ID");
        }

        Twitter twitter = new TwitterTemplate(CLIENT_ID, CLIENT_SECRET);
        List<Object> tweets = twitter.timelineOperations().getUserTimeline(username, 10)
            .stream()
            .map(new Function<Tweet, Object>() {
				public Object apply(Tweet status) {
					return status.getText();
				}
			})
            .collect(Collectors.toList());

        return ResponseEntity.ok(tweets);
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterTestAPI.class, args);
    }
}


