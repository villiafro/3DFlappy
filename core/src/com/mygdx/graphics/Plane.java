package com.mygdx.graphics;

/**
 * Created by VilhjalmurAlex on 02/11/2016.
 */
public class Plane {

    int rotationSide;
    int rotationUpDown;

    public Vector3D u;
    public Vector3D v;
    public Vector3D n;

    Point3D move;

    Point3D initialDrag;

    boolean movingUp;
    boolean movingDown;
    boolean movingLeft;
    boolean movingRight;

    public Plane()
    {
        rotationSide = 0;
        rotationUpDown = 0;

        u = new Vector3D(1,0,0);
        v = new Vector3D(0,1,0);
        n = new Vector3D(0,0,1);

        move = new Point3D();

        initialDrag = new Point3D();

        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }


    /**
     * set the right direction if the
     * movement is controlled by screen touch
     */
    public void setMovingUp() {
        this.movingUp = true;
        this.movingDown = false;
    }

    public void setMovingDown() {
        this.movingUp = false;
        this.movingDown = true;

    }

    public void setMovingLeft() {
        this.movingLeft = true;
        this.movingRight = false;
    }

    public void setMovingRight() {
        this.movingLeft = false;
        this.movingRight = true;
    }

    public void resetMovement() {
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public int getRotationSide() {
        return rotationSide;
    }

    public int getRotationUpDown() {
        return rotationUpDown;
    }

    public Point3D getMove() {
        return move;
    }

    /**
     * sets the rotation of the plane if it is moving left or right
     * @param rotationSide +-5 of the rotation, int
     */
    public void setRotationSide(int rotationSide) {
        if(this.rotationSide >= -30 && this.rotationSide <= 30){
            this.rotationSide += rotationSide;
        }
    }

    /**
     * slowly resets the rotation if right or left key is released
     */
    public void resetRotationSide(){
        if(this.rotationSide < 0){
            this.rotationSide += 5;
        }
        if(this.rotationSide > 0){
            this.rotationSide -= 5;
        }
    }

    /**
     * sets the rotation of the plane if it is moving up or down
     * @param rotationUpDown +-5 of the rotation, int
     */
    public void setRotationUpDown(int rotationUpDown) {
        if(this.rotationUpDown >= -30 && this.rotationUpDown <= 30){
            this.rotationUpDown += rotationUpDown;
        }
    }

    /**
     * slowly resets the rotation if up or down key is released
     */
    public void resetRotationUpDown(){
        if(this.rotationUpDown < 0){
            this.rotationUpDown += 5;
        }
        if(this.rotationUpDown > 0){
            this.rotationUpDown -= 5;
        }
    }

    /**
     * moves the plane according to the key input
     * @param delU x coordinates
     * @param delV y coordinates
     * @param delN z coordinates
     * @return true if it was moved
     */
    public boolean setMove(float delU, float delV, float delN) {
        boolean moved = false;
        float checkSide = this.move.x - delU*u.x + delV*v.x + delN*n.x;
        float checkUp = this.move.y - delU*u.y + delV*v.y + delN*n.y;

        //Setting the right rotation of the movement
        //Right
        if(this.move.x > checkSide && checkSide > -3.5f){
            this.move.x = checkSide;

            moved = true;
            if(getRotationUpDown() > 0){
                setRotationSide(-5);
            }
            else{
                setRotationSide(5);
            }
        }
        //Left
        else if(this.move.x < checkSide && checkSide < 3.5f){
            this.move.x = checkSide;

            moved = true;
            if(getRotationUpDown() > 0){
                setRotationSide(5);
            }
            else{
                setRotationSide(-5);
            }
        }
        //Down
        if(this.move.y > checkUp && checkUp > -3){
            this.move.y = checkUp;
            setRotationUpDown(5);
            moved = true;

        }
        //Up
        else if(this.move.y < checkUp && checkUp < 5){
            this.move.y = checkUp;
            setRotationUpDown(-5);
            moved = true;

        }
        this.move.z -= delU*u.z + delV*v.z + delN*n.z;

        if(moved){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Gets the position of the initial drag on the screen
     * @param x coordinates
     * @param y coordinates
     */
    public void setInitialDrag(int x, int y) {
        this.initialDrag.x = x;
        this.initialDrag.y = y;
    }

    /**
     * check collision of the plane in a given cell
     * @param cell the cell the plane is in
     * @return true if a collision
     */
    public boolean wallCollision(Cell cell){

        boolean possibleCollision = false;
        float relativeZ = this.move.z%10;
        if((relativeZ > 3.5f && relativeZ < 6.5f)){ // relative to middle point of plane...
            possibleCollision = true;
        }

        if(possibleCollision){
            //colliding top right side
            if( !cell.isUpperLeft() && (this.move.x - 0.7) < 0.01f && (this.move.y + 0.5) > 1.01f){
                return true;
            }
            //colliding top left side
            if( !cell.isUpperRight() && (this.move.x + 0.7)> 0.01f && (this.move.y + 0.5) > 1.01f){
                return true;
            }
            //colliding bottom right side
            if( !cell.isDownerLeft() && (this.move.x - 0.7) < 0.01f && (this.move.y - 0.15) < 1.01f){
                return true;
            }
            //colliding bottom left side
            if( !cell.isDownerRight() && (this.move.x + 0.7) > 0.01f && (this.move.y - 0.15) < 1.01f){
                return true;
            }
        }

        return false;
    }

    public Point3D getInitialDrag() {
        return initialDrag;
    }
}
