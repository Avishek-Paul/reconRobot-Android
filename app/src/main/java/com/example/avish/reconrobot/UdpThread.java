package com.example.avish.reconrobot;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpThread extends Thread{

    DatagramSocket socket;
    String addr;
    int port;
    String message;

    public UdpThread(String addr, int port, String message){
        this.addr = addr;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run(){
        send_packet();
    }

    void send_packet(){
        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(this.addr); //InetAddress.getByName(dstAddress);

            // send request
            byte[] buf = this.message.getBytes();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, this.port);
            socket.send(packet);
            Log.i("Message", this.message);

        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }

}
