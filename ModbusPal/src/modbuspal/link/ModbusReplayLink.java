/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modbuspal.link;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import modbuspal.main.ModbusPalProject;
import modbuspal.main.ModbusRequest;
import modbuspal.recorder.ModbusPalRecord;

/**
 *
 * @author nnovic
 */
public class ModbusReplayLink
extends ModbusSlaveProcessor
implements ModbusLink, Runnable
{
    private File recordFile = null;
    private boolean executeThread=false;
    private Thread serverThread;
    private ModbusLinkListener listener = null;

    public ModbusReplayLink(ModbusPalProject mpp, File source)
    {
        super(mpp);
        recordFile = source;
    }

    @Override
    public void start(ModbusLinkListener l)
    throws IOException
    {
        executeThread = true;
        serverThread = new Thread(this,"replay");
        listener = l;
        serverThread.start();
    }


    public void stop()
    {
        executeThread = false;
        serverThread.interrupt();
        
       /* try
        {
            input.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ModbusReplayLink.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        try
        {
            serverThread.join();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ModbusTcpIpLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            serverThread = null;
        }
    }

    public void run()
    {
        System.out.println("Start ModbusReplayLink");

        BufferedReader input = null;
        try
        {
            FileReader reader = new FileReader(recordFile);
            input = new BufferedReader(reader);
        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(ModbusReplayLink.class.getName()).log(Level.SEVERE, null, ex);
        }

        long prev_timestamp = 0;

        while(executeThread == true)
        {
            try
            {
                String line = input.readLine();

                // if some data is available then parse it:
                if( line==null )
                {
                    executeThread = false;
                }
                else
                {
                    ModbusPalRecord record = new ModbusPalRecord(line);

                    if( record.getType()==ModbusPalRecord.RECORD_IN )
                    {
                        long timestamp = record.getTimestamp();
                        System.out.println("sleep "+(timestamp-prev_timestamp)+" ms");
                        if( prev_timestamp != 0 )
                        {
                            Thread.sleep( timestamp - prev_timestamp );
                        }
                        prev_timestamp = timestamp;

                        int slaveId = record.getSlaveID();
                        byte[] data = record.getData();
                        int pduLength = processPDU(slaveId, data, 0, record.getDataLength() );
                    }
                }
            }
            catch (Exception ex)
            {
                if( Thread.interrupted() == false )
                {
                    Logger.getLogger(ModbusReplayLink.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try
        {
            // close the file reader
            input.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ModbusReplayLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Stop ModbusReplayLink");
        listener.linkBroken();
        listener = null;
    }



    public void execute(ModbusRequest req)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
