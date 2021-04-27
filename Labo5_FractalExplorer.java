import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.geom.Rectangle2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;



public class FractalExplorer {
    public static void main(String[] args){
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    private int sizeWindow;
    private JImageDisplay image;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double fractalRectangle;

    public FractalExplorer(int size){
        this.sizeWindow = size;
        image = new JImageDisplay(sizeWindow, sizeWindow);        
        setFractalGenerator(new Mandelbrot());

        fractalRectangle = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(fractalRectangle);
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame();

        JPanel buttons = new JPanel();
        JButton reset = new JButton("Reset");        
        JButton save = new JButton("Save Image");
        buttons.add(reset);
        buttons.add(save);
        reset.addActionListener(new Resetistener());
        save.addActionListener(new SaveListener());

        JPanel dropdown = new JPanel();
        JComboBox comboBox = new JComboBox();
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());
        JLabel label = new JLabel();
        dropdown.add(label);
        dropdown.add(comboBox);

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                FractalExplorer.this.setFractalGenerator((FractalGenerator) comboBox.getSelectedItem());
                fractalGenerator.getInitialRange(fractalRectangle);
                drawFractal();
            }
        });

        frame.getContentPane().add(image, BorderLayout.CENTER);
        frame.getContentPane().add(buttons, BorderLayout.SOUTH);        
        frame.getContentPane().add(dropdown, BorderLayout.NORTH);
        frame.setTitle("Fractal explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack ();
        frame.setVisible (true);
        frame.setResizable (false);

        image.addMouseListener(new MouseDisplayListener());
    }
    
    private void drawFractal (){
        for (int x = 0; x < sizeWindow; ++x) {
            for (int y = 0; y < sizeWindow; ++y) {
                double xCoord = fractalGenerator.getCoord(fractalRectangle.x,
                        fractalRectangle.x + fractalRectangle.width, sizeWindow,
                        x);
                double yCoord = fractalGenerator.getCoord(fractalRectangle.y,
                        fractalRectangle.y + fractalRectangle.height, sizeWindow,
                        y);

                int rgbColor = 0;
                int iterations = fractalGenerator.numIterations(xCoord, yCoord);

                if (iterations != -1) {
                    float hue =  0.7f + (float) iterations / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }

                image.drawPixel(x, y, rgbColor);
            }
        }

        image.repaint();
    }

    private class Resetistener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            fractalGenerator.getInitialRange(fractalRectangle);
            drawFractal();
        }
    }
    
    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);
            
            int status = chooser.showSaveDialog(image);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    ImageIO.write(image.getImage(), "png", file);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(image, e1.getMessage(), "Не удалось сохранить изображение!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class MouseDisplayListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            int x = e.getX();
            int y = e.getY();

            double xCoord = FractalGenerator.getCoord(fractalRectangle.x,
                    fractalRectangle.x + fractalRectangle.width, sizeWindow, x);
            double yCoord = FractalGenerator.getCoord(fractalRectangle.y,
                    fractalRectangle.y + fractalRectangle.height, sizeWindow, y);

            fractalGenerator.recenterAndZoomRange(fractalRectangle, xCoord, yCoord, 0.5);
            drawFractal();
        }

    }

    public final void setFractalGenerator(final FractalGenerator generator) {
        this.fractalGenerator = generator;
    }
    
}
