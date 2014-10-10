package pong;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Rectangle;
import org.lwjgl.util.vector.Vector2f;

public class Game extends BasicGame {
	
	static int SCREEN_WIDTH = 800;
	static int SCREEN_HEIGHT = 600;
	static boolean FULL_SCREEN = false;
	static String TITLE = "Pong";
	static int FPS_LIMIT = 60;
	
	private Circle ball;
	private RoundedRectangle paddleLeft;
	private RoundedRectangle paddleRight;
	private Rectangle foreground;
	
	private int BALL_SPEED = 190;
	private int BALL_RADIUS = 7;
	private int PADDLE_SPEED = 400;
	private int PADDLE_WIDTH = 10;
	private int PADDLE_HEIGHT = 110;
	private int CORNER_ANGLE = 4;
	
	private float yL;
	private float yR;
	private Vector2f ballVelocity;
	
	private boolean hitting;
	private boolean moving;
	private boolean start;
	
	private int scoreLeft;
	private int scoreRight;
	
	private Input input;
	
	private AppGameContainer app;
	
	
	
	public Game() {
		super(TITLE);
	}
	
	
	
	public void init(GameContainer container) throws SlickException {
		if (container instanceof AppGameContainer)
			app = (AppGameContainer) container;
		
		input = container.getInput();
		
		ball = new Circle(400, 300, BALL_RADIUS);
		paddleLeft = new RoundedRectangle(5, 350 - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT, CORNER_ANGLE);
		paddleRight = new RoundedRectangle(SCREEN_WIDTH - 15, 350 - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT, CORNER_ANGLE);
		foreground = new Rectangle(25, 15, SCREEN_WIDTH - 50, SCREEN_HEIGHT - 30);
		ballVelocity = new Vector2f(-3, 1);
		scoreLeft = 0;
		scoreRight = 0;
		start = true;
	}
	
	
	
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setBackground(Color.gray);
		g.setColor(Color.lightGray);
		g.fill(foreground);
		g.setColor(Color.white);
		if (hitting) 
			g.fill(ball);
		else
			g.draw(ball);
		
		g.fill(paddleLeft);
		g.fill(paddleRight);
		
		g.drawString("Score: " + scoreLeft, 5, 575);
		g.drawString("Score: " + scoreRight, 705, 575);
	}
	
	
	
	public void update(GameContainer container, int delta) throws SlickException {
		float ball_distance = BALL_SPEED * ((float)delta/1000);
		float paddle_distance = PADDLE_SPEED * ((float)delta/1000);
		yL = paddleLeft.getY();
		yR = paddleRight.getY();
		
		if (input.isKeyDown(Input.KEY_UP) && (yL >= 0)) {
	        paddleLeft.setY(yL - paddle_distance);
	        moving = true;
	        start = false;
		}
		if (input.isKeyDown(Input.KEY_DOWN) && (yL < (600 - PADDLE_HEIGHT))) {
	        paddleLeft.setY(yL + paddle_distance);
	        moving = true;
	        start = false;
		}
		if (input.isKeyDown(Input.KEY_W) && (yR >= 0)) {
	        paddleRight.setY(yR - paddle_distance);
	        moving = true;
	        start = false;
		}
		if (input.isKeyDown(Input.KEY_S) && (yR < (600 - PADDLE_HEIGHT))) {
	        paddleRight.setY(yR + paddle_distance);
	        moving = true;
	        start = false;
		}
			
		if (ball.getMinX() <= 0) {
			scoreRight++;
			ball.setLocation(400, 300);
			start = true;
		} else if (ball.getMaxX() >= SCREEN_WIDTH) {
			scoreLeft++;
			ball.setLocation(400, 300);
			start = true;
		}
		
		if (ball.intersects(paddleLeft) || ball.intersects(paddleRight)) {
			ballVelocity.setX(-ballVelocity.getX());
			hitting = true;
		} else {
			hitting = false;
		}
		
		if (ball.getMinY() <= 0) 
			ballVelocity.setY(-ballVelocity.getY()); 
		else if (ball.getMaxY() >= SCREEN_HEIGHT) 
			ballVelocity.setY(-ballVelocity.getY());
		
		if (!start && moving)
			ball.setLocation(ball.getX() + ballVelocity.getX() * ball_distance, ball.getY() + ballVelocity.getY() * ball_distance);
	}
	
	
	
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE)
			System.exit(0);
		
		if (key == Input.KEY_F1) {
			if (app != null) {
				try {
					app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, FULL_SCREEN);
					app.reinit();
				} catch (Exception e) {
					Log.error(e);
				}
			}
		}
	}
	
	
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new Game());
			container.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, FULL_SCREEN);
			container.setTargetFrameRate(FPS_LIMIT);
			container.setVSync(true);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
}
