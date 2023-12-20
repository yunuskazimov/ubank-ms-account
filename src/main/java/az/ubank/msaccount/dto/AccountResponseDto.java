package az.ubank.msaccount.dto;

import az.ubank.msaccount.enums.CurrencyType;
import az.ubank.msaccount.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("Account Data")
@Builder
public class AccountResponseDto {
    @ApiModelProperty("Unique id of Account. Don't use POST")
    private String accountId;

    @ApiModelProperty(value = "Enter valid Customer PIN of Account.", example = "aze1234")
    private String customerPin;

    @ApiModelProperty(value = "The Amount of Account.", example = "1000")
    private BigDecimal amount;

    @ApiModelProperty(value = "The Currency Type of Account.", example = "azn")
    private CurrencyType currencyType;

    @ApiModelProperty(value = "Enter valid Status for Account", example = "active")
    private Status status;
}
