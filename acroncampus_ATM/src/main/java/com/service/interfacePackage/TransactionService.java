package com.service.interfacePackage;

import java.util.List;

public interface TransactionService {
    // 2. 입출금 및 잔액 제어 [cite: 11]
    long checkBalance(String accountNo); // 2.1 현재 잔액 확인 [cite: 12]
    void deposit(String accountNo, long amount); // 2.2 현금 입금 [cite: 13]
    void withdraw(String accountNo, long amount) throws Exception; // 2.3 현금 출금 (잔액 차감 및 단위 제한(2.4), 부족 시 제한) [cite: 14, 15]

    // 4. 계좌 이체 [cite: 20]
    void transfer(String fromAccountNo, String toAccountNo, long amount) throws Exception; // 4.1 ,4.2 보내는 계좌 차감 + 받는 계좌 합산, 없는 계좌 체크 [cite: 21, 22]

    // 5. 거래 내역 기록 (Log) [cite: 23]
    void recordTransaction(String accountNo, String type, long amount); // 5.1 입금/출금/이체 발생 시 시간, 금액, 거래 유형 기록 [cite: 24]
    List<String> getRecentHistory(String accountNo); // 5.2 최근 거래 내역 리스트 보기 [cite: 25]
}
