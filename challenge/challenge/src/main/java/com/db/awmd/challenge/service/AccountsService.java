package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	EmailNotificationService emailNotificationService = new EmailNotificationService();

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public void moneyTransfer(String accountFromId, String accountToId,BigDecimal amount) {

		try {
			Account accountFrom = getAccount(accountFromId);
			Account accountTo = getAccount(accountToId);

			if(!accountFrom.equals(accountTo) && amount.doubleValue() > 0){
				
				System.out.println("--------Before transfer---------");
				System.out.println("Account A Balance"+accountFrom.getBalance());
				System.out.println("Account B Balance"+accountTo.getBalance());

				accountFrom.withdraw(amount);
				accountTo.deposit(amount);

				String msg = "Your account has been credited with amount of Rs "+amount+" from account "+accountFromId;
				emailNotificationService.notifyAboutTransfer(accountTo, msg);

				msg = "Your account has been debited with amount of Rs "+amount+" to account "+ accountToId;
				emailNotificationService.notifyAboutTransfer(accountFrom, msg);


				System.out.println("--------After transfer---------");
				System.out.println("Account A Balance"+accountFrom.getBalance());
				System.out.println("Account B Balance"+accountTo.getBalance());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
