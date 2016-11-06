package com.mygdx.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.graphics.shapes.*;
import com.mygdx.graphics.shapes.g3djmodel.G3DJModelLoader;
import com.mygdx.graphics.shapes.g3djmodel.MeshModel;

import com.mygdx.graphics.*;
import com.sun.org.apache.xpath.internal.operations.Mod;

public class FlappyGame extends ApplicationAdapter implements InputProcessor {

	Shader shader;

	private float angle;

	private Camera cam;
	//private Camera topCam;

	private float fov = 120.0f;

	MeshModel model3D;

	//private Texture tex;
	private Texture world;
	private Texture menu;
	private Texture sky;
	private Texture over;

	private Texture stageImage;

	private Random rand;

	private static Cell[] cells;

	int stages;

	Plane plane;

	boolean dragging;

	int score = 0;
	boolean gameOver = true;

	float movingSpeed = 3.0f;

	boolean begining = true;

	@Override
	public void create () {


		Gdx.input.setInputProcessor(this);

		//DisplayMode disp = Gdx.graphics.getDesktopDisplayMode();
		//Gdx.graphics.setDisplayMode(disp.width, disp.height, true);

		shader = new Shader();

		//For Android
		/*sky = new Texture(Gdx.files.internal("textures/testsky.png"));
		world = new Texture(Gdx.files.internal("textures/testwall.png"));
		menu = new Texture(Gdx.files.internal("textures/menu.png"));*/

		//For Desktop App
		sky = new Texture(Gdx.files.internal("core/assets/textures/testsky.png"));
		world = new Texture(Gdx.files.internal("core/assets/textures/testwall.png"));
		menu = new Texture(Gdx.files.internal("core/assets/textures/menu.png"));
		over = new Texture(Gdx.files.internal("core/assets/textures/planeOver.png"));

		model3D = G3DJModelLoader.loadG3DJFromFile("/plane/plane.g3dj", true);

		BoxGraphic.create();
		SphereGraphic.create();
		plane = new Plane();

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		cam.look(new Point3D(0f, 4f, -4f), new Point3D(0,4,0), new Vector3D(0,1,0));

		//topCam = new Camera();
		//orthoCam.orthographicProjection(-5, 5, -5, 5, 3.0f, 100);
		//topCam.perspectiveProjection(30.0f, 1, 3, 100);

		//try this way to create a texture image
		/*Pixmap pm = new Pixmap(128, 128, Format.RGBA8888);
		for(int i = 0; i < pm.getWidth(); i++)
		{
			for(int j = 0; j < pm.getWidth(); j++)
			{
				pm.drawPixel(i, j, rand.nextInt());
			}
		}
		tex = new Texture(pm);*/

		dragging = false;

		//stages = 10;
		stages = 10;

		rand = new Random();

		Cave cave = new Cave();
		cells = cave.getCells();
		
		for(int i = 2; i < stages; i++){
			int randNR = rand.nextInt(4);
			if(randNR == 0){
				cells[i].setCell(true,false,false,false);
			}
			else if(randNR == 1){
				cells[i].setCell(false,true,false,false);
			}
			else if(randNR == 2){
				cells[i].setCell(false,false,true,false);
			}
			else{
				cells[i].setCell(false,false,false,true);
			}
		}
		cells[stages-1].setCell(true,true,true,true);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	private void input()
	{
	}

	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		if(!gameOver) {
			angle += 180.0f * deltaTime;
			cam.slide(0, 0, -movingSpeed * deltaTime);
			plane.setMove(0, 0, -movingSpeed * deltaTime);
			//updatePlane(0, 0, -3.0f * deltaTime);


			if (plane.getMove().z > ((stages-2) * 10)) {
				plane.setMove(0, 0, -movingSpeed * deltaTime);
			}
			if (plane.getMove().z > ((stages*10)-5)) {
				score += 1;
				movingSpeed += 1;

				System.out.println("Score: " + score);
				create();
			}

			if (Gdx.input.isKeyPressed(Input.Keys.A) || plane.isMovingLeft()) {
				//cam.slide(-3.0f * deltaTime, 0, 0);
				if (plane.setMove(-movingSpeed * deltaTime, 0, 0)) {
					cam.slide(-movingSpeed * deltaTime, 0, 0);
				}
				//plane.setRotationSide(-5);
				//updatePlane(-3.0f * deltaTime, 0, 0);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D) || plane.isMovingRight()) {
				//cam.slide(3.0f * deltaTime, 0, 0);
				if (plane.setMove(movingSpeed * deltaTime, 0, 0)) {
					cam.slide(movingSpeed * deltaTime, 0, 0);
				}
				//plane.setRotationSide(5);
				//updatePlane(3.0f * deltaTime, 0, 0);
			}
			if (!Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
				plane.resetRotationSide();
			}

			if (Gdx.input.isKeyPressed(Input.Keys.W) || plane.isMovingUp()) {
				//cam.slide(0, 0, -3.0f * deltaTime);
				//updatePlane(0, 0, -3.0f * deltaTime);
				//cam.slide(0, 3.0f * deltaTime, 0);
				if (plane.setMove(0, movingSpeed * deltaTime, 0)) {
					cam.slide(0, movingSpeed * deltaTime, 0);
				}
				//plane.setRotationUpDown(-5);
				//updatePlane(0, -3.0f * deltaTime, 0);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.S) || plane.isMovingDown()) {
				//cam.slide(0, 0, 3.0f * deltaTime);
				//updatePlane(0, 0, 3.0f * deltaTime);
				//cam.slide(0, -3.0f * deltaTime, 0);
				if (plane.setMove(0, -movingSpeed * deltaTime, 0)) {
					cam.slide(0, -movingSpeed * deltaTime, 0);
				}
				//plane.setRotationUpDown(5);
				//updatePlane(0, 3.0f * deltaTime, 0);
			}
			if (!Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
				plane.resetRotationUpDown();
			}

			/*if (Gdx.input.isKeyPressed(Input.Keys.R)) {
				if(plane.setMove(0, -3.0f * deltaTime, 0)){
					cam.slide(0, -3.0f * deltaTime, 0);
				}
				//updatePlane(0, -3.0f * deltaTime, 0);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.F)) {
				if(plane.setMove(0, 3.0f * deltaTime, 0)){
					cam.slide(0, 3.0f * deltaTime, 0);
				}
				//updatePlane(0, 3.0f * deltaTime, 0);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				cam.yaw(-90.0f * deltaTime);
				//cam.rotateY(90.0f * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				cam.yaw(90.0f * deltaTime);
				//cam.rotateY(-90.0f * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				cam.pitch(90.0f * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				cam.pitch(-90.0f * deltaTime);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
				cam.roll(-90.0f * deltaTime);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.E)) {
				cam.roll(90.0f * deltaTime);
			}*/

			/*if(Gdx.input.isKeyPressed(Input.Keys.T)) {
				fov -= 30.0f * deltaTime;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.G)) {
				fov += 30.0f * deltaTime;
			}*/

			int cell = (int)(plane.getMove().z/10) + 1;
			//System.out.println("cell is: " + cell);
			if(cell < stages ){

				if(plane.wallCollision(cells[cell])){
					this.gameOver = true;
					create();
				}
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			//Gdx.graphics.setDisplayMode(500, 500, false);
			Gdx.app.exit();
		}


		if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || dragging) {
			score = 0;
			gameOver = false;
			begining = false;
		}

		/*else{
			Cell bla = new Cell();
			bla.setCell(false,false,false,false);
			plane.wallCollision(bla);
		}*/
		//HAX

		//do all updates to the game
	}

	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		/*
		Gdx.gl.glEnable(GL20.GL_BLEND);
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		*/


		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 100.0f);
		shader.setViewMatrix(cam.getViewMatrix());
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);

		/*else
		{
			Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
			topCam.look(new Point3D(cam.eye.x, 20.0f, cam.eye.z), cam.eye, new Vector3D(0,0,-1));
			//orthoCam.look(new Point3D(7.0f, 40.0f, -7.0f), new Point3D(7.0f, 0.0f, -7.0f), new Vector3D(0,0,-1));
			topCam.perspectiveProjection(30.0f, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 3, 100);
			shader.setViewMatrix(topCam.getViewMatrix());
			shader.setProjectionMatrix(topCam.getProjectionMatrix());
			shader.setEyePosition(topCam.eye.x, topCam.eye.y, topCam.eye.z, 1.0f);
		}*/


		//BoxGraphic.drawOutlineCube();
		//SphereGraphic.drawSolidSphere();
		//SphereGraphic.drawOutlineSphere();


		ModelMatrix.main.loadIdentityMatrix();

		//ModelMatrix.main.addRotationZ(angle);

		//float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
		//float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);

		//shader.setLightPosition(0.0f + c * 3.0f, 5.0f, 0.0f + s * 3.0f, 1.0f);
		shader.setLightPosition(cam.eye.x,cam.eye.y-2,cam.eye.z+1,1);
		//shader.setLightPosition(0, cam.eye.y, cam.eye.z, 1.0f);

		//lights that follow plane
		shader.setLightPositionPlane(cam.eye.x,cam.eye.y+2,cam.eye.z+1,1);

		//float s2 = Math.abs((float)Math.sin((angle / 1.312) * Math.PI / 180.0));
		//float c2 = Math.abs((float)Math.cos((angle / 1.312) * Math.PI / 180.0));

		//shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
		shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);
		//shader.setSpotDirection(0, 0, 0, 0.0f);

		shader.setSpotExponent(0.0f);
		shader.setConstantAttenuation(1f);
		shader.setLinearAttenuation(0.00f);
		shader.setQuadraticAttenuation(0.00f);

		//shader.setLightColor(s2, 0.4f, c2, 1.0f);
		shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);

		shader.setGlobalAmbient(0.3f, 0.3f, 0.3f, 1);

		//shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1f, 1f, 1f, 1.0f);
		//shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		shader.setShininess(150.0f);

		if(!gameOver){
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0.0f, 4.0f, 0.0f);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			ModelMatrix.main.pushMatrix();
			Point3D planeMove = plane.getMove();
			ModelMatrix.main.addTranslation(planeMove.x, planeMove.y, planeMove.z);
			ModelMatrix.main.addRotationZ(plane.getRotationSide());
			ModelMatrix.main.addRotationX(plane.getRotationUpDown());
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			model3D.draw(shader, null);
			ModelMatrix.main.popMatrix();

			ModelMatrix.main.popMatrix();

			drawCourse();
		}

		else {
			makeMenu();
		}


	}

	@Override
	public void render () {

		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();
	}

	private void makeMenu(){

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0f, 5f, 0f);
		ModelMatrix.main.addScale(15f, 15f, 1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		if(begining){
			BoxGraphic.drawSolidCube(shader, menu);
		}
		else{
			BoxGraphic.drawSolidCube(shader, over);
		}
		ModelMatrix.main.popMatrix();

		String gameScore;
		if(score > 10){
			gameScore = "stageUN.png";
		}
		else{
			gameScore = "stage"+score+".png";
		}

		//For Android App
		//stageImage = new Texture(Gdx.files.internal("textures/"+gameScore));

		//For Desktop App
		stageImage = new Texture(Gdx.files.internal("core/assets/textures/"+gameScore));

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(-4f, 4.5f, -0.1f);
		ModelMatrix.main.addScale(1f, 2f, 1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, stageImage);
		ModelMatrix.main.popMatrix();
	}

	private void drawCourse()
	{
		ModelMatrix.main.addTranslation(0.0f, 0.0f, -15.0f);

		for(int i = 0; i < stages; i++){

			ModelMatrix.main.addTranslation(0f, 0f, 10f);

			//bottom
			ModelMatrix.main.pushMatrix();


			ModelMatrix.main.addScale(10f, 1f, 10f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube(shader, world);
			ModelMatrix.main.popMatrix();

			//left
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(5f, 5f, 0f);
			ModelMatrix.main.addScale(1f, 10f, 10f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube(shader, world);
			ModelMatrix.main.popMatrix();

			//right
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(-5f, 5f, 0f);
			ModelMatrix.main.addScale(1f, 10f, 10f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube(shader, world);
			ModelMatrix.main.popMatrix();

			//top
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0f, 10f, 0f);
			ModelMatrix.main.addScale(10f, 1f, 10f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube(shader, sky);
			ModelMatrix.main.popMatrix();


			if(!cells[i].isUpperRight()){
				//upper right
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(2.5f, 7.5f, 0f);
				ModelMatrix.main.addScale(5f, 5f, 1f);
				//ModelMatrix.main.addScale(1f, 1f, 1f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube(shader, world);
				ModelMatrix.main.popMatrix();
			}
			if(!cells[i].isUpperLeft()){
				//upper left
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(-2.5f, 7.5f, 0f);
				ModelMatrix.main.addScale(5f, 5f, 1f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube(shader, world);
				ModelMatrix.main.popMatrix();
			}
			if(!cells[i].isDownerRight()){
				//upper left
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(2.5f, 2.5f, 0f);
				ModelMatrix.main.addScale(5f, 5f, 1f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube(shader, world);
				ModelMatrix.main.popMatrix();
			}
			if(!cells[i].isDownerLeft()){
				//upper left
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(-2.5f, 2.5f, 0f);
				ModelMatrix.main.addScale(5f, 5f, 1f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube(shader, world);
				ModelMatrix.main.popMatrix();
			}

		}

		drawEndBackground();
	}

	public void drawEndBackground(){
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0f, 5f, 20f);
		for(float i = -15; i < 16f; i = i + 5f){
			for(float j = -15f; j < 16f; j = j + 5f ){
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(i, j, 0f);
				ModelMatrix.main.addScale(10f, 10f, 1f);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				BoxGraphic.drawSolidCube(shader, sky);
				ModelMatrix.main.popMatrix();
			}
		}
		ModelMatrix.main.popMatrix();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		plane.setInitialDrag(screenX, screenY);
		dragging = true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		dragging = false;
		plane.resetMovement();
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!dragging) return false;
		if(plane.getInitialDrag().y < screenY-50){
			plane.setMovingDown();
		}
		else if(plane.getInitialDrag().y > screenY+50){
			plane.setMovingUp();
		}
		if(plane.getInitialDrag().x < screenX-50){
			plane.setMovingRight();
		}
		else if(plane.getInitialDrag().x > screenX+50){
			plane.setMovingLeft();
		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


}