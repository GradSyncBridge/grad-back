package backend.service.impl;

import backend.model.entity.User;
import backend.service.UserSecurityService;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return
     */
    @Override
    public User getUserById(Integer id) {
        return null;
    }
}
