package com.service.servicePackage;

import com.entity.Account;
import com.entity.AdminLog;
import com.entity.AtmMachine;
import com.service.servicePackage.transaction.TransactionBalanceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeunAdminTest {

    public static void main(String[] args) throws Exception {

        DeunAdmin admin = new DeunAdmin();

        // 생성자인데, 매개변수를 받지 않아서 새 Atm기계(객체)를 만드는 것을 막음
        // 1번 메서드 돈 조회하기
        long result = admin.checkAtmTotalCash();
        System.out.println(result);
        System.out.println();

        // 2번 돈추가
        long temp = 200000;
        admin.addAtmCash(temp);
        System.out.println("추가한 금액 : " + temp + " 원");
        System.out.println();

        // 3번 돈회수
        long temp2 = 200000000;
        admin.withdrawAtmCash(temp2);
        System.out.println("회수한 금액 : " + temp2 + " 원");
        System.out.println();

        // 4번 관리자 로그인
        System.out.println( admin.adminLogin("1234"));
        System.out.println();


        // 5번 관리자 로그 확인
        for (AdminLog adminLog : admin.getAdminLogs()) {
            System.out.println(adminLog.toString());
        }
        System.out.println();

        // 6번 (영석 같이) 입출금 로그
        Account account1 = new Account("1111", "kim", "1234", 500000);
        Account account2 = new Account("2222", "lee", "5678", 300000);

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(account1);
        accountList.add(account2);

        AtmMachine testAtmMachine = new AtmMachine(1000000000);
        TransactionBalanceService transactionService =
                new TransactionBalanceService(accountList, testAtmMachine);
        DeunAdmin logAdmin = new DeunAdmin(testAtmMachine, accountList, transactionService);

        transactionService.deposit("1111", 100000);
        transactionService.withdraw("1111", 50000);
        transactionService.transfer("1111", "2222", 70000);

        List<String> account1Logs = transactionService.getRecentHistory("1111");
        List<String> account2Logs = transactionService.getRecentHistory("2222");

        System.out.println("[1111 계좌 로그]");
        for (String log : account1Logs) {
            System.out.println(log);
        }
        System.out.println();

        System.out.println("[2222 계좌 로그]");
        for (String log : account2Logs) {
            System.out.println(log);
        }
        System.out.println();

        System.out.println("[admin.viewAllTransactionLogs() 현재 결과]");
        System.out.println(logAdmin.viewAllTransactionLogs());




    }
}
