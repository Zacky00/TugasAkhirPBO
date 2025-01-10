import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tampilan extends JFrame {
    private JPanel[][] gridpanel;
    private JPanel[] gridnum;
    private JLabel[][] number;
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedTime = 0;
    private int selectedNumber = 0;
    private Board board;

    Tampilan() {
        this.setVisible(true);
        this.setTitle("Sudoku Archive");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 530);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.CYAN);
        menu();
    }

    public void menu() {
        JLabel gambar;
        JButton easy, medium, hard;
        easy = new JButton("Easy");
        medium = new JButton("Medium");
        hard = new JButton("Hard");

        ImageIcon icon = new ImageIcon("src/Gambar/title.png");
        Image scaledImage = icon.getImage().getScaledInstance(600, 200, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        gambar = new JLabel(icon);
        gambar.setBounds(100, 50, 600, 200);

        easy.setBounds(100, 350, 150, 50);
        medium.setBounds(300, 350, 150, 50);
        hard.setBounds(500, 350, 150, 50);

        easy.setBackground(Color.black);
        medium.setBackground(Color.black);
        hard.setBackground(Color.black);

        easy.setForeground(Color.white);
        medium.setForeground(Color.white);
        hard.setForeground(Color.white);

        easy.addActionListener(e -> permainan(easy.getText()));
        medium.addActionListener(e -> permainan(medium.getText()));
        hard.addActionListener(e -> permainan(hard.getText()));

        this.add(gambar);
        this.add(easy);
        this.add(medium);
        this.add(hard);
        this.revalidate();
        this.repaint();
    }

    public void permainan(String kesulitan) {
        board = new Board(kesulitan);
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
        creategrid();
        createnum();

        JButton kembali;
        elapsedTime = 0;

        startTimer();
        createTimerLabel();

        kembali = new JButton("Kembali");
        kembali.setBounds(600, 420, 150, 50);
        kembali.setBackground(Color.black);
        kembali.setForeground(Color.white);
        kembali.addActionListener(e -> {
            timer.stop();
            this.getContentPane().removeAll();
            this.revalidate();
            this.repaint();
            menu();
        });

        this.add(kembali);
        this.revalidate();
        this.repaint();
    }

    public void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                int minutes = elapsedTime / 60;
                int seconds = elapsedTime % 60;
                timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
            }
        });
        timer.start();
    }

    public void createTimerLabel() {
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setBounds(650, 10, 100, 30);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(timerLabel);
    }

    public void creategrid() {
        int h = 0, i = 0;
        gridpanel = new JPanel[9][9];
        number = new JLabel[9][9];

        for (int j = 0; j < 9; j++) {
            for (int k = 0; k < 9; k++) {
                gridpanel[j][k] = new JPanel();
                gridpanel[j][k].setLayout(null);
                gridpanel[j][k].setBounds(h, i, 50, 50);
                gridpanel[j][k].setBorder(BorderFactory.createLineBorder(Color.white));

                number[j][k] = new JLabel(board.getgrid(j, k) == 0 ? "" : String.valueOf(board.getgrid(j, k)));
                number[j][k].setBounds(15, 15, 20, 20);
                gridpanel[j][k].add(number[j][k]);

                int finalJ = j;
                int finalK = k;

                gridpanel[j][k].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (board.getIseditable(finalJ, finalK) && selectedNumber != 0) {
                            if (board.insert(finalJ, finalK, selectedNumber)) {
                                number[finalJ][finalK].setText(String.valueOf(selectedNumber));

                                if (board.Iscompleted()) {
                                    selesai();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Angka tidak valid di posisi ini!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Sel ini tidak dapat diubah!");
                        }
                    }
                });
                this.add(gridpanel[j][k]);
                if ((k + 1) % 3 == 0 && k != 0) {
                    i += 10;
                }
                i += 50;
            }
            if ((j + 1) % 3 == 0 && j != 0) {
                h += 10;
            }
            i = 0;
            h += 50;
        }
        this.revalidate();
        this.repaint();
    }

    public void createnum() {
        int h = 500, i = 0;
        gridnum = new JPanel[9];

        for (int j = 0; j < 9; j++) {
            gridnum[j] = new JPanel();
            gridnum[j].setLayout(new BorderLayout());
            gridnum[j].setBackground(Color.black);
            gridnum[j].setBounds(h, i, 50, 50);
            gridnum[j].setBorder(BorderFactory.createLineBorder(Color.white));

            JLabel numberLabel = new JLabel(String.valueOf(j + 1), SwingConstants.CENTER);
            numberLabel.setForeground(Color.white);
            numberLabel.setFont(new Font("Arial", Font.BOLD, 20));
            gridnum[j].add(numberLabel, BorderLayout.CENTER);

            int finalNumber = j + 1;

            // Tambahkan MouseListener untuk memilih angka
            gridnum[j].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedNumber = finalNumber; // Simpan angka yang dipilih
                    JOptionPane.showMessageDialog(null, "Angka " + selectedNumber + " dipilih!");
                }
            });
            if((j + 1) % 3 == 0 && j != 0)i+=10;
            this.add(gridnum[j]);
            i += 50;
        }

        this.revalidate();
        this.repaint();
    }

    public void selesai() {
        JLabel gambar, waktuSelesai, pertanyaan;
        ImageIcon icon = new ImageIcon("src/Gambar/SELESAI.png");
        Image scaledImage = icon.getImage().getScaledInstance(600, 200, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        gambar = new JLabel(icon);
        gambar.setBounds(100, 50, 600, 200);

        // Tampilkan waktu selesai
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        waktuSelesai = new JLabel(String.format("Waktu selesai: %02d:%02d", minutes, seconds), SwingConstants.CENTER);
        waktuSelesai.setBounds(100, 260, 600, 30);
        waktuSelesai.setFont(new Font("Arial", Font.PLAIN, 16));

        // Pertanyaan "Main lagi?"
        pertanyaan = new JLabel("Main lagi?", SwingConstants.CENTER);
        pertanyaan.setBounds(100, 300, 600, 30);
        pertanyaan.setFont(new Font("Arial", Font.BOLD, 20));

        JButton ya, tidak;
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();

        ya = new JButton("Ya");
        ya.setBounds(200, 400, 150, 50);
        ya.setBackground(Color.black);
        ya.setForeground(Color.white);
        ya.addActionListener(e -> {
            this.getContentPane().removeAll(); // Bersihkan semua komponen
            this.revalidate();
            this.repaint();
            menu(); // Tampilkan menu lagi
        });
        tidak = new JButton("Tidak");
        tidak.setBounds(400, 400, 150, 50);
        tidak.setBackground(Color.black);
        tidak.setForeground(Color.white);
        tidak.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Terima kasih sudah bermain!");
            System.exit(0); // Keluar dari program
        });
        this.add(gambar);
        this.add(waktuSelesai);
        this.add(pertanyaan);
        this.add(ya);
        this.add(tidak);
        this.revalidate();
        this.repaint();
    }
}
