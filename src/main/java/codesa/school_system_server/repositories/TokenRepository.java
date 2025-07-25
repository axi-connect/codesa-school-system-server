package codesa.school_system_server.repositories;

import codesa.school_system_server.models.Token;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
}
