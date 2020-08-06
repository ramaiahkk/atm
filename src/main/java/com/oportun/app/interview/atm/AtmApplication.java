package com.oportun.app.interview.atm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtmApplication implements CommandLineRunner {
    @Autowired
    private ATMService atmService;

    public static void main(String[] args) {
        SpringApplication.run(AtmApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        turnOnATM();
    }


    public void turnOnATM() throws Exception {
        String input = "1";
        while (input != null) {

            switch (ATMUtil.printATMMenu()) {
                case "1":
                    // String depositString = ATMUtil.getInputString("Enter Deposit Info {BillValue}:{NoofBills} seperated by , :\n");
                    System.out.println(atmService.deposit(ATMUtil.getDepositMap("deposit :")));
                    break;
                case "2":

                    System.out.println(atmService.withdraw(ATMUtil.getNumberInput("Enter Withdraw1 amount :")));

                    break;
                case "3":
                    System.out.println("ATM Available Balance" + atmService.getCurrentBalance());
                    break;
                case "4":
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }

    }
}
