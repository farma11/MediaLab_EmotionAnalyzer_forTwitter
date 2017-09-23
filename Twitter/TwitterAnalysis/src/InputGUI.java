import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;


public class InputGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	static JPanel tweets_p;		// �c�C�[�g�p�l���\����
	static List<Tweet> tweets = new ArrayList<Tweet>();		// �c�C�[�g���X�g
	static List<JLabel> twitTXTs = new ArrayList<JLabel>(); // �c�C�[�g�p�l���ɕ\��������e�L�X�g�̃��X�g
	static List<JPanel> tweet_ps = new ArrayList<JPanel>(); // �c�C�[�g�p�l���̃��X�g
	static List<JButton> tweet_btn = new ArrayList<JButton>(); // �c�C�[�g�p�l���̃{�^��
	
	static Container contentPane;
	
	static JPanel left_p;		// �T�C�h�p�l���p
	static JTextField text_id; 	//TwitterID�p�e�L�X�g�t�B�[���h
	static JLabel label_id = null;		//TwitterID�\���p���x��
	static JPanel accout_data_p = null;
	
	// �擾�f�[�^�I��p�̃��W�I�{�^��
	static JRadioButton getData1 = new JRadioButton("Latest");
	static JRadioButton getData2 = new JRadioButton("100 items");
	
	
	
	public static void main(String args[]){
		TweetAnalze.setupAnalyisis();
		InputGUI frame = new InputGUI("TwitterAnalysis");
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	InputGUI(String title){
		// �E�C���h�E�̑S�̂̐ݒ�
	    setTitle(title);								// �^�C�g���̐ݒ�
	    setBounds(10, 10, 1000, 700);					// �E�C���h�E�̃T�C�Y�̐ݒ�
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X�{�^���Ńv���O�����I��
	    
	    contentPane = getContentPane();
	    
	    /* �T�C�h�p�l���J�n(�ݒ蓙�ŗ��p) */
	    Color left_pc = new Color(176,224,230);
	    
	    left_p = new JPanel();
	    left_p.setPreferredSize(new Dimension(200, 100));
	    left_p.setBackground(left_pc);
	    
	    BevelBorder border = new BevelBorder(BevelBorder.RAISED);
	    left_p.setBorder(border);

	    JLabel label_id = new JLabel("ID: ");
	    text_id = new JTextField(10);
	    
	    // ���W�I�{�^���Ŏ擾����f�[�^�̎�ނ��m��
	    ButtonGroup getData_group = new ButtonGroup();
	    getData1.setBackground(left_pc);
	    getData2.setBackground(left_pc);
	    getData_group.add(getData1);
	    getData_group.add(getData2);
	    
	    
	    JButton button_getID = new JButton("�擾");
	    button_getID.addActionListener(this);
	    
	   
	    left_p.add(label_id);
	    left_p.add(text_id);
	    left_p.add(getData1);
	    left_p.add(getData2);
	    left_p.add(button_getID);

	    /* �T�C�h�p�l���I��(�ݒ蓙�ŗ��p) */
	   
	    // �c�C�[�g�ꗗ�̕\��
	    //showTweets();
	    
	    contentPane.add(left_p, BorderLayout.WEST);

	}
	
	/**
	 * �c�C�[�g�ꗗ��\��
	 * @param tweets
	 * @param contentPane
	 */
	static void showTweets(){
		if(getData1.isSelected()){
			tweets = TweetAnalze.getTweets(text_id.getText(),1);
		} else {
			tweets = TweetAnalze.getTweets(text_id.getText(),2);
		}
		
		tweet_ps = new ArrayList<JPanel>();	// �c�C�[�g�p�l���̏�����
		tweets_p = new JPanel();			// �c�C�[�g�p�l���\����̏�����
		
		
	    tweets_p.setMinimumSize(new Dimension(778, 200));
	    tweets_p.setBackground(Color.LIGHT_GRAY);
	    BoxLayout boxlayout = new BoxLayout(tweets_p,BoxLayout.Y_AXIS);
	    tweets_p.setLayout(boxlayout);
	    
		for(int i = 0; i < tweets.size(); i++){
			// �P��c�C�[�g�p�p�l���̈ꎞ�錾
			JPanel tweet_p = new JPanel();
	    	//tweet_p.setPreferredSize(new Dimension(tweets_p.getWidth(), 100));
	    	tweet_p.setMaximumSize(new Dimension(778, 80));
	    	tweet_p.setMinimumSize(new Dimension(778, 80));
	    	
	    	tweet_p.setBackground(Color.WHITE);
	    	tweet_p.setBorder(new LineBorder(Color.GRAY, 1, true));
	    	tweet_p.setLayout(new BorderLayout());
	    	
	    	// �c�C�[�g�̓��t���̕\��
	    	String info_ac = Tweet.print_info_ac(tweets.get(i));
	    	JLabel date = new JLabel(info_ac);
	    	date.setPreferredSize(new Dimension(778,20));
	    	date.setForeground(Color.GRAY);
	    	tweet_p.add(date, BorderLayout.NORTH);
	    	
	    	// �c�C�[�g�̃e�L�X�g���e��\��
	    	JLabel twNo = new JLabel(String.valueOf(i+1) + ". ");
	    	twNo.setFont(new Font("Century", Font.BOLD, 18));
	    	twNo.setPreferredSize(new Dimension(40,40));
	    	tweet_p.add(twNo, BorderLayout.WEST);
	    	
	    	// �c�C�[�g�̃e�L�X�g���e��\��
	    	JLabel text = new JLabel(tweets.get(i).getText());
	    	text.setFont(new Font("�l�r �S�V�b�N", Font.BOLD, 18));
	    	text.setPreferredSize(new Dimension(600,40));
	  
	    	tweet_p.add(text, BorderLayout.CENTER);
	    	
	    	// �c�C�[�g�̃e�L�X�g���e��\��
	    	// �e����l�̉��Z
        	Emotion scores = TweetAnalze.analyzeTweet(tweets.get(i));
	    	JLabel emos = new JLabel(Emotion.printEmotions_lite(scores));
	    	emos.setPreferredSize(new Dimension(400,20));
	    	emos.setHorizontalAlignment(JLabel.RIGHT);
	    	tweet_p.add(emos, BorderLayout.SOUTH);
	    	
	    	/*
	    	// ��͗p�{�^�����L�q
	    	JButton button = new JButton("Analaze�I");
	    	tweet_p.add(button, BorderLayout.EAST);
	    	*/
	    	
	    	// �c�C�[�g�p�l�����X�g�ɒǉ�
	    	tweet_ps.add(tweet_p);
	    	tweets_p.add(tweet_ps.get(i));
		}
		contentPane.add(tweets_p, BorderLayout.EAST);
		
		// �c�C�[�g�S�̂̊���l���v�Z���AWEB��֑��M
        Emotion scoresAVE = TweetAnalze.analyzeTweets(tweets);
        Emotion.toJSonWEB(scoresAVE);
		
	    JScrollPane scrollpane = new JScrollPane(tweets_p);
        contentPane.add(scrollpane, BorderLayout.CENTER);
	}
	
	
	static void makeAccountPanel(){
		// ���ɃA�J�E���g�f�[�^���\������Ă�����폜����
		if(accout_data_p != null) left_p.remove(accout_data_p);
		
	    accout_data_p = new JPanel();
	    accout_data_p.setPreferredSize(new Dimension(150, 120));
	    accout_data_p.setBackground(Color.WHITE);
	    
	    JLabel label2 = new JLabel("-Account-");
	    
	    URL url = null;
	    try{
	    	String urlSTR = "http://www.paper-glasses.com/api/twipi/"
	    			+ text_id.getText() + "/bigger";
	        url = new URL(urlSTR);
	    }catch(MalformedURLException e){
	        e.printStackTrace();
	    }
	    
	    if (url != null){
	        ImageIcon icon = new ImageIcon(url);
	        label_id = new JLabel(icon);
	    }

	    
	    accout_data_p.add(label2);
	    accout_data_p.add(label_id);

	    left_p.add(accout_data_p);
	    contentPane.add(left_p, BorderLayout.WEST);
	}

	public void actionPerformed(ActionEvent e){
		makeAccountPanel();
		showTweets();
		
		label_id.setHorizontalTextPosition(JLabel.RIGHT);
		label_id.setVerticalTextPosition(JLabel.BOTTOM);
		label_id.setText(text_id.getText());
	}
	
}



