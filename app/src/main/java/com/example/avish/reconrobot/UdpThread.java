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
    InetAddress serverAddress;

    String addr;
    int port;
    String message;

    boolean isRunning = false; //enables the thread to continously run in the background

    public UdpThread(String addr, int port, String message){
        this.addr = addr;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run(){
        isRunning = true;
        send_packet();
    }

    void send_packet() {

        while (isRunning) {

            if(this.message != null && !this.message.isEmpty()) {

                try {

                    this.serverAddress = InetAddress.getByName(this.addr); //this throws Unknown Host Exception

                    byte[] buf = this.message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, this.serverAddress, this.port);

                    this.socket = new DatagramSocket(); //this throws socketException
                    this.socket.setBroadcast(false);
                    Log.i("Message", this.message);

                    this.socket.send(packet);
                    this.message = "";

                } catch (SocketException e) {
                    Log.e("Udp:", "Socket Error:", e);
                    this.message = "";
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    this.message = "";
                } catch (IOException e) {
                    Log.e("Udp Send:", "IO Error:", e);
                    this.message = "";
                }
            }
        }
    }

    public void update_message(String message){
        this.message = message;
    }

}
