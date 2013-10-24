/**
 * 坦克游戏1.0
 * 1,画出坦克
 * 2,我的坦克可以上下左右移动
 * 3,子弹可以连发
 * 4,当我的坦克击中敌人的坦克时，敌人坦克就消失（爆炸的效果）
 */

package com.tank;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.*;

public class MyTankGame1 extends JFrame {

	MyPanel mp = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame1 mtg = new MyTankGame1();
	}
	
	//构造函数
	public MyTankGame1(){
		mp = new MyPanel();
		
		//启动mp线程
		Thread t = new Thread(mp);
		t.start();
		
		this.add(mp);
		
		this.addKeyListener(mp);
	
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}

//我的面板
class MyPanel extends JPanel implements KeyListener, Runnable{
	
	//定义一个我的坦克
	Hero hero = null;	//在构造函数中初始化
	
	// 定义敌人的坦克组
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	
	//定义炸弹组合
	Vector<Bomb> bombs = new Vector<Bomb>();
	
	int enSize = 3;
	
	//定义三张图片,三张图片才能组成一颗炸弹
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	
	
	//构造函数
	public MyPanel(){
		hero = new Hero(100, 100);
		
		//初始化敌人的坦克
		for(int i=0; i<enSize; i++){
			EnemyTank et = new EnemyTank((i+1)*50, 0);		//创建一辆敌人的坦克
			et.setColor(1);
			et.setDirect(2);
			
			//启动敌人的坦克
			Thread t = new Thread(et);
			t.start();
			
			//给敌人坦克添加一颗子弹
			Shot s = new Shot(et.x+10, et.y+30, 2);
			//把子弹加入给敌人
			et.ss.add(s);
			
			Thread t2 = new Thread(s);
			t2.start();
			
			ets.add(et);		//加入
		}
		
		//初始化图片
		image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo/bomb1.jpg"));
		image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo/bomb2.jpg"));
		image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo/bomb3.jpg"));
		
	}
	
//	public MyPanel(int x, int y){
//		hero = new Hero(x, y);
//		this.x = x;
//		this.y = y;
//	}
	
	//重写paint
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		//画出自己的坦克
		if(hero.isLive == true){
			this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 0);
		}
//		this.drawTank(g, 0, 1);
		
//		//画出一颗子弹
//		if(hero.s != null && hero.s.isLive == true){
//			g.draw3DRect(hero.s.x, hero.s.y, 1, 1, false);
//		}
		
		//从ss中取出每颗子弹，并画出
		for(int i=0; i<this.hero.ss.size(); i++){
			
			Shot myShot = hero.ss.get(i);
			
			//画出一颗子弹
			if(myShot != null && myShot.isLive == true){
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			}
			
			if(myShot.isLive == false){
				//从ss中删除该子弹
				hero.ss.remove(myShot);
			}
			
		}
		
		//画出炸弹
		for(int i=0; i<bombs.size(); i++){
			//取出炸弹
			Bomb b = bombs.get(i);
			if(b.life > 6){
				g.drawImage(image1, b.x, b.y, 30, 30, this);	//this代表就在该Panel上画出炸弹
			}else if(b.life > 3){
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			}else{
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			//让b的生命值减少
			b.lifeDown();
			//如果炸弹生命值等于0，就把该炸弹从 bombs 向量去掉
			if(b.life == 0){
				bombs.remove(b);
			}
		}
		
		//画出敌人的坦克
		for(int i=0; i<ets.size(); i++){
			EnemyTank et = ets.get(i);
			if(et.isLive == true){				
				this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 1);
				
				//再画出敌人的子弹
				for(int j=0; j<et.ss.size(); j++){
					//取出子弹
					Shot enemyShot = et.ss.get(j);
					if(enemyShot.isLive == true){
						g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
					}else{
						//如果敌人的坦克死亡就从Vector中去掉
						et.ss.remove(enemyShot);
					}
				}
				
			}
		}
		
	}
	
	//判断敌人的子弹是否击中我的坦克
	public void hitMe(){
		//取出每一个敌人的坦克
		for(int i=0; i<this.ets.size(); i++){
			//取出坦克
			EnemyTank et = ets.get(i);
			//取出每一颗子弹
			for(int j=0; j<et.ss.size(); j++){
				//取出子弹
				Shot enemyShot = et.ss.get(j);
				this.hitTank(enemyShot, hero);
			}
		}
	}
	
	//判断我的子弹是否击中敌人坦克
	public void hitEnemyTank(){
		//判断是否击中敌人的坦克
		for(int i=0; i<hero.ss.size(); i++){
			//取出子弹
			Shot myShot = hero.ss.get(i);
			//判断子弹是否有效（活着）
			if(myShot.isLive == true){
				//取出每个敌人坦克，与之匹配
				for(int j=0; j<ets.size(); j++){
					//取出坦克
					EnemyTank et = ets.get(j);
					if(et.isLive == true){
						this.hitTank(myShot, et);
					}
				}
			}
		}
	}
	
	//写一个函数专门判断子弹是否击中敌人的坦克
	public void hitTank(Shot s, Tank et){
		//判断该坦克的方向
		switch(et.direct){
		case 0:
		case 2:
			if((s.x > et.x && s.x < et.x+20) && (s.y > et.y && s.y < et.y+30)){
				//击中 
				//子弹死亡
				s.isLive = false;
				//敌人坦克死亡
				et.isLive = false;
				//创建一颗炸弹，放入Vector
				Bomb b = new Bomb(et.x, et.y);
				bombs.add(b);
			}
			break;
		case 1:
		case 3:
			if((s.x > et.x && s.x < et.x+30) && (s.y > et.y && s.y < et.y+20)){
				//击中
				//子弹死亡
				s.isLive = false;
				//敌人坦克死亡
				et.isLive = false;
				//创建一颗炸弹，放入Vector
				Bomb b = new Bomb(et.x, et.y);
				bombs.add(b);
			}
			break;
		}
	}
	
	//画出坦克的函数
	public void drawTank(int x, int y, Graphics g, int direct, int type){
		//判断是什么类型的坦克
		switch(type){
		case 0:
			g.setColor(Color.BLUE);		//我的坦克的颜色
			break;
		case 1:
			g.setColor(Color.yellow);	//敌人的坦克的颜色
			break;
		}
		//判断方向
		switch(direct){
		case 0:		//向上
			//画出我的坦克（待会再封装成函数）
			//1，画出左边的矩形
//			g.drawRect(x, y, 5, 30);
			g.fill3DRect(x, y, 5, 30, false);
			//2,画出右边的矩形
			g.fill3DRect(x+15, y, 5, 30, false);
			//3，画出中间矩形
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//4,画出圆形
			g.fillOval(x+5, y+10, 10, 10);
			//5,画出中间那条线
			g.drawLine(x+9, y+15, x+9, y);
			break;
		case 1: 	//向右
			g.fill3DRect(x, y, 30, 5, false);
			//2,画出右边的矩形
			g.fill3DRect(x, y+15, 30, 5, false);
			//3，画出中间矩形
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//4,画出圆形
			g.fillOval(x+10, y+5, 10, 10);
			//5,画出中间那条线
			g.drawLine(x+15, y+9, x+30, y+9);
			break;
		case 2:		//向下
			g.fill3DRect(x, y, 5, 30, false);
			//2,画出右边的矩形
			g.fill3DRect(x+15, y, 5, 30, false);
			//3，画出中间矩形
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//4,画出圆形
			g.fillOval(x+5, y+10, 10, 10);
			//5,画出中间那条线
			g.drawLine(x+9, y+15, x+9, y+30);
			break;
		case 3:		//向左
			g.fill3DRect(x, y, 30, 5, false);
			//2,画出右边的矩形
			g.fill3DRect(x, y+15, 30, 5, false);
			//3，画出中间矩形
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//4,画出圆形
			g.fillOval(x+10, y+5, 10, 10);
			//5,画出中间那条线
			g.drawLine(x+15, y+9, x, y+9);
			break;
		}
			
	}

	//键按下处理 ： a表示向左，w表示向上，s表示向下，d表示向右
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == KeyEvent.VK_W){
			//设置我的坦克的方向		向上
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(arg0.getKeyCode() == KeyEvent.VK_D){
			//向右
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(arg0.getKeyCode() == KeyEvent.VK_S){
			//向下
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(arg0.getKeyCode() == KeyEvent.VK_A){
			//向左
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		//判断是否按下 J 键
		if(arg0.getKeyCode() == KeyEvent.VK_J){
			//开火
			if(this.hero.ss.size() <= 4){
				this.hero.shotEnemy();
			} 
		}
		//调用repaint函数,来重新绘制界面(必须)
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
		//每隔100ms去重绘子弹
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			
			//函数，判断敌人的坦克是否击中我的坦克
			this.hitMe();
			
			
//			//判断是否需要给坦克加入新的子弹
//			for(int i=0; i<ets.size(); i++){
//				EnemyTank et = ets.get(i);
//				if(et.isLive == true){
//					if(et.ss.size() < 1){
//						
//						Shot s = null;
//						//没有子弹
//						//添加
//						switch(et.direct){
//						case 0:
//							s = new Shot(et.x+9, et.y, 0);	//创建一颗子弹
//							et.ss.add(s);		//把子弹加入向量
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
//						//启动子弹线程
//						Thread t = new Thread(s);
//						t.start();
//						
//					}
//				}
//			}
			
			//重绘
			this.repaint();
		}
	}
	
}

