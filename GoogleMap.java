package Scientia;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.teamdev.jxbrowser.engine.RenderingMode.*;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;

public class GoogleMap{
	private JPanel jpGoogleMap;
    private JPanel jp;
    private static List<double[]> dataCoordinate= new ArrayList<double[]>();
    private static Browser browser;
    private static ArrayList<String> matchLocation = new ArrayList<String>();
    private GridBagConstraints constraints;
	String setMarkerScript =
 			"var location = \"總統府\";\n"+
 			"jQuery.get(api,{\"key\":apiKey,\"language\":\"zh_tw\",\"address\":location},getResult,\"text\");";
    private static int zoomValue = 4;
    public GoogleMap(GridBagConstraints constraints)
    {
    	this.jpGoogleMap=new JPanel();
        this.jp=new JPanel();
        this.constraints=constraints;
        init(constraints);
    }
    public void init(GridBagConstraints constraints)
    {	
    	
    	System.setProperty("jxbrowser.license.key", "Your_JxBrowser_License_Key");
    	EngineOptions options = EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey("Your_JxBrowser_License_Key").build();
        Engine engine = Engine.newInstance(options);
        browser = engine.newBrowser();
        //----------------------------------------------------------------------小的區域//
       
        //----------------------------------------------------------------------
        SwingUtilities.invokeLater(() -> {
	        BrowserView view = BrowserView.newInstance(browser);
	        view.setSize(2000,2000);
	        browser.navigation().loadUrl("/Users/scientia/Documents/Java/Code/GoogleMaps/COVID-19-Footprint-Check-Final-Version/map.html");
	        JButton zoomInButton = new JButton("Zoom In");
	        GridLayout layoutP = new GridLayout(1,1);
	        
	        jp.setLayout(layoutP);
	        jp.add(view);
	        
	        //---------------------------------------------------------------------大的區域//
	        constraints.fill = GridBagConstraints.BOTH;//BOTH是把空間填滿//把大的空間填滿
	        constraints.gridx = 1;//X軸座標
	        constraints.gridy = 1;//Y軸座標
	        constraints.gridwidth = 1;//照順序從左到右(數字不重要，但是0為最後一個)
	        constraints.gridheight= 0;//照順序從上到下(數字不重要，但是0為最後一個)
	        constraints.weightx = 7;//佔區域寬度的權重(比例)
	        constraints.weighty = 1;//佔區域高度的權重(比例)
	        readFootPrintTag();
	        MainClass mainClass = new MainClass();
	        mainClass.layout.setConstraints(this.getJp(),this.getConstraints());//把大的區域(JPanel)的位置設定輸入
	        mainClass.window.add(this.getJp());//把設定好的JPanel區域加到整個視窗
        });       
        
    }
    private static void readFootPrintTag(){
	    Double xPosition = 0.0;
	    Double yPosition = 0.0;	
	    BufferedReader reader;
	    try{
		//using buffer reader open FootPrint.txt
		reader = new BufferedReader(new FileReader("/Users/scientia/Documents/Java/Code/GoogleMaps/COVID-19-Footprint-Check-Final-Version/Footprint.txt"));
		//read txt line
		String line = reader.readLine();
		//read until to last
		while(line != null){
			//one line contain x position and y position for google maps tag
			//utilize a comma segment x position and y position
			//so use split for recognize comma
			String[] split = line.split(",");
			//get x position and y position from array
			xPosition = Double.parseDouble(split[0]);
			yPosition = Double.parseDouble(split[1]);
		//set mark script
			String setMarkerScript =
    		        "var myLatlng = new google.maps.LatLng(" + xPosition + ","+ yPosition+");\n" +
                	   "var marker = new google.maps.Marker({\n" +
                    	   "    position: myLatlng,\n" +
                   	   "    map: map,\n" +
                   	   "    title: 'COVID-19 Foot Print'\n" +
                        "});";
			try {
			    Thread.sleep(50);
			} catch(InterruptedException ex) {
			}
			dataCoordinate.add(new double[] { xPosition, yPosition });
		//tag mark to google maps 
			browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(setMarkerScript));
		//}
		//read next line in FootPrint.txt
			line = reader.readLine();
		}
		//close buffer reader
		reader.close();
		//try catch error for handle open fail
		}catch(IOException ioException){
			System.err.println("Read File Error, Terminating.");
			System.exit(1);
		}
	}
    public JPanel getJp()//取得JPanel
    {
        return jp;
    }
    public GridBagConstraints getConstraints()//取得GridBagConstraints
    {
        return constraints;
    }
    public void footPrintInMap(String [] locations) {
    	for(int i=0;i<locations.length;i++){    		
			parseLocation(locations[i]);
			}
    }
    public static void parseLocation(String locationName){

        String ERROR_MESSAGE = "Couldn't find specified custom location, falling back to co-ordinates";
        if (locationName == null || locationName.equals("")) {
            System.out.println(ERROR_MESSAGE);
        }

        GeoApiContext context = new GeoApiContext.Builder().apiKey("Your_Google_Maps_Geocoding_API").build();
        try {
            GeocodingResult[] request = GeocodingApi.newRequest(context).address(locationName).await();
            LatLng location = request[0].geometry.location;
            double latitude = location.lat;
            double longitude = location.lng;
            for(int i=0;i<dataCoordinate.size();i++) {
            	System.out.print(dataCoordinate.get(i)[0]+" "+dataCoordinate.get(i)[1]+"\n");
            	double distance=LocationUtils.getDistance(latitude, longitude, dataCoordinate.get(i)[0], dataCoordinate.get(i)[1]);
            	System.out.print(distance+"\n");
            	if(distance<500) {
            		matchLocation.add(locationName);
            		break;
            	}
            	
            }
            String setMarkerScript =
    		        "var myLatlng = new google.maps.LatLng(" + latitude + ","+ longitude+");\n" +
                	   "var marker = new google.maps.Marker({\n" +
                    	   "    position: myLatlng,\n" +
                   	   "    map: map,\n" +
                       "	icon: image,\n" +  
                   	   "    title: 'COVID-19 Foot Print'\n" +
                        "});";
            browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(setMarkerScript));
        } catch (Exception e) {
            System.out.println(ERROR_MESSAGE);
        }
    }
    public String getMatchLocation(){
    	String myStr="";
    	if(matchLocation.size()==0) {
    		return myStr;
    	}
		for(int i = 0 ; i<matchLocation.size(); i++) {
			myStr=myStr+matchLocation.get(i)+"\n";
		}
		JTextArea ta = new JTextArea();
		ta.setFont(new Font("標楷體", Font.PLAIN, 20));
		ta.setEditable(false);//禁止編輯
		ta.setText("下列為重疊地點:\n"+myStr);
		ta.setBackground(Color.CYAN);
		ImageIcon icon = new ImageIcon("/Users/scientia/Documents/Java/Code/GoogleMaps/COVID-19-Footprint-Check-Final-Version/clock.png");
		ImageIcon icon2 = new ImageIcon("/Users/scientia/Documents/Java/Code/GoogleMaps/COVID-19-Footprint-Check-Final-Version/COVID-19FootPrintNone.gif");
		JOptionPane.showMessageDialog(null,ta,"請注意！足跡與確診者重疊", JOptionPane.WARNING_MESSAGE, icon);
		JOptionPane.showMessageDialog(null, null, "請相信這位為了台灣一夜白髮的男人，能帶領大家度過疫情！", JOptionPane.INFORMATION_MESSAGE, icon2);
		return myStr;
    }
  

}
