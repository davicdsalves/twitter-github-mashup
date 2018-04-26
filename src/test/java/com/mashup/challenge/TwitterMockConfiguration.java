package com.mashup.challenge;

import com.mashup.challenge.configuration.TwitterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * Created by davi on 5/29/17.
 */
@Configuration
public class TwitterMockConfiguration {

    @Bean
    public TwitterTemplate twitter(TwitterProperties properties) {
        return new TwitterTemplate(properties.getAppId(), properties.getAppSecret(), "", "");
    }

}
