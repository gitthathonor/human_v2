package site.metacoding.humancloud.domain.user;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.metacoding.humancloud.dto.user.UserReqDto.UserUpdateReqDto;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class User {

	private Integer userId;
	private String username;
	private String password;
	private String name;
	private String email;
	private String phoneNumber;
	private Timestamp createdAt;

	public User update(UserUpdateReqDto userUpdateReqDto) {
		return User.builder()
				.password(userUpdateReqDto.getPassword())
				.name(userUpdateReqDto.getName())
				.email(userUpdateReqDto.getEmail())
				.phoneNumber(userUpdateReqDto.getPhoneNumber())
				.build();
	}

}
