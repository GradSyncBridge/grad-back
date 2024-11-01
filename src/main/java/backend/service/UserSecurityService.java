package backend.service;

import backend.model.entity.User;

public interface UserSecurityService {
    User getUserById(Integer id);
}
