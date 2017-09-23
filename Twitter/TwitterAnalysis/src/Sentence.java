import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * ���߂̃��X�g���琬��1���D���ߊԂ̌W��󂯍\����ێ�����N���X�D
 */
public class Sentence extends ArrayList<Chunk> {

    Chunk head;

    static Process caboChaPrc;
    static PrintWriter caboChaOut;
    static BufferedReader caboChaIn;
    static String cabochaCmd = "C:\\Program Files (x86)\\CaboCha\\bin\\cabocha.exe -f1";
    static String encoding = "SJIS";


    // ���̂ւ��main���\�b�h��ǉ����ĉ�����

    
    public Sentence() {
        super();
    }
    
    public Sentence(List<Chunk> chunks) {
        super(chunks);
        initDependency();
    }
    
    public void initDependency() {
        Iterator<Chunk> i = this.iterator();
        while (i.hasNext()) {
            Chunk chunk = i.next();  
            int dependency = chunk.getDependency();
            if (dependency == -1) {
                head = chunk;
            } else {
                Chunk depChunk = this.get(dependency);
                depChunk.addDependentChunk(chunk);
                chunk.setDependencyChunk(depChunk);
            }
        }        
    }
    
    /**
     * �厫�̕��߂�Ԃ�
     * @return �厫�̕���
     */
    public Chunk getHeadChunk() {
        return head;
    }
    
    /**
     * �w�肵���i���E���^�̌`�ԑf���厫�Ɏ����߂�T���ĕԂ�
     * @param pos �T���ĂĂ镶�߂̎厫�`�ԑf�̕i��
     * @param baseform �T���Ă��镶�߂̎厫�`�ԑf�̌��^
     * @return �����������߂̃��X�g
     */
    public List<Chunk> findChunkByHeads(String pos, String baseform) {
        List<Chunk> matches = new ArrayList<Chunk>();
        for (Iterator<Chunk> i = this.iterator(); i.hasNext(); ) {
            Chunk chunk = i.next();
            Morpheme head = chunk.getHeadMorpheme();
            if (pos.equals(head.getPos()) && baseform.equals(head.getBaseform())) {
                matches.add(chunk);
            }
        }
        return matches;
    }
    
    
    /**
     * �����i�̕��߂�Ԃ�
     * @return �����i�̕���
     */
    public Chunk getAgentCaseChunk() {
    	// �厫�ɌW�镶�߂̒�����K�i�̕��߂�T��
        Chunk cand = findChunkByHead(head.getDependents(), "����", "��");
        if (cand != null) return cand;
        // �厫�ɌW�镶�߂̒�����n�i�̕��߂�T��
        cand = findChunkByHead(head.getDependents(), "����", "��");
        if (cand != null) return cand;
        // �S�Ă̕��߂̒�����K�i�̕��߂�T��
        cand = findChunkByHead(this, "����", "��");
        if (cand != null) return cand;
        // �S�Ă̕��߂̒�����n�i�̕��߂�T��
        cand = findChunkByHead(this, "����", "��");
        return cand;
    }
    
    /**
     * �w�肵���i���E���^�̌`�ԑf���厫�Ɏ����߂𕶐߃��X�gchunks����T���ĕԂ�
     * @param chunks ���߃��X�g
     * @param pos �T���ĂĂ镶�߂̎厫�`�ԑf�̕i��
     * @param baseform �T���Ă��镶�߂̎厫�`�ԑf�̌��^
     * @return ������������
     */
    public Chunk findChunkByHead(List<Chunk> chunks, String pos, String baseform) {
        for (Iterator<Chunk> i = chunks.iterator(); i.hasNext(); ) {
            Chunk chunk = i.next();
            Morpheme head = chunk.getHeadMorpheme();
            if (pos.equals(head.getPos()) && baseform.equals(head.getBaseform())) {
                return chunk;
            }
        }
        return null;
    }
    
    
    /**
     * ���̕��Ɋ܂܂�镶�ߊԂ̌W��󂯍\����\��XML���̕������Ԃ�
     * @return XML���̕�����
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<�� �厫=\""+ head.getId() +"\">\n");
        for (Iterator<Chunk> i = iterator(); i.hasNext(); ) {
            sb.append(i.next().toString());
            sb.append("\n");
        }
        sb.append("</��>");
        return sb.toString();
    }
    
    
    /**
     * CaboCha�̌W��󂯉�͂�������
     */
    public static void main(String[] args) {
        String text = "�m���Ă��H�ׂ̂��q����͂�������`��H�ׂ���āB"; // ��͑Ώۂ̃e�L�X�g
        System.out.println("��͑Ώۂ̃e�L�X�g: "+text);
        
        // �Ώۃe�L�X�g�𕶂ɕ����������CaboCha�ɓn���C��͌��ʂ��󂯎��D
        Sentence.parseTweet(text);
    }
    
    
    static void startCaboCha() {
        if (caboChaPrc != null) {
            caboChaPrc.destroy();
        }
        try {
            caboChaPrc = Runtime.getRuntime().exec(cabochaCmd);
            caboChaOut = new PrintWriter(new OutputStreamWriter(caboChaPrc.getOutputStream(), encoding));
            caboChaIn = new BufferedReader(new InputStreamReader(caboChaPrc.getInputStream(), encoding));
        } catch (IOException ex) {
            System.err.println("�W��󂯉�͊�CaboCha���N���ł��܂���ł���");
            System.exit(-1);
        }
    }
    
        
    /**
     * ���ɋ�؂邽�߂̃Z�p���[�^
     */
    static List separators = Arrays.asList(new String[]{
            "�B", "�I", "!", "�H", "?", "�D", "\n"});
    
    /**
     * ���ɋ�؂�
     * @param text �����̕����܂މ\���̂���String
     * @return ��؂�ꂽ���iString�j�̃��X�g
     */
    static List<String> splitSentences(String text) {
        List<String> sentences = new ArrayList<String>();
        while(text.length() > 0) {
            int i = -1;
            for (int k = 0; k < separators.size(); k++) {
                String sep = (String)separators.get(k);
                int j = text.indexOf(sep);
                if (j >= 0 && (i < 0 || j < i)) {
                    i = j;
                }
            }
            if (i < 0 || i == text.length() - 1) {
                sentences.add(text);
                text = "";
            } else {
                sentences.add(text.substring(0, i + 1));
                text = text.substring(i + 1);
            }
        }
        return sentences;
    }
    

    /**
     * �Ώۃe�L�X�g�𕶂ɕ����������CaboCha�ɓn���C��͌��ʂ��擾
     * @param tweet ��͑Ώۂ̃e�L�X�g�i�c�C�[�g�j
     * @return ��͌��ʁi���̃��X�g�j
     */
    static List<Sentence> parseTweet(String tweet) {
        List<String> sentenceStrs = splitSentences(tweet); // ���ɕ���
        List<Sentence> sentences = new ArrayList<Sentence>();
        for (String sentenceStr: sentenceStrs) {
            Sentence sentence = parse(sentenceStr); // 1�������
            sentences.add(sentence);
        }
        return sentences;
    }
    
    
    /**
     * �����W��󂯉��
     * @param sentenceStr ���{�ꕶ�̕�����
     * @return ���̃o�[�W�����ł�null
     */
    static Sentence parse(String sentenceStr) {
        if (caboChaOut == null) {
            startCaboCha();  // CaboCha�����s����Ă��Ȃ��ꍇ�͎��s����
        }
        caboChaOut.println(sentenceStr); // CaboCha�ɕ���n��
        caboChaOut.flush();

        try {
            // CaboCha�����͌��ʂ��󂯎��for���D�ϐ�line��1�s����������
            for (String line = caboChaIn.readLine();
                 line != null;
                 line = caboChaIn.readLine()) {
                System.out.println(line); // ��͌��ʂ�1�s�\��
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("CaboCha�̌W��󂯉�͂Ɏ��s���܂���: �u"+sentenceStr+"�v");
        }
        return null; // ���̗�ł͉�͌��ʂ�\�����邾���ŁCnull��Ԃ��Ă���
    }    

    
}
