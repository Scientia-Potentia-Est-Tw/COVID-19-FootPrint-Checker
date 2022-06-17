package Scientia;
import javax.management.remote.JMXServiceURL;
import javax.swing.*;
import java.awt.*;


public class MainClass {
    static GoogleMap googleMap;
    static Covid19FootprintDatabase covid19FootprintDatabase;
    static UserInput userInput;
    static UserFootprintViaNLPParser userFootprintViaNLPParser;
    static JFrame window;
    static GridBagLayout layout;
    static GridBagConstraints constraints;

    public static void main(String args[]) {
        window = new JFrame("COVID-19 Footprint Checker"); //建立標題名
        window.setBounds(300,50,800,800);// 顯示X軸位置,顯示Y軸位置 ,寬,長
        window.setVisible(true); // 視窗預設是不可見的
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//設定單擊窗體右上角關閉圖示後，程式會做出怎樣的處理。
        //----------------------------------------------------------------------------------------------------------------------------
        layout = new GridBagLayout();//整個頁面的位置編排
        window.setLayout(layout);//把整個頁面的設定設為layout
        constraints= new GridBagConstraints();//紀錄位置設定的物件
        //----------------------------------------------------------------------------------------------------------------------------
        googleMap = new GoogleMap(constraints);

        covid19FootprintDatabase = new Covid19FootprintDatabase(constraints);

        userInput = new UserInput(constraints);

        userFootprintViaNLPParser = new UserFootprintViaNLPParser(constraints);
        //----------------------------------------------------------------------------------------------------------------------------


    }

}
