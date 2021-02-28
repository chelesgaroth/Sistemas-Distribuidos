import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


class ContactList {
    private List<Contact> contacts;
    private ReentrantLock lock = new ReentrantLock();

    public ContactList() {
        contacts = new ArrayList<>();

        contacts.add(new Contact("John", 20, 253123321, null, new ArrayList<>(Arrays.asList("john@mail.com"))));
        contacts.add(new Contact("Alice", 30, 253987654, "CompanyInc.", new ArrayList<>(Arrays.asList("alice.personal@mail.com", "alice.business@mail.com"))));
        contacts.add(new Contact("Bob", 40, 253123456, "Comp.Ld", new ArrayList<>(Arrays.asList("bob@mail.com", "bob.work@mail.com"))));
    }

    // @TODO
    public boolean addContact (DataInputStream in) throws IOException {
        lock.lock();
        Contact c = new Contact(null,0,0,null,null);
        c = c.deserialize(in);
        contacts.add(c);
        return true;
    }


    // @TODO
    public List<Contact> getContacts () throws IOException {
        return this.contacts;
    }

}

class ServerWorker implements Runnable {
    private Socket socket;
    private ContactList contactList;

    public ServerWorker(Socket socket, ContactList contactList) {
        this.socket = socket;
        this.contactList = contactList;
    }

    // @TODO
    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            boolean isOpen = true;

            while (isOpen) {
                contactList.addContact(in);
            }

            socket.shutdownInput();
            socket.close();

            System.out.println("Connection Closed...");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        ContactList contactList = new ContactList();

        while (true) {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, contactList));
            worker.start();
        }
    }

}
