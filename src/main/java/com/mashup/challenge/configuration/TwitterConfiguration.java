package com.mashup.challenge.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * Created by davi on 5/28/17.
 */
@Configuration
@Profile("!integration-test")
public class TwitterConfiguration {

    @Bean
    public TwitterTemplate twitter(TwitterProperties properties) {
        return new TwitterTemplate(properties.getAppId(), properties.getAppSecret());
    }
}
