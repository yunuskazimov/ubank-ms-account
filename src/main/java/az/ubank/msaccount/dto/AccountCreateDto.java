package az.ubank.msaccount.dto;

import az.ubank.msaccount.enums.CurrencyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("Account Create Data")
public class AccountCreateDto {

    @ApiModelProperty(value = "The Amount of Account.", example = "1000")
    private BigDecimal amount;

    @ApiModelProperty(value = "The Currency Type of Account.", example = "azn")
    private CurrencyType currencyType;
}
