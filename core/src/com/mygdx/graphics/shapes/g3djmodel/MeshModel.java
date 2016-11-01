package com.mygdx.graphics.shapes.g3djmodel;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.graphics.Material;
import com.mygdx.graphics.ModelMatrix;
import com.mygdx.graphics.Shader;

public class MeshModel {
	public Vector<Mesh> meshes;
	public Vector<MeshPart> parts;
	public Vector<Material> materials;
	public Vector<MeshModelNode> nodes;

	public MeshModel()
	{
		meshes = new Vector<Mesh>();
		parts = new Vector<MeshPart>();
		materials = new Vector<Material>();
		nodes = new Vector<MeshModelNode>();
	}

	public void draw(Shader shader, Texture diffuseTexture) {

		for(MeshModelNode node : nodes)
		{
			ModelMatrix.main.pushMatrix();

			ModelMatrix.main.addTranslation(node.translation.x, node.translation.y, node.translation.z);
			ModelMatrix.main.addRotationQuaternion(node.rotation.x, node.rotation.y, node.rotation.z, node.rotation.w);
			ModelMatrix.main.addScale(node.scale.x, node.scale.y, node.scale.z);

			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			for(MeshModelNodePart part : node.parts)
			{
				//send part.material.xxx into the shader
				shader.setMaterialDiffuse(part.material.diffuse.r, part.material.diffuse.g, part.material.diffuse.b, 1.0f);
				shader.setMaterialSpecular(part.material.specular.r, part.material.specular.g, part.material.specular.b, 1.0f);

				//use glVertexAttribPointer to activate the vertex and normal lists in part.part.mesh
				Gdx.gl.glVertexAttribPointer(shader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, part.part.mesh.vertices);
				Gdx.gl.glVertexAttribPointer(shader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, part.part.mesh.vertices);
				//make sure you're reading these in 3 and 3 together, not 2 and 2 like the UV coordinates

				//if you've added textures to your shader but will not be using them here
				//you should set the UV vertex attribute pointer to something long enough,
				//just so it doesn't crash
				if(part.part.mesh.usesTexture)
				{
					Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, part.part.mesh.uvBuffer);
				}
				else
				{
					Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, part.part.mesh.normals);
				}

				shader.setDiffuseTexture(diffuseTexture);

				if(part.part.type.equals("TRIANGLES"))
				{
					//here you actually draw, using the index list from part.part to decide in which order the polygons are rendered
					Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, part.part.indices.capacity(), GL20.GL_UNSIGNED_SHORT, part.part.indices);
				}
			}
			ModelMatrix.main.popMatrix();
		}
	}
}
