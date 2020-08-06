package com.oportun.app.interview.atm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ATMService {
     String deposit(HashMap<Long, Long> deposit) throws ATMInvalidTransactionException;
     String withdraw(long with) throws ATMInvalidTransactionException;
     String getCurrentNotes();
     List<Long> getAllowedCurrencies();
     Long getCurrentBalance();

}
