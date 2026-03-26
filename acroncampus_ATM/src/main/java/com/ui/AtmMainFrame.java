package com.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtmMainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel screenPanel; // ATM 화면 부분

    private String selectedAccount = "";
    private boolean isMachineReady = true;

    // 화면 패널들
    private JPanel welcomePanel;
    private JPanel menuPanel;
    private AnimationPanel animationPanel;
    private JPanel passwordPanel;
    private JPasswordField passwordField;

    // 브랜드 컬러 (우리/신한은행 느낌의 블루)
    private Color brandColor = new Color(20, 60, 140);
    private Color screenBgColor = new Color(245, 248, 255); // 밝은 스크린 배경

    public AtmMainFrame() {
        setTitle("ACRON ATM 1인칭 시뮬레이터");
        setSize(850, 850); // 물리 키패드 공간 확보를 위해 세로 길이 약간 늘림
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(40, 45, 50)); // 주변 배경 (어두운 실내)
        setLayout(new BorderLayout(0, 10));

        // --- [ATM 기계 몸체 패널] ---
        JPanel machineBodyPanel = new JPanel(new BorderLayout(0, 10));
        machineBodyPanel.setBackground(new Color(225, 230, 235)); // 밝은 메탈/플라스틱 느낌
        machineBodyPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 40, 10, 40),
                BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY)
        ));

        // --- [상단 간판 (브랜드)] ---
        JLabel brandLabel = new JLabel(" 에이콘 은행 ACORN BANK", SwingConstants.LEFT);
        brandLabel.setOpaque(true);
        brandLabel.setBackground(brandColor);
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        brandLabel.setBorder(new EmptyBorder(15, 20, 15, 20));
        machineBodyPanel.add(brandLabel, BorderLayout.NORTH);

        // --- [중앙 스크린 세팅] ---
        cardLayout = new CardLayout();
        screenPanel = new JPanel(cardLayout);
        screenPanel.setBackground(screenBgColor);

        // 스크린을 감싸는 두꺼운 베젤
        JPanel bezelPanel = new JPanel(new BorderLayout());
        bezelPanel.setBackground(new Color(15, 15, 15)); // 검은색 얇은 모니터 테두리
        bezelPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 30, 10, 30), // 베젤 바깥 여백
                new LineBorder(brandColor, 8, true) // 은행 컬러 프레임
        ));
        bezelPanel.add(screenPanel, BorderLayout.CENTER);

        initPanels();
        screenPanel.add(welcomePanel, "WELCOME");
        screenPanel.add(animationPanel, "ANIMATION");
        screenPanel.add(passwordPanel, "PASSWORD");
        screenPanel.add(menuPanel, "MENU");

        machineBodyPanel.add(bezelPanel, BorderLayout.CENTER);

        // --- [하단 하드웨어 패널 (키패드, 투입구)] ---
        JPanel hardwarePanel = createHardwarePanel();
        machineBodyPanel.add(hardwarePanel, BorderLayout.SOUTH);

        add(machineBodyPanel, BorderLayout.CENTER);

        // --- [인벤토리 (기존 유지)] ---
        JPanel hotbarPanel = createHotbarPanel();
        add(hotbarPanel, BorderLayout.SOUTH);
    }

    private void initPanels() {
        welcomePanel = createWelcomePanel();
        menuPanel = createMenuPanel();
        animationPanel = new AnimationPanel();
        passwordPanel = createPasswordPanel();
    }

    // --- 1. 대기 화면 (WELCOME) ---
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(screenBgColor);

        JLabel welcomeText = new JLabel("<html><center><span style='font-size:24px; color:#143c8c;'><b>환영합니다</b></span><br><br><span style='font-size:16px; color:#333333;'>아래 인벤토리에서<br>사용할 카드를 클릭하여 기계에 넣어주세요</span></center></html>", SwingConstants.CENTER);
        panel.add(welcomeText, BorderLayout.CENTER);

        return panel;
    }

    // --- 2. 플레이어 인벤토리 (기존 로직 유지, 색상만 약간 톤다운) ---
    private JPanel createHotbarPanel() {
        JPanel hotbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        hotbar.setBackground(new Color(40, 45, 50));

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "내 지갑 (인벤토리)",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("맑은 고딕", Font.BOLD, 12), Color.WHITE);
        hotbar.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 10, 5, 10), border));

        String[] dummyAccounts = {"111-111\n(현겸)", "222-222\n(해든)", "333-333\n(영석)", "빈 슬롯", "빈 슬롯"};

        for (String acc : dummyAccounts) {
            JButton slotBtn = new JButton("<html><center>" + acc.replaceAll("\n", "<br>") + "</center></html>");
            slotBtn.setPreferredSize(new Dimension(80, 70));
            slotBtn.setFont(new Font("맑은 고딕", Font.BOLD, 11));
            slotBtn.setBackground(new Color(200, 205, 210));
            slotBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            if (!acc.equals("빈 슬롯")) {
                slotBtn.addActionListener(e -> {
                    if (isMachineReady) {
                        isMachineReady = false;
                        selectedAccount = acc.split("\n")[0];

                        cardLayout.show(screenPanel, "ANIMATION");
                        animationPanel.startAnimation(selectedAccount, () -> {
                            passwordField.setText("");
                            cardLayout.show(screenPanel, "PASSWORD");
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "이미 기계가 사용 중입니다!", "경고", JOptionPane.WARNING_MESSAGE);
                    }
                });
            } else {
                slotBtn.setEnabled(false);
            }
            hotbar.add(slotBtn);
        }
        return hotbar;
    }

    // --- 3. 비밀번호 입력 화면 (모던 스타일) ---
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(screenBgColor);

        JLabel titleLabel = new JLabel("비밀번호 4자리를 입력해주십시오", SwingConstants.CENTER);
        titleLabel.setForeground(brandColor);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setBounds(150, 80, 400, 40);
        panel.add(titleLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("맑은 고딕", Font.BOLD, 40));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setBounds(250, 150, 200, 60);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(new LineBorder(Color.GRAY, 2));
        // 물리 키패드로만 입력하게 하려면 아래 주석 해제
        // passwordField.setEditable(false);
        panel.add(passwordField);

        // 스크린 내 확인 버튼
        JButton confirmBtn = new JButton("확인");
        confirmBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        confirmBtn.setBackground(brandColor);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBounds(250, 250, 200, 50);

        confirmBtn.addActionListener(e -> {
            String pwd = new String(passwordField.getPassword());
            boolean authSuccess = true; // [현겸 파트] 연동 위치

            if (authSuccess) {
                cardLayout.show(screenPanel, "MENU");
            } else {
                JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(confirmBtn);

        JButton cancelBtn = new JButton("거래 취소");
        cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        cancelBtn.setBackground(new Color(200, 50, 50));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBounds(250, 310, 200, 50);
        cancelBtn.addActionListener(e -> ejectBankbook());
        panel.add(cancelBtn);

        return panel;
    }

    // --- 4. 메인 메뉴 화면 (터치 스크린 느낌) ---
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(screenBgColor);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("원하시는 거래를 선택해주십시오", SwingConstants.CENTER);
        titleLabel.setForeground(brandColor);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBackground(screenBgColor);

        String[] btnTitles = {"잔액 조회", "현금 입금", "현금 출금", "계좌 이체", "거래 내역 조회", "거래 종료 (카드 반환)"};

        for(int i=0; i<btnTitles.length; i++) {
            JButton btn = new JButton(btnTitles[i]);
            btn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(50, 50, 50));
            btn.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
            btn.setFocusPainted(false);

            // 종료 버튼만 색상 다르게
            if (i == 5) {
                btn.setBackground(new Color(240, 240, 240));
                btn.setForeground(Color.RED.darker());
                btn.addActionListener(e -> ejectBankbook());
            } else {
                final int idx = i;
                btn.addActionListener(e -> handleMenuAction(idx));
            }
            buttonPanel.add(btn);
        }

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private void handleMenuAction(int index) {
        switch (index) {
            case 0: // 잔액조회 [영석 파트]
                JOptionPane.showMessageDialog(this, "현재 잔액은 150,000원 입니다.", "잔액 조회", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 1: // 입금
                String dep = JOptionPane.showInputDialog(this, "하단 투입구에 넣을 금액을 입력하세요:");
                if(dep != null) JOptionPane.showMessageDialog(this, dep + "원이 입금 처리되었습니다.");
                break;
            case 2: // 출금
                String wid = JOptionPane.showInputDialog(this, "출금하실 금액을 입력하세요:");
                if(wid != null) JOptionPane.showMessageDialog(this, wid + "원 출금 완료.\n명세표와 현금을 챙겨주세요.");
                break;
            case 3: // 이체
                JOptionPane.showInputDialog(this, "이체받을 계좌번호를 입력하세요:");
                break;
            case 4: // 내역
                JOptionPane.showMessageDialog(this, "[최근 거래 내역]\n1. 입금 5만원\n2. 출금 1만원", "내역 조회", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private void ejectBankbook() {
        selectedAccount = "";
        isMachineReady = true;
        cardLayout.show(screenPanel, "WELCOME");
        JOptionPane.showMessageDialog(this, "카드가 반환되었습니다.\n잊지 말고 챙겨가주십시오.", "반환 완료", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- [하드웨어 패널 생성 (현금구, 키패드)] ---
    private JPanel createHardwarePanel() {
        JPanel hwPanel = new JPanel(new BorderLayout(20, 0));
        hwPanel.setBackground(new Color(210, 215, 220));
        hwPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        hwPanel.setPreferredSize(new Dimension(0, 180));

        // 왼쪽: 현금 및 명세표 나오는 곳
        JPanel slotPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        slotPanel.setOpaque(false);

        // 현금 투입/배출구 디자인
        JLabel cashSlot = new JLabel("====== 현 금 (CASH) ======", SwingConstants.CENTER);
        cashSlot.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        cashSlot.setOpaque(true);
        cashSlot.setBackground(new Color(30, 30, 30));
        cashSlot.setForeground(Color.GREEN);
        cashSlot.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        // 명세표 및 관리자 버튼 묶음
        JPanel receiptAndAdminPanel = new JPanel(new BorderLayout());
        receiptAndAdminPanel.setOpaque(false);
        JLabel receiptSlot = new JLabel("▼ 명세표", SwingConstants.CENTER);
        receiptSlot.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        JButton adminBtn = new JButton("설정");
        adminBtn.setPreferredSize(new Dimension(60, 30));
        adminBtn.setBackground(Color.DARK_GRAY);
        adminBtn.setForeground(Color.WHITE);
        adminBtn.addActionListener(e -> {
            if("1234".equals(JOptionPane.showInputDialog(this, "관리자 암호:"))) {
                JOptionPane.showMessageDialog(this, "[ 관리자 모드 ]\n해든님 기능 연동 대기중");
            }
        });

        receiptAndAdminPanel.add(receiptSlot, BorderLayout.CENTER);
        receiptAndAdminPanel.add(adminBtn, BorderLayout.EAST);

        slotPanel.add(cashSlot);
        slotPanel.add(receiptAndAdminPanel);
        hwPanel.add(slotPanel, BorderLayout.CENTER);

        // 오른쪽: 물리 숫자 키패드
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        keypadPanel.setOpaque(false);
        keypadPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "KEYPAD"));

        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "정정", "0", "확인"};
        for (String key : keys) {
            JButton keyBtn = new JButton(key);
            keyBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            keyBtn.setBackground(new Color(230, 230, 230));
            keyBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            // 특수키 색상
            if(key.equals("정정")) keyBtn.setBackground(new Color(255, 200, 100));
            if(key.equals("확인")) keyBtn.setBackground(new Color(100, 200, 100));

            // 키패드 클릭 시 비밀번호 필드에 입력되도록 연동
            keyBtn.addActionListener(e -> {
                if(passwordField != null && passwordField.isShowing()) {
                    if(key.equals("정정")) {
                        passwordField.setText("");
                    } else if(key.equals("확인")) {
                        // 확인 버튼은 스크린의 확인버튼 로직과 동일하게 추후 연동 가능
                    } else {
                        passwordField.setText(new String(passwordField.getPassword()) + key);
                    }
                }
            });
            keypadPanel.add(keyBtn);
        }
        hwPanel.add(keypadPanel, BorderLayout.EAST);

        return hwPanel;
    }

    // --- 애니메이션 패널 (모던 스타일에 맞게 색상 수정) ---
    class AnimationPanel extends JPanel {
        private int itemY;
        private Timer timer;
        private String accNumber = "";
        private Runnable onComplete;

        public AnimationPanel() {
            setBackground(screenBgColor);
        }

        public void startAnimation(String accNumber, Runnable onComplete) {
            this.accNumber = accNumber;
            this.onComplete = onComplete;
            this.itemY = getHeight() > 0 ? getHeight() : 500;

            if (timer != null && timer.isRunning()) timer.stop();

            timer = new Timer(20, e -> {
                itemY -= 8;
                if (itemY < 150) {
                    timer.stop();
                    Timer delay = new Timer(500, ev -> onComplete.run());
                    delay.setRepeats(false);
                    delay.start();
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 통장/카드
            g.setColor(new Color(240, 200, 100));
            g.fillRoundRect(getWidth() / 2 - 60, itemY, 120, 180, 10, 10);

            g.setColor(Color.BLACK);
            g.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            g.drawString("Acorn Card", getWidth() / 2 - 40, itemY + 30);
            g.drawString(accNumber, getWidth() / 2 - 35, itemY + 60);

            // 투입구 덮개 (스크린 배경색과 동일하게)
            g.setColor(screenBgColor);
            g.fillRect(0, 0, getWidth(), 170);

            // 검은색 카드 구멍
            g.setColor(Color.BLACK);
            g.fillRoundRect(getWidth() / 2 - 80, 160, 160, 10, 5, 5);

            g.setColor(brandColor);
            g.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            g.drawString("카드를 읽는 중입니다...", getWidth() / 2 - 100, 100);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AtmMainFrame frame = new AtmMainFrame();
            frame.setVisible(true);
        });
    }
}