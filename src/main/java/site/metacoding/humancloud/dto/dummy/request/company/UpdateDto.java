package site.metacoding.humancloud.dto.dummy.request.company;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.humancloud.domain.company.Company;

@Getter
@Setter
public class UpdateDto {
	private String companyPassword;
	private String companyName;
	private String companyEmail;
	private String companyPhoneNumber;
	private String companyAddress;
	private String companyLogo;

	// public Company toEntity(String companyLogo) {
	// return new Company(this.companyPassword, this.companyName, this.companyEmail,
	// this.companyPhoneNumber,
	// this.companyAddress, companyLogo);
	// }
}
