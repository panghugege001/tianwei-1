package dfh.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import dfh.model.RecordMail;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class XlsUtil  {
	
    public static void createExcel(OutputStream os,List<RecordMail>list) {
        //创建工作薄
      	try {
      		
      		WritableWorkbook workbook = Workbook.createWorkbook(os);
   
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet",0);
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        Label labelMail = new Label(0,0,"邮件地址");
        sheet.addCell(labelMail);
        Label labelStatus = new Label(1,0,"是否有效");
        sheet.addCell(labelStatus);
   
        
        for (int i = 0; i < list.size(); i++) {
        	
           RecordMail recordMail=list.get(i);
           Label mail = new Label(0,i+1,recordMail.getEmail());
           sheet.addCell(mail);
           Label status = new Label(1,i+1,recordMail.getStatus()==0?"有效":"没效");
           sheet.addCell(status);
        	 
		}
       
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
        
  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}