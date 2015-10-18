package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.objs.Item;

public class GameScreen implements Screen {
	private DropGame game;
	private Array<Texture> dropImages;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Vector3 touchpos;
	private Array<Item> raindrops;
	private long lastDropTime;
	private BitmapFont font;
	int hit=0;
	int miss=0;
	long score=0;
	
	
	
	private void spawnHorlicksDrop()
	{
		int htype=MathUtils.random(1,7);
		Item itm=new Item();
		
		Rectangle horlixdrop=new Rectangle();
		horlixdrop.x=MathUtils.random(0, 800-64);
		horlixdrop.y=480;
		horlixdrop.width=64;
		horlixdrop.height=64;
		itm.setDims(horlixdrop);
		itm.setImg(dropImages.get(htype-1));
		itm.setPoints(htype*5);
		raindrops.add(itm);
		lastDropTime=TimeUtils.nanoTime();
	}
	
	
	/*private void spawnRaindrop()
	{
		Rectangle raindrop=new Rectangle();
		raindrop.x=MathUtils.random(0, 800-64);
		raindrop.y=480;
		raindrop.width=64;
		raindrop.height=64;
		raindrops.add(raindrop);
		lastDropTime=TimeUtils.nanoTime();
	}*/
	
	public GameScreen(DropGame game) {
		// TODO Auto-generated constructor stub
		
		this.game=game;
		font=new BitmapFont();
		touchpos=new Vector3();
		dropImages=new Array<Texture>();
		dropImages.add(new Texture(Gdx.files.internal("1.png")));
		dropImages.add(new Texture(Gdx.files.internal("2.png")));
		dropImages.add(new Texture(Gdx.files.internal("3.png")));
		dropImages.add(new Texture(Gdx.files.internal("4.png")));
		dropImages.add(new Texture(Gdx.files.internal("5.png")));
		dropImages.add(new Texture(Gdx.files.internal("6.png")));
		dropImages.add(new Texture(Gdx.files.internal("7.png")));
		
		bucketImage=new Texture(Gdx.files.internal("milk.png"));
		dropSound= Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
		rainMusic=Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		camera=new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		bucket=new Rectangle();
		bucket.x=800/2-64/2;
		bucket.y=20;
		bucket.width=64;
		bucket.height=64;
		raindrops=new Array<Item>();
		spawnHorlicksDrop();
	}
	
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		rainMusic.setLooping(true);
		rainMusic.play();
		
	}

	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		font.draw(game.batch, "Score:"+score, 20, 480-10);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for(Item rainDrop:raindrops)
		{
			//game.batch.draw(rainDrop.getImg(), rainDrop.getDims().x,rainDrop.getDims().y,64,64);
			game.batch.draw(rainDrop.getImg(), rainDrop.getDims().x,rainDrop.getDims().y);
			
		}
		game.batch.end();
		if(Gdx.input.isTouched())
		{
			
			touchpos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchpos);
			bucket.x=touchpos.x-64/2;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 400*Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 400*Gdx.graphics.getDeltaTime();
		if(bucket.x<0)
			bucket.x=0;
		if(bucket.x>800-64)
			bucket.x=800-64;
		if(TimeUtils.nanoTime()-lastDropTime>=1000000000)
			spawnHorlicksDrop();
		Iterator<Item> dropsI=raindrops.iterator();
		while(dropsI.hasNext())
		{
			Item raindrop=dropsI.next();
			raindrop.getDims().y -= 200*Gdx.graphics.getDeltaTime();
			if(raindrop.getDims().y+64<0)
			{
				miss++;
				dropsI.remove();
				//score=(hit*10-miss*5);
				score-=raindrop.getPoints();
				if(score<0){score=0;hit=0;miss=0;}
			}
			if(raindrop.getDims().overlaps(bucket))
			{
				hit++;
				dropSound.play();
				dropsI.remove();
				score+=raindrop.getPoints();
				//score=(hit*10-miss*5);
				if(score<0){score=0;hit=0;miss=0;}
			}
		}
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		for(Texture t:dropImages)
		t.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		
	}

}
