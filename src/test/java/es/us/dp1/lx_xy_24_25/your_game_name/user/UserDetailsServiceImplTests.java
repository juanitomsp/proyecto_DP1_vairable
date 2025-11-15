package es.us.dp1.lx_xy_24_25.your_game_name.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import es.us.dp1.lx_xy_24_25.your_game_name.configuration.services.UserDetailsServiceImpl;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserDetailsServiceImplTests {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_whenUserExists() {
        User u = new User();
        u.setId(10);
        u.setUsername("player10");
        u.setPassword("secret");

        Authorities auth = new Authorities();
        auth.setAuthority("PLAYER");
        u.setAuthority(auth);

        when(userRepository.findByUsername("player10")).thenReturn(Optional.of(u));

        UserDetails ud = userDetailsService.loadUserByUsername("player10");

        assertNotNull(ud);
        assertEquals("player10", ud.getUsername());
    }

    @Test
    void loadUserByUsername_whenUserNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing"));
    }
}