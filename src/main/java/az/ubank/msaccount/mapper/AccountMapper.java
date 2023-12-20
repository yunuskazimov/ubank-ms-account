package az.ubank.msaccount.mapper;

import az.ubank.msaccount.dao.entity.AccountEntity;
import az.ubank.msaccount.dto.AccountCreateDto;
import az.ubank.msaccount.dto.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountResponseDto toAccountResponseDtoFromEntity(AccountEntity entity);

    AccountEntity toEntityFromResponseDto(AccountResponseDto dto);

    AccountResponseDto toAccountResponseDtoFromCreateDto(AccountCreateDto createDto);

}
