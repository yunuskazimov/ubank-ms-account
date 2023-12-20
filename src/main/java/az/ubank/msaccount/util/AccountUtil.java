package az.ubank.msaccount.util;

import az.ubank.msaccount.dao.entity.AccountEntity;
import az.ubank.msaccount.dao.repository.AccountRepository;
import az.ubank.msaccount.enums.Status;
import az.ubank.msaccount.exception.AccountNotFoundException;
import az.ubank.msaccount.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountUtil {
    private final AccountRepository repo;

    public Page<AccountEntity> findAllByStatusIsActive(Pageable pageable) {
        log.info("service util findAllByStatusIsActive started");
        Optional<Page<AccountEntity>> entityList = repo.findAllByStatus(Status.ACTIVE, pageable);
        if (entityList.isEmpty()) {
            log.info("service util findAllByStatusIsActive run exception");
            throw AccountNotFoundException.of(ErrorCodes.NOT_FOUND, "Account Not Found");
        } else {
            log.info("service util findAllByStatusIsActive completed");
            return entityList.get();
        }
    }

    public Page<AccountEntity> findAllByCustomerIdAndStatusIsActive(String customerPin,
                                                                    Pageable pageable) {
        log.info("service util findAllByCustomerIdAndStatusIsActive started");
        Optional<Page<AccountEntity>> entityList = repo
                .findAllByCustomerPinAndStatus(customerPin, Status.ACTIVE, pageable);
        if (entityList.isEmpty()) {
            log.info("service util findAllByCustomerIdAndStatusIsActive run exception");
            throw AccountNotFoundException.of(ErrorCodes.NOT_FOUND, "Account Not Found");
        } else {
            log.info("service util findAllByCustomerIdAndStatusIsActive completed");
            return entityList.get();
        }
    }

    public AccountEntity findById(String uuid) {
        log.info("service util findById started");
        AccountEntity entity = repo.findByAccountId(uuid).orElseThrow(
                () -> AccountNotFoundException.of(ErrorCodes.NOT_FOUND, "Account Not Found"));
        log.info("service util findById completed");
        return entity;

    }
}
