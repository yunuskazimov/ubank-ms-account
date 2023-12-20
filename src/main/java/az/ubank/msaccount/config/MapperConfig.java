package az.ubank.msaccount.config;

import az.ubank.msaccount.mapper.AccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public AccountMapper accountMapper() {
        return AccountMapper.INSTANCE;
    }

}
