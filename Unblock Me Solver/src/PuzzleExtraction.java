import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class PuzzleExtraction extends javax.swing.JFrame {
    
    private final int SIZE = 120;
    private final int NUM_SIZE = 14400;
    private final int RX = 40;
    private final int RY = 40;
    
    private VideoCapture webSource;
    private Mat frame = null;
    private MatOfByte mem;
    
    private BufferedImage video_img,ed_img;
    
    private int contours[][];
    private int nump[][];
    
    private boolean ready = false;
    private int pv = 0;
    private int thres = 0;
    private int colors[];
    private int time = 0;
    private int ind=0 ,pind = 0;
    private int state[][];
    private int pstate[][];
    Graphics g;
    
    int val1=0;
    
    private javax.swing.JPanel pnl_video; 
    
    private Timer t;
    
    public PuzzleExtraction() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        frame = new Mat(640,480,CvType.CV_8UC3);
        mem = new MatOfByte();
        video_img = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
        ed_img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_3BYTE_BGR);
        colors = new int[]{Color.BLACK.getRGB(),Color.BLUE.getRGB()
                ,Color.CYAN.getRGB(),Color.DARK_GRAY.getRGB()
                ,Color.GRAY.getRGB(),Color.GREEN.getRGB()
                ,Color.LIGHT_GRAY.getRGB(),Color.MAGENTA.getRGB()
                ,Color.ORANGE.getRGB(),Color.PINK.getRGB()
                ,Color.RED.getRGB(),Color.YELLOW.getRGB()};
        contours = new int[SIZE][SIZE];
        nump = new int[SIZE][SIZE];
        state = new int[6][6];
        pstate = new int[6][6];
        webSource = new VideoCapture(0);
        
        // <editor-fold defaultstate="collapsed" desc="Timer">   
        t = new Timer(100, (java.awt.event.ActionEvent ae) -> {
            long ct = System.currentTimeMillis();
            if (webSource.grab()) {
                webSource.retrieve(frame);
                if (frame!=null) {
                    int rgb[];
                    double rgb_d[];
                    thres = (int)(pv*1.1)-7;
                    pv = 0;
                    for (int i = 0; i <640; i++) {
                        for (int j = 0; j < 480; j++) {
                            rgb_d = frame.get(j, i);
                            rgb =  new int[]{(int)rgb_d[0],(int)rgb_d[1],(int)rgb_d[2]};
                            Color c = new Color(rgb[0], rgb[1], rgb[2]);
                            val1+=(rgb[0]>rgb[1])?(rgb[0]>rgb[2]?rgb[0]:rgb[2]):(rgb[1]>rgb[2]?rgb[1]:rgb[2]);
                            if(639-i>=RX && 639-i<RX+SIZE && j>=RY && j<RY+SIZE)
                            {
                                int val = (rgb[0]>rgb[1])?(rgb[0]>rgb[2]?rgb[0]:rgb[2]):(rgb[1]>rgb[2]?rgb[1]:rgb[2]);
                                pv+=val;
                                nump[j-RY][639-i-RX] = (rgb[2]>=thres)?0:1;
                            }
                        }
                    }
                    
                    try {
                        Core.flip(frame, frame, 1);
                        Imgcodecs.imencode(".bmp",frame,mem);
                        video_img = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                        g = video_img.getGraphics();
                    } catch (IOException ex) {
                        Logger.getLogger(PuzzleExtraction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    for (int i = 1; i < SIZE-1; i++) {
                        for (int j = 1; j < SIZE-1; j++) {
                            if( nump[j][i]==0 && nump[j][i-1]==0 &&
                                    nump[j-1][i]==0 && nump[j+1][i]==0 &&
                                    nump[j-1][i-1]==0 && nump[j+1][i-1]==0 &&
                                    nump[j-1][i+1]==0 && nump[j+1][i+1]==0){
                                contours[j][i] = 1;
                            }
                            else
                                contours[j][i] = 0;
                        }
                    }   int min;
                    int equi[] = new int[500];
                    nump = new int[SIZE][SIZE];
                    for (int i = 1; i < SIZE-1; i++) {
                        for (int j = 1; j < SIZE-1; j++) {
                            if(contours[i][j]==1)
                            {
                                if(nump[i][j-1]==0 && nump[i-1][j-1]==0
                                        && nump[i-1][j]==0 && nump[i-1][j+1]==0)
                                {
                                    ind++;
                                    nump[i][j]=ind;
                                }
                                else
                                {
                                    min = 1000;
                                    if(nump[i][j-1]>0 && nump[i][j-1]<min)
                                        min = nump[i][j-1];
                                    if(nump[i-1][j-1]>0 && nump[i-1][j-1]<min)
                                        min = nump[i-1][j-1];
                                    if(nump[i-1][j]>0 && nump[i-1][j]<min)
                                        min = nump[i-1][j];
                                    if(nump[i-1][j+1]>0 && nump[i-1][j+1]<min)
                                        min = nump[i-1][j+1];
                                    
                                    if(nump[i][j-1]>0)
                                        equi[nump[i][j-1]] = min;
                                    if(nump[i-1][j-1]>0)
                                        equi[nump[i-1][j-1]] = min;
                                    if(nump[i-1][j]>0)
                                        equi[nump[i-1][j]] = min;
                                    if(nump[i-1][j+1]>0)
                                        equi[nump[i-1][j+1]] = min;
                                    nump[i][j] = min;
                                }
                                
                            }
                        }
                    }   int lab[] = new int[500];
                    for (int i = 1; i < SIZE-1; i++) {
                        for (int j = 1; j < SIZE-1; j++) {
                            if(nump[i][j]>0)
                            {
                                nump[i][j]=equi[nump[i][j]];
                                lab[nump[i][j]]++;
                            }
                        }
                    }   ind = 0;
                    equi = new int[500];
                    boolean shouldCheck = true;
                    for (int i = 1; i < SIZE-1; i++)
                    {
                        for (int j = 1; j < SIZE-1; j++)
                        {
                            if(nump[i][j]>0)
                            {
                                if(lab[nump[i][j]]>300)
                                {
                                    if(lab[nump[i][j]]<1000)
                                    {
                                        if(equi[nump[i][j]]==0)
                                        {
                                            ind++;
                                            equi[nump[i][j]]=ind;
                                        }
                                    }
                                    else
                                    {
                                        time = 0;
                                        shouldCheck =false;
                                    }
                                }
                                else
                                    nump[i][j]=0;
                            }
                        }
                    }   
                    for (int i = 1; i < SIZE-1; i++) {
                        for (int j = 1; j < SIZE-1; j++) {
                            if(nump[i][j]>0)
                            {
                                nump[i][j]=equi[nump[i][j]];
                                ed_img.setRGB(j, i, colors[nump[i][j]%12]);
                            }
                            else
                                ed_img.setRGB(j, i,-1);
                        }
                    }
                    state = new int[6][6];
                    if(ind>6 && shouldCheck)
                    {
                        if(ind == pind)
                        {
                            lab = new int[50];
                            int val,num=0;
                            for (int i = 0; i < 6; i++) {
                                for (int j = 0; j < 6; j++) {
                                    
                                    val = getValue(i,j);
                                    if(val==0)
                                        state[i][j]=0;
                                    else
                                    {
                                        if(lab[val]==0)
                                        {
                                            num++;
                                            lab[val]=num;
                                            state[i][j]=num;
                                        }
                                        else
                                        {
                                            state[i][j]=lab[val];
                                        }
                                    }
                                }
                            }
                            boolean match = true;
                            for (int i = 0; i < 6; i++) {
                                for (int j = 0; j < 6; j++) {
                                    if (state[i][j]!=pstate[i][j]) {
                                        match = false;
                                        break;
                                    }
                                }
                            }
                            if(match)
                                time++;
                            else
                                time = 0;
                            
                            if(time==2)
                                ready = true;
                        }
                        else
                            time=0;
                        for (int i = 0; i < 6; i++) {
                            System.arraycopy(state[i], 0, pstate[i], 0, 6);
                        }
                    }
                    else
                        time=0;
                    pind = ind;
                    pv/=NUM_SIZE;
                    val1/=307200;
                    g.drawImage(ed_img,480 ,RY , null);
                    g.setColor(Color.red);
                    for (int i = 1; i < 6; i++) {
                        g.drawLine(480+20*i,RY, 480+20*i, 160);
                        g.drawLine(480,40+20*i, 480+120,40+20*i);
                    }
                    g.setColor(Color.white);
                    g.drawRect(RX-1, RY-1, SIZE+2, SIZE+2);
                    g.drawString("Try to hold PuzzleExtractionzle in rectangle for 1 sec", 20, 20);
                    pnl_video.getGraphics().drawImage(video_img, 0, 0, null);
                    if (ready) {
                        if(checkValidity(ind))
                        {
                        int val = 0;
                        for(int j=0;j<5;j++)
                        {
                            if(state[2][j]>0 && state[2][j]==state[2][j+1])
                            {
                                val = state[2][j];
                                state[2][j]=-1;
                                state[2][j+1]=-1;
                                break;
                            }
                        }   for (int i = 0; i < 6; i++) {
                            for (int j = 0; j < 6; j++) {
                                if(state[i][j]==1)
                                    state[i][j]=val;
                                else if(state[i][j]==-1)
                                    state[i][j]=1;
                            }
                        }
                        t.stop();
                        webSource.release();
                        PuzzleExtraction.this.dispose();
                        Game.setStage(state,ind);
                        }
                        else{
                            ready = false;
                            time = 0;
                        }
                    }
                }
            }
            ct = System.currentTimeMillis() - ct;
            System.out.println(""+val1);
        });
        
        t.start();
        // </editor-fold> 
        
        pnl_video = new javax.swing.JPanel();
        Dimension dim = new Dimension(640, 480);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        add(pnl_video);
        pnl_video.setPreferredSize(dim);
        pnl_video.setBounds(0, 0, 640, 480);
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        pack();
    }
    
    private int getValue(int i,int j)
    {
        int i0 = i*20;
        int j0 = j*20;
        int i1 = i0+20;
        int j1 = j0+20;
        int num = 0;
        int val = 0;
        for(i=i0;i<i1;i++)
        {
            for(j = j0; j < j1; j++) {
                if(nump[i][j]>0)
                {
                    val = nump[i][j];
                    num++;
                }
            }
        }
        
        if(num>100)
            return val;
        
        return 0;
    }     
    
    private boolean checkValidity(int num)
    {
        int checked[] = new int[num+1];
        for (int i = 0; i <6; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                if(state[i][j]>0)
                {
                    if(checked[state[i][j]]==0)
                    {
                        checked[state[i][j]]=1;
                        if(i<5 && j<5 && state[i][j]==state[i+1][j+1])
                            return false;
                        else if(i<5 && j>0 && state[i][j]==state[i+1][j-1])
                            return false;
                        else if(j<5 && state[i][j]==state[i][j+1])
                        {
                            if(i>0 && state[i][j]==state[i-1][j+1])
                                return false;
                            else if(i>0 && j<4 && state[i][j]==state[i-1][j+2])
                                return false;
                            else if(i<5 && j<4 && state[i][j]==state[i+1][j+2])
                                return false;
                            else if(i<5 && state[i][j]==state[i+1][j+1])
                                return false;
                            else if(j<4 && state[i][j]==state[i][j+2])
                            {
                                if(i>0 && state[i][j]==state[i-1][j+2])
                                    return false;
                                else if(i>0 && j<3 && state[i][j]==state[i-1][j+3])
                                    return false;
                                else if(j<3 && state[i][j]==state[i][j+3])
                                    return false;
                                else if(i<5 && j<3 && state[i][j]==state[i+1][j+3])
                                    return false;
                                else if(i<5 && state[i][j]==state[i+1][j+2])
                                    return false;
                            }
                        }
                        else if(i<5 && state[i][j]==state[i+1][j])
                        {
                            if(j>0 && state[i][j]==state[i+1][j-1])
                                return false;
                            else if(j>0 && i<4 && state[i][j]==state[i+2][j-1])
                                return false;
                            else if(j<5 && i<4 && state[i][j]==state[i+2][j+1])
                                return false;
                            else if(j<5 && state[i][j]==state[i+1][j+1])
                                return false;
                            else if(i<4 && state[i][j]==state[i+2][j])
                            {
                                if(j>0 && state[i][j]==state[i+2][j-1])
                                    return false;
                                else if(j>0 && i<3 && state[i][j]==state[i+3][j-1])
                                    return false;
                                else if(i<3 && state[i][j]==state[i+3][j])
                                    return false;
                                else if(i<3 && j<5 && state[i][j]==state[i+3][j+1])
                                    return false;
                                else if(j<5 && state[i][j]==state[i+2][j+1])
                                    return false;
                            }
                        }
                        else
                            return false;
                    }
                }
            }
        }
        return true;
    }
}