package com.mygdx.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

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

    Point initialDrag;

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

        initialDrag = new Point();

        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }

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


    public void setRotationSide(int rotationSide) {
        if(this.rotationSide >= -30 && this.rotationSide <= 30){
            this.rotationSide += rotationSide;
        }
    }
    public void resetRotationSide(){
        if(this.rotationSide < 0){
            this.rotationSide += 5;
        }
        if(this.rotationSide > 0){
            this.rotationSide -= 5;
        }
    }

    public void setRotationUpDown(int rotationUpDown) {
        if(this.rotationUpDown >= -30 && this.rotationUpDown <= 30){
            this.rotationUpDown += rotationUpDown;
        }
    }

    public void resetRotationUpDown(){
        if(this.rotationUpDown < 0){
            this.rotationUpDown += 5;
        }
        if(this.rotationUpDown > 0){
            this.rotationUpDown -= 5;
        }
    }

    public boolean setMove(float delU, float delV, float delN) {
        boolean moved = false;
        float checkSide = this.move.x - delU*u.x + delV*v.x + delN*n.x;
        float checkUp = this.move.y - delU*u.y + delV*v.y + delN*n.y;

        //hægri
        if(this.move.x > checkSide && checkSide > -3.5f){
            this.move.x = checkSide;

            moved = true;
            if(getRotationUpDown() > 0){
                setRotationSide(-5);
            }
            else{ // venjulegt fara til hægri
                setRotationSide(5);
            }
        } // vinstri
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
        //niður
        if(this.move.y > checkUp && checkUp > -3){
            this.move.y = checkUp;
            setRotationUpDown(5);
            moved = true;

        } // upp
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

    public void setInitialDrag(int x, int y) {
        this.initialDrag.x = x;
        this.initialDrag.y = y;
    }

    public boolean wallCollision(Cell cell){

        /*if(cell.isUpperLeft() || cell.isUpperRight() || cell.isDownerLeft() || cell.isDownerRight()){
            System.out.println("cell has wall");
        }*/
        boolean possibleCollision = false;
        float relativeZ = this.move.z%10;
        if((relativeZ > 3.5f && relativeZ < 6.5f)){ // relative to middle point of plane...
            possibleCollision = true;
        }/*
        if(cell.isUpperLeft()){
            System.out.println(" gap is to top right");
        }
        if(cell.isDownerLeft()){
            System.out.println(" gap is to down right");
        }
        if(cell.isUpperRight()){
            System.out.println(" gap is to top left");
        }
        if(cell.isDownerRight()){
            System.out.println(" gap is to down left");
        }*/
       // float planePosLeft = (this.move.x - 0.7f);
        if(possibleCollision){

            if( !cell.isUpperLeft() && (this.move.x - 0.7) < 0.01f && (this.move.y + 0.5) > 1.01f){

                System.out.println("colliding top right side");
                return true;
            }
            if( !cell.isUpperRight() && (this.move.x + 0.7)> 0.01f && (this.move.y + 0.5) > 1.01f){
                System.out.println("colliding top left side");
                return true;
            }
            if( !cell.isDownerLeft() && (this.move.x - 0.7) < 0.01f && (this.move.y - 0.15) < 1.01f){
                System.out.println("colliding bottom right side");
                return true;
            }
            if( !cell.isDownerRight() && (this.move.x + 0.7) > 0.01f && (this.move.y - 0.15) < 1.01f){
                System.out.println("colliding bottom left side");
                return true;
            }
        }

       /* if( !cell.isUpperRight() && (this.move.x - 0.7) < 0.01f && (this.move.y + 0.05) > 0.01f){
            System.out.println("colliding top right side");
        }
        if( !cell.isUpperLeft() && (this.move.x + 0.7)> 0.01f && (this.move.y - 0.05) > 0.01f){
            System.out.println("colliding top left side");
        }
        if( !cell.isDownerRight() && (this.move.x - 0.7) < 0.01f && (this.move.y + 0.05) < 0.01f){
            System.out.println("colliding bottom right side");
        }
        if( !cell.isDownerLeft() && (this.move.x + 0.7) > 0.01f && (this.move.y - 0.05) < 0.01f){
            System.out.println("colliding bottom left side");
        }

*/
       /* System.out.println(" relative z = " + relativeZ);
*/
       // System.out.println("this x = " + this.move.x + " this z = " + this.move.z + " this y = " + this.move.y);



        return false;
    }

    public Point getInitialDrag() {
        return initialDrag;
    }
}
