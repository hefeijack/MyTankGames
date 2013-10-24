package com.tank;

import java.util.Vector;

//炸弹类
class Bomb{
	//定义炸弹的坐标
	int x, y;
	//炸弹的生命
	int life = 9;
	boolean isLive = true;
	
	public Bomb(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	//减少生命值
	public void lifeDown(){
		if(life > 0){
			life--;
		}else{
			this.isLive = false;
		}
	}
	
}

//子弹类
class Shot implements Runnable {
	int x;
	int y;
	int direct;
	int speed = 4;
	//是否还活着
	boolean isLive = true;
	
	public Shot(int x, int y, int direct){
		this.x = x;
		this.y = y;
		this.direct = direct;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			
			try{
				Thread.sleep(50);
			}catch(Exception e){
				
			}
			
			switch(direct){
			case 0: 	//子弹向上
				y -= speed;
				break;
			case 1:
				x += speed;
				break;
			case 2:
				y += speed;
				break;
			case 3:
				x -= speed;
				break;
			}
//			System.out.println("x = " + x + " , y = " + y);
			//子弹何时死亡?????
			
			//判断该子弹是否碰到边缘
			if( x < 0 || x>400 || y<0 || y>300){
				this.isLive = false;
				break;
			}
			
		}
	}
	
}

//坦克类
class Tank{
	int x = 0;	//表示坦克的横坐标
	int y = 0;	//表示坦克的纵坐标
	//坦克方向： 0表示上，1表示右，2表示下，3表示左
	int direct = 0;		
	//设置坦克的速度
	int speed = 2;
	//设置坦克的颜色
	int color;
	
	boolean isLive = true;
	
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
}

//敌人的坦克,把敌人的坦克做成线程类
class EnemyTank extends Tank implements Runnable{

	int times = 0;
	
	//定义一个向量，可以存放敌人的子弹
	Vector<Shot> ss = new Vector<Shot>();
	//敌人添加子弹，应当在刚刚创建坦克
	
	public EnemyTank(int x, int y){
		super(x, y);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			
			switch(this.direct){
			case 0:
				//说明坦克正在向上走
				for(int i=0; i<30; i++){
					if(y >= 0){
						y -= speed;
					}
					try{
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for(int i=0; i<30; i++){
					if(x <= 300){
						x += speed;
					}
					try{
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for(int i=0; i<30; i++){
					if(y <= 200){
						y += speed;
					}
					try{
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for(int i=0; i<30; i++){
					if(x >= 0){
						x -= speed;
					}
					try{
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			}
			
			this.times ++;
			if(times%2 == 0){
				if(isLive == true){
					if(ss.size() < 5){
						Shot s = null;
						switch(direct){
						case 0:
							s = new Shot(x+9, y, 0);	//创建一颗子弹
							ss.add(s);		//把子弹加入向量
							break;
						case 1:
							s = new Shot(x+30, y+10, 1);
							ss.add(s);
							break;
						case 2:
							s = new Shot(x+9, y+30, 2);
							ss.add(s);
							break;
						case 3:
							s = new Shot(x, y+10, 3);
							ss.add(s);
							break;
						}
									
						//启动子弹线程
						Thread t = new Thread(s);
						t.start();
									
					}
				}
			}
			
			//让坦克随机产生一个新的方向
			this.direct = (int)(Math.random()*4);
			
			//判断敌人坦克是否死亡
			if(this.isLive == false){
				//让坦克死亡后，退出线程
				break;
			}
			
			
			
		}
	}
}

//我的坦克
class Hero extends Tank{
	
	//子弹
//	Shot s = null;
	Vector<Shot> ss = new Vector<Shot>();
	Shot s = null;
	
	public Hero(int x, int y){
		super(x, y);
		
	}
	
	//坦克开火
	public void shotEnemy(){
				
		switch(this.direct){
		case 0:
			s = new Shot(x+9, y, 0);	//创建一颗子弹
			ss.add(s);		//把子弹加入向量
			break;
		case 1:
			s = new Shot(x+30, y+10, 1);
			ss.add(s);
			break;
		case 2:
			s = new Shot(x+9, y+30, 2);
			ss.add(s);
			break;
		case 3:
			s = new Shot(x, y+10, 3);
			ss.add(s);
			break;
		}
		
		//启动子弹线程
		Thread t = new Thread(s);
		t.start();
		
	}
	
	//坦克向上移动
	public void moveUp(){
		this.y -= this.speed;
	}
	//坦克向下移动
	public void moveDown(){
		this.y += this.speed;
	}
	//坦克向左移动
	public void moveLeft(){
		this.x -= this.speed;
	}
	//坦克向右移动
	public void moveRight(){
		this.x += this.speed;
	}
	
}