package az.ubank.msaccount.controller;

import az.ubank.msaccount.dto.AccountResponseDto;
import az.ubank.msaccount.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/int/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountControllerInt {
    private final AccountService service;

    @GetMapping("/id/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDto getAccountsByUUID(@PathVariable String uuid) {
        log.info("controller int getAccountsByUUID started");
        return service.getAccountById(uuid);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountResponseDto updateAccount(@RequestBody AccountResponseDto dto) {
        log.info("controller int updateAccount started");
        return service.updateAccount(dto);
    }

}
