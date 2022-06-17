package Scientia;
import java.io.IOException;

import org.fnlp.nlp.cn.CNFactory;
import org.fnlp.nlp.cn.CNFactory.Models;
import taobe.tec.jcc.JChineseConvertor;
public class Seg {
	private String[][] seggedWord;
	
	public Seg (String str)throws Exception{
		JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
	 	CNFactory factory = CNFactory.getInstance("/Users/scientia/Documents/Java/Code/GoogleMaps/COVID-19-Footprint-Check-Final-Version/models");
	 	String simple =jChineseConvertor.t2s(str);
	 	this.seggedWord = factory.tag(simple);

	
	}
	public String posStr() throws IOException {
		JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
	 	String myStr="";
	 	for(int i = 0 ; i < seggedWord[0].length; i++){
	 		myStr=myStr+jChineseConvertor.s2t(seggedWord[0][i])+"("+seggedWord[1][i]+") ";
	 		
	 	}
	 	return myStr;	
	}
	
	public String locate() throws IOException {
		JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
		String myStr="";
		boolean isLocate=false;
		int n = seggedWord[0].length;
		for(int i = 0 ; i < n; i++){
	 		if(seggedWord[1][i].equals("地名") || seggedWord[1][i].equals("机构名") || seggedWord[1][i].equals("实体名")) {
	 			isLocate=true;
	 			myStr=myStr+jChineseConvertor.s2t(seggedWord[0][i]);
	 			i++;
	 			for(; i < n ; i++){
	 				if(seggedWord[1][i].equals("机构名") ||seggedWord[1][i].equals("地名") || seggedWord[1][i].equals("实体名")||seggedWord[1][i].equals("人名")|| seggedWord[1][i].equals("名词")) {
	 					myStr=myStr+jChineseConvertor.s2t(seggedWord[0][i]);
	 						 					
	 				}
	 				else if(seggedWord[1][i].equals("方位词")||seggedWord[1][i].equals("结构助词")||seggedWord[1][i].equals("形谓词")) {
	 					continue;
	 				}
	 				else {
	 					break ;
	 				}
	 			}
	 		}
//	 		else if(isLocate & () {
//	 			myStr=myStr+jChineseConvertor.s2t(seggedWord[0][i]);
//	 		}	 		
	 	}	
		return myStr;
	}
}
