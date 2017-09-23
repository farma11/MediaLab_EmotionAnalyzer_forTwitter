import java.io.*;
import java.util.*;

public class TweetAnalze {
	
	// �e�`�ԑf��DF�l���i�[����HashMap
    static HashMap<String,Integer> dfMap = new HashMap<String,Integer>();
    // DF�𐔂��������W���̑S������
    static int N;
	
	// ���͂��ꂽ�`�ԑf�R�[�p�X
	static TreeMap<String,Corpus> corpora = new TreeMap<String,Corpus>();
	// �`�ԑf�R�[�p�X���ɑ��݂��Ȃ��P�� val:{��,TF-IDF}
	static TreeMap<String,Pair<Integer,Double> > undefs = new TreeMap<String,Pair<Integer,Double> >();
	// �c�C�[�gDB
	static List<TwitCorpus> tweetDB = new ArrayList<TwitCorpus>();
	
    public static void main( String[] args ) {
    	 try {
    		 
    		 setupAnalyisis();
    		 
    		 String Id = inputId();
    		// �c�C�[�g���X�g���擾��A�S�̂̊���l���v�Z
            List<Tweet> tweets = getTweets(Id, 1);
            Emotion scoresAVE = analyzeTweets(tweets);

            Emotion.toJSonWEB(scoresAVE);
            System.out.println();
            System.out.println("ALL TWEETS EMOTIONS");
            Emotion.printEmotions(scoresAVE);
            
            outputUndefMorphs();
        }
        catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * ��͂̏���
     */
    static void setupAnalyisis(){
    	try {
    		// �e�P���DF��dfMap�ɓǂݍ���ŏ�����
   		 	initDf();
   		 
   		 	inputTwitCorpus();
   		 	inputCorpus();
   		 	
   		 	// MeCab���N��
   		 	Morpheme.startMeCab();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * �P��c�C�[�g�̉��
     * @param tw
     * @return
     */
    static Emotion analyzeTweet(Tweet tw){
		 
    	Emotion scores = new Emotion();
    	// �e�`�ԑf��TF-IDF�l���v�Z
        ArrayList<Double> morphsTFIDF = calTFIDF(tw.getMorphs());
        calScore(tw.getMorphs(), morphsTFIDF, scores);
        modifyEmotion(tw.getMorphs(), scores);
    	
    	return scores;
    }
    
    /**
     * �P��c�C�[�g�̉��(Corpus�ύX��)
     * @param tw
     * @return
     */
    static Emotion analyzeTweet(Tweet tw, TreeMap<String,Corpus> co){
		 
    	Emotion scores = new Emotion();
    	// �e�`�ԑf��TF-IDF�l���v�Z
        ArrayList<Double> morphsTFIDF = calTFIDF(tw.getMorphs());
        calScore(tw.getMorphs(), morphsTFIDF, scores, co);
        modifyEmotion(tw.getMorphs(), scores);
    	
    	return scores;
    }
    
    /**
     * �����c�C�[�g�̉��
     */
    static Emotion analyzeTweets(List<Tweet> tweets){
    	Emotion scoresAVE = new Emotion();
    	for(Tweet tw : tweets){
        	// �e����l�̉��Z
        	Emotion scores = analyzeTweet(tw);
            for(int j = 0; j < 8; j++){
            	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j) + scores.getEmotion(j));
            }
        }
        for(int j = 0; j < 8; j++){
        	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j)/tweets.size());
        }
    	return scoresAVE;
    }
    
    /**
     * �����c�C�[�g�̉��(Corpus�ύX��)
     */
    static Emotion analyzeTweets(List<Tweet> tweets, TreeMap<String,Corpus> co){
    	Emotion scoresAVE = new Emotion();
    	for(Tweet tw : tweets){
        	// �e����l�̉��Z
        	Emotion scores = analyzeTweet(tw, co);
            for(int j = 0; j < 8; j++){
            	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j) + scores.getEmotion(j));
            }
        }
        for(int j = 0; j < 8; j++){
        	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j)/tweets.size());
        }
    	return scoresAVE;
    }
    
    /**
     * ��̓A�J�E���g��ID����
     */
    static String inputId(){
    	Scanner sc = new Scanner(System.in);
    	System.out.print("ID: ");
    	return sc.next();
    }
    
    /**
     * ID����c�C�[�g�ꗗ���擾
     * kind = 1:Latest, kind = 2:100 items
     */
    static List<Tweet> getTweets(String id, int kind){
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	
    	try{
	    	// �t�@�C�����͂̏���
    		String url = "";
    		int warning = 0;
    		switch(kind){
    		case 1:
    			url = "http://shippoproj1.webcrow.jp/twit2-bot.php?id=" + id ;
    			warning = 5;
    			break;
    		case 2:
    			url = "http://shippoproj1.webcrow.jp/twit2.php?id=" + id ;
    			warning = 3;
    			break;
    		}
	    	
			BufferedReader br = URLRead.makeBR( url,"UTF-8");
	        
			// web�y�[�W�`����warming�����΂�
	        for(int i = 0; i < warning; i++){
	        	String firstline = br.readLine(); //�Ƃ΂�
	        }
	    	
	    	String[] inputs = {"","","",""};
			int i = 0;
			int endi = 0;
			
	        while( ( inputs[i] = br.readLine() ) != null ){
	        	
	        	int inputSize = inputs[i].length();
	        	StringBuilder temp = new StringBuilder();
	        	inputs[i] = temp.append(inputs[i]).delete(inputSize-6,inputSize).toString();
	        	i = (i + 1) % 4;
	        	if(i == 0){
	        		//@�ȉ���ID���폜
	        		inputs[3] = inputs[3].replaceAll("@[^ ]+ ","");
	        		
	        		tweets.add(new Tweet(inputs[0],inputs[1],inputs[2],inputs[3]));
	        		endi += 1;
	        		if(endi == 100) break;
	        	}
	        } 
    	}
        catch( IOException e ){
            System.err.println(e);
        }
    	return tweets;
    }
    
    /**
     * �c�C�[�gDB�̓���
     */
    static void inputTwitCorpus(){
    	try {
	        	
		// �t�@�C�����͂̏���
    	//String url = "http://fama11.sitemix.jp/MediaLab3/tweet-corpus1.csv";
    	String url = "http://shippoproj1.webcrow.jp/Corpus/tweet-corpus1.csv";
		BufferedReader br = URLRead.makeBR( url,"SJIS");
	
		String line;
	    while( ( line = br.readLine() ) != null ){
	    	tweetDB.add(new TwitCorpus(line));
	    }
	 	br.close();
 	
    	} catch (IOException e) {
    		System.out.println("�R�[�p�X�̓��͂Ɏ��s���܂����B");
			e.printStackTrace();
		}
    }
    
    /**
     * �c�C�[�gDB�̓���(twDB�ύX��)
     */
    static void inputTwitCorpus(List<TwitCorpus> twDB){
    	try {
	        	
		// �t�@�C�����͂̏���
    	String url = "http://fama11.sitemix.jp/MediaLab3/tweet-corpus1.csv";
		BufferedReader br = URLRead.makeBR( url,"SJIS");
	
		String line;
	    while( ( line = br.readLine() ) != null ){
	    	twDB.add(new TwitCorpus(line));
	    }
	 	br.close();
 	
    	} catch (IOException e) {
    		System.out.println("�R�[�p�X�̓��͂Ɏ��s���܂����B");
			e.printStackTrace();
		}
    }
    
    
    /**
     *  �R�[�p�X�̓���
     */
    static void inputCorpus(){
    	try {       
    		
            //String url = "http://fama11.sitemix.jp/MediaLab3/corpus1.csv";
    		String url = "http://shippoproj1.webcrow.jp/Corpus/corpus1.csv";
    		BufferedReader br = URLRead.makeBR( url,"SJIS");

			String line;
            while( ( line = br.readLine() ) != null ){
            	Corpus co = new Corpus(line);
            	corpora.put(co.getText(),co);
            }
         	br.close();
    	} catch (IOException e) {
    		System.out.println("�R�[�p�X�̓��͂Ɏ��s���܂����B");
			e.printStackTrace();
		}
    }
    
    /**
     *  �R�[�p�X�̓���(Corpus�ύX��)
     */
    static void inputCorpus(TreeMap<String,Corpus> co){
    	try {       
            String url = "http://fama11.sitemix.jp/MediaLab3/corpus1.csv";
    		BufferedReader br = URLRead.makeBR( url,"SJIS");

			String line;
            while( ( line = br.readLine() ) != null ){
            	Corpus c = new Corpus(line);
            	co.put(c.getText(),c);
            }
         	br.close();
    	} catch (IOException e) {
    		System.out.println("�R�[�p�X�̓��͂Ɏ��s���܂����B");
			e.printStackTrace();
		}
    }
    
    /**
     * �`�ԑf�R�[�p�X�̏d�݂Â�
     */
     static void calScore(ArrayList<Morpheme> morphs, ArrayList<Double> morphsTFIDF, Emotion scores){
    	 // �e�`�ԑf�ɂ���
    	 for(int i = 0; i < morphs.size(); i++){
    		 Morpheme morph = morphs.get(i);
    		 if(morph.base.equals("*")) continue;
    		 
    		 // �R�[�p�X���ɑ��݂��Ă�����
         	if(corpora.containsKey(morph.base)){
         		Corpus coefficiency = corpora.get(morph.base);
         		for(int j = 0; j < 8; j++){
         			scores.setEmotion(j, calWeight(scores.getEmotion(j), morphsTFIDF.get(i) * coefficiency.getEmotion(j)));
         		}
         	} //�R�[�p�X���ɑ��݂��Ă��Ȃ�������A����`���X�g�ƂƂ��ďo��
         	else {
         		// System.out.println("undefined: " + morph.base);
         		if(undefs.containsKey(morph.base)){
         			undefs.put(morph.base, new Pair(undefs.get(morph.base).first+1,undefs.get(morph.base).second));
         		} else {
         			undefs.put(morph.base, new Pair( 1,morphsTFIDF.get(i)));
         		}
         	}
    	 }
    	 
     }
     
     /**
      * �`�ԑf�R�[�p�X�̏d�݂Â�(corpus�ύX��)
      */
      static void calScore(ArrayList<Morpheme> morphs, ArrayList<Double> morphsTFIDF, Emotion scores, TreeMap<String,Corpus> co){
     	 // �e�`�ԑf�ɂ���
     	 for(int i = 0; i < morphs.size(); i++){
     		 Morpheme morph = morphs.get(i);
     		 if(morph.base.equals("*")) continue;
     		 
     		 // �R�[�p�X���ɑ��݂��Ă�����
          	if(co.containsKey(morph.base)){
          		Corpus coefficiency = co.get(morph.base);
          		for(int j = 0; j < 8; j++){
          			scores.setEmotion(j, calWeight(scores.getEmotion(j), morphsTFIDF.get(i) * coefficiency.getEmotion(j)));
          		}
          	} //�R�[�p�X���ɑ��݂��Ă��Ȃ�������A����`���X�g�ƂƂ��ďo��
          	else {
          		// System.out.println("undefined: " + morph.base);
          		if(undefs.containsKey(morph.base)){
          			undefs.put(morph.base, new Pair(undefs.get(morph.base).first+1,undefs.get(morph.base).second));
          		} else {
          			undefs.put(morph.base, new Pair( 1,morphsTFIDF.get(i)));
          		}
          	}
     	 }
     	 
      }
     
     /**
      * �d�݂Â��֐�
      */
     static double calWeight(Double score, double val){
    	 score += val;
    	 return score;
     }
     
     /**
      * �e�P���DF���L�^����df.tsv ��dfMap �ɓǂݍ���
      */
     static void initDf() throws Exception {
         BufferedReader in = new BufferedReader(new FileReader("df.tsv"));
         for(String line = in.readLine(); line != null; line = in.readLine()) {
             String[] array = line.split("\t");
             if (array[0].equals("TOTAL_DOC_COUNT_N")) {
                 N = Integer.parseInt(array[1]); // �����W���̑S������
             } else if (array.length == 2) {
                 // �P��array[0]�̌��ꂽ������array[1]��dfMap�Ɋi�[
                 dfMap.put(array[0], Integer.parseInt(array[1]));
             }
         }
     }
     
     /**
      * �`�ԑft��IDF��Ԃ�
      */
     static double getIdf(Morpheme t) {
         return 1 + Math.log((double)N / getDf(t));
     }
     
     /**
      * �`�ԑft��DF��Ԃ�
      */
     static int getDf(Morpheme t) {
         String base = t.getBaseform(); // �`�ԑf�̌��`���擾
         if (dfMap.get(base) != null) {
             return dfMap.get(base);    // dfMap�Ɋi�[���Ă�����DF�l��Ԃ�
         } else {
             return 1;                  // DF�l���i�[����Ă��Ȃ����1��Ԃ�
         }
     }

     /**
      * �`�ԑf���X�gmorphs�̒��Ő������`�ԑft��TF�i�o���񐔁j��Ԃ�
      */
     static int getTf(Morpheme t, List<Morpheme> morphs) {
         int tf = 0;
         String tBase = t.getBaseform();  // t�̌��`
         for(int i = 0; i < morphs.size(); i++) {  // �`�ԑf��1�����o��for���[�v
             Morpheme morph = morphs.get(i);          // i�Ԗڂ̌`�ԑfmorph
             String morphBase = morph.getBaseform();  // morph�̌��`
             if (morphBase.equals(tBase)) {    // �����������`�Ȃ�
                 tf++;
             }
         }
         return tf;
     }

     /**
      * �`�ԑf���X�gmorphs�̒��Ő������`�ԑft��TF-IDF��Ԃ�
      */
     static double getTfIdf(Morpheme t, List<Morpheme> morphs) {
         return getTf(t, morphs) * getIdf(t);
     }
     
     /**
      * TF-IDF���g�����X�R�A�v�Z
      */
     static ArrayList<Double> calTFIDF(List<Morpheme> morphs){
    	 ArrayList<Double> vals = new ArrayList<Double>();
    	 for (int i = 0; i < morphs.size(); i++) {
             Morpheme morph = morphs.get(i);
             double score = getTfIdf(morph, morphs);  // TF-IDF���g�����X�R�A�v�Z
             vals.add(score);
             // System.out.println(morph.getBaseform()+": "+score);  // �X�R�A�\��
         }
    	 return vals;
     }
        
     /**
      * ����`�`�ԑf���X�g�̃t�@�C���o��
      */
     static void outputUndefMorphs(){
    	 try{
         	String fname = "D:\\Twitter\\TwitterAnalysis\\samples\\undefMorphs.csv";
    		BufferedReader br = FileRead.makeBR(fname, "SJIS");
    		 
         	String in_line = "";
         	while( ( in_line = br.readLine() ) != null ){
         		String[] inputs = in_line.split(",",-1);
         		
         		if(undefs.containsKey(inputs[0])){
         			undefs.put(inputs[0], new Pair(undefs.get(inputs[0]).first+1,undefs.get(inputs[0]).second));
         		} else {
         			undefs.put(inputs[0], new Pair( 1,Double.parseDouble(inputs[2])));
         		}
         	}
    		 
         	// ����`���X�g�̃t�@�C���o��
    	      File file = new File("D:\\Twitter\\TwitterAnalysis\\samples\\undefMorphs.csv");

    	      if (checkBeforeWritefile(file)){
    	        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

    	        for(String str : undefs.keySet()){
                	pw.println(str + ", " + undefs.get(str).first + ", " + undefs.get(str).second);
                }
    	        pw.close();
    	      }else{
    	        System.out.println("�t�@�C���ɏ������߂܂���");
    	      }
    	    }catch(IOException e){
    	      System.out.println(e);
    	    }
     }
     private static boolean checkBeforeWritefile(File file){
    	    if (file.exists()){
    	      if (file.isFile() && file.canWrite()){
    	        return true;
    	      }
    	    }
    	    return false;
     }
     
     /**
      * �c�C�[�gDB��̃c�C�[�g�Ɨގ��x���v�Z���A��͌�̊���l��ϓ�������
      */
     static void modifyEmotion(ArrayList<Morpheme> morphs, Emotion scores){
    	 HashMap<String, Integer> morphsCount = new HashMap<String, Integer>();
    	 for(Morpheme morph: morphs){
    		 if(morphsCount.containsKey(morph.base)){
         		morphsCount.put(morph.base, morphsCount.get(morph.base) + 1);
         	} else {
         		morphsCount.put(morph.base, 1);
         	}
    	 }
    	 
    	 for(TwitCorpus twDB : tweetDB){
    		 double simi = calSimilarity(morphsCount, twDB);
    		 if(simi > 0.5){
    			 for(int i = 0; i < 8; i++){
    				 scores.setEmotion(i, scores.getEmotion(i) * 0.6 + twDB.getEmotion(i) * 0.4); 
    			 }
    			 //System.out.println("�ގ�: " + simi + " " + twDB.getText());
    		 }
    	 }
     }
     
     /**
      * 2�̃c�C�[�g�̗ގ��x�����߂�
      */
     static double calSimilarity(HashMap<String, Integer> tw1, TwitCorpus tw2_db){
    	 double ans = 0.0;
    	 int tw1_morph_num = 0;
    	 for(Map.Entry<String, Integer> entry : tw1.entrySet()) {
    		 tw1_morph_num += entry.getValue();
    		 if(tw2_db.morphsCount.containsKey(entry.getKey())){
    			ans += entry.getValue() * tw2_db.morphsCount.get(entry.getKey());
         	}
     	}
    	 return ans / (tw1_morph_num + tw2_db.morph_num);
     }
     
     static double calSimilarity(HashMap<String, Integer> tw1, HashMap<String, Integer> tw2){
    	 double ans = 0.0;
    	 for(Map.Entry<String, Integer> entry : tw1.entrySet()) {
    		 if(tw2.containsKey(entry.getKey())){
    			ans += entry.getValue() * tw2.get(entry.getKey());
         	}
     	}
    	 return ans / (tw1.size() * tw2.size());
     }
     
     
     
     
}