
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
public class AirplaneSimulation extends javax.swing.JPanel {

    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateQueue(arrivals, arrivalsTextArea);
            updateQueue(takeoffs, takeoffsTextArea);
            try {
                /**
                 * if is it time to update arrivals then update arrivals code...
                 * else if it is time to update takeoffs then update takeoffs
                 * code... else it is time to wait wait... end if
                 *
                 *
                 * if it is not time, then determine if
                 */

                // the time requred for an arrival is 4 units while takeoffs is 2
                int timePass = tDuration * 2;
                // is it time to update the planeLandings?
                if (timerIntervalCounter >= timePass * 2 && whoArrive == 1) {
                    // increment plane landings for ratio
                    planeLandings++;
                    // removes flight frome queue
                    arrivals.poll();
                    System.out.println("Flight --> " + flight);
                    // display text on screen
                    infoLabel2.setText("Flight " + Integer.toString(flight) + " landed successfully!");
                    // if there are now flights, then do something about it
                    checkNext();
                    // update the queue
                    updateQueue(takeoffs, takeoffsTextArea);
                    // reset the timer
                    timerIntervalCounter = -tDuration;
                    // is it time to update the takeoffs?
                } else if (timerIntervalCounter >= timePass && whoArrive == 2) {
                    // reset plane landings for ratio to restart
                    planeLandings = 0;
                    // remove flight from queue
                    takeoffs.poll();
                    System.out.println("Flight --> " + flight);
                    // display text on screen
                    infoLabel2.setText("Flight " + Integer.toString(flight) + " departed successfully!");
                    // method to check which flight should be next
                    checkNext();
                    // update the queue
                    updateQueue(takeoffs, takeoffsTextArea);
                    // reset the timer
                    timerIntervalCounter = -tDuration;
                    // display arriving flight 
                } else if (whoArrive == 1 && timerIntervalCounter <= -tDuration) {
                    // display landing flight w/ graphics
                    DrawArea.draw = (1);
                    // set the planes x and y coords
                    DrawArea.planeX = 275;
                    DrawArea.planeY = 50;
                    // set the time as well
                    DrawArea.millis = System.currentTimeMillis();
                    // display next land
                    infoLabel2.setText("Flight " + Integer.toString(flight) + " is arriving.");
                    // increment timer
                    timerIntervalCounter += tDuration;
                    // display departing flight 
                } else if (whoArrive == 2 && timerIntervalCounter <= -tDuration) {
                    // display departing flight w/ graphics
                    DrawArea.draw = 2;
                    // set the planes x and y coords
                    DrawArea.planeX = 275;
                    DrawArea.planeY = 180;
                    // set the time as well
                    DrawArea.millis = System.currentTimeMillis();
                    // display next leave
                    infoLabel2.setText("Flight " + Integer.toString(flight) + " is departing.");
                    // increment timer
                    timerIntervalCounter += tDuration;
                    // wait
                } else if (whoArrive == 0) {
                    // Wait for flights
                    infoLabel2.setText("Waiting for flights");
                    // if there are now flights, then do something about it
                    checkNext();
                    // reset the timer
                    timerIntervalCounter = -tDuration;
                    // display plane status
                } else {
                    // display what happens when its not time to check
                    // arrivals need 4 time units, so anything less than 4 gets an update
                    if (whoArrive == 1 && timerIntervalCounter / tDuration < 4) {
                        infoLabel2.setText("Flight " + flight + " is landing in " + Integer.toString(4 - (timerIntervalCounter / tDuration)));
                        // takeoffs need 2 units and confirmation that it is a takeoff
                    } else if (whoArrive == 2 && timerIntervalCounter / tDuration < 2) {
                        infoLabel2.setText("Flight " + flight + " is taking off in " + Integer.toString(2 - (timerIntervalCounter / tDuration)));
                        // if neither can be displayed, its probably because we are displaying the status of the flight (i.e. it departed or waiting for flights) 
                    } else {
                        timerIntervalCounter = 0 - tDuration;
                    }
                    // time increments
                    timerIntervalCounter += tDuration;
                }

                // This exception should only happen if there are no values FOR BOTH
            } catch (Exception exc) {
                // Wait for flights
                infoLabel2.setText("Waiting for flights");
            }
        }

    }

    // the current flight being processed
    int flight;
    // timer duration in milliseconds
    static int tDuration = 600;
    // 1 means arrival 2 means takeoff 0 means wait
    int whoArrive = 1;
    // to count the timer each second
    static int timerIntervalCounter = -tDuration;
    // the current stage of the game
    static int stage = 0;
    // the amount of planelandings
    int planeLandings = 0;
//    // the amount of plane takeoffs
//    int planeTakeoffs = 0;
    // timer
    public Timer t = new Timer(tDuration, new TimerListener());
    // arrivals
    public static Queue<Integer> arrivals = new LinkedList<Integer>();
    // takeoffs
    public static Queue<Integer> takeoffs = new LinkedList<Integer>();

    /**
     * Creates new form AirplaneSimulation
     *
     * @throws java.io.FileNotFoundException
     */
    public AirplaneSimulation() throws FileNotFoundException, IOException {
        initComponents();
    }

    /**
     * Essentially, what this method does is determine what the next flight
     * should be It uses the ratio of planeLandings to planeTakeoffs to see if
     * their ratio is the same as the specified variable
     *
     * whoArrive must be 1 for arrival, 2 for departure, 0 for wait
     */
    void checkNext() {
        System.out.println(planeLandings);
        // next flight is arrivals because the ratio is off (more takeoffs than landings) and there are still flights waiting to arrive
        if (planeLandings < 2 && !arrivals.isEmpty()) {
            flight = arrivals.peek();
            // the next flight can't be an arrival, so see if it can be a takeoff
            whoArrive = 1;
            // the only qualification for this to be a takeoff if there are values in the queue (and the program only gets here if the plane landed too much OR of the arrivals queue is empty)
        } else if (!takeoffs.isEmpty()) {
            flight = takeoffs.peek();
            whoArrive = 2;
            // check again for arrival flights, as the ratio may be off but maybe they are still there
        } else if (!arrivals.isEmpty()) {
            flight = arrivals.peek();
            // the next flight can't be an arrival, so see if it can be a takeoff
            whoArrive = 1;
            // this should only be called if both queue are empty and nothing can be done but wait
        } else {
            // set variables to their appropriate values 
            planeLandings = 0;
            whoArrive = 0;
        }
    }

    /**
     * Updates the text area by adding whatever is in the queues into a
     * temporary String and displays with its respective area. It also builds a
     * line-feed-delimited string (\n) of the current state of the flight
     * numbers in the queue that is used to update the respective textArea to
     * show the current flights waiting to arrive or take off.
     */
    private Queue<Integer> updateQueue(Queue<Integer> queue, javax.swing.JTextArea area) {
        // Create a temporary queue to hold data
        Queue<Integer> temp = new LinkedList<>(queue);
        // Delimed text for text area.
        String str = "";
        // while not empty, add all values into a new line delimed string
        while (!temp.isEmpty()) {
            str += Integer.toString(temp.poll()) + "\n";
        }
        area.setText(str);
        return queue;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel1 = new javax.swing.JLabel();
        infoLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        takeoffsTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        arrivalsTextArea = new javax.swing.JTextArea();
        arrivalLabel = new javax.swing.JLabel();
        takeoffsLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        arrivingLabel = new javax.swing.JLabel();
        arriveTextField = new javax.swing.JTextField();
        takeoffLabel = new javax.swing.JLabel();
        takeoffsTextField = new javax.swing.JTextField();
        drawArea1 = new DrawArea();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        setMaximumSize(new java.awt.Dimension(259, 453));
        setMinimumSize(new java.awt.Dimension(259, 453));

        infoLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        infoLabel1.setText("Airport Simulator (PAY)");
        infoLabel1.setToolTipText("Welcome to CHVD International Airport!");
        infoLabel1.setFocusable(false);

        infoLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        infoLabel2.setText("Press 'START' to begin simulation");

        takeoffsTextArea.setEditable(false);
        takeoffsTextArea.setColumns(20);
        takeoffsTextArea.setRows(5);
        takeoffsTextArea.setFocusable(false);
        jScrollPane1.setViewportView(takeoffsTextArea);

        arrivalsTextArea.setEditable(false);
        arrivalsTextArea.setColumns(20);
        arrivalsTextArea.setRows(5);
        arrivalsTextArea.setFocusable(false);
        jScrollPane2.setViewportView(arrivalsTextArea);

        arrivalLabel.setText("Arrivals");

        takeoffsLabel.setText("Takeoffs");

        startButton.setText("START");
        startButton.setFocusable(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        arrivingLabel.setText("Arriving Flight: ");

        arriveTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arriveTextFieldActionPerformed(evt);
            }
        });

        takeoffLabel.setText("Takeoff Flight: ");

        takeoffsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeoffsTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout drawArea1Layout = new javax.swing.GroupLayout(drawArea1);
        drawArea1.setLayout(drawArea1Layout);
        drawArea1Layout.setHorizontalGroup(
            drawArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        drawArea1Layout.setVerticalGroup(
            drawArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(infoLabel1)
                .addContainerGap(170, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(arrivalLabel)
                                .addGap(105, 105, 105)
                                .addComponent(takeoffsLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(startButton)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(arrivingLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(arriveTextField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(takeoffLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(takeoffsTextField))
                            .addComponent(drawArea1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(infoLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel1)
                .addGap(18, 18, 18)
                .addComponent(infoLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(arrivalLabel)
                    .addComponent(takeoffsLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(takeoffsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(arrivingLabel)
                                            .addComponent(arriveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(37, 37, 37)
                                        .addComponent(takeoffLabel))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(startButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(drawArea1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private Queue<Integer> addFileDataToQueue(Queue<Integer> queue, File file) {
        try {
            BufferedReader ta = new BufferedReader(new FileReader(file));
            while (ta.ready()) {
                queue.add(Integer.parseInt(ta.readLine()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AirplaneSimulation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AirplaneSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return queue;
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (stage == 0) {
            stage++;
            // reads from the files
            // Arrivals
            File file = new File("src\\arrivals.txt");
            // Put data into queue
            arrivals = addFileDataToQueue(arrivals, file);
            // Departures
            file = new File("src\\takeoffs.txt");
            // Put data into queue
            takeoffs = addFileDataToQueue(takeoffs, file);
            // Update the text areas
            updateQueue(arrivals, arrivalsTextArea);
            updateQueue(takeoffs, takeoffsTextArea);
            flight = arrivals.peek();
            // Start the simulation
            t.start();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void arriveTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arriveTextFieldActionPerformed
        // This is the user input number
        int userInputInt = 0;
        try {
            // If integer is real and does not get an exception, then continue
            userInputInt = Integer.parseInt(arriveTextField.getText());
            // add to respective queue
            arrivals.add(userInputInt);
            // update the text area
            arrivals = updateQueue(arrivals, this.arrivalsTextArea);
            // if the simulation is running and waiting for values, then the program immediately does something about it
            if (t.isRunning() && whoArrive == 0) {
                checkNext();
            }
        } catch (NumberFormatException e) {
            // If there are any complications, then say there is
            JOptionPane.showMessageDialog(this, "Not an Integer");
        } finally {
            // No matter what happens, the field is set to empty
            arriveTextField.setText("");
        }
    }//GEN-LAST:event_arriveTextFieldActionPerformed

    private void takeoffsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeoffsTextFieldActionPerformed
        // The Integer user input is this variable
        int n = 0;
        try {
            // If integer is real and does not get an exception, then continue
            n = Integer.parseInt(takeoffsTextField.getText());
            // add to respective queue
            takeoffs.add(n);
            // update the text area
            takeoffs = updateQueue(takeoffs, this.takeoffsTextArea);
            // if the simulation is running and waiting for values, then the program immediately does something about it
            if (t.isRunning() && whoArrive == 0) {
                checkNext();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Not an Integer");
        } finally {
            takeoffsTextField.setText("");
        }
    }//GEN-LAST:event_takeoffsTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel arrivalLabel;
    private javax.swing.JTextArea arrivalsTextArea;
    private javax.swing.JTextField arriveTextField;
    private javax.swing.JLabel arrivingLabel;
    private DrawArea drawArea1;
    private javax.swing.JLabel infoLabel1;
    private javax.swing.JLabel infoLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel takeoffLabel;
    private javax.swing.JLabel takeoffsLabel;
    private javax.swing.JTextArea takeoffsTextArea;
    private javax.swing.JTextField takeoffsTextField;
    // End of variables declaration//GEN-END:variables
}
