/*
    This file need to be modified to adjust unique repo names.
 */
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataSrcFromXLS {   // Init once.
    private List<Object> data=new ArrayList<Object>();
    public boolean isValid=false;

    public DataSrcFromXLS(String filename) {
        try {
            InputStream is = new FileInputStream(System.getProperty("user.home") + System.getProperty("file.separator") + filename);
            List<Object> datatmp = EasyExcelFactory.read(is, new Sheet(1, 0));
            for(Object da:datatmp){
                if(!da.toString().split(", ")[2].equals("null")) {
                    data.add(da);
                }
            }
            try {
                is.close();
            }catch(IOException err){}
            this.isValid=true;
        } catch (FileNotFoundException err) {
            System.err.println("students.xlsx not found!");
        }
    }

    public int getCount(){
        return data.size()-1;
    }

    public studentInfo getStuInfo(int index){
        String infoStr=data.get(index).toString();
        infoStr=infoStr.substring(1,infoStr.length()-1);
        String[] val= infoStr.split(", ");
        return new studentInfo(val[0],val[1],val[2]);
    }
}