import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 17.10.2016
  * @author 
  */

public class Waldbrand extends JFrame {
  // Anfang Attribute
  private String[][] waldMap;
  private String[][] nextWaldMap;
  private JButton btnNextStep = new JButton();
  private DefaultTableModel waldMapModel = new DefaultTableModel();
    private JTable tblWaldMap = new JTable(waldMapModel);
    private JScrollPane tableScrollPane = new JScrollPane(tblWaldMap);
  private JButton btnLoad = new JButton();
  private JSpinner spnWaldMapX = new JSpinner();
    private SpinnerNumberModel spnWaldMapXModel = new SpinnerNumberModel(5, 1, 10, 1);
  private JSpinner spnWaldMapY = new JSpinner();
    private SpinnerNumberModel spnWaldMapYModel = new SpinnerNumberModel(5, 1, 10, 1);
  private JCheckBox chbxManuell = new JCheckBox();
  private JNumberField nbrBaumWahrscheinlichkeit = new JNumberField();
  // Ende Attribute
  
  public Waldbrand() { 
    // Frame-Initialisierung
    super();
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 342; 
    int frameHeight = 337;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setTitle("Waldbrand Simulator 2016");
    setResizable(false);
    Container cp = getContentPane();
    cp.setLayout(null);
    // Anfang Komponenten
    
    btnNextStep.setBounds(16, 192, 75, 25);
    btnNextStep.setText("N+1");
    btnNextStep.setMargin(new Insets(2, 2, 2, 2));
    btnNextStep.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent evt) { 
        btnNextStep_ActionPerformed(evt);
      }
    });
    btnNextStep.setEnabled(false);
    cp.add(btnNextStep);
    tableScrollPane.setBounds(80, 8, 172, 172);
    tblWaldMap.setTableHeader(null);
    tblWaldMap.setRowSelectionAllowed(false);
    cp.add(tableScrollPane);
    btnLoad.setBounds(184, 256, 131, 25);
    btnLoad.setText("Waldmap laden");
    btnLoad.setMargin(new Insets(2, 2, 2, 2));
    btnLoad.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent evt) { 
        btnLoad_ActionPerformed(evt);
      }
    });
    cp.add(btnLoad);
    spnWaldMapX.setBounds(184, 192, 54, 24);
    spnWaldMapX.setValue(5);
    spnWaldMapX.setModel(spnWaldMapXModel);
    cp.add(spnWaldMapX);

    spnWaldMapY.setBounds(264, 192, 54, 24);
    spnWaldMapY.setValue(5);
    spnWaldMapY.setModel(spnWaldMapYModel);
    cp.add(spnWaldMapY);

    chbxManuell.setBounds(184, 224, 76, 20);
    chbxManuell.setText("Manuell");
    chbxManuell.setOpaque(false);
    chbxManuell.setSelected(false);
    cp.add(chbxManuell);

    nbrBaumWahrscheinlichkeit.setBounds(264, 224, 51, 20);
    nbrBaumWahrscheinlichkeit.setText("");
    nbrBaumWahrscheinlichkeit.setToolTipText("Wahrscheinlichkeit, dass ein Baum gepflanzt wird.");
    nbrBaumWahrscheinlichkeit.setDouble(0.8);
    cp.add(nbrBaumWahrscheinlichkeit);
    // Ende Komponenten
    
    setVisible(true);
  } // end of public Waldbrand
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    new Waldbrand();
  } // end of main
  
  public void btnNextStep_ActionPerformed(ActionEvent evt) {
    // TODO hier Quelltext einfügen
    feuerZuStumpf(); 
     
    wirdFeuerZuFeuer();
        
    nextWaldMap = copyArray(waldMap);
    
    nachbarnAnzuenden(nextWaldMap); 
    
    updateTable();
  } // end of btnNextStep_ActionPerformed

  public void btnLoad_ActionPerformed(ActionEvent evt) {
    // TODO hier Quelltext einfügen
    // jTable und Array laden
    waldMap = new String[(int) spnWaldMapX.getValue()][(int) spnWaldMapY.getValue()]; 
    tableScrollPane.setSize((int) spnWaldMapX.getValue() * 16 + 3, (int) spnWaldMapY.getValue() * 16 + 3);
    waldMapModel.setColumnCount((int) spnWaldMapX.getValue());
    waldMapModel.setRowCount((int) spnWaldMapY.getValue());
    
    if (chbxManuell.isSelected()) {
      // Manuell
      System.out.println("NOCH NICHT FERTIG!");
    } else {
      // Zufall
      
      // Array mit Baeumen erzeugen
      for (int y=0; y <= waldMap.length-1; y++) {
        for (int x=0; x <= waldMap[y].length-1; x++) {
          if (Math.random() <= nbrBaumWahrscheinlichkeit.getDouble()) {
            // Baum setzen
            waldMap[y][x] = "B";
          } else {
            waldMap[y][x] = " ";
          } // end of if-else
        } // end of for
      } // end of for
      
      // Linke Spalte Baeume anzuenden
      for (int y=0; y <= waldMap.length-1; y++) {
        if (waldMap[y][0].equals("B")) {
          waldMap[y][0] = "F";  
        } // end of if
      } // end of for
      
      // jTable aktualisieren
      updateTable();
    } // end of if-else   
    
    // WaldMap kopieren
    nextWaldMap = copyArray(waldMap);
    
    nachbarnAnzuenden(nextWaldMap);
    
    // Aktion uebergeben
    btnNextStep.setEnabled(true);
  } // end of btnLoad_ActionPerformed
  
  private void updateTable() {
    for (int y=0; y <= waldMap.length-1; y++) {
      for (int x=0; x <= waldMap[y].length-1; x++) {
        tblWaldMap.setValueAt(waldMap[y][x], y, x);
      } // end of for
    } // end of for
  }

  private String[][] copyArray(String[][] original) {
    String[][] copy = new String[original.length][original[0].length];
    
    for (int y=0; y <= original.length-1; y++) {
      for (int x=0; x <= original[y].length-1; x++) {
        copy[y][x] = original[y][x];
      } // end of for
    } // end of for
    
    return copy;
  }
  
  private void feuerZuStumpf() {
    for (int y=0; y <= waldMap.length-1; y++) {
      for (int x=0; x <= waldMap[y].length-1; x++) {
        if (waldMap[y][x].equals("F")) {
          waldMap[y][x] = "-";
        } // end of if
      } // end of for
    } // end of for 
  }

  private void wirdFeuerZuFeuer() {
    for (int y=0; y <= nextWaldMap.length-1; y++) {
      for (int x=0; x <= nextWaldMap[y].length-1; x++) {
        if (nextWaldMap[y][x].equals("X")) {
          waldMap[y][x] = "F";
        }
      } // end of for
    } // end of for
  }
  
  private void nachbarnAnzuenden(String[][] nextWaldMap) {
    for (int y=0; y <= waldMap.length-1; y++) {
      for (int x=0; x <= waldMap[y].length-1; x++) {
        if (waldMap[y][x].equals("F")) {
          try {
            if (waldMap[y][x+1].equals("B")) {
              nextWaldMap[y][x+1] = "X";
            } // end of if
          } catch(ArrayIndexOutOfBoundsException e) {
            
          }
          
          try {
            if (waldMap[y][x-1].equals("B")) {
              nextWaldMap[y][x-1] = "X";
            } // end of if
          } catch(ArrayIndexOutOfBoundsException e) {
            
          }
          
          try {
            if (waldMap[y-1][x].equals("B")) {
              nextWaldMap[y-1][x] = "X";
            } // end of if
          } catch(ArrayIndexOutOfBoundsException e) {
            
          }
          
          try {
            if (waldMap[y+1][x].equals("B")) {
              nextWaldMap[y+1][x] = "X";
            } // end of if
          } catch(ArrayIndexOutOfBoundsException e) {
          
          }
        } 
      } // end of for
    } // end of for 
  }



  // Ende Methoden
} // end of class Waldbrand
