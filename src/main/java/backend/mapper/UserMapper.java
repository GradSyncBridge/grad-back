package backend.mapper;

import backend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 插入用户
     * @param user 插入值
     */
    void insertUser(User user);

    /**
     * 查询用户
     * @param user 查询条件
     * @param scope 查询返回的字段
     * @return 用户列表
     */
    List<User> selectUser(User user, Map<String, Boolean> scope);

    /**
     * 更新用户
     * @param user 更新值
     * @param scope 更新的条件
     */
    void updateUser(User user, Map<String, Boolean> scope);
}
