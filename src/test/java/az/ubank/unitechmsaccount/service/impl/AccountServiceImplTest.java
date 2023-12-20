package az.ubank.unitechmsaccount.service.impl;

import az.ubank.msaccount.dao.entity.AccountEntity;
import az.ubank.msaccount.dao.repository.AccountRepository;
import az.ubank.msaccount.dto.AccountCreateDto;
import az.ubank.msaccount.dto.AccountResponseDto;
import az.ubank.msaccount.dto.PageDto;
import az.ubank.msaccount.enums.CurrencyType;
import az.ubank.msaccount.enums.Status;
import az.ubank.msaccount.exception.AccountNotFoundException;
import az.ubank.msaccount.exception.ErrorCodes;
import az.ubank.msaccount.mapper.AccountMapper;
import az.ubank.msaccount.service.impl.AccountServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private static final String CUSTOMER_PIN = "1OYG677";
    private static final String ACCOUNT_ID = "dda8850c-5bff-4ee3-a118-9684e2fe004d";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal AMOUNT2 = BigDecimal.valueOf(150);

    @Mock
    private AccountRepository accountRepo;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountUtil accountUtil;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepo, accountMapper, accountUtil);
    }

    @Test
    void createAccount() {
        //given
        AccountCreateDto createDto = new AccountCreateDto(AMOUNT, CurrencyType.AZN);
        AccountResponseDto dto = getAccountResponseDto();
        AccountEntity entity = getAccountEntity();

        //when
        when(accountMapper.toAccountResponseDtoFromCreateDto(createDto)).thenReturn(dto);
        when(accountMapper.toEntityFromResponseDto(dto)).thenReturn(entity);
        when(accountRepo.save(entity)).thenReturn(entity);
        when(accountMapper.toAccountResponseDtoFromEntity(entity)).thenReturn(dto);

        var actual = accountService.createAccount(CUSTOMER_PIN, createDto);

        assertEquals(actual, dto);

        verify(accountRepo, times(1)).save(entity);

    }

    @Test
    void getAccountsByCustomerId() {
        //given
        PageDto pageDto = getPageDto();

        AccountEntity entity = getAccountEntity();
        Page<AccountEntity> entityList = new PageImpl<>(List.of(entity));

        AccountResponseDto dto = getAccountResponseDto();
        Page<AccountResponseDto> dtoList = new PageImpl<>(List.of(dto));

        //when
        when(accountUtil.findAllByCustomerIdAndStatusIsActive(CUSTOMER_PIN, getPageable(pageDto)))
                .thenReturn(entityList);
        when(accountMapper.toAccountResponseDtoFromEntity(entity)).thenReturn(dto);


        var actual = accountService.getAccountsByCustomerId(CUSTOMER_PIN, pageDto);

        assertEquals(actual, dtoList);

        verify(accountUtil, times(1))
                .findAllByCustomerIdAndStatusIsActive(CUSTOMER_PIN, getPageable(pageDto));

    }

    @Test
    void getAccounts() {
        //given
        PageDto pageDto = getPageDto();

        AccountEntity entity = getAccountEntity();
        Page<AccountEntity> entityList = new PageImpl<>(List.of(entity));

        AccountResponseDto dto = getAccountResponseDto();
        Page<AccountResponseDto> dtoList = new PageImpl<>(List.of(dto));

        //when
        when(accountUtil.findAllByStatusIsActive(getPageable(pageDto))).thenReturn(entityList);
        when(accountMapper.toAccountResponseDtoFromEntity(entity)).thenReturn(dto);

        var actual = accountService.getAccounts(pageDto);

        assertEquals(actual, dtoList);

        verify(accountUtil, times(1))
                .findAllByStatusIsActive(getPageable(pageDto));

    }

    @Test
    void updateAccount() {
        // given
        AccountResponseDto newDto = getAccountResponseDto();
        newDto.setAmount(AMOUNT2);
        AccountEntity entity = getAccountEntity();
        AccountEntity newEntity = getAccountEntity();
        newEntity.setAmount(AMOUNT2);

        //when
        when(accountMapper.toEntityFromResponseDto(newDto)).thenReturn(newEntity);
        when(accountRepo.save(newEntity)).thenReturn(newEntity);
        when(accountMapper.toAccountResponseDtoFromEntity(newEntity)).thenReturn(newDto);

        var actual = accountService.updateAccount(newDto);

        assertEquals(actual, newDto);
        verify(accountRepo, times(1)).save(newEntity);
    }

    @Test
    void getAccountById() {
        //given
        AccountEntity entity = getAccountEntity();
        AccountResponseDto dto = getAccountResponseDto();

        //when
        when(accountUtil.findById(ACCOUNT_ID)).thenReturn(entity);
        when(accountMapper.toAccountResponseDtoFromEntity(entity)).thenReturn(dto);

        var actual = accountService.getAccountById(ACCOUNT_ID);

        assertEquals(actual, dto);
        verify(accountUtil, times(1)).findById(ACCOUNT_ID);

    }

    @Test
    void getAccountById_AccountNotFound_ShouldThrowAccountNotFoundException() {

        //when
        when(accountUtil.findById(ACCOUNT_ID))
                .thenThrow(AccountNotFoundException
                        .of(ErrorCodes.NOT_FOUND, "Account Not Found"));

        var actual = assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountById(ACCOUNT_ID));

        Assertions.assertEquals(ErrorCodes.NOT_FOUND, actual.getCode());

        verify(accountUtil, times(1)).findById(ACCOUNT_ID);


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

    private AccountResponseDto getAccountResponseDto() {
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
