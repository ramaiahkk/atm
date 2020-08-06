package com.oportun.app.interview.atm;

import com.oportun.app.interview.atm.config.Denominations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;

public class ATMServiceImplTest {


    private ATMService atmService;

    @BeforeEach
    public void init() {

        Denominations denominations = new Denominations();
        Long denArray[] = {1L, 5L, 10L, 20L};
        denominations.setAllowedCurrencies(Arrays.asList(denArray));
        atmService = new ATMServiceImpl(denominations);
    }

    @Test
    public void testDepositSuccess() throws ATMInvalidTransactionException {
        HashMap map = new HashMap();
        map.put(10L, 1L);
        Assert.isTrue(atmService.deposit(map).equalsIgnoreCase("Balance: 10's=1  ,Total=10"));
        map.put(5L, 5L);
        map.put(1L, 15L);
        Assert.isTrue((atmService.deposit(map).equalsIgnoreCase("Balance: 1's=15  5's=5  10's=2  ,Total=60")));
    }

    @Test
    public void testDepositFailureMessage() throws ATMInvalidTransactionException {
        HashMap map = new HashMap();
        map.put(10L, 0L);
        Assert.isTrue(atmService.deposit(map).equalsIgnoreCase("Deposit amount cannot be zero"));
    }

    @Test
    public void testDepositFailureMessageNegative() throws ATMInvalidTransactionException {
        HashMap map = new HashMap();
        map.put(10L, -1L);
        //  System.out.println(atmService.deposit(map));
        Assert.isTrue(atmService.deposit(map).equalsIgnoreCase("Incorrect deposit amount"));
    }

    @Test
    public void testDepositFailureMessageforInvalidBills() throws ATMInvalidTransactionException {
        HashMap map = new HashMap();
        map.put(200L, 1L);
        Assert.isTrue(atmService.deposit(map).equalsIgnoreCase("Incorrect deposit bills"));
    }

    @Test
    public void testWithdrwalMoreAmount() throws ATMInvalidTransactionException {
        Assert.isTrue(atmService.withdraw(atmService.getCurrentBalance() + 1).equalsIgnoreCase("Incorrect or insufficient funds"));
    }

    @Test
    public void testWithdrawZero() throws ATMInvalidTransactionException {

        Assert.isTrue(atmService.withdraw(0L).equalsIgnoreCase("Incorrect or insufficient funds"));
    }

    @Test
    public void testWithdrawSuccess() throws ATMInvalidTransactionException {
        HashMap map = new HashMap();
        map.put(10L, 1L);
        atmService.deposit(map);
        Assert.isTrue(atmService.withdraw(10).contains("10"));
    }

    @Test
    public void testAllowedList() throws ATMInvalidTransactionException {
        System.out.println(atmService.getAllowedCurrencies());
        Assert.notNull(atmService.getAllowedCurrencies());
    }


}
