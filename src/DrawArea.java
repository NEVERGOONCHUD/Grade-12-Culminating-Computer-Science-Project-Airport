
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ethan
 */
public class DrawArea extends javax.swing.JPanel {

    Image airport;
    Image airplane;

    /**
     * Creates new form DrawArea
     */
    public DrawArea() {
        initComponents();
        // init the airplane and airport files into images
        airplane = Toolkit.getDefaultToolkit().getImage("src\\airplane.png");
        airport = Toolkit.getDefaultToolkit().getImage("src\\airport.jpg");
        // start the draw timer
        timer.start();
    }
    // what to draw
    static int draw = 0;
    // position of plane (they start at this so the plane won't appear on screen)
    static float planeX = 450;
    static float planeY = 20;
    static long millis = 0;
    static int time = 25;
    // timer
    public Timer timer = new Timer(time, new TimerListener());

    /**
     * the formula here is a bit complex, basically there is a ratio between the
     * airplane simulation timer iteration count and this one. The ratio between
     * is calculated by getting the other over this one. This gets us how many
     * times this can iterate in one iteration of the other. With that, I
     * multiply it by how many iterations of the other timer I want (in this
     * case 0.75) and all of that under how many pixels I want the plane to
     * move. Its not gonna be pitch perfect, probably thanks to an off by one
     * error (if any) but it should be very precise. This formula is copied a
     * couple of times
     *
     * @param px The distance, in pixels, in order to
     * @return The # of increments per iteration needed to achieve the desired
     * distance
     */
    static float getPixelPerIteration(int px, float otherIt) {
        return (float) ((float) px / ((float) (AirplaneSimulation.tDuration / time) * otherIt));
    }

    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // checks what should be drawing
            switch (draw) {
                // wait
                case 0:
                    millis = System.currentTimeMillis();
                    break;

                // landing
                case 1:
                    // 1st stage of landing animation when plane flies from right to left by 125 px from (275,50)
                    if (millis + AirplaneSimulation.tDuration * 0.75 >= System.currentTimeMillis()) {
                        planeX -= getPixelPerIteration(75, (float) 0.75);
                        // 2nd stage of landing animation where plane descends down onto tarmac by 125 px on the x & 150 on the y plane 
                    } else if (millis + AirplaneSimulation.tDuration * 2.75 >= System.currentTimeMillis()) {
                        planeX -= getPixelPerIteration(150, (float) 2);
                        planeY += getPixelPerIteration(150, (float) 2);
                        // 3rd stage of landing animation when plane touches ground
                    } else if (millis + AirplaneSimulation.tDuration * 4.75 >= System.currentTimeMillis()) {
                        planeX -= getPixelPerIteration(180, 2);
                        //the animation finished, stop it
                    } else {
                        draw = 0;
                        millis = System.currentTimeMillis();
                    }
                    break;

                // taking off
                case 2:
                    // the code here is similar in logic to the previous one (case 1), just vertically flipped

                    // 1st stage, get onto tarmac
                    if (millis + AirplaneSimulation.tDuration * 1.75 >= System.currentTimeMillis()) {
                        planeX -= getPixelPerIteration(125, (float) 1.75);
                        // plane takes off
                    } else if (millis + AirplaneSimulation.tDuration * 2.5 >= System.currentTimeMillis()) {
                        planeX -= getPixelPerIteration(100, (float) 0.75);
                        planeY -= getPixelPerIteration(150, (float) 0.75);
                        // smooth sailing
                    } else if (millis + AirplaneSimulation.tDuration * 3.25 >= System.currentTimeMillis()) {
                        planeX -= getPixelPerIteration(200, (float) 0.75);
                        //the animation finished, stop it
                    } else {
                        draw = 0;
                        millis = System.currentTimeMillis();
                    }
                    break;

                // wait
                default:
                    draw = 0;
                    break;
            }
            repaint();
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // always be drawing the airplane and airport
        // airport is static (and below the plane in terms of who is drawn last)
        g.drawImage(airport, 0, 0, null);
        // airplane moves (on tom of airport)
        g.drawImage(airplane, (int) planeX, (int) planeY, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
