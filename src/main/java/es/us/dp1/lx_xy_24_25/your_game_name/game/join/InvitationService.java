package es.us.dp1.lx_xy_24_25.your_game_name.game.join;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class InvitationService {
    private static final Logger log = LoggerFactory.getLogger(InvitationService.class);
    private final RestTemplate restTemplate;

    public InvitationService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ResponseEntity<String> sendInvitation(Long receiverId, String gameMode) {
        if (gameMode == null) {
            gameMode = "default";
        }

        InvitationRequest payload = new InvitationRequest(receiverId, gameMode);
        try {
            return restTemplate.postForEntity("/games/invite", payload, String.class);
        } catch (RestClientException e) {
            log.error("Error sending invitation: {}", e.getMessage(), e);
            throw e;
        }
    }


    public void updateStatus(Long id, String status) {
        try {
            String url = String.format("/games/invite/%d/status?status=%s", id, status);
            restTemplate.put(url, null);
        } catch (RestClientException e) {
            log.error("Error updating invitation status: {}", e.getMessage(), e);
            throw e;
        }
    }


    public ResponseEntity<String> getReceived(Long userId) {
        try {
            String url = String.format("/games/invite/received/%d", userId);
            return restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            log.error("Error fetching received invitations: {}", e.getMessage(), e);
            throw e;
        }
    }
}
