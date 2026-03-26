package com.service.servicePackage.transaction;

import com.entity.Account;
import com.entity.AtmMachine;
import com.entity.TransactionLog;
import com.service.interfacePackage.TransactionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionBalanceService implements TransactionService {
    private final ArrayList<Account> accountList = new ArrayList<>();
    private AtmMachine atmMachine;
    private LocalDateTime timestamp; // 작업 시간

    public TransactionBalanceService() {
    }

    public TransactionBalanceService(Account account) {
        this.accountList.add(account);
    }

    public TransactionBalanceService(ArrayList<Account> accountList) {
        this.accountList.addAll(accountList);
    }

    public TransactionBalanceService(ArrayList<Account> accountList, AtmMachine atmMachine) {
        this.accountList.addAll(accountList);
        this.atmMachine = atmMachine;
    }

    public void addAccount(Account account) {
        this.accountList.add(account);
    }



    //계좌 찾기
    private Account findAccount(String accountNo){
        for(Account account : accountList){
            if(account.getAccountNo().equals(accountNo)){
                return account;
            }
        }
        return null;
    }

    // 잔액 증가
    private void transactionBalance(Account account, boolean isPlus, long amount){
        account.setBalance(account.getBalance() + amount);
    }
    // ATM 현금 증가&감소
    private void atmAddCash(String type, boolean toAtm,long amount){
        if(type.equals("recordWithdraw")){
            atmMachine.withdrawCash(amount);
        } else if( type.equals("recordDeposit")){
            atmMachine.addCash(amount);
        }
    }

    //로그 기록(거래 시간 , 거래 후 잔액)
    // TransactionLog 자료형으로 반환하고 이것을 list<String>에 추가
    /*private TransactionLog transactionLogInfo(Account account,String type, long amount){
        TransactionLog transactionLog = new TransactionLog(account.getAccountNo(), type, amount);
        long afterBalance = account.getBalance();
    }*/

    private long isAccount(String accountNo){
        return findAccount(accountNo).getBalance();
    }
    private void isAccount(String accountNo, long amount, int note, boolean isPlus, String type){
        for (Account account : accountList) {
            if (account.getAccountNo().equals(accountNo)) {
                if (amount % note == 0) {
                    if(isPlus == true){
                        account.setBalance(account.getBalance() + amount);
                        atmMachine.addCash(amount);
                        recordTransaction(accountNo,type,amount);
                    } else{
                        account.setBalance(account.getBalance() - amount);
                        atmMachine.addCash(amount);
                        recordTransaction(accountNo,type,amount);
                    }
                } else {
                    // 1000원 단위 입금만 허용
                }
            } else {
                // 예외 처리
            }
        }
    }


    @Override
    public long checkBalance(String accountNo) {
        long balance = isAccount(accountNo);
        return balance;
    }

    @Override
    public void deposit(String accountNo, long amount) {
        isAccount(accountNo,amount,1000,true,"recordDeposit");

    }

    @Override
    public void withdraw(String accountNo, long amount) throws Exception {
        isAccount(accountNo, amount, 10000, false, "recordWithdraw");
    }

    @Override
    public void transfer(String fromAccountNo, String toAccountNo, long amount) throws Exception {
        for (Account account : accountList) {
            if (account.getAccountNo().equals(fromAccountNo)) {
                account.setBalance(account.getBalance() - amount);
                recordTransaction(fromAccountNo,"recordTransferMinus",amount);
            } else {
                // 예외 처리
            }
        }
        for (Account account : accountList) {
            if (account.getAccountNo().equals(toAccountNo)) {
                account.setBalance(account.getBalance() + amount);
                recordTransaction(toAccountNo,"recordTransferPlus",amount);
            } else {
                // 예외 처리
            }
        }
    }

    @Override
    public void recordTransaction(String accountNo, String type, long amount) {
        String recordAccount;
        /*type */
        // 계좌번호 검증
        for (Account account : accountList) {
            if (account.getAccountNo().equals(accountNo)) {
                long afterBalance = account.getBalance();
                // type 검증 : [입금,계좌이체 받음](recordDeposit), [계좌이체 돈 보냄, 출금](recordWithdraw)
                timestamp = LocalDateTime.now();
                recordAccount = timestamp + " | " + accountNo + " | " + type + " | ";
                if ((type.equals("recordWithdraw"))){
                    recordAccount += "-" + amount +" | ";
                } else if(type.equals("recordDeposit")) {
                    recordAccount += "+" + amount;
                } else   {
                    //예외처리
                }
                recordAccount += afterBalance;
                account.getAccountRecord().add(recordAccount);
            } else {
                // 예외 처리
            }
        }

    }

    @Override
    public List<String> getRecentHistory(String accountNo) {
        return List.of();
    }
}
