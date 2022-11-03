package site.metacoding.humancloud.domain.company;

import java.util.List;
import java.util.Optional;

import site.metacoding.humancloud.dto.company.CompanyRespDto.CompanyFindById;
import site.metacoding.humancloud.dto.dummy.response.page.PagingDto;

public interface CompanyDao {
	public void save(Company company);

	public Optional<CompanyFindById> findById(Integer id);

	public List<Company> findAll(int startNum);

	public void update(Company company);

	public void deleteById(Integer id);

	public Company findByUsername(String companyUsername);

	public Company findAllUsername(String companyUsername);

	public PagingDto paging(Integer page);
}
