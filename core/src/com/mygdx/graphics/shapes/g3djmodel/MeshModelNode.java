package com.mygdx.graphics.shapes.g3djmodel;

import java.util.Vector;

import com.mygdx.graphics.ModelMatrix;
import com.mygdx.graphics.Point3D;
import com.mygdx.graphics.Quaternion;
import com.mygdx.graphics.Vector3D;

public class MeshModelNode {
	public String id;
	public Quaternion rotation;
	public Vector3D scale;
	public Point3D translation;
	public Vector<MeshModelNodePart> parts;

	public Point3D heli;
	public Vector3D u;
	public Vector3D v;
	public Vector3D n;

	public MeshModelNode()
	{
		heli = new Point3D();
		u = new Vector3D(1,0,0);
		v = new Vector3D(0,1,0);
		n = new Vector3D(0,0,1);

		rotation = new Quaternion();
		scale = new Vector3D(1.0f, 1.0f, 1.0f);
		translation = new Point3D(0, 0, 0);
		parts = new Vector<MeshModelNodePart>();
	}

	public void slide(float delU, float delV, float delN)
	{
		translation.x -= delU*u.x + delV*v.x + delN*n.x;
		translation.y -= delU*u.y + delV*v.y + delN*n.y;
		translation.z -= delU*u.z + delV*v.z + delN*n.z;
	}

}
