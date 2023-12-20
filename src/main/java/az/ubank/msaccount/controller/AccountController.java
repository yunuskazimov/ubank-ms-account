package az.ubank.msaccount.controller;

import az.ubank.msaccount.dto.AccountCreateDto;
import az.ubank.msaccount.dto.AccountResponseDto;
import az.ubank.msaccount.dto.PageDto;
import az.ubank.msaccount.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto createAccount(@RequestHeader(name = "Customer-Pin") String customerPin,
                                            @RequestBody AccountCreateDto createDto) {
        log.info("controller createAccount started");
        return service.createAccount(customerPin, createDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<AccountResponseDto> getAccountsByCustomer(
            @RequestHeader(name = "Customer-Pin") String customerPin, PageDto page) {
        log.info("controller getAccountsByCustomer started");
        return service.getAccountsByCustomerId(customerPin, page);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<AccountResponseDto> getAccounts(PageDto page) {
        log.info("controller getAccounts started");
        return service.getAccounts(page);
    }
}
