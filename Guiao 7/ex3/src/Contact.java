import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
    private String company;     // Pode ser null
    private List<String> emails;

    public Contact (String name, int age, long phone_number, String company, List<String> emails) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phone_number;
        this.company = company;
        this.emails = new ArrayList<>(emails);
    }

    public static Contact deserialize (DataInputStream in) throws IOException {
        try {
            String name = in.readUTF();

            int age = in.readInt();
            long phonenumber = in.readLong();

            String company = null;
            if(in.readBoolean()) company = in.readUTF();

            List<String> emails = new ArrayList<>();
            int number_emails = in.readInt();
            for(int i = 0; i <number_emails; i++){
                emails.add(in.readUTF());
            }
            return new Contact(name,age,phonenumber,company,emails);

        }catch (EOFException e){
            e.printStackTrace();
        }
        in.close();
        return null;
    }

    public void serialize (DataOutputStream out) throws  IOException{
        out.writeUTF(this.name);
        out.writeInt(this.age);
        out.writeLong(this.phoneNumber);
        out.writeUTF(this.company);
        for(String s:this.emails) out.writeUTF(s);
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.age).append(";");
        builder.append(this.phoneNumber).append(";");
        builder.append(this.company).append(";");
        builder.append("{");
        for (String s : this.emails) {
            builder.append(s).append(";");
        }
        builder.append("}");
        return builder.toString();
    }

}