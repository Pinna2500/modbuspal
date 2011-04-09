/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ModbusRegistersPanel.java
 *
 * Created on 11 janv. 2009, 15:26:06
 */

package modbuspal.slave;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import modbuspal.main.ModbusConst;
import modbuspal.toolkit.GUITools;

/**
 *
 * @author nnovic
 */
class ModbusFunctionsPanel
extends javax.swing.JPanel
implements ModbusConst
{
    private final ModbusSlaveDialog slaveDialog;
    private final ModbusSlave modbusSlave;
    private final FunctionTable functionTableModel;
    private final FunctionFactory ffactory;

    class FunctionTable
    extends AbstractTableModel
    {
        private final ModbusSlave slave;
        private static final int COL_ENABLED = 0;
        private static final int COL_FCODE = 1;

        FunctionTable(ModbusSlave ms)
        {
            slave = ms;
        }

        @Override
        public int getRowCount()
        {
            return USER_DEFINED_FUNCTION_CODES.length;
        }

        @Override
        public int getColumnCount()
        {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex)
        {
            switch(columnIndex)
            {
                case 0: return "Enabled";
                case 1: return "Function";
                case 2: return "Instance";
                default: return super.getColumnName(columnIndex);
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex)
        {
            switch(columnIndex)
            {
                case 0: return Boolean.class;
                case 1: return Byte.class;
                case 2: return String.class;
                default: return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            switch(columnIndex)
            {
                case 0: return false;
                case 1: return false;
                case 2: return false;
                default: return false;
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            switch(columnIndex)
            {
                case 0: return Boolean.TRUE;
                case 1: return USER_DEFINED_FUNCTION_CODES[rowIndex];
                case 2:
                {
                    byte fc = USER_DEFINED_FUNCTION_CODES[rowIndex];
                    ModbusSlavePduProcessor mspp = slave.getPduProcessor(fc);
                    if(mspp!=null)
                    {
                        return FunctionFactory.makeInstanceName(mspp); // mspp.getClassName();
                    }
                }
                default: return null;
            }
        }


        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /** Creates new form ModbusRegistersPanel */
    public ModbusFunctionsPanel(ModbusSlaveDialog parent, FunctionFactory ff)
    {
        slaveDialog = parent;
        modbusSlave = parent.getModbusSlave();
        functionTableModel = new FunctionTable(modbusSlave);
        ffactory = ff;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        setFunctionsButton = new javax.swing.JButton();
        resetFunctoinsButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        functionsTable = new JTable(functionTableModel);

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        setFunctionsButton.setText("Set");
        setFunctionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setFunctionsButtonActionPerformed(evt);
            }
        });
        jPanel2.add(setFunctionsButton);

        resetFunctoinsButton.setText("Reset");
        resetFunctoinsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFunctoinsButtonActionPerformed(evt);
            }
        });
        jPanel2.add(resetFunctoinsButton);

        add(jPanel2, java.awt.BorderLayout.NORTH);

        functionsTable.setAutoCreateRowSorter(true);
        functionsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(functionsTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void setFunctionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setFunctionsButtonActionPerformed

        // check the validty of current selection:
        int rowCount = functionsTable.getSelectedRowCount();
        
        if( rowCount <= 0 )
        {
            return;
        }

        else
        {
            // display the bind dialog
            AddFunctionsDialog dialog = new AddFunctionsDialog(GUITools.findFrame(this),ffactory,modbusSlave);
            slaveDialog.setStatus("Setting...");
            dialog.setVisible(true);

            // retrieve the selected function
            ModbusSlavePduProcessor source = dialog.getInstance();
            if( source == null )
            {
                slaveDialog.setStatus("Function selected cancelled by user.");
                return;
            }
/*
            // get the selected binding order
            int selectedOrder = dialog.getSelectedOrder();
            
            // get the selected binding class
            String selectedClass = dialog.getSelectedClass();
*/
            // get the selected rows
            //int selectedAddresses[] = ((ModbusRegistersTable)functionsTable).getSelectedAddresses();
            int selectedRows[] = functionsTable.getSelectedRows();

            // bind all selected registers
            for(int i=0;i<selectedRows.length;i++)
            {
                try
                {
                    // instanciate the binding:
                    //Binding binding = slaveDialog.modbusPalProject.getBindingFactory().newBinding(selectedClass);
                    //Binding binding = BindingFactory.newBinding(selectedClass);
                    //binding.setup(source, selectedOrder);
                    // do the binding:
                    //registers.bind(selectedAddresses[i],binding);
                    byte functionCode = (Byte)functionsTable.getValueAt(selectedRows[i], FunctionTable.COL_FCODE);
                    modbusSlave.setPduProcessor(functionCode, source);
                } 
                catch(Exception ex)
                {
                    Logger.getLogger(ModbusSlaveDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // refresh display
            functionsTable.validate(); functionsTable.repaint();
            slaveDialog.setStatus("Function completed.");
        }
}//GEN-LAST:event_setFunctionsButtonActionPerformed

    private void resetFunctoinsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFunctoinsButtonActionPerformed
/*
        // get the selected rows
        int addresses[] = ((ModbusRegistersTable)functionsTable).getSelectedAddresses();

        if( addresses.length<=0 )
        {
            return;
        }

        // delete bindings
        for( int i=0; i<addresses.length; i++ )
        {
            registers.unbind(addresses[i]);
        }

        slaveDialog.setStatus("Registers unbound.");
        */

    }//GEN-LAST:event_resetFunctoinsButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable functionsTable;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton resetFunctoinsButton;
    private javax.swing.JButton setFunctionsButton;
    // End of variables declaration//GEN-END:variables

}
