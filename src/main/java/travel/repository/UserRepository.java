package travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import travel.domain.User;

public interface UserRepository extends JpaRepository<User, Long> { }
