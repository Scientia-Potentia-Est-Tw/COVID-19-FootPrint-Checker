package Scientia;

public class Article {
	private String[] sentences;
	private String[] seggedSentences;
	public Article(String str) {
		this.sentences = str.split("，|。|,");
		this.seggedSentences= new String[sentences.length];
	}
	public void segTheSentences() {
		String segResult="";
		for(int i = 0 ; i<sentences.length; i++) {
			try {
				Seg result = new Seg(this.sentences[i]);
				System.out.println(result.posStr());
				segResult=result.locate();
			} catch (Exception e) {
				System.out.print("seg error");				
			}
			this.seggedSentences[i]=segResult;
		}			
	}
	public String getSeggedSentences() {
		String myStr="";
		for(int i = 0 ; i<seggedSentences.length; i++) {
			if(seggedSentences[i].length()>0){
				myStr=myStr+seggedSentences[i]+"\n";
			}
		}
		return myStr;
	}
	public String[] getSeggedSentencesArray() {
		return seggedSentences;
	}
}
