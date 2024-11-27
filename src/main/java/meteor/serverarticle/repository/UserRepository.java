package meteor.serverarticle.repository;

import meteor.serverarticle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //получить пользователя по имени
    Optional<User> findByUsername(String username);
}
