package chat_tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private InetAddress host;
    private int port;
    private String name;

    public Client(String name, InetAddress host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public void execute() throws IOException {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Nhập tên : ");
//        String name = sc.nextLine();

        Socket client = new Socket(host,port);
        //System.out.println();
        ReadClient read = new ReadClient(client);
        read.start();
        WriteClient write = new WriteClient(client, name);
        write.start();

    }

    public static void main(String[] args) throws IOException {
//        InetAddress hosts = InetAddress.getLocalHost();
//        String host = hosts.getHostAddress();
        InetAddress host = InetAddress.getByName("192.168.2.198");
        //InetAddress host = InetAddress.getLocalHost();
        System.out.println(host);
        Client client = new Client("abc", host, 15797);
        client.execute();
    }
}

class ReadClient extends Thread{
    private Socket client;

    public ReadClient(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            while (true) {
                String sms = dis.readUTF();
                System.out.println(sms);
            }
        } catch (Exception e) {

            try {
                dis.close();
                client.close();
            } catch (Exception ex) {
                System.out.println("Ngắt kết nối Server");
            }
        }
    }
}

class WriteClient extends Thread {
    private Socket client;
    private String name;

    public WriteClient(Socket client, String name) {
        this.client = client;
        this.name = name;
    }

    @Override
    public void run() {
        DataOutputStream dos = null;
        Scanner sc = null;
        try {
            dos = new DataOutputStream(client.getOutputStream());
            sc = new Scanner(System.in);
            while (true){
                String sms = sc.nextLine();
                dos.writeUTF(name + ": " + sms);
            }
        } catch (Exception e) {
            try {
                dos.close();
                client.close();
            } catch (IOException ex) {
                System.out.println("Ngắt kết nối Server");
            }
        }
    }
}

class Run{
    public static void main(String[] args) throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        System.out.println(host);
        Client client1 = new Client("bcd", host, 8888);
        client1.execute();
    }
}
