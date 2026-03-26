package com.service.servicePackage;

import com.entity.Account;
import com.entity.AdminLog;
import com.entity.AtmMachine;
import com.service.interfacePackage.AdminService;
import com.service.servicePackage.transaction.TransactionBalanceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeunAdmin implements AdminService {

    private AtmMachine atmMachine;
    private List<Account> accounts;
    private TransactionBalanceService transactionBalanceService;

    public DeunAdmin() {
        this.atmMachine = new AtmMachine(1000000000);  // 10억
        this.accounts = new ArrayList<>();
    }

    public DeunAdmin(AtmMachine atmMachine, List<Account> accounts,
                     TransactionBalanceService transactionBalanceService) {
        this.atmMachine = atmMachine;
        this.accounts = accounts;
        this.transactionBalanceService = transactionBalanceService;
    }

    @Override
    public long checkAtmTotalCash() {   // 기계 안의 총 돈 1
        return atmMachine.getTotalCash();
    }

    // 돈은 10,000원 단위

    @Override
    public void addAtmCash(long amount) {   // 기계의 돈 채우기 2
         atmMachine.addCash(amount);
         recordAdminLog( "현금추가", amount, LocalDateTime.now());
         System.out.println("현재 ATM 총금액 : " + atmMachine.getTotalCash() + " 원");
    }

    @Override
    public void withdrawAtmCash(long amount) {  // 기계의 돈 빼기 3
        atmMachine.withdrawCash(amount);
        recordAdminLog( "현금회수", amount, LocalDateTime.now());
        System.out.println("현재 ATM 총금액 : " + atmMachine.getTotalCash() + " 원");
    }

    @Override
    public boolean adminLogin(String adminPassword) {   // 관리자 로그인 4
        // 비번은 1234 임
        String password = "1234";
        if (adminPassword.equals(password)){
            return true;
        } else {
            return false;
        }
    }

    private final List<AdminLog> adminLogs = new ArrayList<>();

    public List<AdminLog> getAdminLogs() {
        return adminLogs;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public void recordAdminLog(String type, long amount, LocalDateTime timestamp) {   // 5 관리자 로그 기계 자체에서 입출금 기록
        adminLogs.add(new AdminLog(type, amount, timestamp));
    }

    @Override
    public List<String> viewAllTransactionLogs() {  // 영석 같이 6 기계 고객 거래 전체 로그 내역 확인
        List<String> viewAll = new ArrayList<>();
        for (int i = 0 ; i<accounts.size(); i++) {
            Account account = accounts.get(i);      // 계좌 목록에서 계좌를 뽑아냄
            String accountNo = account.getAccountNo();  // 계좌에서 계좌번호를 뽑아냄

            List<String> viewAccount = transactionBalanceService.getRecentHistory(accountNo);// 뽑아낸 계좌번호로 계좌 기록을 뽑아냄

            viewAll.addAll(viewAccount);
        }
        return viewAll;
    }



}
