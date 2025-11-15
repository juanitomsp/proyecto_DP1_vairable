package es.us.dp1.lx_xy_24_25.your_game_name.invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.us.dp1.lx_xy_24_25.your_game_name.game.join.InvitationRequest;
import es.us.dp1.lx_xy_24_25.your_game_name.game.join.InvitationService;

@SpringBootTest
public class InvitationServiceTest {

    @Mock
    private RestTemplate restTemplate;
    
    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    private InvitationService invitationService;

    private static final Long USER_ID = 1L;
    private static final String GAME_MODE = "default";

    @BeforeEach
    void setup() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        invitationService = new InvitationService(restTemplateBuilder);
    }
    @Test
    void shouldSendInvitation() {
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Invitation sent", HttpStatus.OK);
        when(restTemplate.postForEntity(eq("/games/invite"), any(InvitationRequest.class), eq(String.class)))
            .thenReturn(expectedResponse);

        ResponseEntity<String> response = invitationService.sendInvitation(USER_ID, GAME_MODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Invitation sent", response.getBody());
    }

    @Test
    void shouldHandleErrorWhenSendingInvitation() {
        when(restTemplate.postForEntity(eq("/games/invite"), any(InvitationRequest.class), eq(String.class)))
            .thenThrow(new RestClientException("Error sending invitation"));

        assertThrows(RestClientException.class, () -> {
            invitationService.sendInvitation(USER_ID, GAME_MODE);
        });
    }

    @Test
    void shouldUpdateInvitationStatus() {
        invitationService.updateStatus(USER_ID, "ACCEPTED");

        verify(restTemplate).put("/games/invite/1/status?status=ACCEPTED", null);
    }

    @Test
    void shouldGetReceivedInvitations() {
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("[]", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenReturn(expectedResponse);

        ResponseEntity<String> response = invitationService.getReceived(USER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[]", response.getBody());
    }

    @Test
    void shouldHandleErrorWhenGettingReceivedInvitations() {
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
            .thenThrow(new RestClientException("Error fetching invitations"));

        assertThrows(RestClientException.class, () -> {
            invitationService.getReceived(USER_ID);
        });
    }
}