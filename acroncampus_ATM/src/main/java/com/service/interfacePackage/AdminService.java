package com.service.interfacePackage;

import java.util.List;

public interface AdminService {
    // 6. 기기 관리자 기능
    boolean adminLogin(String adminPassword); // 6.6 관리자 전용 암호 인증 [cite: 34]

    long checkAtmTotalCash(); // 6.1 ATM 기기 내 총 현금 잔고 확인 [cite: 27]
    void addAtmCash(long amount); // 6.2 ATM 기기에 현금 추가하기 [cite: 28]
    void withdrawAtmCash(long amount); // 6.3 ATM 기기에 현금 빼기 [cite: 29]
    void recordAdminLog(String type, long amount); // 6.4 현금 추가하고 뺄 경우의 로그 기록 [cite: 30]

    List<String> viewAllTransactionLogs(); // 6.5 거래 내역 확인 - Log 에 접근하여 확인 [cite: 31]
}
