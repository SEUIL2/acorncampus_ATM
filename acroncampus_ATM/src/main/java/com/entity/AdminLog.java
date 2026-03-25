package com.entity;

import java.time.LocalDateTime;

public class AdminLog {
    private String actionType;       // 관리자 작업 유형 (예: "현금추가", "현금회수")
    private long amount;             // 처리 금액
    private LocalDateTime timestamp; // 작업 시간

    public AdminLog(String actionType, long amount) {
        this.actionType = actionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getActionType() {
        return actionType;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // 관리자 로그 확인 시 출력용
    @Override
    public String toString() {
        return "AdminLog{" +
                "작업유형='" + actionType + '\'' +
                ", 금액=" + amount +
                ", 시간=" + timestamp +
                '}';
    }
}