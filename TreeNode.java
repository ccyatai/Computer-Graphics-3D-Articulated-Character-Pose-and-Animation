package pa2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ejml.factory.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

import pa2.PA2.Camera;

public class TreeNode {

	private Model root;
	private Model axis;
	public boolean Drawn = false;
	public boolean IsParent = false;
	public boolean IsEnd = false;
	public boolean ForwardMove = false;
	public boolean ForwardSelected = false;
	public boolean InverseMove = false;
	public boolean InverseSelected = false;
	public float thetaTemp = 0;
	public Vector3f axisTemp = new Vector3f(0, 0, 1);
	public Vector3f dist;
	public Vector3f distTemp = new Vector3f(0, 0, 0);
	public Vector3f end;
	public Vector3f endTemp = new Vector3f(0, 0, 0);
	public TreeNode parent;
	public List<TreeNode> children = new ArrayList<TreeNode>();
	public List<TreeNode> relation = new ArrayList<TreeNode>();
	public List<Rotation> rotation = new ArrayList<Rotation>();
	public SimpleMatrix CurrentMatrix = SimpleMatrix.identity(3);
	
	//Get current rotation axis vertical to the screen pointing out, and output the axis in GL-coordinate
	public Vector3f getAxis(TreeNode t) {
		Vector3f v = new Vector3f(0, 0, 0);
		SimpleMatrix l = new SimpleMatrix(3,1);
		SimpleMatrix Axis;
				
		l.set(2, 0, 1);
		try {
            Axis = t.CurrentMatrix.solve(l);
//            System.out.println("solution x = " + x);
            
            // verify the solution: Ax - b = 0
            SimpleMatrix res = t.CurrentMatrix.mult(Axis).minus(l);
//            System.out.println("residual = " + res);
            
            // due the the numerical error norm(Ax - b) might not be exact zero, 
            // but it should be very small
            if ( res.normF() > 1E-8f ) {
                throw new RuntimeException("The linear solve has problems!");
            }
        } catch ( SingularMatrixException e ) {
            throw new IllegalArgumentException("Singular matrix");
        }
		v.x = (float)Axis.get(0, 0);
		v.y = (float)Axis.get(1, 0);
		v.z = (float)Axis.get(2, 0);
		return v;
	}
	
	//Matrix for GL-coordinate -> Global-coordinate
	public SimpleMatrix rotate(Vector3f axis, float theta) {
		SimpleMatrix l = new SimpleMatrix(3,1);
		SimpleMatrix Axis = new SimpleMatrix(3,1);
		SimpleMatrix A = new SimpleMatrix(3,3);
		
		l.set(2, 0, 1);
		Axis.set(0, 0, axis.x);
		Axis.set(1, 0, axis.y);
		Axis.set(2, 0, axis.z);

		A.set(0, 1, -axis.z);
		A.set(0, 2, axis.y);
		A.set(1, 0, axis.z);
		A.set(1, 2, -axis.x);
		A.set(2, 0, -axis.y);
		A.set(2, 1, axis.x);
		A = A.scale(Math.sin(Math.toRadians(theta))).plus(SimpleMatrix.identity(3).minus(Axis.mult(Axis.transpose())).scale(Math.cos(Math.toRadians(theta)))).plus(Axis.mult(Axis.transpose()));
				
		return A;
	}
	
	public Vector3f translate(Vector3f vec, Vector3f point) {
		Vector3f v = new Vector3f(0, 0, 0);
		v.x = vec.x + point.x;
		v.y = vec.y + point.y;
		v.z = vec.z + point.z;
		return v;
	}
		
	public void draw(){
		GL11.glLoadIdentity(); // Reset The View
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(0.0f, 0.0f, -7.0f);
       
        Camera.apply();	
        
        distTemp.x = 0;
        distTemp.y = 0;
        distTemp.z = -7;
        
        endTemp.x = 0;
        endTemp.y = 0;
        endTemp.z = -7;
        
        if (ForwardSelected) {
    		GL11.glColor3f(0.0f, 0.0f, 1.0f);
    	}
		
		if (ForwardMove) {
			GL11.glColor3f(1.0f, 0.0f, 0.0f);
		}
		
		if (InverseSelected) {
			GL11.glColor3f(0.0f, 1.0f, 0.0f);
		}
		
		if (ForwardMove && InverseSelected) {
			GL11.glColor3f(1.0f, 1.0f, 0.0f);
		}
		
//		TreeNode t = parent;
//		List<TreeNode> relation = new ArrayList<TreeNode>();
		
//		while (t != null) {
//			relation.add(t);
//			t = t.parent;
//		}
		
		Vector3f angle = new Vector3f();
		Vector3f axisx = new Vector3f(1,0,0);
		Vector3f axisy = new Vector3f(0,1,0);
		Vector3f axisz = new Vector3f(0,0,1);
//		Vector3f v = new Vector3f();
		
		angle.x = Camera.rotation.x;
		angle.y = Camera.rotation.y;
		angle.z = Camera.rotation.z;
		
		SimpleMatrix matrix = SimpleMatrix.identity(3);
		
		if (relation.size() != 0) {
			for (int i = relation.size() - 1; i >= 0; i--) {

				if (i == relation.size() - 1) {
					GL11.glTranslatef(relation.get(i).dist.x, relation.get(i).dist.y, relation.get(i).dist.z);
					for (int j = 0;j<=relation.get(i).rotation.size()-1;j++)
						GL11.glRotatef(relation.get(i).rotation.get(j).theta, relation.get(i).rotation.get(j).axis.x, relation.get(i).rotation.get(j).axis.y, relation.get(i).rotation.get(j).axis.z);
					GL11.glRotatef(relation.get(i).thetaTemp, relation.get(i).axisTemp.x, relation.get(i).axisTemp.y, relation.get(i).axisTemp.z);

					
					SimpleMatrix rtemp = new SimpleMatrix(3,1);
					rtemp.set(0, 0, relation.get(i).dist.x);
					rtemp.set(1, 0, relation.get(i).dist.y);
					rtemp.set(2, 0, relation.get(i).dist.z);
					matrix = matrix.mult(rotate(axisx, angle.x)).mult(rotate(axisy, angle.y)).mult(rotate(axisz, angle.z));
					rtemp = matrix.mult(rtemp);
					
					distTemp = translate(new Vector3f((float)rtemp.get(0, 0),(float)rtemp.get(1, 0),(float)rtemp.get(2, 0)), distTemp);
					
				}
				
				else {
//					angle.z += relation.get(i+1).theta;
					GL11.glTranslatef(relation.get(i).dist.x - relation.get(i+1).dist.x, relation.get(i).dist.y - relation.get(i+1).dist.y, relation.get(i).dist.z - relation.get(i+1).dist.z);
					for (int j = 0 ;j<=relation.get(i).rotation.size()-1 ;j++)
						GL11.glRotatef(relation.get(i).rotation.get(j).theta, relation.get(i).rotation.get(j).axis.x, relation.get(i).rotation.get(j).axis.y, relation.get(i).rotation.get(j).axis.z);
					GL11.glRotatef(relation.get(i).thetaTemp, relation.get(i).axisTemp.x, relation.get(i).axisTemp.y, relation.get(i).axisTemp.z);

					
					SimpleMatrix rtemp = new SimpleMatrix(3,1);
					rtemp.set(0, 0, relation.get(i).dist.x - relation.get(i+1).dist.x);
					rtemp.set(1, 0, relation.get(i).dist.y - relation.get(i+1).dist.y);
					rtemp.set(2, 0, relation.get(i).dist.z - relation.get(i+1).dist.z);
				
					for (int j = 0 ;j<=relation.get(i+1).rotation.size()-1; j++)
						matrix = matrix.mult(rotate(relation.get(i+1).rotation.get(j).axis, relation.get(i+1).rotation.get(j).theta));
					matrix = matrix.mult(rotate(relation.get(i+1).axisTemp, relation.get(i+1).thetaTemp));
					rtemp = matrix.mult(rtemp);
					
					distTemp = translate(new Vector3f((float)rtemp.get(0, 0),(float)rtemp.get(1, 0),(float)rtemp.get(2, 0)), distTemp);
				}
			}
		}
		else
			System.out.println("wrong");
		
		for (int j = 0 ;j<=rotation.size()-1; j++)
			matrix = matrix.mult(rotate(rotation.get(j).axis,rotation.get(j).theta));
		matrix = matrix.mult(rotate(axisTemp, thetaTemp));
				
		CurrentMatrix = matrix;
		
		if (IsEnd) {
			SimpleMatrix rtemp = new SimpleMatrix(3,1);
			
			rtemp.set(0, 0, end.x - dist.x);
			rtemp.set(1, 0, end.y - dist.y);
			rtemp.set(2, 0, end.z - dist.z);

			rtemp = matrix.mult(rtemp);
			endTemp = translate(new Vector3f((float)rtemp.get(0, 0),(float)rtemp.get(1, 0),(float)rtemp.get(2, 0)), distTemp);
		}
		
		for (Face face : root.faces) {
    		
    		GL11.glTexCoord2f(0, 0);
    		Vector3f n1 = root.normals.get((int) face.normal.x - 1);
    		GL11.glNormal3f(n1.x, n1.y, n1.z);
    		Vector3f v1 = root.vertices.get((int) face.vertex.x - 1);
    		GL11.glVertex3f(v1.x, v1.y, v1.z);
    		
    		GL11.glTexCoord2f(0, 1);
    		Vector3f n2 = root.normals.get((int) face.normal.y - 1);
    		GL11.glNormal3f(n2.x, n2.y, n2.z);
    		Vector3f v2 = root.vertices.get((int) face.vertex.y - 1);
    		GL11.glVertex3f(v2.x, v2.y, v2.z);
    		
    		GL11.glTexCoord2f(1, 1);
    		Vector3f n3 = root.normals.get((int) face.normal.z - 1);
    		GL11.glNormal3f(n3.x, n3.y, n3.z);
    		Vector3f v3 = root.vertices.get((int) face.vertex.z - 1);
    		GL11.glVertex3f(v3.x, v3.y, v3.z);
    	}
		
		if (ForwardMove || (InverseMove && !InverseSelected)) {
			GL11.glLoadIdentity(); // Reset The View
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glTranslatef(distTemp.x, distTemp.y, distTemp.z);
			for (Face face : axis.faces) {
	    		
	    		Vector3f n1 = axis.normals.get((int) face.normal.x - 1);
	    		GL11.glNormal3f(n1.x, n1.y, n1.z);
	    		Vector3f v1 = axis.vertices.get((int) face.vertex.x - 1);
	    		GL11.glVertex3f(v1.x, v1.y, v1.z);
	    		
	    		Vector3f n2 = axis.normals.get((int) face.normal.y - 1);
	    		GL11.glNormal3f(n2.x, n2.y, n2.z);
	    		Vector3f v2 = axis.vertices.get((int) face.vertex.y - 1);
	    		GL11.glVertex3f(v2.x, v2.y, v2.z);
	    		
	    		Vector3f n3 = axis.normals.get((int) face.normal.z - 1);
	    		GL11.glNormal3f(n3.x, n3.y, n3.z);
	    		Vector3f v3 = axis.vertices.get((int) face.vertex.z - 1);
	    		GL11.glVertex3f(v3.x, v3.y, v3.z);
	    	}
		}
		
		if (InverseMove && InverseSelected) {
			GL11.glLoadIdentity(); // Reset The View
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glTranslatef(endTemp.x, endTemp.y, endTemp.z);
			
			for (Face face : axis.faces) {
	    		
	    		Vector3f n1 = axis.normals.get((int) face.normal.x - 1);
	    		GL11.glNormal3f(n1.x, n1.y, n1.z);
	    		Vector3f v1 = axis.vertices.get((int) face.vertex.x - 1);
	    		GL11.glVertex3f(v1.x, v1.y, v1.z);

	    		Vector3f n2 = axis.normals.get((int) face.normal.y - 1);
	    		GL11.glNormal3f(n2.x, n2.y, n2.z);
	    		Vector3f v2 = axis.vertices.get((int) face.vertex.y - 1);
	    		GL11.glVertex3f(v2.x, v2.y, v2.z);
	    		
	    		Vector3f n3 = axis.normals.get((int) face.normal.z - 1);
	    		GL11.glNormal3f(n3.x, n3.y, n3.z);
	    		Vector3f v3 = axis.vertices.get((int) face.vertex.z - 1);
	    		GL11.glVertex3f(v3.x, v3.y, v3.z);
	    	}
		}
		
		Drawn = true;
	}
	
	public TreeNode(Model rootData1, Model rootData2) {
		root = rootData1;
		axis = rootData2;
	}
}
