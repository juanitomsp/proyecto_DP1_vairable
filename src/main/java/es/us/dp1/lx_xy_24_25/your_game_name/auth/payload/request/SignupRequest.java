package es.us.dp1.lx_xy_24_25.your_game_name.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	
	@NotBlank
	private String username;
	
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String authority;

	@NotBlank
	private String password;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
		
	
	private String city;
	private String address;
	private String telephone;

}