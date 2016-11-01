package com.mygdx.graphics.shapes.g3djmodel;

import com.badlogic.gdx.utils.BufferUtils;
import com.mygdx.graphics.Vector3D;

import java.nio.FloatBuffer;

public class Mesh {
	//public Vector<String> attributes;
	public FloatBuffer vertices;
	public FloatBuffer normals;
	public FloatBuffer uvBuffer;
	public boolean usesTexture;

	public Mesh()
	{
		vertices = null;
		normals = null;
		uvBuffer = null;
		usesTexture = false;
	}

	public void buildSphericalUVMap()
	{
		int vertexCount = vertices.capacity()/3;
		uvBuffer = BufferUtils.newFloatBuffer(vertexCount*2);
		for(int vertexNum = 0; vertexNum < vertexCount; vertexNum++)
		{
			Vector3D v = new Vector3D(vertices.get(vertexNum*3), vertices.get(vertexNum*3+1), vertices.get(vertexNum*3+2));

			if(v.length() == 0.0f)
			{
				v.y = 1.0f;
			}

			//horizontal v
			Vector3D vH = new Vector3D(v.x, 0 , v.z);

			if(vH.length() == 0.0f)
			{
				vH.x = 1.0f;
			}

			//x-axis
			Vector3D xA = new Vector3D(1, 0, 0);

			vH.normalize();
			v.normalize();

			float latitude;
			if(vH.z <= 0.0)
			{
				latitude = (float)Math.acos(vH.dot(xA));
			}
			else
			{
				latitude = (2.0f * (float)Math.PI) - (float)Math.acos(vH.dot(xA));
			}
			uvBuffer.put(vertexNum*2, latitude / (2.0f * (float)Math.PI));

			float longtitude;
			if(v.y == 0.0f){
				longtitude = ((float)Math.PI / 2.0f);
			}
			else if(v.y > 0.0f)
			{
				longtitude = (float)Math.acos(v.dot(vH)) + ((float)Math.PI / 2.0f);
			}
			else
			{
				longtitude = ((float)Math.PI / 2.0f) - (float)Math.acos(v.dot(vH));
			}
			uvBuffer.put(vertexNum*2 + 1, longtitude / (float)Math.PI);
		}
		uvBuffer.rewind();
		usesTexture = true;
	}
}
