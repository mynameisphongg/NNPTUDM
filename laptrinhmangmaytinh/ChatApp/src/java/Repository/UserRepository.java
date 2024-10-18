package Repository;

import model.User;

public interface UserRepository {
    User findUserByUsername(String username);
    User findUserById(int id); // Thêm phương thức này
    void save(User user);
}
