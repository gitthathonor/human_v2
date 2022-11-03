package site.metacoding.humancloud.domain.company;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
<<<<<<< HEAD
import site.metacoding.humancloud.dto.AuthUser;
import site.metacoding.humancloud.dto.company.CompanyReqDto.CompanyUpdateReqDto;
=======
import site.metacoding.humancloud.dto.dummy.request.company.UpdateDto;
>>>>>>> master

@NoArgsConstructor
@Getter
@Setter
public class Company {

	private Integer companyId;
	private String companyUsername;
	private String companyPassword;
	private String companyName;
	private String companyEmail;
	private String companyPhoneNumber;
	private String companyAddress;
	private String companyLogo;
	private Timestamp companyCreatedAt;

<<<<<<< HEAD
	@Override
	public Integer getId() {
		return companyId;
	}
=======
	// public Company(String companyUsername, String companyPassword, String
	// companyName, String companyEmail,
	// String companyPhoneNumber, String companyAddress, String companyLogo) {
	// this.companyUsername = companyUsername;
	// this.companyPassword = companyPassword;
	// this.companyName = companyName;
	// this.companyEmail = companyEmail;
	// this.companyPhoneNumber = companyPhoneNumber;
	// this.companyAddress = companyAddress;
	// this.companyLogo = companyLogo;
	// }

	// public Company(String companyPassword, String companyName, String
	// companyEmail,
	// String companyPhoneNumber, String companyAddress, String companyLogo) {
	// this.companyPassword = companyPassword;
	// this.companyName = companyName;
	// this.companyEmail = companyEmail;
	// this.companyPhoneNumber = companyPhoneNumber;
	// this.companyAddress = companyAddress;
	// this.companyLogo = companyLogo;
	// }

	// @Override
	// public Integer getId() {
	// return companyId;
	// }
>>>>>>> master

	// @Override
	// public String getUsername() {
	// return companyUsername;
	// }

	@Builder
	public Company(Integer companyId, String companyUsername, String companyPassword, String companyName,
			String companyEmail, String companyPhoneNumber, String companyAddress, String companyLogo,
			Timestamp companyCreatedAt) {
		this.companyId = companyId;
		this.companyUsername = companyUsername;
		this.companyPassword = companyPassword;
		this.companyName = companyName;
		this.companyEmail = companyEmail;
		this.companyPhoneNumber = companyPhoneNumber;
		this.companyAddress = companyAddress;
		this.companyLogo = companyLogo;
		this.companyCreatedAt = companyCreatedAt;
	}

	public void update(CompanyUpdateReqDto companyUpdateReqDto) {
		this.companyPassword = companyUpdateReqDto.getCompanyPassword();
		this.companyName = companyUpdateReqDto.getCompanyName();
		this.companyEmail = companyUpdateReqDto.getCompanyEmail();
		this.companyPhoneNumber = companyUpdateReqDto.getCompanyPhoneNumber();
		this.companyAddress = companyUpdateReqDto.getCompanyAddress();
		this.companyLogo = companyUpdateReqDto.getCompanyLogo();
	}

	public void toPhoneNumber(String num) {
		this.companyPhoneNumber = num;
	}
}
