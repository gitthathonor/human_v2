package site.metacoding.humancloud.domain.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import site.metacoding.humancloud.dto.dummy.response.user.CompanyRankingDto;

public interface UserDao {
	public int save(User user);

	public User findById(Integer id);

	public List<User> findAll();

	public int update(@Param("user") User user);

	public int deleteById(Integer userId);

	public User findByUsername(String username);

	public User findAllUsername(String username);

	public List<CompanyRankingDto> findByRank();

}
