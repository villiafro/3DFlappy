package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.graphics.shapes.*;
import com.mygdx.graphics.shapes.g3djmodel.G3DJModelLoader;
import com.mygdx.graphics.shapes.g3djmodel.MeshModel;
import com.mygdx.graphics.*;

public class FlappyGame extends ApplicationAdapter implements InputProcessor {

	Shader shader;

	private float angle;

	private Camera cam;

	private float fov = 120.0f;

	private MeshModel model3D;

	private Texture world;
	private Texture menu;
	private Texture sky;
	private Texture over;
	private Texture comp;

	private Texture stageImage;

	private Random rand;

	private static Cell[] cells;

	private int stages;

	private Plane plane;

	private boolean dragging;

	private int score = 0;
	private boolean gameOver = true;

	private float movingSpeed = 3.0f;

	private boolean begining = true;

	private int completed = 5;

	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		shader = new Shader();

		if(Gdx.app.getType() == Application.ApplicationType.Android){
			sky = new Texture(Gdx.files.internal("textures/testsky.png"));
			world = new Texture(Gdx.files.internal("textures/testwall.png"));
			menu = new Texture(Gdx.files.internal("textures/menu.png"));
			over = new Texture(Gdx.files.internal("textures/planeOver.png"));
			comp = new Texture(Gdx.files.internal("textures/victory.png"));
		}
		else{
			sky = new Texture(Gdx.files.internal("core/assets/textures/testsky.png"));
			world = new Texture(Gdx.files.internal("core/assets/textures/testwall.png"));
			menu = new Texture(Gdx.files.internal("core/assets/textures/menu.png"));
			over = new Texture(Gdx.files.internal("core/assets/textures/planeOver.png"));
			comp = new Texture(Gdx.files.internal("core/assets/textures/victory.png"));
		}

		model3D = G3DJModelLoader.loadG3DJFromFile("/plane/plane.g3dj", true);

		BoxGraphic.create();
		SphereGraphic.create();
		plane = new Plane();

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		cam.look(new Point3D(0f, 4f, -4f), new Point3D(0,4,0), new Vector3D(0,1,0));

		dragging = false;

		stages = 10;

		rand = new Random();

		Cave cave = new Cave();
		fillRandomCell(cave);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * fill the cave with randomized blocks
	 * @param cave cave to be filled
	 */
	private void fillRandomCell(Cave cave){
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
	}

	/**
	 * reset the model matrix and camera
	 * and generate a new randomized cave
	 */
	private void reCreate()
	{
		Gdx.input.setInputProcessor(this);
		plane = new Plane();

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		cam.look(new Point3D(0f, 4f, -4f), new Point3D(0,4,0), new Vector3D(0,1,0));

		rand = new Random();

		Cave cave = new Cave();
		fillRandomCell(cave);
	}

	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		if(!gameOver && (completed != score)) {
			angle += 180.0f * deltaTime;
			cam.slide(0, 0, -movingSpeed * deltaTime);
			plane.setMove(0, 0, -movingSpeed * deltaTime);

			// when on at the end of the cave move plane further away
			if (plane.getMove().z > ((stages-2) * 10)) {
				plane.setMove(0, 0, -movingSpeed * deltaTime);
			}

			// when plane is far enough recreate the cave
			if (plane.getMove().z > ((stages*10)-5)) {
				score += 1;
				movingSpeed += 1;

				System.out.println("Score: " + score);
				reCreate();
			}

			if (Gdx.input.isKeyPressed(Input.Keys.A) || plane.isMovingLeft()) {
				if (plane.setMove(-movingSpeed * deltaTime, 0, 0)) {
					cam.slide(-movingSpeed * deltaTime, 0, 0);
				}
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D) || plane.isMovingRight()) {
				if (plane.setMove(movingSpeed * deltaTime, 0, 0)) {
					cam.slide(movingSpeed * deltaTime, 0, 0);
				}
			}

			// reset the rotation if D and A keys are released
			if (!Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
				plane.resetRotationSide();
			}

			if (Gdx.input.isKeyPressed(Input.Keys.W) || plane.isMovingUp()) {
				if (plane.setMove(0, movingSpeed * deltaTime, 0)) {
					cam.slide(0, movingSpeed * deltaTime, 0);
				}
			}

			if (Gdx.input.isKeyPressed(Input.Keys.S) || plane.isMovingDown()) {
				if (plane.setMove(0, -movingSpeed * deltaTime, 0)) {
					cam.slide(0, -movingSpeed * deltaTime, 0);
				}
			}

			//reset the rotation if S and W keys are released
			if (!Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
				plane.resetRotationUpDown();
			}

			int cell = (int)(plane.getMove().z/10) + 1;

			// check collision if cell is in stage
			if(cell < stages ){
				if(plane.wallCollision(cells[cell])){
					this.gameOver = true;
					dragging = false;
					reCreate();

				}
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		// detect if a player wants to start again
		if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || dragging) {
			score = 0;
			gameOver = false;
			begining = false;
		}
	}

	private void display()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 100.0f);
		shader.setViewMatrix(cam.getViewMatrix());
		shader.setProjectionMatrix(cam.getProjectionMatrix());
		shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);

		ModelMatrix.main.loadIdentityMatrix();

		shader.setLightPosition(cam.eye.x,cam.eye.y-2,cam.eye.z+1,1);

		//lights that follow plane
		shader.setLightPositionPlane(cam.eye.x,cam.eye.y+2,cam.eye.z+1,1);

		shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);

		shader.setSpotExponent(0.0f);
		shader.setConstantAttenuation(1f);
		shader.setLinearAttenuation(0.00f);
		shader.setQuadraticAttenuation(0.00f);

		shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);

		shader.setGlobalAmbient(0.3f, 0.3f, 0.3f, 1);

		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1f, 1f, 1f, 1.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		shader.setShininess(150.0f);

		if(!gameOver && (completed != score)){
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(0.0f, 4.0f, 0.0f);
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

		update();
		display();
	}

	/**
	 * Make a menu window if the game is starting or ending
	 */
	private void makeMenu(){

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0f, 5f, 0f);
		ModelMatrix.main.addScale(15f, 15f, 1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		if(begining && (completed != score)){
			BoxGraphic.drawSolidCube(shader, menu);
		}
		else if(completed == score){
			BoxGraphic.drawSolidCube(shader, comp);
			movingSpeed = 3.0f;
		}
		else{
			BoxGraphic.drawSolidCube(shader, over);
			movingSpeed = 3.0f;
		}
		ModelMatrix.main.popMatrix();

		String gameScore;
		if(score > 10){
			gameScore = "stageUN.png";
		}
		else{
			gameScore = "stage"+score+".png";
		}

		if(Gdx.app.getType() == Application.ApplicationType.Android){
			stageImage = new Texture(Gdx.files.internal("textures/"+gameScore));

		}
		else{
			stageImage = new Texture(Gdx.files.internal("core/assets/textures/"+gameScore));

		}

		if(completed != score){
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(-4f, 4.5f, -0.1f);
			ModelMatrix.main.addScale(1f, 2f, 1f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube(shader, stageImage);
			ModelMatrix.main.popMatrix();
		}

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

	/**
	 * draw a endless space at the end of each stage
	 */
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

	//controls for the android version

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
		System.out.println("screenX: " + screenX + " screenY: " + screenY);
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