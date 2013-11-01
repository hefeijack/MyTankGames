/**
 * ̹����Ϸ1.0
 * 1,����̹��
 * 2,�ҵ�̹�˿������������ƶ�
 * 3,�ӵ���������
 * 4,���ҵ�̹�˻��е��˵�̹��ʱ������̹�˾���ʧ����ը��Ч����
 * 5,�ұ����б�ըЧ��
 * 6����ֹ����̹���ص��˶�
 * 	6.1 �������Ƿ���ײ�ĺ���д��EnemyTank��
 * 7�����Էֹ�
 * 	7.1 ��һ����ʼ��Panel������һ���յ�
 * 	7.2 ��˸Ч��
 * 8������������Ϸ��ʱ����ͣ�ͼ�����Ϸ
 * 	8.1 ���û������ͣʱ���ӵ����ٶȺ�̹�˵��ٶ���Ϊ0������̹�˵ķ���ֹͣ�仯
 * 9�����Լ�¼��ҳɼ�
 * 	9.1 ���ļ�������
 *  9.2 ��дһ����¼�࣬��ɶ���ҵļ�¼
 * 10��java��β��������ļ�
 */

package com.tank;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.*;

public class MyTankGame1 extends JFrame implements ActionListener {

	MyPanel mp = null;
	
	//����һ����ʼ���
	MyStartPanel msp = null;
	
	//��������Ҫ�Ĳ˵�
	JMenuBar jmb = null;
	//��ʼ��Ϸ
	JMenu jm1 = null;
	JMenuItem jmi1 = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame1 mtg = new MyTankGame1();
	}
	
	//���캯��
	public MyTankGame1(){
//		mp = new MyPanel();
//		
//		//����mp�߳�
//		Thread t = new Thread(mp);
//		t.start();
//		
//		this.add(mp);
//		
//		this.addKeyListener(mp);
		
		//�����˵����˵�ѡ��
		jmb = new JMenuBar();
		jm1 = new JMenu("��Ϸ��G��");
		//���ÿ�ݷ�ʽ
		jm1.setMnemonic('G');
		jmi1 = new JMenuItem("��ʼ����Ϸ��N��");
		
		//��jmi1������Ӧ
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		
		jm1.add(jmi1);
		jmb.add(jm1);
		
		msp = new MyStartPanel();
		Thread t = new Thread(msp);
		t.start();
		
		this.setJMenuBar(jmb);
		this.add(msp);
		
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//���û���ͬ�ĵ��������ͬ����Ӧ
		//����ս�����
		mp = new MyPanel();
		//����mp�߳�
		Thread t = new Thread(mp);
		t.start();
		//��ɾ���ɵ���壬Ȼ���ټ���һ���µ����
		this.remove(msp);
		
		this.add(mp);
		
		this.addKeyListener(mp);
		//��ʾ��ˢ�� JFrame
		this.setVisible(true);
	}

}

//����һ����ʾ����
class MyStartPanel extends JPanel implements Runnable{
	
	int times = 0;
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//��ʾ��Ϣ 
		if(times % 2 == 0){
			g.setColor(Color.yellow);
			//������Ϣ������ 
			Font myFont = new Font("������κ", Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("stage 1 : ", 150, 150);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			//����
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			//�ػ�
			times++;
			this.repaint();
		}
	}
}

//�ҵ����
class MyPanel extends JPanel implements KeyListener, Runnable{
	
	//����һ���ҵ�̹��
	Hero hero = null;	//�ڹ��캯���г�ʼ��
	
	// ������˵�̹����
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	
	//����ը�����
	Vector<Bomb> bombs = new Vector<Bomb>();
	
	int enSize = 3;
	
	//��������ͼƬ,����ͼƬ�������һ��ը��
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	
	
	//���캯��
	public MyPanel(){
		hero = new Hero(100, 100);
		
		//��ʼ�����˵�̹��
		for(int i=0; i<enSize; i++){
			EnemyTank et = new EnemyTank((i+1)*50, 0);		//����һ�����˵�̹��
			et.setColor(1);
			et.setDirect(2);
			
			//��MyPanel�ĵ��˵�̹�����������õ���̹��
			et.setEts(ets);
			
			//�������˵�̹��
			Thread t = new Thread(et);
			t.start();
			
			//������̹�����һ���ӵ�
			Shot s = new Shot(et.x+10, et.y+30, 2);
			//���ӵ����������
			et.ss.add(s);
			
			Thread t2 = new Thread(s);
			t2.start();
			
			ets.add(et);		//����
		}
		
		//��ʼ��ͼƬ
		image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo/bomb1.jpg"));
		image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo/bomb2.jpg"));
		image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo/bomb3.jpg"));
		
	}
	
	//������ʾ��Ϣ
	public void showInfo(Graphics g){
		//������ʾ��Ϣ̹�ˣ���̹�˲�����ս����
		this.drawTank(80, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 110, 350);
		
		this.drawTank(130, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 165, 350);
		
		//������ҵ��ܳɼ�
		g.setColor(Color.black);
		Font f = new Font("����", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("�����ܳɼ��� ", 420, 30);
		
		this.drawTank(420, 60, g, 0, 1);
		
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"", 450, 80);
		
	}
	
//	public MyPanel(int x, int y){
//		hero = new Hero(x, y);
//		this.x = x;
//		this.y = y;
//	}
	
	//��дpaint
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		//������ʾ��Ϣ̹�ˣ���̹�˲�����ս����
		this.showInfo(g);
		
		
		//�����Լ���̹��
		if(hero.isLive == true){
			this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 0);
		}
//		this.drawTank(g, 0, 1);
		
//		//����һ���ӵ�
//		if(hero.s != null && hero.s.isLive == true){
//			g.draw3DRect(hero.s.x, hero.s.y, 1, 1, false);
//		}
		
		//��ss��ȡ��ÿ���ӵ���������
		for(int i=0; i<this.hero.ss.size(); i++){
			
			Shot myShot = hero.ss.get(i);
			
			//����һ���ӵ�
			if(myShot != null && myShot.isLive == true){
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			}
			
			if(myShot.isLive == false){
				//��ss��ɾ�����ӵ�
				hero.ss.remove(myShot);
			}
			
		}
		
		//����ը��
		for(int i=0; i<bombs.size(); i++){
			//ȡ��ը��
			Bomb b = bombs.get(i);
			if(b.life > 6){
				g.drawImage(image1, b.x, b.y, 30, 30, this);	//this������ڸ�Panel�ϻ���ը��
			}else if(b.life > 3){
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			}else{
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			//��b������ֵ����
			b.lifeDown();
			//���ը������ֵ����0���ͰѸ�ը���� bombs ����ȥ��
			if(b.life == 0){
				bombs.remove(b);
			}
		}
		
		//�������˵�̹��
		for(int i=0; i<ets.size(); i++){
			EnemyTank et = ets.get(i);
			if(et.isLive == true){				
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 1);
				
				//�ٻ������˵��ӵ�
				for(int j=0; j<et.ss.size(); j++){
					//ȡ���ӵ�
					Shot enemyShot = et.ss.get(j);
					if(enemyShot.isLive == true){
						g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
					}else{
						//������˵�̹�������ʹ�Vector��ȥ��
						et.ss.remove(enemyShot);
					}
				}
				
			}
		}
		
	}
	
	//�жϵ��˵��ӵ��Ƿ�����ҵ�̹��
	public void hitMe(){
		//ȡ��ÿһ�����˵�̹��
		for(int i=0; i<this.ets.size(); i++){
			//ȡ��̹��
			EnemyTank et = ets.get(i);
			//ȡ��ÿһ���ӵ�
			for(int j=0; j<et.ss.size(); j++){
				//ȡ���ӵ�
				Shot enemyShot = et.ss.get(j);
				if(hero.isLive){
					this.hitTank(enemyShot, hero);
				}
			}
		}
	}
	
	//�ж��ҵ��ӵ��Ƿ���е���̹��
	public void hitEnemyTank(){
		//�ж��Ƿ���е��˵�̹��
		for(int i=0; i<hero.ss.size(); i++){
			//ȡ���ӵ�
			Shot myShot = hero.ss.get(i);
			//�ж��ӵ��Ƿ���Ч�����ţ�
			if(myShot.isLive == true){
				//ȡ��ÿ������̹�ˣ���֮ƥ��
				for(int j=0; j<ets.size(); j++){
					//ȡ��̹��
					EnemyTank et = ets.get(j);
					if(et.isLive == true){
						this.hitTank(myShot, et);
					}
				}
			}
		}
	}
	
	//дһ������ר���ж��ӵ��Ƿ���е��˵�̹��
	public void hitTank(Shot s, Tank et){
		//�жϸ�̹�˵ķ���
		switch(et.direct){
		case 0:
		case 2:
			if((s.x > et.x && s.x < et.x+20) && (s.y > et.y && s.y < et.y+30)){
				//���� 
				//�ӵ�����
				s.isLive = false;
				//����̹������
				et.isLive = false;
				Recorder.reduceEnNum();
				//�����ҵļ�¼
				Recorder.addEnNumRec();
				//����һ��ը��������Vector
				Bomb b = new Bomb(et.x, et.y);
				bombs.add(b);
			}
			break;
		case 1:
		case 3:
			if((s.x > et.x && s.x < et.x+30) && (s.y > et.y && s.y < et.y+20)){
				//����
				//�ӵ�����
				s.isLive = false;
				//����̹������
				et.isLive = false;
				Recorder.reduceEnNum();
				//�����ҵļ�¼
				Recorder.addEnNumRec();
				//����һ��ը��������Vector
				Bomb b = new Bomb(et.x, et.y);
				bombs.add(b);
			}
			break;
		}
	}
	
	//����̹�˵ĺ���
	public void drawTank(int x, int y, Graphics g, int direct, int type){
		//�ж���ʲô���͵�̹��
		switch(type){
		case 0:
			g.setColor(Color.BLUE);		//�ҵ�̹�˵���ɫ
			break;
		case 1:
			g.setColor(Color.yellow);	//���˵�̹�˵���ɫ
			break;
		}
		//�жϷ���
		switch(direct){
		case 0:		//����
			//�����ҵ�̹�ˣ������ٷ�װ�ɺ�����
			//1��������ߵľ���
//			g.drawRect(x, y, 5, 30);
			g.fill3DRect(x, y, 5, 30, false);
			//2,�����ұߵľ���
			g.fill3DRect(x+15, y, 5, 30, false);
			//3�������м����
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//4,����Բ��
			g.fillOval(x+5, y+10, 10, 10);
			//5,�����м�������
			g.drawLine(x+9, y+15, x+9, y);
			break;
		case 1: 	//����
			g.fill3DRect(x, y, 30, 5, false);
			//2,�����ұߵľ���
			g.fill3DRect(x, y+15, 30, 5, false);
			//3�������м����
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//4,����Բ��
			g.fillOval(x+10, y+5, 10, 10);
			//5,�����м�������
			g.drawLine(x+15, y+9, x+30, y+9);
			break;
		case 2:		//����
			g.fill3DRect(x, y, 5, 30, false);
			//2,�����ұߵľ���
			g.fill3DRect(x+15, y, 5, 30, false);
			//3�������м����
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//4,����Բ��
			g.fillOval(x+5, y+10, 10, 10);
			//5,�����м�������
			g.drawLine(x+9, y+15, x+9, y+30);
			break;
		case 3:		//����
			g.fill3DRect(x, y, 30, 5, false);
			//2,�����ұߵľ���
			g.fill3DRect(x, y+15, 30, 5, false);
			//3�������м����
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//4,����Բ��
			g.fillOval(x+10, y+5, 10, 10);
			//5,�����м�������
			g.drawLine(x+15, y+9, x, y+9);
			break;
		}
			
	}

	//�����´��� �� a��ʾ����w��ʾ���ϣ�s��ʾ���£�d��ʾ����
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_W){
			//�����ҵ�̹�˵ķ���		����
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(arg0.getKeyCode() == KeyEvent.VK_D){
			//����
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(arg0.getKeyCode() == KeyEvent.VK_S){
			//����
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(arg0.getKeyCode() == KeyEvent.VK_A){
			//����
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		//�ж��Ƿ��� J ��
		if(arg0.getKeyCode() == KeyEvent.VK_J){
			//����
			if(this.hero.ss.size() <= 4){
				this.hero.shotEnemy();
			} 
		}
		//����repaint����,�����»��ƽ���(����)
		this.repaint();
//		System.out.println("the repaint is done!");
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//ÿ��100msȥ�ػ��ӵ�
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			
			//�������жϵ��˵�̹���Ƿ�����ҵ�̹��
			this.hitMe();
			
			
//			//�ж��Ƿ���Ҫ��̹�˼����µ��ӵ�
//			for(int i=0; i<ets.size(); i++){
//				EnemyTank et = ets.get(i);
//				if(et.isLive == true){
//					if(et.ss.size() < 1){
//						
//						Shot s = null;
//						//û���ӵ�
//						//���
//						switch(et.direct){
//						case 0:
//							s = new Shot(et.x+9, et.y, 0);	//����һ���ӵ�
//							et.ss.add(s);		//���ӵ���������
//							break;
//						case 1:
//							s = new Shot(et.x+30, et.y+10, 1);
//							et.ss.add(s);
//							break;
//						case 2:
//							s = new Shot(et.x+9, et.y+30, 2);
//							et.ss.add(s);
//							break;
//						case 3:
//							s = new Shot(et.x, et.y+10, 3);
//							et.ss.add(s);
//							break;
//						}
//						
//						//�����ӵ��߳�
//						Thread t = new Thread(s);
//						t.start();
//						
//					}
//				}
//			}
			
			//�ػ�
			this.repaint();
		}
	}
	
}

