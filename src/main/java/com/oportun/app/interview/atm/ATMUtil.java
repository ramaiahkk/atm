package com.oportun.app.interview.atm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ATMUtil {
    public static String getInputString(String message) throws IOException {
        System.out.print(message);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    public static Long getNumberInput(String message) throws IOException {
        String input = getInputString(message);
        Long inputNum = 0L;
        try {
            inputNum = new Long(input);
        } catch (Exception e) {
            return getNumberInput("Incorrect or insufficient funds");
        }
        return inputNum;
    }

    public static String printATMMenu() throws IOException {
        System.out.println("1. Deposit 2. Withdraw 3. ATM Available Balance 4. Exit");
        return ATMUtil.getInputString("select option number:");

    }

    public static HashMap<Long, Long> getDepositMap(String message) throws IOException {
        HashMap<Long, Long> result = new HashMap<>();
        try{
            String inputString = getInputString(message).replaceAll(" ", "");
            String inputStrings[] = inputString.split("\\,");
            Arrays.asList(inputStrings).forEach(x -> {
                result.put(new Long(x.split(":")[0]), new Long(x.split(":")[1]));
            });
        }catch (Exception e){
            return getDepositMap(message);
        }

        return result;
    }

}
