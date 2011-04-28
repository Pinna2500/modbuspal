/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BindingEditor.java
 *
 * Created on 30 déc. 2008, 19:59:57
 */

package modbuspal.binding;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import modbuspal.automation.*;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import modbuspal.main.ModbusPalPane;
import modbuspal.main.ModbusPalProject;

/**
 *
 * @author nnovic
 */
public class BindingEditor
extends javax.swing.JDialog
{
    class BindingList
    implements ListModel
    {
        private String[] bindings;

        public BindingList(String[] list) {
            bindings = list;
        }

        @Override
        public int getSize() {
            return bindings.length;
        }

        @Override
        public Object getElementAt(int index) {
            return bindings[index];
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            return;
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            return;
        }
    }


    class OrderList
    extends AbstractListModel
    implements ComboBoxModel
    {
        private int maxOrder=1;
        private int selectedOrder=0;
        
        public void setMax(int max) {
            fireIntervalRemoved(this, 0, maxOrder-1);
            maxOrder = max;
            fireIntervalAdded(this, 0, maxOrder-1);
        }

        @Override
        public int getSize() {
            return maxOrder;
        }

        @Override
        public Object getElementAt(int index) {
            if( index==0 ) return "0 (LSW)";
            else if( index==maxOrder-1) return String.valueOf(maxOrder-1) + " (MSW)";
            else return String.valueOf(index);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            String string = (String)anItem;
            Pattern p = Pattern.compile("(\\d+)(.*)");
            Matcher m = p.matcher(string);
           if( m.find() )
           {
                int groups = m.groupCount();
                String group = m.group(1);
                selectedOrder = Integer.parseInt(group);
           }
        }

        @Override
        public Object getSelectedItem() {
            return getElementAt(selectedOrder);
        }

    }

    private final ModbusPalPane modbusPalPane;
    private final ModbusPalProject modbusPalProject;
    private final AutomationListModel automations;
    private final BindingList bindings;
    private final HashMap<String,Binding> bindingCache = new HashMap<String,Binding>();
    private final OrderList orderList = new OrderList();

    /** Creates new form BindingEditor 
     * @param p the ModbusPalPane that is the root panel
     * of the graphical interface
     */
    public BindingEditor(ModbusPalPane p)
    {
        modbusPalPane = p;
        modbusPalProject = modbusPalPane.getProject();
        setModalityType(ModalityType.APPLICATION_MODAL);
        automations = new AutomationListModel( modbusPalProject.getAutomations() );
        bindings = new BindingList( modbusPalProject.getBindingFactory().getList() );
        initComponents();
        Image img = Toolkit.getDefaultToolkit().createImage( getClass().getResource("/modbuspal/main/img/icon32.png") );
        setIconImage(img);
    }

    /**
     * Returns a string containing the name of the binding class that
     * has been selected by the user.
     * @return the selected binding class name
     */
    public String getSelectedClass()
    {
        return (String)bindingsList.getSelectedValue();
    }

    /**
     * Returns the automation instance that has been selected for
     * association with the selected binding.
     * @return the automation to associate with the binding
     */
    public Automation getSelectedAutomation()
    {
        int index = automationsList.getSelectedIndex();
        if( index < 0 )
        {
            return null;
        }
        
        Automation selected = automations.getAutomation(index);
        return selected;
    }

    /**
     * Returns the order that has been chosen by the user for
     * the binding to create.
     * @return the selected order for the binding
     */
    public int getSelectedOrder()
    {
        return orderComboBox.getSelectedIndex();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        orderComboBox = new javax.swing.JComboBox();
        scriptedBindingsButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        summaryPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        automationsPanel = new javax.swing.JPanel();
        automationsScrollPane = new javax.swing.JScrollPane();
        automationsList = new javax.swing.JList();
        bindingsPanel = new javax.swing.JPanel();
        bindingsScrollPane = new javax.swing.JScrollPane();
        bindingsList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Automation binding");

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel2.setText("Order:");
        buttonsPanel.add(jLabel2);

        orderComboBox.setModel(orderList);
        orderComboBox.setMinimumSize(new java.awt.Dimension(50, 18));
        orderComboBox.setPreferredSize(new java.awt.Dimension(80, 20));
        buttonsPanel.add(orderComboBox);

        scriptedBindingsButton.setText("...");
        scriptedBindingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scriptedBindingsButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(scriptedBindingsButton);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(okButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);
        getContentPane().add(summaryPanel, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        automationsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automations"));
        automationsPanel.setLayout(new java.awt.BorderLayout());

        automationsList.setModel(automations);
        automationsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        automationsScrollPane.setViewportView(automationsList);

        automationsPanel.add(automationsScrollPane, java.awt.BorderLayout.CENTER);

        jPanel1.add(automationsPanel);

        bindingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bindings"));
        bindingsPanel.setLayout(new java.awt.BorderLayout());

        bindingsList.setModel(bindings);
        bindingsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                bindingsListValueChanged(evt);
            }
        });
        bindingsScrollPane.setViewportView(bindingsList);

        bindingsPanel.add(bindingsScrollPane, java.awt.BorderLayout.CENTER);

        jPanel1.add(bindingsPanel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
}//GEN-LAST:event_okButtonActionPerformed

    private void scriptedBindingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scriptedBindingsButtonActionPerformed

        // ask script manager to appear, with the "generators" tab selected
        modbusPalPane.showScriptManagerDialog();

}//GEN-LAST:event_scriptedBindingsButtonActionPerformed


    private void bindingsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_bindingsListValueChanged

        // get selected binding
        String bindingname = (String)bindingsList.getSelectedValue();

        Binding b = bindingCache.get(bindingname);
        if( b == null )
        {
            try
            {
                b = modbusPalProject.getBindingFactory().newInstance(bindingname);
            }
            catch(InstantiationException ex)
            {
                Logger.getLogger(BindingEditor.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            catch (IllegalAccessException ex)
            {
                Logger.getLogger(BindingEditor.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }

        orderList.setMax( b.getSize() /16 );

    }//GEN-LAST:event_bindingsListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList automationsList;
    private javax.swing.JPanel automationsPanel;
    private javax.swing.JScrollPane automationsScrollPane;
    private javax.swing.JList bindingsList;
    private javax.swing.JPanel bindingsPanel;
    private javax.swing.JScrollPane bindingsScrollPane;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox orderComboBox;
    private javax.swing.JButton scriptedBindingsButton;
    private javax.swing.JPanel summaryPanel;
    // End of variables declaration//GEN-END:variables

}
