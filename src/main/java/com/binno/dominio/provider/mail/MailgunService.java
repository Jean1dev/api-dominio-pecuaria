package com.binno.dominio.provider.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
@EnableAsync
@RequiredArgsConstructor
public class MailgunService implements MailProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailgunService.class);

    private Boolean jaAdicionadoKeyNoRestTemplate = false;

    @Value("${mail.domain}")
    private String domain;

    @Value("${mail.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final Environment environment;

    @Override
    @Async
    public void send(SendEmailPayload payload) {
        String[] activeProfiles = environment.getActiveProfiles();
        String profiles = Arrays.toString(activeProfiles);
        LOGGER.info("active profiles: {}", Arrays.toString(activeProfiles));
        if (profiles.contains("local") || profiles.contains("test"))
            return;

        String uri = UriComponentsBuilder.fromHttpUrl("https://api.mailgun.net/v3/")
                .pathSegment(domain, "messages")
                .build()
                .toUriString();

        if (!jaAdicionadoKeyNoRestTemplate) {
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("api", apiKey));
            jaAdicionadoKeyNoRestTemplate = true;
        }

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", payload.getFrom());
        map.add("to", payload.getTo());
        map.add("subject", payload.getSubject());
        map.add("text", payload.getText());

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, null);
        restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Object.class).getBody();
        LOGGER.info("Email enviado para " + payload.getTo());
    }

    @Override
    public String getFrom() {
        return "Binno apps <equipe@central.binnoapp.com>";
    }
}
