package site.metacoding.humancloud.domain.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import site.metacoding.humancloud.dto.dummy.response.user.CompanyRankingDto;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindByAllUsername;
import site.metacoding.humancloud.dto.user.UserRespDto.UserFindById;

public interface UserDao {
	public int save(User user);

	public UserFindById findById(Integer id);

	public List<User> findAll();

	public int update(@Param("user") User user);

	public int deleteById(Integer userId);

	public User findByUsername(String username);

	public UserFindByAllUsername findAllUsername(String username);

	public List<CompanyRankingDto> findByRank();

}
