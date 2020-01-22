package com.db.awmd.challenge.domain;

import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.NegativeValueException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }
  
  public BigDecimal withdraw(BigDecimal amount) {
	  this.balance = this.balance.subtract(amount);
	  
	  return this.balance;
  }
  
  public BigDecimal deposit(BigDecimal amount) {
	  
	  if (amount.doubleValue() < 0) // if deposit value is negative
      {
		  
		  throw new NegativeValueException("Deposit value is negative " + amount.doubleValue() + "!!!");
          
      }
	  
	  this.balance = this.balance.add(amount);
	  
	  return this.balance;
  }
  
}
