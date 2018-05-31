package app.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RequestUtil {
    public static <T> T GetRequest(String uri, Class<T> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, clazz);
    }

    public static <T> T PostRequest(String uri, MultiValueMap<String, String> payload, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(uri, request, clazz);
    }
}
