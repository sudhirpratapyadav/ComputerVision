
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.opencv.core.Core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sudhir Pratap Yadav
 */
public class Game extends javax.swing.JFrame {

    /**
     * Creates new form Game
     */
    private static Graphics g;
    private static BufferedImage bi;
    
    private static int[][][] solution;
    public static int[][] state;
    private static Piece[] pieces;
    private static  int numPiece;
    private static int ind = 0;
    private static Image[] images;
    
    
    private static Timer animation; 
    
    public static void setStage(int pstate[][],int num)
    {
        numPiece = num;
        solution = new int[10][6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                state[i][j]=pstate[i][5-j];
                solution[0][i][j] = state[i][j];
                System.out.print(state[i][j]+"\t");
            }
            System.out.println("");
        }
        pieces = new Piece[numPiece];
        for (int i = 0; i < numPiece; i++) {
            pieces[i] = new Piece(0, 0, 0);
        }
        Logic l = new Logic(state,numPiece);
        solution = l.getShortestPath();
        ind=1;
        btn_nxt.setEnabled(true);
        drawBoard();
        JOptionPane.showMessageDialog(null,"Solved in "+(solution.length-1)+" steps");
        animation.start();
    }
    
    void loadState()
    {
        new PuzzleExtraction().setVisible(true);
    }
    
    
    private static class Piece
    {
        int x,y;
        int img;

        public Piece(int x, int y, int img) {
            this.x = x;
            this.y = y;
            this.img = img;
        }
    }
    
    public Game() {
        initComponents();
        
        animation = new Timer(100, (java.awt.event.ActionEvent ae) -> {
            if(btn_nxt.isEnabled())
                btn_nxt.doClick();
            else
                animation.stop();
        });
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        images = new Image[6];
        for (int i = 0; i < 6; i++) {
            try {
                images[i] = ImageIO.read(this.getClass().getResource(i+".png"));
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        bi = new BufferedImage(jlbl.getWidth(), jlbl.getHeight(), BufferedImage.TYPE_INT_RGB);
        state = new int[][]{{5,5,5,0,0,8},
                            {0,0,7,0,0,8},
                            {1,1,7,0,0,8},
                            {3,0,7,0,2,2},
                            {3,0,0,0,4,0},
                            {6,6,6,0,4,0},
                                };
        numPiece = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if(state[i][j]>numPiece)
                    numPiece = state[i][j];
            }
        }
        solution = new int[10][0][0];
        solution[0] = state;
        pieces = new Piece[numPiece];
        for (int i = 0; i < numPiece; i++) {
            pieces[i] = new Piece(0, 0, 0);
        }
        drawBoard();
    } 
    
    private static void setPieces()
    {
        for (int i = 0; i < numPiece; i++) {
            pieces[i] = new Piece(0, 0, 0);
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if(solution[ind][i][j]>0)
                {
                    if(pieces[solution[ind][i][j]-1].img==0)
                    {
                        pieces[solution[ind][i][j]-1].x = (j+1)*45+2*j;
                        pieces[solution[ind][i][j]-1].y = (i+1)*45+2*i;
                        if(solution[ind][i][j]==1)
                            pieces[solution[ind][i][j]-1].img=1;
                        else if(j<4 && solution[ind][i][j]==solution[ind][i][j+1] && solution[ind][i][j]==solution[ind][i][j+2])
                            pieces[solution[ind][i][j]-1].img=3;
                        else if(j<5 && solution[ind][i][j]==solution[ind][i][j+1])
                            pieces[solution[ind][i][j]-1].img=2;
                        else if(i<4 &&solution[ind][i][j]==solution[ind][i+1][j] && solution[ind][i][j]==solution[ind][i+2][j])
                            pieces[solution[ind][i][j]-1].img=5;
                        else
                            pieces[solution[ind][i][j]-1].img=4;
                    }
                }
                
            }
        }
    }
    
    private static void drawBoard()
    {
        g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.drawImage(images[0], 35, 35, null);
        setPieces();
        for (Piece piece : pieces) {
            g.drawImage(images[piece.img], piece.x, piece.y, null);
        }
        jlbl.setIcon(new ImageIcon(bi));
        if(ind>0)
            lbl_st.setText("Steps : "+(ind-1));
        else
        {
            lbl_st.setText("Steps : 0");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        Puzzle_Extraction = new javax.swing.JFrame();
        jlbl = new javax.swing.JLabel();
        btn_nxt = new javax.swing.JButton();
        btn_pre = new javax.swing.JButton();
        lbl_st = new javax.swing.JLabel();
        btn_ls = new javax.swing.JButton();

        javax.swing.GroupLayout Puzzle_ExtractionLayout = new javax.swing.GroupLayout(Puzzle_Extraction.getContentPane());
        Puzzle_Extraction.getContentPane().setLayout(Puzzle_ExtractionLayout);
        Puzzle_ExtractionLayout.setHorizontalGroup(
            Puzzle_ExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        Puzzle_ExtractionLayout.setVerticalGroup(
            Puzzle_ExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_nxt.setText("Next Move");
        btn_nxt.setEnabled(false);
        btn_nxt.addActionListener((java.awt.event.ActionEvent evt) -> {
            ind++;
            btn_pre.setEnabled(true);
            if(ind>solution.length-1)
            {
                ind = solution.length-1;
                btn_nxt.setEnabled(false);
            }
            drawBoard();
        });

        btn_pre.setText("Previous Move");
        btn_pre.setEnabled(false);
        btn_pre.addActionListener((java.awt.event.ActionEvent evt) -> {
            ind--;
            btn_nxt.setEnabled(true);
            if(ind<1)
            {
                ind = 1;
                btn_pre.setEnabled(false);
            }
            drawBoard();
        });

        lbl_st.setText("Steps : 0");

        btn_ls.setText("Load Stage");
        btn_ls.addActionListener((java.awt.event.ActionEvent evt) -> {
            loadState();
        });


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btn_pre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_nxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_st, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_ls, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlbl, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_ls, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_nxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_pre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_st, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>  
    
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> {
            new Game().setVisible(true);
        });
    }
    
    private javax.swing.JFrame Puzzle_Extraction;
    private javax.swing.JButton btn_ls;
    private static javax.swing.JButton btn_nxt;
    private javax.swing.JButton btn_pre;
    private static javax.swing.JLabel jlbl;
    private static javax.swing.JLabel lbl_st;           
}
