package backend.service;

import backend.model.entity.User;

public interface UserSecurityService {
    /**
     * 根据id获取用户
     * @param id 用户id
     * @return
     */
    User getUserById(Integer id);
}
