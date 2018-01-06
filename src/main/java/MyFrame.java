import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame implements ActionListener, ComponentListener {

    private long start, end;
    private JButton grey, negative, blur, outline;
    private ImageIcon originalImageIcon, changedImageIcon;
    private JMenuItem openFile, saveFile, closeFile, exitFile ;
    private JPanel originalImagePanel = new JPanel();
    private JPanel changedImagePanel = new JPanel();
    private JComboBox comboBox = new JComboBox();
    JPanel container = new JPanel();

    private BufferedImage originalImage = null;
    private BufferedImage changeImage = null;

    private int scaleWidth;
    private int scaleHeight;

    JPanel buttomPanel = new JPanel();
    JLabel timeLable = new JLabel();
    public MyFrame() throws IOException {

        this.setTitle("miniPhotoshop");
        this.setSize(1000, 500);
      //  this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addComponentListener(this);

        this.setLayout(new BorderLayout());

        setLocationRelativeTo(null);

        container.setLayout(new GridLayout(1,2));

        originalImagePanel.setBorder(BorderFactory.createTitledBorder("Оригинал"));
        originalImagePanel.setLayout(new BorderLayout(1, 1));
        //originalImagePanel.setPreferredSize(new Dimension(IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT));
        changedImagePanel.setBorder(BorderFactory.createTitledBorder("Измененная"));
        changedImagePanel.setLayout(new BorderLayout(1, 1));
        //changedImagePanel.setPreferredSize(new Dimension(IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT));

        container.add(originalImagePanel);
        container.add(changedImagePanel);

        add(this.setMenu(), BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        add(this.setButtonPanel(), BorderLayout.EAST);

        //pack();
        this.setVisible(true);
    }

    private Component setMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu();
        file.setText("File");

        openFile = new JMenuItem();
        openFile.setText("Open");
        openFile.addActionListener(this);

        saveFile = new JMenuItem();
        saveFile.setText("Save");
        saveFile.addActionListener(this);

        closeFile = new JMenuItem();
        closeFile.setText("Close");
        closeFile.addActionListener(this);

        JSeparator separator = new JSeparator();

        exitFile = new JMenuItem();
        exitFile.setText("EXIT");
        exitFile.addActionListener(this);

        file.add(openFile);
        file.add(saveFile);
        file.add(closeFile);
        file.add(separator);
        file.add(exitFile);

        menuBar.add(file);

        return menuBar;
    }

    private Component setButtonPanel() {

        buttomPanel.setLayout(new GridLayout(10, 1));

        grey = new JButton("GREY");
        grey.setActionCommand("GREY");
        grey.addActionListener(this);

        negative = new JButton("NEGATIVE");
        negative.setActionCommand("NEGATIVE");
        negative.addActionListener(this);

        blur = new JButton("BLUR");
        blur.setActionCommand("BLUR");
        blur.addActionListener(this);

        outline = new JButton("OUTLINE");
        outline.setActionCommand("OUTLINE");
        outline.addActionListener(this);

        comboBox.addItem("последовательный способ");
        comboBox.addItem("параллельный (Thread)");
        comboBox.addItem("параллельный (Executor)");
        comboBox.addItem("параллельный (Fork_Join)");

        buttomPanel.add(grey);
        buttomPanel.add(negative);
        buttomPanel.add(blur);
        buttomPanel.add(outline);

        buttomPanel.add(comboBox);

        return buttomPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == openFile) {
            try {
                openFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (source == closeFile) {
            closeFile();
        }
        if (source == exitFile) {
            exitFile();
        }
        if (source == saveFile) {
            try {
                saveFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        String actionCommand = e.getActionCommand();
        switch (actionCommand){
            case "GREY":
            {
                clearChangedImage();
                if ( comboBox.getSelectedItem() == "последовательный способ" ){
                    start = System.nanoTime();

                    setGreyConsistent();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Thread)" ){
                    start = System.nanoTime();

                    setGreyThread();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Executor)" ){
                    start = System.nanoTime();

                    setGreyExecutor();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Fork_Join)" ){
                    start = System.nanoTime();

                    setGreyFork_Join();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
            }
            break;
            case "NEGATIVE":
            {
                clearChangedImage();
                if ( comboBox.getSelectedItem() == "последовательный способ" ){
                    start = System.nanoTime();

                    setNegativeConsistent();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Thread)" ){
                    start = System.nanoTime();

                    setNegativeThread();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Executor)" ){
                    start = System.nanoTime();

                    setNegativeExecutor();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Fork_Join)" ){
                    start = System.nanoTime();

                    setNegativeFork_Join();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
            }
            break;
            case "BLUR":
            {
                clearChangedImage();
                if ( comboBox.getSelectedItem() == "последовательный способ" ){
                    start = System.nanoTime();

                    setBlurConsistent();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Thread)" ){
                    start = System.nanoTime();

                    setBlurThread();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Executor)" ){
                    start = System.nanoTime();

                    setBlurExecutor();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Fork_Join)" ){
                    start = System.nanoTime();

                    setBlurFork_Join();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
            }
            break;
            case "OUTLINE":
            {
                clearChangedImage();
                if ( comboBox.getSelectedItem() == "последовательный способ" ){
                    start = System.nanoTime();

                    setOutLineConsistent();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Thread)" ){
                    start = System.nanoTime();

                    setOutLineThread();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Executor)" ){
                    start = System.nanoTime();

                    setOutLineExecutor();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
                if ( comboBox.getSelectedItem() == "параллельный (Fork_Join)" ){
                    start = System.nanoTime();

                    setOutLineFork_Join();

                    end = System.nanoTime();
                    String str = ("Time: " + (end - start)/1e9);
                    timeLable.setText(str);
                    buttomPanel.add(timeLable);
                }
            }
            break;
        }
        revalidate();
    }

    private void setGreyConsistent(){

        changeImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                originalImage.getType());

        Graphics g = changeImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);

        int height = changeImage.getHeight();
        int width = changeImage.getWidth();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = changeImage.getRGB(j, i);

                int alpha = (pixel & 0xFF000000) >>> 24;

                int red = (pixel & 0x00FF0000) >>> 16;
                int green = (pixel & 0x0000FF00) >>> 8;
                int blue = (pixel & 0x000000FF);

                int mean = (red + green + blue) / 3;
                int newPixel = (alpha << 24) + (mean << 16) + (mean << 8) + mean;

                changeImage.setRGB(j, i, newPixel);
            }
        }
        changedImageIcon = new ImageIcon(changeImage.getScaledInstance(scaleWidth, scaleHeight, originalImage.SCALE_SMOOTH));
        changedImagePanel.add(new JLabel(changedImageIcon));
    }
    private void setGreyThread(){

        changedImagePanel.add(new JLabel("setGreyThread ждёт лучших времён )))"));
}
    private void setGreyExecutor(){
        changedImagePanel.add(new JLabel("setGreyExecutor ждёт лучших времён )))")); }
    private void setGreyFork_Join(){
        changedImagePanel.add(new JLabel("setGreyFork_Join ждёт лучших времён )))")); }

    private void setNegativeConsistent(){

        changeImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                originalImage.getType());

        Graphics g = changeImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);

        int height = changeImage.getHeight();
        int width = changeImage.getWidth();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = changeImage.getRGB(j, i);

                int red = 0xFF - (pixel >> 16) & 0xFF;
                int green = 0xFF - (pixel >> 8) & 0xFF;
                int blue = 0xFF - pixel & 0xFF;

                int newPixel = (0xFF000000 | red << 16 | green << 8 | blue);

                changeImage.setRGB(j, i, newPixel);
            }
        }
        changedImageIcon = new ImageIcon(changeImage.
                getScaledInstance(scaleWidth, scaleHeight, originalImage.SCALE_SMOOTH));
        changedImagePanel.add(new JLabel(changedImageIcon));
    }
    private void setNegativeThread(){

        changedImagePanel.add(new JLabel("setNegativeThread ждёт лучших времён )))"));
    }
    private void setNegativeExecutor(){
        changedImagePanel.add(new JLabel("setNegativeExecutor ждёт лучших времён )))"));
    }
    private void setNegativeFork_Join(){
        changedImagePanel.add(new JLabel("setNegativeFork_Join ждёт лучших времён )))"));
    }

    private void setBlurConsistent(){

        changeImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                originalImage.getType());

        Graphics g = changeImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);

        float[] matrix = new float[400];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = 1.0f/400.0f;
        }

        BufferedImageOp op = new ConvolveOp( new Kernel(20, 20, matrix),
                ConvolveOp.EDGE_NO_OP, null );
        BufferedImage blurredImage = op.filter(originalImage, changeImage);

        changedImageIcon = new ImageIcon(blurredImage.getScaledInstance(scaleWidth,scaleHeight, originalImage.SCALE_SMOOTH));
        changedImagePanel.add(new JLabel(changedImageIcon));
    }
    private void setBlurThread(){
        changedImagePanel.add(new JLabel("setBlurThread ждёт лучших времён )))"));  }
    private void setBlurExecutor(){
        changedImagePanel.add(new JLabel("setBlurExecutor ждёт лучших времён )))"));  }
    private void setBlurFork_Join(){
        changedImagePanel.add(new JLabel("setBlurFork_Join ждёт лучших времён )))"));  }

    private void setOutLineConsistent(){

        changeImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(),
                originalImage.getType());

        Graphics g = changeImage.getGraphics();
        g.drawImage(originalImage, 0, 0, null);

            int[][] sx = {{-1,0,1},{-1,0,1},{-1,0,1}};
            int[][] sy = {{-1,-1,-1},{0,0,0},{1,1,1}};

            // a sobel template 2D array for calculation
            int[][] sob;

        int width = changeImage.getWidth();
        int height = changeImage.getHeight();

               // at first need to greyscale and populate sob[][] array
            sob = new int[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = changeImage.getRGB(x, y);

                    int alpha = (pixel & 0xFF000000) >>> 24;

                    int red = (pixel & 0x00FF0000) >>> 16;
                    int green = (pixel & 0x0000FF00) >>> 8;
                    int blue = (pixel & 0x000000FF);

                    int mean = (red + green + blue) / 3;
                    sob[x][y] = mean;
                    int newPixel = (alpha << 24) + (mean << 16) + (mean << 8) + mean;

                    changeImage.setRGB(x, y, newPixel);
                }
            }

            // sobel calculation
            for (int y = 1; y < height-1; y++) {
                for (int x = 1; x < width-1; x++) {
                    int px = (sx[0][0] * sob[x-1][y-1]) + (sx[0][1] * sob[x][y-1]) +
                            (sx[0][2] * sob[x+1][y-1]) + (sx[1][0] * sob[x-1][y]) +
                            (sx[1][1] * sob[x][y]) + (sx[1][2] * sob[x+1][y]) +
                            (sx[2][0] * sob[x-1][y+1]) + (sx[2][1] * sob[x][y+1]) +
                            (sx[2][2] * sob[x+1][y+1]);

                    int py = (sy[0][0] * sob[x-1][y-1]) + (sy[0][1] * sob[x][y-1]) +
                            (sy[0][2] * sob[x+1][y-1]) + (sy[1][0] * sob[x-1][y]) +
                            (sy[1][1] * sob[x][y]) + (sy[1][2] * sob[x+1][y]) +
                            (sy[2][0] * sob[x-1][y+1]) + (sy[2][1] * sob[x][y+1]) +
                            (sy[2][2] * sob[x+1][y+1]);

                    int pixel = (int) Math.sqrt((px * px) + (py * py));

                    if (pixel>255) {
                        pixel = 255;
                    } else if (pixel<0) {
                        pixel = 0;
                    }

                    Color pix = new Color(pixel,pixel,pixel);
                    changeImage.setRGB(x, y, pix.getRGB());
                }
            }
        changedImageIcon = new ImageIcon(changeImage.getScaledInstance(scaleWidth, scaleHeight, originalImage.SCALE_SMOOTH));
        changedImagePanel.add(new JLabel(changedImageIcon));
    }
    private void setOutLineThread(){

        changedImagePanel.add(new JLabel("setOutLineThread ждёт лучших времён )))"));
    }
    private void setOutLineExecutor(){
        changedImagePanel.add(new JLabel("setOutLineExecutor ждёт лучших времён )))"));
    }
    private void setOutLineFork_Join(){
        changedImagePanel.add(new JLabel("setOutLineFork_Join ждёт лучших времён )))"));
    }

    private void clearChangedImage(){
        changedImagePanel.removeAll();
        changedImagePanel.repaint();
    }

    private void clearImagesPanel() {
        originalImagePanel.removeAll();
        originalImagePanel.repaint();
        changedImagePanel.removeAll();
        changedImagePanel.repaint();
    }

    private void getSizeScale() {

        scaleWidth = originalImagePanel.getWidth();
        scaleHeight = originalImagePanel.getHeight();

        if (originalImage.getWidth() >= originalImage.getHeight()) {
            scaleHeight = (scaleWidth * originalImage.getHeight()) / originalImage.getWidth();
        } else {
            scaleWidth = (scaleHeight * originalImage.getWidth()) / originalImage.getHeight();
        }
    }

    private void openFile() throws IOException {

        clearImagesPanel();

        JFileChooser fileopen = new JFileChooser();

        int ret = fileopen.showDialog(null, "Открыть файл");

        File file = fileopen.getSelectedFile();
        originalImage = ImageIO.read(file);

        if (ret == JFileChooser.APPROVE_OPTION) {
//-----------------------------------------------------------------------------------

            getSizeScale();

            originalImageIcon = new ImageIcon(originalImage.getScaledInstance(
                    scaleWidth, scaleHeight, originalImage.SCALE_SMOOTH));
            originalImagePanel.add(new JLabel(originalImageIcon));//, SwingConstants.CENTER
//-----------------------------------------------------------------------------------

        }
    }

    private void saveFile() throws IOException {
        if (originalImageIcon!= null && originalImageIcon.equals(changedImageIcon)) {
           JFileChooser chooser = new JFileChooser();
           int ret = chooser.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                BufferedImage image = (BufferedImage) changedImageIcon.getImage();
                File output = new File("newJPG");
                        ImageIO.write(image, "jpg",output);
                chooser.setSelectedFile(output);
            }
        }
        clearImagesPanel();
    }

    private void closeFile() {
        clearImagesPanel();
    }

    private void exitFile() {
        //clearImagesPanel();
        dispose();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (originalImage != null) {
            getSizeScale();
            originalImageIcon.setImage(originalImage.getScaledInstance(scaleWidth, scaleHeight, originalImage.SCALE_SMOOTH));
        }
        if (changeImage != null) {
            getSizeScale();
            changedImageIcon.setImage(changeImage.getScaledInstance(scaleWidth, scaleHeight, changeImage.SCALE_SMOOTH));
        }
        //revalidate();
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {


    }
}
