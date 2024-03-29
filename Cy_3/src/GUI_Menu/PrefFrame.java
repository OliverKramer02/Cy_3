
package GUI_Menu;

import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;

import de.mpg.mpi_inf.bioinf.prioritizer.Preferences;
import de.mpg.mpi_inf.bioinf.prioritizer.graph.GDType;
import de.mpg.mpi_inf.bioinf.prioritizer.ranking.RDType;
import de.mpg.mpi_inf.bioinf.prioritizer.ranking.aggregation.AAType;

public class PrefFrame extends javax.swing.JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Creates new form Prefframe
     */
    public PrefFrame() {
        initComponents();
       // createRankingPane();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
      	
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField6 = new JFormattedTextField();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        
        
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Preferences");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(" Graph-Relation"));

        jLabel8.setText("Edge-weight attribute:");

        jLabel9.setText("Graph distance:");

        jLabel10.setText("Edge-weight contribution:");

        jLabel11.setText("(only for turned distance)");

        jLabel12.setText("Compute centralities:");

        jTextField6.setText("jTextField6");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jRadioButton4.setText("for the whole graph");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jRadioButton5.setText("per connected component");
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel11)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jLabel9)
                            .add(jLabel10)
                            .add(jLabel12))
                        .add(62, 62, 62)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jRadioButton4)
                            .add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jComboBox2, 0, 150, Short.MAX_VALUE)
                                .add(jComboBox3, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(jRadioButton5))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9)
                    .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel10)
                    .add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(jRadioButton4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButton5)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ranking-Relation"));

        jLabel1.setText("New rankings are:");

        jLabel2.setText("Ranking distance:");

        jLabel3.setText("Rank aggregation algorithm:");

        jRadioButton1.setText("injective");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("non-onjective");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel());
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
        	
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });       
        
        
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " "}));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
        	
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	 RDType[] rldtypes = RDType.values();
                 String[] rlddescs = new String[rldtypes.length];
                 for (int i = 0; i < rldtypes.length; i++) {
                 	jComboBox3.addItem(rldtypes[i].description());
                 } 
                jComboBox1ActionPerformed(evt);
            }
        });
        

       
        
        
        AAType[] rlfatypes = AAType.values();
    	String[] rlfadescs = new String[rlfatypes.length];
    	for (int i = 0; i < rlfatypes.length; i++) {
    	       jComboBox4.addItem( rlfatypes[i].description());
    	}

    	

       

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jRadioButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jRadioButton2))
                    .add(jComboBox1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jComboBox4, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jRadioButton1)
                    .add(jRadioButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Restore Defaults");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Close");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButton1)
                        .add(63, 63, 63)
                        .add(jButton2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(jButton2)
                    .add(jButton3))
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("Graph-Relation");

        pack();
    }// </editor-fold>  
    
    private void createRankingPane()
    {
    	RDType[] rldtypes = RDType.values();
    	String[] rlddescs = new String[rldtypes.length];
    	for (int i = 0; i < rldtypes.length; i++) {
    		rlddescs[i] = rldtypes[i].description();
    	}
    	this.jComboBox3 = new JComboBox(rlddescs);
    	this.jComboBox3.setSelectedItem(Preferences.getRDist().description());
    	AAType[] rlfatypes = AAType.values();
    	String[] rlfadescs = new String[rlfatypes.length];
    	for (int i = 0; i < rlfatypes.length; i++) {
    		rlfadescs[i] = rlfatypes[i].description();
    	}
    	
    	this.jComboBox4 = new JComboBox(rlfadescs);
    	this.jComboBox4.setSelectedItem(Preferences.getAAlg().description());
    	this.jRadioButton4 = new JRadioButton("injective");
    	this.jRadioButton4.setActionCommand("inj");
    	this.jRadioButton4.setSelected(Preferences.areInjective());
    	this.jRadioButton5 = new JRadioButton("non-injective");
    	this.jRadioButton5.setActionCommand("ninj");
    	this.jRadioButton5.setSelected(!Preferences.areInjective());
    	this.injg = new ButtonGroup();
    	this.injg.add(this.jRadioButton4);
    	this.injg.add(this.jRadioButton5);
    	
    }
    

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	Preferences.setWeightAtt((String)PrefFrame.this.jComboBox1.getSelectedItem());
    	Preferences.setAlpha(((Double)PrefFrame.this.getjTextField6().getValue()).doubleValue());
    	Preferences.setGDist(GDType.getType(
    			(String)PrefFrame.this.jComboBox2.getSelectedItem()));
    	Preferences.setRLDist(RDType.getType(
         (String)PrefFrame.this.jComboBox3.getSelectedItem()));
    	Preferences.setRAAlg(AAType.getType(
    	        (String)PrefFrame.this.jComboBox4.getSelectedItem()));
    	Preferences.setInjective(
    	         PrefFrame.this.injg.getSelection().getActionCommand().equals("inj"));
    	Preferences.setCompCentsForWholeGraph(
    	         PrefFrame.this.ccomp.getSelection().getActionCommand().equals(
    	         "wholeg"));
    }                                        

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {                                           
    	
    }                                          

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	PrefFrame.this.jComboBox1.setSelectedItem(Preferences.getDefaultWeightAtt());
    	PrefFrame.this.getjTextField6().setValue(Double.valueOf(Preferences.getDefaultAlpha()));
    	PrefFrame.this.jComboBox2.setSelectedItem(Preferences.getDefaultGDist().description());
    	PrefFrame.this.jComboBox3.setSelectedItem(Preferences.getDefaultRLDist().description());
    	PrefFrame.this.jComboBox4.setSelectedItem(Preferences.getDefaultRAAlg().description());
    	PrefFrame.this.jRadioButton4.setSelected(Preferences.getDefaultInjective());
    	PrefFrame.this.jRadioButton5.setSelected(!Preferences.getDefaultInjective());
    	PrefFrame.this.jRadioButton1.setSelected(Preferences.getDefaultCompCentsForWholeGraph());
    	PrefFrame.this.jRadioButton2.setSelected(!Preferences.getDefaultCompCentsForWholeGraph());
    	
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    	setVisible(false);
    }                                        
    
    /**
     * @param args the command line arguments
     */
    private String[] getAAlg(){
    	AAType[] rlfatypes = AAType.values();
    	String[] rlfadescs = new String[rlfatypes.length];
    	for (int i = 0; i < rlfatypes.length; i++) {
    	       this.Box4[i] = rlfatypes[i].description();
    	}
		return this.Box4;
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
  
    	
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrefFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrefFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrefFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrefFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrefFrame().setVisible(true);
            }
        });
    }
    
    private void createGraphPane()
    {
    	 this.ccomp = new ButtonGroup();
    	 this.ccomp.add(this.jRadioButton1);
    	 this.ccomp.add(this.jRadioButton2);
    	 this.setjTextField6(new JFormattedTextField(
    			 new AlphaTextfieldAbstractFormatterFactory(), 
    			 Double.valueOf(Preferences.getAlpha())));
    }
    
    public JFormattedTextField getjTextField6() {
    	return jTextField6;
    }
    public void setjTextField6(JFormattedTextField alpha) {
    	this.jTextField6 = alpha;
    }
    
    
    
    private class AlphaTextfieldAbstractFormatterFactory
      extends JFormattedTextField.AbstractFormatterFactory
    {
      private AlphaTextfieldAbstractFormatterFactory() {}
      
      public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf)
      {
    return new JFormattedTextField.AbstractFormatter()
        {
          public String valueToString(Object value)
            throws ParseException
          {
            if (!value.getClass().equals(Double.class)) {
              throw new ParseException(
                "AlphaParameterField can only handle values of type double!", 
                0);
            }
            Double d = (Double)value;
            if (d.doubleValue() > 1.0D) {
              d = new Double(1.0D);
            } else if (d.doubleValue() < 0.0D) {
              d = new Double(0.0D);
            }
            return d.toString();
          }
          
          public Object stringToValue(String text)
            throws ParseException
          {
            try
            {
              Double d = new Double(text);
              if (d.doubleValue() > 1.0D) {
                d = new Double(1.0D);
              } else if (d.doubleValue() >= 0.0D) {}
              return new Double(0.0D);
           }
           catch (Exception e)
           {
             throw new ParseException("The String in AlphaParameterField must represent a value of type double!", 
             
               0);
           }
         }
       };     }
}
    
    
    // Variables declaration - do not modify                     
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JFormattedTextField jTextField6;
    private ButtonGroup injg;
    private ButtonGroup ccomp;
    private String[] Box4;
    // End of variables declaration                   
    
}
