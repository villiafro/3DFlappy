package com.mygdx.graphics;

import com.badlogic.gdx.utils.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Created by VilhjalmurAlex on 02/11/2016.
 */
public class Plane {
    public Point3D heli;
    public Vector3D u;
    public Vector3D v;
    public Vector3D n;

    public Plane()
    {
        heli = new Point3D();
        u = new Vector3D(1,0,0);
        v = new Vector3D(0,1,0);
        n = new Vector3D(0,0,1);
    }

    public void slide(float delU, float delV, float delN)
    {
        heli.x -= delU*u.x + delV*v.x + delN*n.x;
        heli.y -= delU*u.y + delV*v.y + delN*n.y;
        heli.z -= delU*u.z + delV*v.z + delN*n.z;
    }

    public Point3D getHeli(){
        return heli;
    }

}
