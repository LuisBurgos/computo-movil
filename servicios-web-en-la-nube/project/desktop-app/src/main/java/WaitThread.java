import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class WaitThread implements Runnable{

    private InputStream in;
    private OutputStream out;
	
    /** Constructor */
    public WaitThread() {}

    @Override
    public void run() {
    	waitForConnection();		
    }

   /**
     * Waiting for connection from devices
     */
    private void waitForConnection() {
        // retrieve the local Bluetooth device object
        LocalDevice local = null;
        StreamConnectionNotifier notifier;
        StreamConnection connection = null;
        // setup the server to listen for connection
        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
            System.out.println(uuid.toString());
            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier) Connector.open(url);
        } catch (BluetoothStateException e) {
            System.out.println("Bluetooth is not turned on.");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        int cmd;
        // waiting for connection
        while (true) {
            try {
                byte[] input = new byte[1024];
                System.out.println("waiting for connection...");
                connection = notifier.acceptAndOpen();
                System.out.println("Connected to: " + connection);
                in = connection.openDataInputStream();
                out = connection.openDataOutputStream();
                while (true) {
                    cmd = in.read(input);
                    if (cmd == -1) {
                        break;
                    }
                    processInput(input);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
    
    private void processInput(byte[] input) {
        
        try {
            String string = new String(input,"UTF-8");
            System.out.println(string);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WaitThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
