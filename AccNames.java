package OOP;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class AccNames {
    private ArrayList<String> nameList;
    public AccNames(){
        Set<String> names = new TreeSet<>();
        try{
            BufferedReader bR = new BufferedReader(new FileReader("src/OOP/random_names"));
            String name = bR.readLine();
            while(name != null){
                names.add(name);
                name = bR.readLine();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.nameList = new ArrayList<>(names);
    }
    public String getRandomName(){
        return this.nameList.get((int) (Math.random() * this.nameList.size()));
    }
}
