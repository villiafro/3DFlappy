package com.mygdx.graphics;

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

    public Plane()
    {
        rotationSide = 0;
        rotationUpDown = 0;

        u = new Vector3D(1,0,0);
        v = new Vector3D(0,1,0);
        n = new Vector3D(0,0,1);

        move = new Point3D();
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
        this.rotationSide = rotationSide;
    }

    public void setRotationUpDown(int rotationUpDown) {
        this.rotationUpDown = rotationUpDown;
    }

    public void setMove(float delU, float delV, float delN) {
        this.move.x -= delU*u.x + delV*v.x + delN*n.x;
        this.move.y -= delU*u.y + delV*v.y + delN*n.y;
        this.move.z -= delU*u.z + delV*v.z + delN*n.z;
    }
}
