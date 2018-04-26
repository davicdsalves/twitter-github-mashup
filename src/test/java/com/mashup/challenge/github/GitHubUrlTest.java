package com.mashup.challenge.github;

import com.mashup.challenge.configuration.GitHubProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by davi on 5/29/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class GitHubUrlTest {
    @Mock
    private GitHubProperties properties;

    @InjectMocks
    private GitHubUrl target;

    @Test
    public void createSearchURI() throws Exception {
        String parameterName = "q";
        String searchKeyword = "reactive";
        String searchUrl = "http://api.github.com/search/repositories";
        GitHubProperties.Endpoint endpoint = new GitHubProperties.Endpoint();
        endpoint.setParameterName(parameterName);
        endpoint.setSearchKeyword(searchKeyword);
        endpoint.setSearchUrl(searchUrl);
        when(properties.getEndpoint()).thenReturn(endpoint);

        String expected = String.format("%s?%s=%s", searchUrl, parameterName, searchKeyword);
        URI uri = target.createSearchURI();
        assertThat(uri.toString(), is(equalTo(expected)));
    }

}