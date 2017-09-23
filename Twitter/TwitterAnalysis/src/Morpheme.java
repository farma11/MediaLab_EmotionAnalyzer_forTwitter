import java.io.*;
import java.util.*;

public class Morpheme {

    String surface;		// �\�w�`
    String posStr;		// �i��������
    String[] pos;		// �i���K�w
    String conjForm;	// ���p�`
    String conjType;	// ���p�^
    String base;		// ���`
    String reading;		// �ǂ�
    String pron;		// ����

    /**
     * ���̌`�ԑf�������Ȃ�true�i�^�j��Ԃ��C�����łȂ����false�i�U�j��Ԃ�
     */
    public boolean isVerb() {
        return pos[0].equals("����");
    }

     // ���̌`�ԑf�̏��𕶎���ɂ��ĕԂ�
    public String toString() {
        return "<�`�ԑf �\�w=\"" + surface + "\"" +
               " �i��=\"" + posStr + "\"" +
               " ���p�`=\"" + conjForm + "\"" +
               " ���p�^=\"" + conjType + "\"" +
               " ���`=\"" + base + "\"" +
               " �ǂ�=\"" + reading + "\"" +
               " ����=\"" + pron + "\" />";
    }

    String mecabLine;    
    static Process mecabPrc;
    static PrintWriter mecabOut;
    static BufferedReader mecabIn;
    static String mecabCmd = "C:\\Program Files (x86)\\MeCab\\bin\\mecab.exe";
    static String encoding = "SJIS";
    

    /**
     * �R���X�g���N�^
     */ 
    public Morpheme(String line) {
        mecabLine = line;
        String[] arr = line.split("\t");
        surface = arr[0];
        String feat = arr[1];
        String[] tokens = feat.split(",");
        posStr = "";
        pos = new String[4];
        for (int i = 0; i <= 3; i++) {
            pos[i] = tokens[i];
            if (i > 0) {
                posStr += ",";
            }
            posStr += tokens[i];
        }
        conjType = tokens[4];
        conjForm = tokens[5];
        base = tokens[6];
        if (tokens.length > 7) {
            reading = tokens[7];
            pron = tokens[8];
        } else {
            reading = "";
            pron = "";
        }
    }

    /**
     * �`�ԑf��͂̌��ʂ�Morpheme�I�u�W�F�N�g�̃��X�g�ɂ��ĕԂ�
     */
     static ArrayList<Morpheme> analyzeMorpheme(String str) {
        if (mecabPrc == null) {
            startMeCab();
        }
        mecabOut.println(str);    // MeCab�ɕ�����𑗂�
        mecabOut.flush();
        ArrayList<Morpheme> morphs = new ArrayList<Morpheme>();
        try {
            for (String line = mecabIn.readLine(); line != null; line = mecabIn.readLine())  {
                // mecab���猋�ʂ��󂯎��
                if (line.equals("EOS")) {
                    break;
                } else {
                    morphs.add(new Morpheme(line));
                }
             }
        } catch (IOException e) {
             System.err.println("MeCab����`�ԑf��͌��ʂ��󂯎��ۂ�IOException���������܂���");
             e.printStackTrace();
        }
        return morphs;
    }

    /**
     * �`�ԑf��͊�MeCab���J�n����
     */
    static void startMeCab() {
        try {
            mecabPrc = Runtime.getRuntime().exec(mecabCmd);
            mecabOut = new PrintWriter(new OutputStreamWriter(mecabPrc.getOutputStream(), encoding));
            mecabIn = new BufferedReader(new InputStreamReader(mecabPrc.getInputStream(), encoding));
        } catch (IOException e) {
            System.err.println("�`�ԑf��͊�MeCab���N���ł��܂���ł���");
            System.exit(-1);
        }
    }

    /**
     * ���̌`�ԑf�̕\�w�I�������Ԃ�
     */
    public String getSurface() {
        return surface;
    }

    /**
     * ���̌`�ԑf�̓ǂ݂�Ԃ�
     */
    public String getReading() {
        return reading;
    }

    /**
     * ���̌`�ԑf�̕i���i���K�w�̂݁j��Ԃ�
     */
    public String getPos() {
        return pos[0];
    }

    /**
     * ���̌`�ԑf�̕i����(i+1)�K�w��Ԃ�
     */
    public String getPos(int i) {
        return pos[i];
    }

    /**
     * ���̌`�ԑf�̊��p�`��Ԃ�
     */
    public String getConjugationForm() {
        return conjForm;
    }

    /**
     * ���̌`�ԑf�̊��p�`��Ԃ�
     */
    public String getConjugationType() {
        return conjType;
    }
    
    
    /**
     * ���̌`�ԑf�̌��`��Ԃ�
     */
    public String getBaseform() {
        return base;
    }

    /**
     * ���̌`�ԑf�̔�����Ԃ�
     */
    public String getPronunciation() {
        return pron;
    }
    
    /**
     * main���\�b�h
     */
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
    	System.out.print("���͂���͂��Ă��������B�F");
    	String text = sc.next();
    	
        // MeCab���N��
        startMeCab();

        // ��������`�ԑf���
        ArrayList<Morpheme> morphs = analyzeMorpheme(text);
        System.out.println(morphs);
    
        for (int i = 0; i < morphs.size(); i++) {
            // �`�ԑf��1����morph�Ɋi�[���郋�[�v
            Morpheme morph = morphs.get(i);
			    
            // �\�w�̕���������o���ɂ́CgetSurface���\�b�h
            String surface = morph.getSurface();
            System.out.println("�\�w�̕�����: " + surface);

            // �i���i��i+1�K�w�j�����o���ɂ́CgetPos(i)
            String pos1 = morph.getPos(0);
            System.out.println("�@�i�����K�w: " + pos1);
            String pos2 = morph.getPos(1);
            System.out.println("�@�i�����K�w: " + pos2);

            // ���`�����o���ɂ́CgetBaseform���\�b�h
            String base = morph.getBaseform();
            System.out.println("�@���`: " + base);

            // �ǂ݂����o���ɂ́CgetReading���\�b�h
            String read = morph.getReading();
            System.out.println("�@�ǂ�: " + read);
        }
        
        
    }
}