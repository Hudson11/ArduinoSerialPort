/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduinomonitorserialport;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Aluno
 */
public class ArduinoMonitorSerialPort {
    
    SerialPort arduinoPort = null;
    Scanner sc;
    
    Date data = new Date(2018, 10, 21);
    
  
    
    public void ArduinoMonitorSerialPort(){}
    
    //Método de Conexão com o arduino
    public boolean connectArduino(){
        
        boolean success = false;
        SerialPort serialPort = new SerialPort("COM7");
        
        try{
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            arduinoPort = serialPort;
            success = true;
            System.out.println("Connect Arduino !!");
            
        }catch(SerialPortException ex){
            System.out.println("SerialPortException:" + ex.getMessage());
            this.disconnectArduino();
        }
        
        return success;
    }
    
    //Método para Disconectar a porta serial do arduino
    public void disconnectArduino(){

        if(arduinoPort != null){
            try{
                if(arduinoPort.isOpened())
                    arduinoPort.closePort();
                    System.out.println("Disconnect Arduino !!");
            }catch(SerialPortException ex){
                System.out.println("SerialPortException:" + ex);
            }
        }
    }
    
    public void dados() throws SerialPortException, UnsupportedEncodingException, IOException{
        FileWriter arq = new FileWriter("C:/arq_sensor/dados.txt");
        PrintWriter gravaarq;
        System.out.println(data);
        int dado = 0;
        while(true){
            String dados = arduinoPort.readString();
            String d  = new String(arduinoPort.readBytes(258), "UTF-8");
       
            gravaarq = new PrintWriter(arq);
            gravaarq.write(d);
            System.out.println(d);
            dado++;
            if(new Date().after(data))
                arq.close();          
        }
        
       
    }
    
    public int start(){
        sc = new Scanner(System.in);
        int opcao = 5;
        
        while(opcao != 1 && opcao != 2){
            System.out.println("1. Ligar medição do sensor");
            System.out.println("2. Desligar medição do sensor");
            opcao = sc.nextInt();
        }
        
        //this.disconnectArduino();
        return opcao;
    }
    
    public void updateSensor(int op){
        
        try{
            if(op == 1){
                if(arduinoPort != null){
                    arduinoPort.writeByte((byte)1);
                    System.out.println("Sensor On");
                }else{
                    System.out.println("ArduinoPort not connected !");
                }
            }else if(op == 2){
                if(arduinoPort != null){
                    arduinoPort.writeByte((byte)0);
                    System.out.println("Sesor Off");
                }else{
                    System.out.println("ArduinoPort not connected !");
                }
            }else{
                System.out.println("Desconectando....");
                this.disconnectArduino();
            }
        }catch(SerialPortException ex){
            
        }
    }
}
    
