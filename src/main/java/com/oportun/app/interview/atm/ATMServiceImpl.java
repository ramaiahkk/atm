package com.oportun.app.interview.atm;


import com.oportun.app.interview.atm.config.Denominations;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Configurable
public class ATMServiceImpl implements ATMService {
    private static final HashMap<Long, Long> currentNotes = new HashMap<>();
    //store valid bills
    private Denominations denominations;
    //Constructor injection
    public ATMServiceImpl(Denominations denominations){
        this.denominations = denominations;
    }
    /*
        Deposit methis @pram deposit witll consists of bills and total from input will return string that is good for output.
     */
    @Override
    public synchronized String deposit(HashMap<Long, Long> deposit) throws ATMInvalidTransactionException {
        String result = null;
        result = validInput(deposit);
        if (result != null) {
            return result;
        }
        deposit.entrySet().parallelStream().forEach(x -> addBills(x.getKey(), x.getValue()));
        return "Balance: " + getCurrentNotes() + ",Total=" + getCurrentBalance();
    }

    private void addBills(Long billType, Long noOfBills) {
        currentNotes.put(billType, currentNotes.getOrDefault(billType, 0L) + noOfBills);
    }

    private void withdrawBills(Long billType, Long noOfBills) {
        if (currentNotes.getOrDefault(billType, 0L) >= noOfBills) {
            currentNotes.put(billType, currentNotes.get(billType) - noOfBills);
        }
    }

    @Override
    public synchronized String withdraw(long amount) throws ATMInvalidTransactionException {
        String result = "";
        if (getCurrentBalance() < amount || amount <= 0) {
            return "Incorrect or insufficient funds";
        }
        HashMap<Long, Long> withdrawMap = new HashMap<>();
        final long[] withAmount = {amount};
        currentNotes.keySet().stream().sorted(Collections.reverseOrder()).forEach(x -> {
            if (withAmount[0] > 0 && withAmount[0] / x > 0) {
                long noOfNotesforX = currentNotes.get(x) >= (withAmount[0] / x) ? (withAmount[0] / x) : currentNotes.get(x);
                withAmount[0] = withAmount[0] - (noOfNotesforX * x);
                withdrawMap.put(x, noOfNotesforX);
            }
        });
        if (getTotal(withdrawMap) == amount) {
            withdrawMap.entrySet().stream().forEach(x -> withdrawBills(x.getKey(), x.getValue()));
            result = "Dispensed: " + getStringFormat(withdrawMap) + "\nBalance:" + getCurrentNotes() + ", Total = " + getCurrentBalance();

        } else {
            result = "Insufficient bills please try rounding";
        }

        return result;
    }
    //formating from map to string.
    private String getStringFormat(HashMap<Long, Long> input) {
        StringBuilder sb = new StringBuilder();
        input.entrySet().stream().forEach(x -> sb.append(x.getKey()).append("'s=").append(x.getValue()).append("  "));
        return sb.toString();
    }

    @Override
    public synchronized String getCurrentNotes() {
        return getStringFormat(currentNotes);
    }

    @Override
    public List<Long> getAllowedCurrencies() {
        return denominations.getAllowedCurrencies();
    }

    private Long getTotal(HashMap<Long, Long> map) {
        return map.entrySet().stream().map(x -> x.getKey() * x.getValue()).reduce(0L, Long::sum);
    }

    @Override
    public synchronized Long getCurrentBalance() {
        return getTotal(currentNotes);
    }

    private String validInput(Map<Long, Long> input) {
        //empty or null map
        if (input == null || input.size() == 0) {
            return "Incorrect deposit amount";
        }
        // iterate input bills filter that are not in allowed list nills and collect to list. if the size greater than 0 means we have invalid bills
        if (input.keySet().stream().filter(x -> !denominations.getAllowedCurrencies().contains(x)).collect(Collectors.toList()).size() > 0) {
            return "Incorrect deposit bills";
        }
        //if no of bills is zero for any bill
        if (input.values().stream().filter(x -> x == 0).collect(Collectors.toList()).size() > 0) {
            return "Deposit amount cannot be zero";
        }
        //if any no of bills are negative
        if (input.values().stream().filter(x -> x < 0).collect(Collectors.toList()).size() > 0) {
            return "Incorrect deposit amount";
        }
        return null;
    }
}
