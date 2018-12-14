/*
 * Copyright (C) 2018 Reginildo Sousa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.reginildo.tomato;

import org.jetbrains.annotations.Contract;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

/* JFrameTomatoMain.java
 * JFrame pricipal do aplicativo.
 * */

final class JFrameTomatoMain extends JFrame {

    Font fontTahoma = new Font("Tahoma", Font.PLAIN,18);
    Font fontArial = new Font("Arial", Font.BOLD, 85);

    static JLabel jLabelToImageIconSmileys, jLabelTimeCounterView, jLabelShowCiclos, jLabelFoco;
    private static boolean showTimeView;
    private static JLabel jLabelHora;
    static ResourceBundle resourceBundle = ResourceBundle
            .getBundle("io.github.reginildo.tomato/Labels", Locales.localeDefault);
    private JRadioButtonMenuItem radioButtonMenuItemPT_BR, radioButtonMenuItemEN_US, radioButtonMenuItemKlingon;
    private int confirmDialog;
    private int startOver;
    private static int countInterations = 1;
    private boolean timeToShortBreak;
    private boolean timeToLongBreak;
    private boolean timeToPomodoro;
    static Timer timer = null;
    static JButton jButtonStart, jButtonPause, jButtonReset;
    private JMenuItem jMenuItemSettings, jMenuItemQuit, jMenuItemAbout;
    private static JFrameSettings jFrameSettings = new JFrameSettings();
    static String stringValorLongBreak;
    static Calendar timerStart = Calendar.getInstance();
    static Date timerPause;
    static final SimpleDateFormat format = new SimpleDateFormat(
            "mm:ss");
    private JMenu jMenuFile, jMenuLanguage, jMenuHelp;
    private JMenuBar jMenuBar;
    private JPanel jPanelButtons, jPanelTimer, jPanelMainInfo, jPanelDetails;
    private ImageIcon imageWork, imageEnjoy, imageSuccess, imagePrepared;

    JFrameTomatoMain() {
        initThreadHour();
        setInitTimerStart(); // todo this
        setImageIcons();
        setLookAndFeel();
        setJLabels();
        setJPanels();
        setThisJMenuBar();
        setJMenuBar(jMenuBar);
        setContentPane(jPanelTimer);
        add(jPanelButtons);
        add(jPanelMainInfo);

        add(jPanelDetails);
        setTitle("Pomodoro tempo");
        setSize(300, 400);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setShowTimeView(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setThisJMenuBar() {
        setJMenus();
        jMenuBar = new JMenuBar();
        jMenuBar.setBackground(Color.orange);
        jMenuBar.setBorderPainted(false);
        jMenuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jMenuBar.add(jMenuFile);
        jMenuBar.add(jMenuLanguage);
        jMenuBar.add(jMenuHelp);
    }

    private void setJLabels() {

        jLabelHora = new JLabel();
        jLabelToImageIconSmileys = new JLabel();
        jLabelToImageIconSmileys.setIcon(getImagePrepared());
        jLabelToImageIconSmileys.setVisible(true);
        jLabelTimeCounterView = new JLabel("00:00");
        jLabelTimeCounterView.setFont(fontArial);
        jLabelTimeCounterView.setText(format.format(timerStart.getTime()));
        jLabelTimeCounterView.setForeground(Color.RED);
        jLabelShowCiclos = new JLabel();
        jLabelShowCiclos.setFont(fontTahoma);
        jLabelShowCiclos.setVisible(false);
        jLabelFoco = new JLabel("Mantenha o foco!");
        jLabelFoco.setFont(fontTahoma);
        jLabelFoco.setVisible(false);
    }

    private void setJPanels() {
        jPanelButtons = new JPanel();
        jPanelButtons.setBackground(Color.YELLOW);
        jPanelTimer = new JPanel();
        jPanelTimer.setLayout(new FlowLayout());
        jPanelTimer.setBackground(Color.YELLOW);
        jPanelTimer.add(jLabelTimeCounterView);
        jPanelMainInfo = new JPanel();
        jPanelMainInfo.setLayout(new BoxLayout(jPanelMainInfo, BoxLayout.Y_AXIS));
        jPanelMainInfo.setBackground(Color.YELLOW);
        jPanelMainInfo.add(jLabelToImageIconSmileys);


        jPanelDetails = new JPanel();

        jPanelDetails.setBackground(Color.YELLOW);
        jPanelDetails.setForeground(Color.darkGray);
        jPanelDetails.setLayout(new BoxLayout(jPanelDetails, BoxLayout.Y_AXIS));
        jPanelDetails.add(jLabelShowCiclos);
        jPanelDetails.add(jLabelFoco);
        jPanelDetails.add(jLabelHora);


        setJButtons();
        jPanelButtons.add(jButtonStart);
        jPanelButtons.add(jButtonPause);
        jPanelButtons.add(jButtonReset);
    }

    @Contract(pure = true)
    static boolean isShowTimeView() {
        return showTimeView;
    }

    /**
     * @param timeView For to see a label with the date and time now
     */
    static void setShowTimeView(boolean timeView) {
        JFrameTomatoMain.showTimeView = timeView;
    }

    private static void initThreadHour() {
        new Thread(new HoraThread()).start();
    }

    private void setImageIcons() {
        imageWork = new ImageIcon(getClass().getResource(
                "/io/github/reginildo/" +
                        "tomato/images/icon_smiley_work.png"));
        imageEnjoy = new ImageIcon(getClass().getResource(
                "/io/github/reginildo/" +
                        "tomato/images/icon_smiley_enjoy.png"));
        imageSuccess = new ImageIcon(getClass().getResource(
                "/io/github/reginildo/" +
                        "tomato/images/icon_smiley_sucess.png"));

        imagePrepared = new ImageIcon(getClass().getResource(
                "/io/github/reginildo/" +
                        "tomato/images/icon_smiley_prepared.png"));
    }

    private void setInitTimerStart() {
        timerStart.set(Calendar.MINUTE, 25);
        timerStart.set(Calendar.SECOND, 0);
    }

    private void setJButtons() {
        jButtonStart = new JButton(resourceBundle.getString("buttonStart"));
        jButtonStart.addActionListener(new JButtonStartListener());
        jButtonStart.setMnemonic(KeyEvent.VK_S);
        jButtonStart.addMouseMotionListener(new JButtonStartMotionListener());
        jButtonPause = new JButton(resourceBundle.getString("buttonPause"));
        jButtonPause.addActionListener(new JButtonPauseListener());
        jButtonPause.setMnemonic(KeyEvent.VK_P);
        jButtonPause.addMouseMotionListener(new JButtonPauseMotionListener());
        jButtonReset = new JButton(resourceBundle.getString("buttonReset"));
        jButtonReset.addActionListener(new JButtonResetListener());
        jButtonReset.setMnemonic(KeyEvent.VK_R);
        jButtonReset.addMouseMotionListener(new JButtonResetMotionListener());
        jButtonPause.setEnabled(false);
        jButtonReset.setEnabled(false);
    }

    private void setJMenuItens() {
        jMenuItemSettings = new JMenuItem(resourceBundle.getString("menuItemSetting"));
        jMenuItemSettings.addMouseMotionListener(new JMenuItemSettingsMotionListener());
        jMenuItemSettings.addActionListener(new JMenuItemSettingsListener());
        jMenuItemSettings.setMnemonic(KeyEvent.VK_E);
        jMenuItemQuit = new JMenuItem("Quit");
        jMenuItemQuit.addActionListener(new JMenuItemQuitListener());
        jMenuItemQuit.addMouseMotionListener(new JMenuItemQuitMotionListener());
        jMenuItemQuit.setMnemonic(KeyEvent.VK_Q);
        jMenuItemAbout = new JMenuItem("About");
        jMenuItemAbout.addActionListener(new JMenuItemAboutListener());
    }

    private void refreshLanguage() {
        setVisible(false);
        repaint();
        setVisible(true);
    }

    private void setButtonGroupLanguages() {
        setJRadioButtonMenuItens();
        ButtonGroup buttonGroupLanguages = new ButtonGroup();
        buttonGroupLanguages.add(radioButtonMenuItemPT_BR);
        buttonGroupLanguages.add(radioButtonMenuItemEN_US);
        buttonGroupLanguages.add(radioButtonMenuItemKlingon);
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info :
                    UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            e.printStackTrace();
        }
    }

    private void setJRadioButtonMenuItens() {
        radioButtonMenuItemPT_BR =
                new JRadioButtonMenuItem(resourceBundle
                        .getString("radioButtonMenuItemPT_BR"));
        radioButtonMenuItemEN_US =
                new JRadioButtonMenuItem(resourceBundle
                        .getString("radioButtonMenuItemEN_US"));
        radioButtonMenuItemKlingon =
                new JRadioButtonMenuItem(resourceBundle
                        .getString("radioButtonMenuItemKlingon"));
        radioButtonMenuItemPT_BR.addActionListener(new RadioButtonMenuItemPT_BRListener());
        radioButtonMenuItemEN_US.addActionListener(new RadioButtonMenuItemEN_USListener());
        radioButtonMenuItemKlingon.addActionListener(new RadioButtonMenuItemKlingonListener());
    }

    private void setJMenus() {
        setButtonGroupLanguages();
        setJMenuItens();
        jMenuFile = new JMenu(resourceBundle
                .getString("menuFile"));
        jMenuLanguage = new JMenu(resourceBundle
                .getString("menuLanguage"));
        jMenuLanguage.add(radioButtonMenuItemPT_BR);
        jMenuLanguage.add(radioButtonMenuItemEN_US);
        jMenuLanguage.add(radioButtonMenuItemKlingon);
        jMenuFile.setMnemonic(KeyEvent.VK_F);
        jMenuHelp = new JMenu("Help");
        jMenuHelp.add(jMenuItemAbout);


        jMenuFile.add(jMenuItemSettings);
        jMenuFile.add(jMenuItemQuit);
    }

    private void startPomodoroTimer() {

        if (timerPause == null) {
            if (jFrameSettings != null) {
                timerStart.set(Calendar.MINUTE, Tomato.getPomodoroTime());
            } else {
                timerStart.set(Calendar.MINUTE, Tomato.getDefaultPomodoroTime());
            }
            timerStart.set(Calendar.SECOND, 0);
        } else {
            timerStart.setTime(timerPause);
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTaskPomodoro(), 0, 1000);


    }

    private static void playAlarm() {
        Toolkit.getDefaultToolkit().beep();
    }

    private void setTimerStartAndTimeCounterView() {
        try {
            timerStart.set(Calendar.SECOND, (
                    timerStart.get(Calendar.SECOND) - 1));
            jLabelTimeCounterView.setText(format.format(timerStart.getTime()));
            jLabelShowCiclos.setText("Ainda faltam " + Tomato.getCiclosTime() + " ciclos");
            jLabelShowCiclos.setVisible(true);
            jLabelFoco.setVisible(true);
            if (isTheLastOneCiclo()){
                jLabelShowCiclos.setText("É o ultimo! Continue!");
            }

            if (isTimeToShortBreak() || isTimeToLongBreak()){
                jLabelShowCiclos.setVisible(false);
                jLabelFoco.setVisible(false);
            } else {
                jLabelShowCiclos.setVisible(true);
                jLabelFoco.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isTimeToShortBreak() {
        if (countInterations == 2 ||
                countInterations == 4 ||
                countInterations == 6) {
            timeToShortBreak = true;
        }
        return timeToShortBreak;
    }

    private boolean isTimeToLongBreak() {
        if (countInterations >= 8) {
            timeToLongBreak = true;
        }
        return timeToLongBreak;
    }

    private boolean isTimeToPomodoro() {
        if (countInterations == 1 ||
                countInterations == 3 ||
                countInterations == 5 ||
                countInterations == 7) {
            timeToPomodoro = true;
        }
        return timeToPomodoro;
    }

    private void startShortBreakTimer() {
        if (timerPause == null) {
            if (jFrameSettings != null) {
                timerStart.set(Calendar.MINUTE, Tomato.getShortBreakTime());
            } else {
                timerStart.set(Calendar.MINUTE, Tomato.getDefaultShortBreakTime());
            }
            timerStart.set(Calendar.SECOND, 0);
        } else {
            timerStart.setTime(timerPause);
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTaskShortBreak(), 0, 1000);
    }

    private void startLongBreakTimer() {
        if (timerPause == null) {
            if (jFrameSettings != null) {
                timerStart.set(Calendar.MINUTE, Tomato.getLongBreakTime());
            } else {
                timerStart.set(Calendar.MINUTE, Tomato.getDefaultLongBreakTime());
            }
            timerStart.set(Calendar.SECOND, 0);
        } else {
            timerStart.setTime(timerPause);
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTaskLongBreak(), 0, 1000);
    }

    private boolean isTheEnd() {

        return ((timerStart.get(Calendar.MINUTE) == 0) && (timerStart.get(Calendar.SECOND) == 0));
    }

    private ImageIcon getImageWork() {
        return imageWork;
    }

    private ImageIcon getImageEnjoy() {
        return imageEnjoy;
    }

    private ImageIcon getImageSuccess() {
        return imageSuccess;
    }

    ImageIcon getImagePrepared() {
        return imagePrepared;
    }

    private void setTimeToShortBreak(boolean timeShortBreak) {
        this.timeToShortBreak = timeShortBreak;
    }

    private void setTimeToLongBreak(boolean timeLongBreak) {
        this.timeToLongBreak = timeLongBreak;
    }

    private void setTimeToPomodoro(boolean timePomodoro) {
        this.timeToPomodoro = timePomodoro;
    }

    private class JButtonPauseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (jButtonPause.isEnabled()) {
                timerPause = timerStart.getTime();
                timer.cancel();
                jButtonPause.setEnabled(false);
                jButtonReset.setEnabled(true);
                jButtonStart.setEnabled(true);
            }

        }
    }

    private class JButtonStartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (jButtonStart.isEnabled()) {
                Tomato.setPomodoroTime(timerStart.get(Calendar.MINUTE));
                jLabelToImageIconSmileys.setIcon(getImageWork());
                jButtonStart.setEnabled(false);
                jButtonPause.setEnabled(true);
                startPomodoroTimer();
            }


        }
    }

    private class JButtonResetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            jLabelToImageIconSmileys.setIcon(getImagePrepared());
            if (isTimeToShortBreak()) {
                timerStart.set(Calendar.MINUTE, Tomato.getShortBreakTime());
            } else if (isTimeToLongBreak()) {
                timerStart.set(Calendar.MINUTE, Tomato.getLongBreakTime());
            } else {
                timerStart.set(Calendar.MINUTE, Tomato.getPomodoroTime());
            }
            jLabelShowCiclos.setVisible(false);
            jLabelFoco.setVisible(false);
            timerStart.set(Calendar.SECOND, 0);
            timerPause = null;
            timer.cancel();
            jButtonStart.setText(resourceBundle.getString("buttonStart"));
            jButtonPause.setEnabled(false);
            jButtonStart.setEnabled(true);
            jButtonReset.setEnabled(false);
            jLabelTimeCounterView.setText(format.format(
                    timerStart.getTime()));

        }
    }

    private class JMenuItemSettingsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // todo mostrar visivel aqui e construir antes
                jFrameSettings.setVisible(true);
        }
    }

    private class JMenuItemQuitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class JMenuItemAboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new JFrameAbout();
        }
    }

    private class RadioButtonMenuItemPT_BRListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resourceBundle = ResourceBundle.
                    getBundle("io.github.reginildo.tomato/Labels",
                            Locales.locale_pt_BR);
            resourceBundle.keySet();
            refreshLanguage();
        }
    }

    private class RadioButtonMenuItemEN_USListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resourceBundle = ResourceBundle.
                    getBundle("io.github.reginildo.tomato/Labels", Locales.locale_en_US);
            resourceBundle.keySet();
            refreshLanguage();

        }
    }

    private class RadioButtonMenuItemKlingonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            resourceBundle = ResourceBundle.
                    getBundle("io.github.reginildo.tomato/Labels",
                            Locales.locale_tlh);
            resourceBundle.keySet();
            refreshLanguage();

        }
    }

    private class JMenuItemSettingsMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            jMenuFile.setToolTipText("Configurar intervalos de tempo");
        }
    }

    private class JButtonStartMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            jButtonStart.setToolTipText("Iniciar pomodoro");
        }
    }

    private class JButtonPauseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            jButtonPause.setToolTipText("Pausa pomodoro");
        }
    }

    private class JButtonResetMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            jButtonReset.setToolTipText("Reiniciando");
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            jButtonReset.setToolTipText("Reiniciar pomodoro");
        }
    }

    private class JMenuItemQuitMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            jMenuItemQuit.setToolTipText("Quit the application");
        }
    }

    private static class HoraThread implements Runnable {

        private static DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM);

        @Override
        public void run() {
            while (JFrameTomatoMain.isShowTimeView()) {
                JFrameTomatoMain.jLabelHora.setLayout(new FlowLayout(FlowLayout.TRAILING));
                JFrameTomatoMain.jLabelHora.setText(dateFormat.format(new Date()));
                JFrameTomatoMain.jLabelHora.setForeground(Color.DARK_GRAY);
            }
        }
    }

    private class TimerTaskPomodoro extends TimerTask {
        @Override
        public void run() {
            setTimerStartAndTimeCounterView();
            if (isTheEnd()) {
                playAlarm();
                countInterations++;
                timer.cancel();

                // todo verifica se é o ultimo ciclo
                if (isTheLastOneCiclo()){
                    startOver = JOptionPane.showConfirmDialog(null,
                            String.format("\nParabéns!!! Você concluiu! \niniciar um novo ciclo?"));
                    if (startOver == JOptionPane.YES_OPTION){
                        Tomato.setCiclosTime(JFrameSettings.getjSliderCiclos().getValue());
                        Tomato.setLongBreakTime(JFrameSettings.getjSliderLongBreak().getValue());
                        Tomato.setShortBreakTime(JFrameSettings.getjSliderShortBreak().getValue());
                        JFrameTomatoMain.countInterations = 1;

                        startPomodoroTimer();
                    }
                }else {
                    confirmDialog = JOptionPane.showConfirmDialog(
                            null, "Hora de relaxar.\nIniciar o intervalo curto?");
                    if (confirmDialog == JOptionPane.YES_OPTION && (isTimeToShortBreak())) {
                        jLabelToImageIconSmileys.setIcon(getImageEnjoy());
                        Tomato.setCiclosTime(Tomato.getCiclosTime() - 1);
                        setTimeToLongBreak(false);
                        setTimeToPomodoro(false);
                        startShortBreakTimer();
                    } else if (confirmDialog == JOptionPane.YES_OPTION && (isTimeToLongBreak())) {
                        jLabelToImageIconSmileys.setIcon(getImageSuccess());
                        setTimeToShortBreak(false);
                        setTimeToPomodoro(false);
                        startLongBreakTimer();
                    }
                }
            }
        }
    }

    private boolean isTheLastOneCiclo() {

        return Tomato.getCiclosTime() == 1;
    }

    private class TimerTaskShortBreak extends TimerTask {

        @Override
        public void run() {
            setTimerStartAndTimeCounterView();
            if (isTheEnd()) {
                playAlarm();
                countInterations++;
                timer.cancel();
                confirmDialog = JOptionPane.showConfirmDialog(
                        null, "Fim do short break.\nIniciar o novo pomodoro?");
                if (confirmDialog == JOptionPane.YES_OPTION && (isTimeToPomodoro())) {
                    jLabelToImageIconSmileys.setIcon(getImageWork());
                    setTimeToShortBreak(false);
                    setTimeToLongBreak(false);
                    startPomodoroTimer();
                } else if (confirmDialog == JOptionPane.YES_OPTION && (isTimeToLongBreak())) {
                    jLabelToImageIconSmileys.setIcon(getImageSuccess());
                    setTimeToShortBreak(false);
                    setTimeToPomodoro(false);
                    startLongBreakTimer();
                }
            }

        }
    }

    private class TimerTaskLongBreak extends TimerTask {

        @Override
        public void run() {
            setTimerStartAndTimeCounterView();
            if (isTheEnd()) {
                playAlarm();
                countInterations++;
                timer.cancel();
                confirmDialog = JOptionPane.showConfirmDialog(
                        null, "Fim do Long Break.\nIniciar um novo ciclo?");
                if (confirmDialog == JOptionPane.YES_OPTION) {
                    jLabelToImageIconSmileys.setIcon(getImageWork());
                    countInterations = 1;
                    setTimeToLongBreak(false);
                    setTimeToShortBreak(false);
                    startPomodoroTimer();
                }
            }

        }
    }
}
