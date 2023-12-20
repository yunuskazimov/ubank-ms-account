package az.ubank.msaccount.service.impl;

import az.ubank.msaccount.dao.entity.AccountEntity;
import az.ubank.msaccount.dao.repository.AccountRepository;
import az.ubank.msaccount.dto.AccountCreateDto;
import az.ubank.msaccount.dto.AccountResponseDto;
import az.ubank.msaccount.dto.PageDto;
import az.ubank.msaccount.enums.Status;
import az.ubank.msaccount.mapper.AccountMapper;
import az.ubank.msaccount.service.AccountService;
import az.ubank.msaccount.util.AccountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;
    private final AccountMapper accountMapper;
    private final AccountUtil accountUtil;

    @Override
    public AccountResponseDto createAccount(String customerPin, AccountCreateDto createDto) {
        log.info("service createAccount started with customer PIN: {}", customerPin);
        AccountResponseDto AccountResponseDto = accountMapper.toAccountResponseDtoFromCreateDto(createDto);
        AccountResponseDto.setCustomerPin(customerPin);
        AccountResponseDto.setStatus(Status.ACTIVE);
        AccountEntity entity = accountRepo.save(accountMapper.toEntityFromResponseDto(AccountResponseDto));
        log.info("service createAccount completed with customer PIN: {}", customerPin);
        return accountMapper.toAccountResponseDtoFromEntity(entity);
    }

    @Override
    public Page<AccountResponseDto> getAccountsByCustomerId(String customerPin, PageDto page) {
        log.info("service getAccountsByCustomerId started by Customer PIN: {}", customerPin);
        return accountUtil
                .findAllByCustomerIdAndStatusIsActive(customerPin, getPageable(page))
                .map(accountMapper::toAccountResponseDtoFromEntity);
    }

    @Override         //only admin have access when add permission ms
    public Page<AccountResponseDto> getAccounts(PageDto page) {
        log.info("service getAccounts started");
        return accountUtil.findAllByStatusIsActive(getPageable(page))
                .map(accountMapper::toAccountResponseDtoFromEntity);
    }

    @Override
    public AccountResponseDto getAccountById(String uuid) {
        log.info("service getAccountById started by UUID: {}", uuid);
        return accountMapper.toAccountResponseDtoFromEntity(accountUtil.findById(uuid));
    }

    @Override
    public AccountResponseDto updateAccount(AccountResponseDto dto) {
        log.info("service updateAccount started with customer PIN: {}", dto.getCustomerPin());
        AccountEntity entity = accountRepo.save(accountMapper.toEntityFromResponseDto(dto));
        log.info("service updateAccount completed with customer PIN: {}", dto.getCustomerPin());
        return accountMapper.toAccountResponseDtoFromEntity(entity);
    }

    private Pageable getPageable(PageDto page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
    }
}
