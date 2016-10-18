package pa2;

import org.lwjgl.util.vector.Vector3f;

public class Rotation {

	public float theta;
	public Vector3f axis;
	
	public Rotation(float theta, Vector3f axis) {
		this.theta = theta;
		this.axis = axis;   //local axis
	}
	
	public Rotation() {
		this.theta = 0;
		this.axis = new Vector3f(0,0,1);
	}

}
