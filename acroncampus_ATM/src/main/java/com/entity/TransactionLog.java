package com.entity;

import java.time.LocalDateTime;

public class TransactionLog {
    private String accountNo;        // 거래가 발생한 계좌번호
    private String transactionType;  // 거래 유형 (예: "입금", "출금", "이체")
    private long amount;             // 거래 금액
    private LocalDateTime timestamp; // 거래가 발생한 시간

    // 거래 로그 생성자 (생성 시 현재 시간이 자동으로 기록됨)
    public TransactionLog(String accountNo, String transactionType, long amount) {
        this.accountNo = accountNo;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now(); // 현재 시간 자동 할당
    }

    // --- Getter ---
    // (로그는 한 번 기록되면 수정할 일이 거의 없으므로 일반적으로 Setter는 생략하거나 제한적으로 둡니다)
    
    public String getAccountNo() {
        return accountNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // 영석님이 내역을 출력할 때 편하게 사용할 수 있도록 toString 오버라이딩
    @Override
    public String toString() {
        return "TransactionLog{" +
                "계좌번호='" + accountNo + '\'' +
                ", 거래유형='" + transactionType + '\'' +
                ", 금액=" + amount +
                ", 시간=" + timestamp +
                '}';
    }
}