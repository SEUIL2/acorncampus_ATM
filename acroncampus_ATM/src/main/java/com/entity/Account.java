package com.entity;

public class Account {
    private String accountNo;       // 계좌번호
    private String ownerName;       // 소유자명
    private String password;        // 비밀번호
    private long balance;           // 잔액
    private int passwordErrorCount; // 비밀번호 오류 횟수 (3회 이상시 잠금용)
    private boolean isLocked;       // 계좌 잠금 상태

    // 새 계좌를 생성할 때 사용하는 생성자
    public Account(String accountNo, String ownerName, String password, long balance) {
        this.accountNo = accountNo;
        this.ownerName = ownerName;
        this.password = password;
        this.balance = balance;
        this.passwordErrorCount = 0; // 초기 생성 시 오류 횟수는 0
        this.isLocked = false;       // 초기 생성 시 잠금 해제 상태
    }

    // --- Getter 및 Setter ---
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getPasswordErrorCount() {
        return passwordErrorCount;
    }

    // 비밀번호 오류 횟수 1 증가 메서드 (현겸님이 로그인 실패 로직에 사용)
    public void increasePasswordErrorCount() {
        this.passwordErrorCount++;
    }

    // 비밀번호 오류 횟수 초기화
    public void resetPasswordErrorCount() {
        this.passwordErrorCount = 0;
    }

    public boolean isLocked() {
        return isLocked;
    }

    // 계좌 잠금 설정/해제
    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}