package backend.service;

import backend.model.entity.User;

public interface UserService {
    User getUserById(Integer id);
}
