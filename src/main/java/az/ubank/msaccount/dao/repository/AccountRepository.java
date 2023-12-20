package az.ubank.msaccount.dao.repository;

import az.ubank.msaccount.dao.entity.AccountEntity;
import az.ubank.msaccount.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<Page<AccountEntity>> findAllByCustomerPinAndStatus(String customerPin,
                                                                Status status,
                                                                Pageable pageable);

    Optional<Page<AccountEntity>> findAllByStatus(Status status, Pageable pageable);

    Optional<AccountEntity> findByAccountId(String accountId);
}
