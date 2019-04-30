package com.example.avish.reconrobot;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class UdpThread extends Thread{

    DatagramSocket socket;
    InetAddress serverAddress;

    String addr;
    int port;

    ArrayList<String> commandList = new ArrayList<>();

    boolean isRunning = false; //enables the thread to continously run in the background

    public UdpThread(String addr, int port){
        this.addr = addr;
        this.port = port;
    }

    @Override
    public void run(){
        isRunning = true;
        send_packet();
    }

    void send_packet() {

        while (isRunning) {

            if(!commandList.isEmpty()) {

                try {

                    for(int i = 0; i < commandList.size(); i++){

                        this.serverAddress = InetAddress.getByName(this.addr); //this throws Unknown Host Exception

                        byte[] buf = commandList.get(i).getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, this.serverAddress, this.port);

                        this.socket = new DatagramSocket(); //this throws socketException
                        this.socket.setBroadcast(false);
                        Log.i("Message", commandList.get(i));

                        this.socket.send(packet);
                    }

                    commandList.clear();

                } catch (SocketException e) {
                    Log.e("Udp:", "Socket Error:", e);
                    commandList.clear();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    commandList.clear();
                } catch (IOException e) {
                    Log.e("Udp Send:", "IO Error:", e);
                    commandList.clear();
                }
            }
        }
    }

    public void addMessage(String message){
        this.commandList.add(message);
    }

}
