package com.entity;

public class AtmMachine {
    private long totalCash; // ATM 기기가 현재 보유 중인 총 현금

    public AtmMachine(long initialCash) {
        this.totalCash = initialCash;
    }

    public long getTotalCash() {
        return totalCash;
    }

    // 해든님이 기기에 현금을 추가/회수할 때 사용할 메서드
    public void addCash(long amount) {
        this.totalCash += amount;
    }
    public void withdrawCash(long amount) {
        this.totalCash -= amount;
    }
}