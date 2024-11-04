package backend.config;

import backend.model.entity.User;
import backend.mapper.UserMapper;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户详情服务实现
 * @field userMapper: 用户mapper
 * @function  loadUserByUsername: 获取当前用户
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class);

        List<String> fields = List.of("id", "username", "password");

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);

        User user;
        try {
            user = userMapper.selectUser(User.builder().username(username).build(), scope).getFirst();
        }catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User loadUserById(Integer id) throws UsernameNotFoundException {

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class);

        User user;
        try {
            user = userMapper.selectUser(User.builder().id(id).build(), scope).getFirst();
        }catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

}
