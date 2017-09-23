import java.io.*;
import java.util.*;

public class Corpus {
	String text;
	Emotion emotions;
	
	enum Emotions{
		Joy,Trust,Fear,Surprise,Sad,Disgust,Anger,Anticipate,
	}
	
	/**
     * �R���X�g���N�^
     */ 
	public Corpus(String txt, double[] emo){
		text = txt;
		emotions = new Emotion(emo);
	}
	
	/**
     * CSV�t�@�C���̓ǂݍ��݂ɂ��R���X�g���N�^
     */ 
	public Corpus(String lineCSV){
		String[] nums = lineCSV.split(",", 0);
		text = nums[0];
		double[] emos = {
    			Double.parseDouble(nums[1]),Double.parseDouble(nums[2]),
    			Double.parseDouble(nums[3]),Double.parseDouble(nums[4]),
    			Double.parseDouble(nums[5]),Double.parseDouble(nums[6]),
    			Double.parseDouble(nums[7]),Double.parseDouble(nums[8])
    			};
		emotions = new Emotion(emos);
	}
	
	/**
     * ���̃e�L�X�g�̓��e��Ԃ�
     */
    public String getText() {
        return text;
    }
    
    /**
     * ���̃e�L�X�g�̊���l�̔z���Ԃ�
     */
    public double[] getEmotionsArray() {
        return emotions.getEmotions();
    };
    
    /**
     * ���̃e�L�X�g�̊���l��Emotion�N���X��Ԃ�
     */
    public Emotion getEmotions() {
        return emotions;
    };
    
    /**
     * ���̃e�L�X�g�̊e����l��Ԃ�
     */
    public double getEmotion(int i) {
    	// �񋓌^Emotion�͈͓̔��ł���΂��̊���l�̒l��int�^�ŕԂ��B
    	if(i >= 0 && i < EnumSet.allOf(Emotions.class).size()) return emotions.getEmotion(i);
    	else return -1.0;
    }
    
    /**
     * ���̃e�L�X�g�̊e����l���X�V����
     */
    public int changeEmotion(int i, double val) {
    	// �񋓌^Emotion�͈͓̔��ł���΂��̊���l�̒l��int�^�ŕԂ��B
    	if(i >= 0 && i < EnumSet.allOf(Emotions.class).size()){
    		emotions.setEmotion(i,val);
    		return 0;
    	}
    	else return -1;
    }
    
  //��r���\�b�h�i�f�[�^�N���X���r����-1, 0, 1��Ԃ��悤�ɋL�q����j
    public int compare(Corpus a, Corpus b) {
    	// a>b:��, a==b:0, a<b:���̒l���Ԃ����
        int x = a.getText().compareTo(b.getText());

        //�����Ń\�[�g�����
        if (x > 0) return 1;
        else if (x == 0) return 0;
        else return -1;
    }
    
    
    public static void main(String[] args){
        try {
        	// �t�@�C�����͂̏���
        	String fname = "D:\\Twitter\\TwitterAnalysis\\samples\\SampleCorpus.csv";
    		BufferedReader br = FileRead.makeBR(fname, "SJIS");

        	//�P�s���ƂɃt�@�C������
			List<Corpus> corpora = new ArrayList<Corpus>();;
			String line;
            while( ( line = br.readLine() ) != null ){
            	corpora.add(new Corpus(line));
            }
         	br.close();
         	
         	// ���͂����`�ԑf�̃\�[�g
         	Collections.sort(corpora, new Comparator<Corpus>(){
         		public static final int ASC = 1;   //���� (1.2.3....)
                public static final int DESC = -1; //�~�� (3.2.1....)
         		public int compare(Corpus a, Corpus b) {
         			int sortType = ASC;
	                return sortType * a.getText().compareTo(b.getText());
         		}
         	});
            
            for(Corpus co : corpora){
            	System.out.print( co.getText() );
            	for(double em : co.emotions.getEmotions()){
            		System.out.print("," + em);
            	}
            	System.out.println();
            }

		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
}



