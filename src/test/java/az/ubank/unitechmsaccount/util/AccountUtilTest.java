package az.ubank.unitechmsaccount.util;

import az.ubank.msaccount.dao.entity.AccountEntity;
import az.ubank.msaccount.dao.repository.AccountRepository;
import az.ubank.msaccount.dto.AccountResponseDto;
import az.ubank.msaccount.enums.CurrencyType;
import az.ubank.msaccount.dto.PageDto;
import az.ubank.msaccount.enums.Status;
import az.ubank.msaccount.exception.AccountNotFoundException;
import az.ubank.msaccount.exception.ErrorCodes;
import az.ubank.msaccount.util.AccountUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountUtilTest {

    private static final String CUSTOMER_PIN = "1OYG677";
    private static final String ACCOUNT_ID = "dda8850c-5bff-4ee3-a118-9684e2fe004d";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);

    @Mock
    private AccountRepository accountRepo;
    @InjectMocks
    private AccountUtil accountUtil;

    @BeforeEach
    void setUp() {
        accountUtil = new AccountUtil(accountRepo);
    }

    @Test
    void findAllByStatusIsActive() {
        //given
        AccountEntity entity = getAccountEntity();
        Optional<Page<AccountEntity>> accountEntities = Optional.of(new PageImpl<>(List.of(entity)));
        PageDto pageDto = getPageDto();
        var expected = accountEntities.get();

        //when
        when(accountRepo.findAllByStatus(Status.ACTIVE, getPageable(pageDto)))
                .thenReturn(accountEntities);

        //then
        Page<AccountEntity> actual = accountUtil.findAllByStatusIsActive(getPageable(pageDto));

        assertEquals(expected, actual);
        verify(accountRepo, times(1))
                .findAllByStatus(Status.ACTIVE, getPageable(pageDto));
    }

    @Test
    void findAllByStatusIsActive_WhenAccountNotFound_ShouldThrowAccountNotFoundException() {
        //given
        PageDto pageDto = getPageDto();

        //when
        when(accountRepo.findAllByStatus(Status.ACTIVE, getPageable(pageDto)))
                .thenReturn(Optional.empty());

        //then
        AccountNotFoundException actual = assertThrows(AccountNotFoundException.class,
                () -> accountUtil.findAllByStatusIsActive(getPageable(pageDto)));

        Assertions.assertEquals(ErrorCodes.NOT_FOUND, actual.getCode());

        verify(accountRepo, times(1))
                .findAllByStatus(Status.ACTIVE, getPageable(pageDto));


    }

    @Test
    void findAllByCustomerIdAndStatusIsActive() {
        // given
        AccountEntity entity = getAccountEntity();
        PageDto pageDto = getPageDto();
        Optional<Page<AccountEntity>> accountEntities = Optional.of(new PageImpl<>(List.of(entity)));
        var expected = accountEntities.get();
        //when
        when(accountRepo.findAllByCustomerPinAndStatus(CUSTOMER_PIN, Status.ACTIVE, getPageable(pageDto)))
                .thenReturn(accountEntities);
        //actual
        Page<AccountEntity> actual = accountUtil
                .findAllByCustomerIdAndStatusIsActive(CUSTOMER_PIN, getPageable(pageDto));

        assertEquals(actual, expected);

        verify(accountRepo, times(1))
                .findAllByCustomerPinAndStatus(CUSTOMER_PIN, Status.ACTIVE, getPageable(pageDto));
    }

    @Test
    void findAllByCustomerIdAndStatus_WhenAccountNotFound_ShouldThrowsAccountNotFoundException() {
        //given
        PageDto pageDto = getPageDto();

        //when
        when(accountRepo
                .findAllByCustomerPinAndStatus(CUSTOMER_PIN, Status.ACTIVE, getPageable(pageDto)))
                .thenReturn(Optional.empty());

        //actual

        AccountNotFoundException actual = assertThrows(AccountNotFoundException.class,
                () -> accountUtil
                        .findAllByCustomerIdAndStatusIsActive(CUSTOMER_PIN, getPageable(pageDto)));

        Assertions.assertEquals(ErrorCodes.NOT_FOUND, actual.getCode());

        verify(accountRepo, times(1))
                .findAllByCustomerPinAndStatus(CUSTOMER_PIN, Status.ACTIVE, getPageable(pageDto));
    }


    @Test
    void findById() {
        // Setup
        final AccountEntity expectedResult = getAccountEntity();

        // Configure AccountRepository.findByAccountId(...).
        AccountEntity entity = getAccountEntity();
        Optional<AccountEntity> accountEntity = Optional.of(entity);
        when(accountRepo.findByAccountId(ACCOUNT_ID)).thenReturn(accountEntity);

        // Run the test
        AccountEntity result = accountUtil.findById(ACCOUNT_ID);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void findById_WhenAccountNotFound_ShouldThrowAccountNotFoundException() {
        //when
        when(accountRepo.findByAccountId(ACCOUNT_ID)).thenReturn(Optional.empty());

        //then
        AccountNotFoundException actual = assertThrows(AccountNotFoundException.class,
                () -> accountUtil.findById(ACCOUNT_ID));

        Assertions.assertEquals(ErrorCodes.NOT_FOUND, actual.getCode());
        verify(accountRepo, times(1)).findByAccountId(ACCOUNT_ID);
    }

    private AccountEntity getAccountEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(ACCOUNT_ID);
        accountEntity.setCustomerPin(CUSTOMER_PIN);
        accountEntity.setAmount(AMOUNT);
        accountEntity.setCurrencyType(CurrencyType.AZN);
        accountEntity.setStatus(Status.ACTIVE);
        accountEntity.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        accountEntity.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        return accountEntity;
    }

    private AccountResponseDto getAccountDto() {
        AccountResponseDto accountDto = new AccountResponseDto(ACCOUNT_ID, CUSTOMER_PIN,
                AMOUNT, CurrencyType.AZN, Status.ACTIVE);
        return accountDto;
    }

    private PageDto getPageDto() {
        PageDto page = new PageDto();
        page.setPageNumber(5);
        page.setPageSize(5);
        page.setSortDirection(Sort.Direction.ASC);
        page.setSortBy("sortBy");
        return page;
    }

    private Pageable getPageable(PageDto page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
    }
}
