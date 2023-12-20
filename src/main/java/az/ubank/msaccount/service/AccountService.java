package az.ubank.msaccount.service;

import az.ubank.msaccount.dto.AccountCreateDto;
import az.ubank.msaccount.dto.AccountResponseDto;
import az.ubank.msaccount.dto.PageDto;
import org.springframework.data.domain.Page;

public interface AccountService {

    AccountResponseDto createAccount(String customerPin, AccountCreateDto accountCreateDto);

    Page<AccountResponseDto> getAccountsByCustomerId(String customerPin, PageDto page);

    Page<AccountResponseDto> getAccounts(PageDto page);

    AccountResponseDto getAccountById(String uuid);

    AccountResponseDto updateAccount(AccountResponseDto dto);
}
