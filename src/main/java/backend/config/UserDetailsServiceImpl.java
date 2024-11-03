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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<String> fields = List.of("username");

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);

        User user = userMapper.selectUser(User.builder().username(username).build(), scope).get(0);

        if (user == null)
            throw new UsernameNotFoundException("User not found");

        return user;
    }

}
