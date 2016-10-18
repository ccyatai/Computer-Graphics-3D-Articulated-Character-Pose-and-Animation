package pa2;
import java.nio.ByteBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
 
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;

import org.ejml.factory.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;

import Entities.*;

public class PA2 {
	
//	Box e = new Box(0, 0, 0, 0);
//	MoveableEntity f = new MoveableASphere(1,1,1,1);
//	MoveableEntity g = new MoveableBox(1,1,1,1);
//	Entity h = new MoveableBox(1,1,1,1);
//	 
	String windowTitle = "Woman";
	public boolean closeRequested = false;
	public boolean IsMoving = false;
	public boolean Chosen = false;
	List<Vector3f> Jacobi = new ArrayList<Vector3f>();
	TreeNode test;
	
	public SimpleMatrix ModelView;
	
	long lastFrameTime; // used to calculate delta
    
    float triangleAngle; // Angle of rotation for the triangles
    float quadAngle; // Angle of rotation for the quads
	
	TreeNode node1 = new TreeNode(LoadObject("head"), LoadObject("axis"));
	TreeNode node2 = new TreeNode(LoadObject("body"), LoadObject("axis"));
	TreeNode node3 = new TreeNode(LoadObject("ass"), LoadObject("axis"));
	TreeNode node4 = new TreeNode(LoadObject("lbarm"), LoadObject("axis"));
	TreeNode node5 = new TreeNode(LoadObject("lsarm"), LoadObject("axis"));
	TreeNode node6 = new TreeNode(LoadObject("lhand"), LoadObject("axis"));
	TreeNode node7 = new TreeNode(LoadObject("rbarm"), LoadObject("axis"));
	TreeNode node8 = new TreeNode(LoadObject("rsarm"), LoadObject("axis"));
	TreeNode node9 = new TreeNode(LoadObject("rhand"), LoadObject("axis"));
	TreeNode node10 = new TreeNode(LoadObject("lbleg"), LoadObject("axis"));
	TreeNode node11 = new TreeNode(LoadObject("lsleg"), LoadObject("axis"));
	TreeNode node12 = new TreeNode(LoadObject("lfoot"), LoadObject("axis"));
	TreeNode node13 = new TreeNode(LoadObject("rbleg"), LoadObject("axis"));
	TreeNode node14 = new TreeNode(LoadObject("rsleg"), LoadObject("axis"));
	TreeNode node15 = new TreeNode(LoadObject("rfoot"), LoadObject("axis"));
	
	public static List<TreeNode> tree = new ArrayList<TreeNode>();

	private Texture skin; // Texture
	
    public void run() {
		
//    	h.getX();
//    	g.getDX();
    	
        createWindow();
        getDelta(); // Initialise delta timer
        initGL();
        
        while (!closeRequested) {
            pollInput();
            updateLogic(getDelta());
            renderGL();

            Display.update();
        }
        
        cleanup();
    }
	
	private void cleanup() {
        Display.destroy();
    }

	private void initGL() {
		node1.parent = node2;
    	node2.parent = node3;
    	node2.children.add(node1);
    	node2.children.add(node4);
    	node2.children.add(node7);
    	node3.children.add(node2);
    	node3.children.add(node10);
    	node3.children.add(node13);
    	node4.parent = node2;
    	node4.children.add(node5);
    	node5.parent = node4;
    	node5.children.add(node6);
    	node6.parent = node5;
    	node7.parent = node2;
    	node7.children.add(node8);
    	node8.parent = node7;
    	node8.children.add(node9);
    	node9.parent = node8;
    	node10.parent = node3;
    	node10.children.add(node11);
    	node11.parent = node10;
    	node11.children.add(node12);
    	node12.parent = node11;
    	node13.parent = node3;
    	node13.children.add(node14);
    	node14.parent = node13;
    	node14.children.add(node15);
    	node15.parent = node14;
    	
    	node1.dist = new Vector3f(0.0f, 1.76053f, 0.01736f);
    	node2.dist = new Vector3f(0.0f, 0.39492f, 0.12329f);
    	node3.dist = new Vector3f(0.0f, 0.0f, 0.0f);
    	node4.dist = new Vector3f(-0.41432f, 1.50216f, 0.04274f);
    	node5.dist = new Vector3f(-1.30259f, 1.50216f, 0.04274f);
    	node6.dist = new Vector3f(-2.5f, 1.50216f, 0.04274f);
    	node7.dist = new Vector3f(0.41432f, 1.50216f, 0.04274f);
    	node8.dist = new Vector3f(1.30259f, 1.50216f, 0.04274f);
    	node9.dist = new Vector3f(2.5f, 1.50216f, 0.04274f);
    	node10.dist = new Vector3f(-0.21257f, -0.23178f, -0.06675f);
    	node11.dist = new Vector3f(-0.21182f, -1.29939f, -0.05121f);
    	node12.dist = new Vector3f(-0.21182f, -2.39304f, -0.05121f);
    	node13.dist = new Vector3f(0.21257f, -0.23178f, -0.06675f);
    	node14.dist = new Vector3f(0.21182f, -1.29939f, -0.05121f);
    	node15.dist = new Vector3f(0.21182f, -2.39304f, -0.05121f);
    	
    	node1.end = new Vector3f(0f, 2.38704f, 0.02911f);
    	node6.end = new Vector3f(-2.7607f, 1.50216f, 0.04274f);
    	node9.end = new Vector3f(2.7607f, 1.50216f, 0.04274f);
    	node12.end = new Vector3f(-0.21182f, -2.82339f, -0.05121f);
    	node15.end = new Vector3f(0.21182f, -2.82339f, -0.05121f);
 	
    	node1.IsEnd = true;
    	node6.IsEnd = true;
    	node9.IsEnd = true;
    	node12.IsEnd = true;
    	node15.IsEnd = true;
    	node3.IsParent = true;
    	    	
    	tree.add(node1);
    	tree.add(node2);
    	tree.add(node3);
    	tree.add(node4);
    	tree.add(node5);
    	tree.add(node6);
    	tree.add(node7);
    	tree.add(node8);
    	tree.add(node9);
    	tree.add(node10);
    	tree.add(node11);
    	tree.add(node12);
    	tree.add(node13);
    	tree.add(node14);
    	tree.add(node15);
    	
    	for (TreeNode node:tree) {
    		TreeNode t = node;
	    	while (t != null) {
				node.relation.add(t);
				t = t.parent;
			}
    	}
    	
//    	Rotation rotation = new Rotation();
    	
//    	Object a;
//    	String ab = "aaa";
//    	Integer cc = 88;
//    	
//    	int d = 88;
//    	
//    	if (d==cc){
//    		System.out.println("aaa");
//    	}
    	
        /* OpenGL */
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();
        
        skin = LoadTexture("skin");
               
        GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
        
//        System.out.println(width);
//        System.out.println(height);
        
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix
        GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f);// ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        GL11.glLoadIdentity(); // Reset The Modelview Matrix
      
        GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
     
        float theta = 45;
        float b = (float) (-0.1*Math.tan(theta/2/180*Math.PI));
        float t = -b;//(float) (0.1*Math.tan(theta/2/180*Math.PI));	

        float nx = width + 1;
        float ny = height + 1;

        float l = b*nx/ny;
        float r = t*nx/ny;

        float n = (float) -0.1;
        float f = -100;
        
        SimpleMatrix Mpa = SimpleMatrix.identity(4);

        Mpa.set(0, 0, 2/(r-l));
        Mpa.set(0, 3, -(r+l)/(r-l));
        Mpa.set(1, 1, 2/(t-b));
        Mpa.set(1, 3, -(t+b)/(t-b));
        Mpa.set(2, 2, 2/(n-f));
        Mpa.set(2, 3, -(n+f)/(n-f));
        
        SimpleMatrix Mpb = SimpleMatrix.identity(4);

        Mpb.set(0, 0, n);
        Mpb.set(1, 1, n);
        Mpb.set(2, 2, n+f);
        Mpb.set(2, 3, -f*n);
        Mpb.set(3, 2, 1);
        Mpb.set(3, 3, 0);
             
        SimpleMatrix Mv = SimpleMatrix.identity(4);

        Mv.set(0, 0, nx/2);
        Mv.set(0, 3, (nx-1)/2);
        Mv.set(1, 1, ny/2);
        Mv.set(1, 3, (ny-1)/2);
    
        ModelView = Mv.mult(Mpa).mult(Mpb);

        Camera.create();        
    }

	private Model LoadObject(String key) {
		Model m = new Model();
		OBJLoader OBJ = new OBJLoader();
	    	try{
	    		m = OBJ.loadModel(new File("D:/workspace/PA#2/" + key + ".obj"));
	    	} catch(FileNotFoundException e) {
	    		e.printStackTrace();
	    		Display.destroy();
	    		System.exit(1);
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    		Display.destroy();
	    		System.exit(1);
	    	}
		return m;
	}
	
	private Texture LoadTexture(String key) {
		try {
	        return TextureLoader.getTexture("JPG", new FileInputStream(new File("D:/workspace/PA#2/" + key + ".jpg")));
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
		return null;
	}

	private void updateLogic(float delta) {
//		float Sensitivity = 0.5f;
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
        	for (TreeNode node : tree) {
        		if (node.ForwardMove) {
 	        		node.thetaTemp += 1;
	        		if (node.thetaTemp / 360 > 1) {
	        		   node.thetaTemp -= 360;
	                } else if (node.thetaTemp / 360 < -1) {
	                   node.thetaTemp += 360;
	                }
        		}
    		}
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
        	for (TreeNode node : tree) {
        		if (node.ForwardMove) {
	        		node.thetaTemp -= 1;
	        		if (node.thetaTemp / 360 > 1) {
	        		   node.thetaTemp -= 360;
	                } else if (node.thetaTemp / 360 < -1) {
	                   node.thetaTemp += 360;
	                }
        		}
    		}
        }
    }

	private void createWindow() {
	        try {
	        	Display.setDisplayMode(new DisplayMode(640,480));
	            Display.setVSyncEnabled(true);
	            Display.setTitle(windowTitle);
	            Display.create();
	        } catch (LWJGLException e) {
	            Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
	            System.exit(0);
	        }
	}

	/**
     * Poll Input
     */
    public void pollInput() {
   	    	
        Camera.acceptInput(getDelta());

        // scroll through key events
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
	        	if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
	                closeRequested = true;
	            if (Keyboard.getEventKey() == Keyboard.KEY_P)
	                snapshot();
	        	if (Keyboard.getEventKey() == Keyboard.KEY_1) {
	        		if (!node1.ForwardMove) 
	        			node1.ForwardMove = true;
	        		else
	        			node1.ForwardMove = false;
	            }
	        	if (Keyboard.getEventKey() == Keyboard.KEY_2) {
	        		if (!node2.ForwardMove)
	        			node2.ForwardMove = true;
	        		else
	        			node2.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_3) {
	        		if (!node3.ForwardMove)
	        			node3.ForwardMove = true;
	        		else
	        			node3.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_4) {
	        		if (!node4.ForwardMove)
	        			node4.ForwardMove = true;
	        		else
	        			node4.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_5) {
	        		if (!node5.ForwardMove)
	        			node5.ForwardMove = true;
	        		else
	        			node5.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_6) {
	        		if (!node6.ForwardMove)
	        			node6.ForwardMove = true;
	        		else
	        			node6.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_7) {
	        		if (!node7.ForwardMove)
	        			node7.ForwardMove = true;
	        		else
	        			node7.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_8) {
	        		if (!node8.ForwardMove)
	        			node8.ForwardMove = true;
	        		else
	        			node8.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_9) {
	        		if (!node9.ForwardMove)
	        			node9.ForwardMove = true;
	        		else
	        			node9.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_A) {
	        		if (!node10.ForwardMove)
	        			node10.ForwardMove = true;
	        		else
	        			node10.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_B) {
	        		if (!node11.ForwardMove)
	        			node11.ForwardMove = true;
	        		else
	        			node11.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_C) {
	        		if (!node12.ForwardMove)
	        			node12.ForwardMove = true;
	        		else
	        			node12.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_D) {
	        		if (!node13.ForwardMove)
	        			node13.ForwardMove = true;
	        		else
	        			node13.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_E) {
	        		if (!node14.ForwardMove)
	        			node14.ForwardMove = true;
	        		else
	        			node14.ForwardMove = false;
	        	}
	        	if (Keyboard.getEventKey() == Keyboard.KEY_F) {
	        		if (!node15.ForwardMove)
	        			node15.ForwardMove = true;
	        		else
	        			node15.ForwardMove = false;
	        	}
	        }
        }
        
        // cancel the selected objects
        
        select();
        
        if (Display.isCloseRequested()) {
            closeRequested = true;
        }
    }
    
    public void select() {
    	for (TreeNode node : tree) {
    		node.ForwardSelected = false;
    	}
    	    	
    	for (TreeNode node : tree) {
    		TreeNode t = node;
    		while (t.parent != null)
    		{
    			if (t.parent.ForwardMove) {
	        		node.ForwardSelected = true;
	        		break;
    			}
    			else
    				t = t.parent;
	        }
    	}
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new PA2().run();
		
	}

	public int getDelta() {
        long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        int delta = (int) (time - lastFrameTime);
        lastFrameTime = time;
     
        return delta;
    }
	
	private void renderGL() {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer

        GL11.glLoadIdentity(); // Reset The View
                
        GL11.glTranslatef(0.0f, 0.0f, -7.0f);
       
        Camera.apply();
               
        int objectDisplayList = GL11.glGenLists(1);
        GL11.glNewList(objectDisplayList, GL11.GL_COMPILE);
        
        // create 3D objects
  	
    	// draw them
        GL11.glBegin(GL11.GL_TRIANGLES);
    	skin.bind();
    	for (TreeNode node : tree) {
    		node.draw();
    	}
    	    
    	GL11.glEnd();
    	int a = Mouse.getX();
    	int b = Mouse.getY();
    	
    	int c = Mouse.getDX();
    	int d = Mouse.getDY();
    	
    	while (Mouse.next()) {
    		SimpleMatrix global = new SimpleMatrix(4,1); 
    		SimpleMatrix pixel;
//			System.out.println("rr");    
//			
//			if (Mouse.getEventButton() == 0) 
//				System.out.println("r0");  
//			if (Mouse.getEventButtonState()) 
//				System.out.println("rs"); 
//			
//	        if (Mouse.isButtonDown(0))
//	        	System.out.println("0"); 
//				
//    		if (Mouse.isInsideWindow())
//    			System.out.println("123" + a + b);
//    		
	        if (Mouse.isButtonDown(1) ) {
//    			System.out.println("ccc");	        	
	           	for (TreeNode node : tree) {
	           		node.ForwardSelected = false;
	           		node.ForwardMove = false;
	           		node.InverseSelected = false;
	           		node.InverseMove = false;
	           		IsMoving = false;
	           		test = null;
	           		Jacobi.clear();
	           	}
	        }
    	
	        
	       if (Mouse.getEventButton() == 0 && !IsMoving) {
	        	if(Mouse.getEventButtonState()) {
	        	boolean treeend = false;
	        	boolean IsFound = false;
	        	for (TreeNode t : tree) {
        			if (!t.IsParent && !t.parent.IsParent) {
        				global.set(0, 0, t.distTemp.x);
    	        		global.set(1, 0, t.distTemp.y);
    	        		global.set(2, 0, t.distTemp.z);
    	        		global.set(3, 0, 1);
    	        		pixel = ModelView.mult(global).scale(1/global.get(2, 0));

    	        		if (((pixel.get(0, 0) - a) * (pixel.get(0, 0) - a) + (pixel.get(1, 0) - b) * (pixel.get(1, 0) - b)) < 20) {
	        				IsFound = true;
	        				if (test == null)
	        					test = t;
	        				else if (t.distTemp.z > test.distTemp.z) 
			        			test = t;
		        		}
        			}
    			}
	        	
	        	for (TreeNode t : tree) {
	        		if (!t.IsParent && !t.parent.IsParent) {
		        		if (t.IsEnd) {
		        			global.set(0, 0, t.endTemp.x);
			        		global.set(1, 0, t.endTemp.y);
			        		global.set(2, 0, t.endTemp.z);
			        		global.set(3, 0, 1);
			        		pixel = ModelView.mult(global).scale(1/global.get(2, 0));
		        			
			        		if (((pixel.get(0, 0) - a) * (pixel.get(0, 0) - a) + (pixel.get(1, 0) - b) * (pixel.get(1, 0) - b)) < 20) {
		        				IsFound = true;
		        				if (test == null) {
		        					test = t;
		        					treeend = true;
		        				}
		        				else if (test.IsEnd) {
		        					if (t.endTemp.z > test.distTemp.z && t.endTemp.z > test.endTemp.z) {
			        				    test = t;
			        				    treeend = true;
		        					}
		        				}
		        				else {
		        					if (t.endTemp.z > test.distTemp.z) {
			        				    test = t;
			        				    treeend = true;
		        					}
		        				}
		        			}
	        			}
	        		}
	        	}
	        	

	        	if (IsFound) {
	        		IsMoving = true;
	        		Chosen = true;
		        	test.InverseMove = true;
	        		
	        		for (TreeNode t : tree)
	        			t.InverseSelected = false;
	        		
		        	if (treeend) {
		        		test.InverseSelected = true;
//		        		Jacobi.add(getJacobi(test,test));
		        		
//		        		TreeNode ti = test;
		        		TreeNode t = test.parent;
		        		while (t.parent != null) {
		        			t.InverseSelected = true;
//		        			Jacobi.add(getJacobi(ti,t));
//		        			ti = t;
		        			t = t.parent;
		        		}
		        	}
		        	
		        	else {
//		        		TreeNode ti = test;
		        		TreeNode t = test.parent;
		        		while (t.parent != null) {
		        			t.InverseSelected = true;
//		        			Jacobi.add(getJacobi(ti,t));
//		        			ti = t;
		        			t = t.parent;
		        		}
		        	}
	        	}
	        	}
	        	break;
	        }
	       
	      if (Mouse.getEventButton() == 0 && IsMoving) {
	    	 if(Mouse.getEventButtonState()) {
	        	if (!test.InverseSelected) {
	        		global.set(0, 0, test.distTemp.x);
	        		global.set(1, 0, test.distTemp.y);
	        		global.set(2, 0, test.distTemp.z);
	        		global.set(3, 0, 1);
        		}
	        	else {
	        		global.set(0, 0, test.endTemp.x);
	        		global.set(1, 0, test.endTemp.y);
	        		global.set(2, 0, test.endTemp.z);
	        		global.set(3, 0, 1);
				}
	        	
	        	pixel = ModelView.mult(global).scale(1/global.get(2, 0));
	        	
	        	if (((pixel.get(0, 0) - a) * (pixel.get(0, 0) - a) + (pixel.get(1, 0) - b) * (pixel.get(1, 0) - b)) < 20)
    				Chosen = true;
	        	break;
	    	 }
	      }
    	}

    	if (!Mouse.isButtonDown(0)) 
    			Chosen = false;
	        
        if (Chosen) {
        	Jacobi.clear();
        	if (test.InverseSelected) {
        		Jacobi.add(getJacobi(test,test));
        		TreeNode ti = test;
        		TreeNode t = test.parent;
        		while (t.parent != null) {
        			Jacobi.add(getJacobi(ti,t));
        			ti = t;
        			t = t.parent;
        		}
        	}
        	
        	else {
        		TreeNode ti = test;
        		TreeNode t = test.parent;
        		while (t.parent != null) {
        			Jacobi.add(getJacobi(ti,t));
        			ti = t;
        			t = t.parent;
        		}
        	}
	        
	        SimpleMatrix J = new SimpleMatrix (2, Jacobi.size());
    		for (int j = 0; j < Jacobi.size(); j++) {
				J.set(0, j, Jacobi.get(j).x);
				J.set(1, j, Jacobi.get(j).y);
			}
    		
    		SimpleMatrix distIncre = new SimpleMatrix (2, 1);
			distIncre.set(0, 0, c * Camera.mouseSensitivity);
			distIncre.set(1, 0, d * Camera.mouseSensitivity);
    		SimpleMatrix thetaIncre;
    		thetaIncre = solve(J, distIncre, (float) 0.0001);
//    		for (int i = 0;i < thetaIncre.getNumElements();i++) {
//        		System.out.println("d" + i + " = " + thetaIncre.get(i, 0));    			
//    		}

//    		for (int i = 0;i<thetaIncre.getNumElements()-1;i++) {
//    			thetaIncre.set(i, 0, thetaIncre.get(i, 0)-thetaIncre.get(i+1, 0));
//    		}
    		
        	for (TreeNode t : tree) {
        		if (t.InverseMove) {
        			if (t.InverseSelected) {
        				t.thetaTemp += Math.toDegrees(thetaIncre.get(0, 0));
        				t = t.parent;
        				int i = 1;
        				while (t.parent != null) {
        					t.thetaTemp += Math.toDegrees(thetaIncre.get(i, 0));
	        				t = t.parent;
	        				i++;
        				}
        			}
        			
        			else {
        				t = t.parent;
        				int i = 0;
        				while (t.parent != null) {
        					t.thetaTemp += Math.toDegrees(thetaIncre.get(i, 0));
	        				t = t.parent;
	        				i++;
        				}
        			}
        		}
        	}
        }
        GL11.glEndList();
        GL11.glCallList(objectDisplayList);   
    }

	public SimpleMatrix solve(SimpleMatrix J, SimpleMatrix distIncre, float delta) {
		
		SimpleMatrix A;
		SimpleMatrix b;
		SimpleMatrix x;
		
		A = J.transpose().mult(J).plus(SimpleMatrix.identity(J.getNumElements()/2).scale(delta));
		b = J.transpose().mult(distIncre);
		
		try {
            x = A.solve(b);
//            System.out.println("solution x = " + x);
            
            // verify the solution: Ax - b = 0
            SimpleMatrix res = A.mult(x).minus(b);
//            System.out.println("residual = " + res);
            
            // due the the numerical error norm(Ax - b) might not be exact zero, 
            // but it should be very small
            if ( res.normF() > 1E-8f ) {
                throw new RuntimeException("The linear solve has problems!");
            }
        } catch ( SingularMatrixException e ) {
            throw new IllegalArgumentException("Singular matrix");
        }
		return x;
	}
	
	//直接首尾Temp坐标求当前的theta
	public Vector3f getJacobi(TreeNode ti, TreeNode t) {
		Vector3f v = new Vector3f();
		float radius;
		float theta;
		if (ti == t) {
			radius = (float) Math.sqrt((t.endTemp.x-t.distTemp.x) * (t.endTemp.x-t.distTemp.x) + (t.endTemp.y-t.distTemp.y) * (t.endTemp.y-t.distTemp.y));
			if (t.endTemp.x-t.distTemp.x >= 0)
				theta = (float) Math.toDegrees(Math.atan((t.endTemp.y-t.distTemp.y) / (t.endTemp.x-t.distTemp.x)));
			else
				theta = (float) Math.toDegrees(Math.atan((t.endTemp.y-t.distTemp.y) / (t.endTemp.x-t.distTemp.x))) + 180;

//			theta += t.theta;
//			t = t.parent;
//			while (t.parent != null) {
//				theta += t.theta;
//				t = t.parent;
//			}
			v.x = (float) (- radius * Math.sin(Math.toRadians(theta)));
			v.y = (float) (radius * Math.cos(Math.toRadians(theta)));
		}
		
		else {
			radius = (float) Math.sqrt((ti.distTemp.x-t.distTemp.x) * (ti.distTemp.x-t.distTemp.x) + (ti.distTemp.y-t.distTemp.y) * (ti.distTemp.y-t.distTemp.y));
			if (ti.distTemp.x-t.distTemp.x >= 0)
				theta = (float) Math.toDegrees(Math.atan((ti.distTemp.y-t.distTemp.y) / (ti.distTemp.x-t.distTemp.x)));
			else
				theta = (float) Math.toDegrees(Math.atan((ti.distTemp.y-t.distTemp.y) / (ti.distTemp.x-t.distTemp.x))) + 180;
//			theta += t.theta;
//			t = t.parent;
//			while (t.parent != null) {
//				theta += t.theta;
//				t = t.parent;
//			}
			v.x = (float) (- radius * Math.sin(Math.toRadians(theta)));
			v.y = (float) (radius * Math.cos(Math.toRadians(theta)));
		}
				
		return v;
	}
	
	//Get current rotation axis vertical to the screen pointing out, and output the axis in GL-coordinate
	public static Vector3f getAxis(TreeNode t) {
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

	public void snapshot() {
        System.out.println("Taking a snapshot ... snapshot.png");

        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = Display.getDisplayMode().getWidth();
        int height= Display.getDisplayMode().getHeight();
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        File file = new File("snapshot.png"); // The file to save to.
        String format = "PNG"; // Example: "PNG" or "JPG"
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
           
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) { e.printStackTrace(); }
    }
	
	public static class Camera {
        public static float moveSpeed = 0.5f;

        private static float maxLook = 85;
        
        private static float mouseSensitivity = 0.005f;

        public static Vector3f pos;
        public static Vector3f rotation;
        public static Vector3f dr;

        public static void create() {
            pos = new Vector3f(0, 0, 0);
            rotation = new Vector3f(0, 0, 0);
            dr = new Vector3f(0, 0, 0);
        }

        public static void apply() {
            if (rotation.y / 180 > 1) {
                rotation.y -= 360;
            } else if (rotation.y / 180 < -1) {
                rotation.y += 360;
            }
            
            if (rotation.x / 180 > 1) {
                rotation.x -= 360;
            } else if (rotation.x / 180 < -1) {
                rotation.x += 360;
            }

           	GL11.glRotatef(rotation.x, 1, 0, 0);
            GL11.glRotatef(rotation.y, 0, 1, 0);
            GL11.glRotatef(rotation.z, 0, 0, 1);
            GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
            
            
        }

        public static void acceptInput(float delta) {
//            System.out.println("delta="+delta);
            acceptInputRotate(delta);
            acceptInputMove(delta);
        }

        public static void acceptInputRotate(float delta) {
//            if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
//                float mouseDX = Mouse.getDX();
//                float mouseDY = -Mouse.getDY();
//                //System.out.println("DX/Y: " + mouseDX + "  " + mouseDY);
//                rotation.y += mouseDX * mouseSensitivity * delta;
//                rotation.x += mouseDY * mouseSensitivity * delta;
//                rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
//            }
        }

        public static void acceptInputMove(float delta) {
            boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_I);
            boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_K);
            boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_J);
            boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_L);
//            boolean keyAnticlock = Keyboard.isKeyDown(Keyboard.KEY_U);
//            boolean keyClock = Keyboard.isKeyDown(Keyboard.KEY_O);
            boolean keyFast = Keyboard.isKeyDown(Keyboard.KEY_Q);
            boolean keySlow = Keyboard.isKeyDown(Keyboard.KEY_S);
//            boolean keyFlyDown = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
//            boolean keyFlyUp = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

            float speed;

            if (keyFast) {
                speed = moveSpeed * 5;
            } else if (keySlow) {
                speed = moveSpeed / 2;
            } else {
                speed = moveSpeed;
            }

            speed *= delta;
            
            if (keyUp) {
            	rotation.x -= mouseSensitivity * delta * speed;
            	dr.x = -mouseSensitivity * delta * speed;
//            	Vector3f f = new Vector3f();
//            	f.x = 1;
//            	node1 = node2;
            	for (TreeNode t:tree) {
            		if (t.thetaTemp != 0){
            			Rotation rot = new Rotation(t.thetaTemp, t.axisTemp);
            			t.rotation.add(rot);
            			t.thetaTemp = 0;
            		}

            		t.axisTemp = getAxis(t);
            	}
            }
            if (keyDown) {
            	rotation.x += mouseSensitivity * delta * speed;
            	dr.x = mouseSensitivity * delta * speed;
            	for (TreeNode t:tree) {
            		if (t.thetaTemp != 0){
            			Rotation rot = new Rotation(t.thetaTemp, t.axisTemp);
            			t.rotation.add(rot);
            			t.thetaTemp = 0;
            		}

            		t.axisTemp = getAxis(t);
            	}
            }
            if (keyLeft) {
            	rotation.y -= mouseSensitivity * delta * speed;
            	dr.y = mouseSensitivity * delta * speed;
            	for (TreeNode t:tree) {
            		if (t.thetaTemp != 0){
            			Rotation rot = new Rotation(t.thetaTemp, t.axisTemp);
            			t.rotation.add(rot);
            			t.thetaTemp = 0;
            		}

            		t.axisTemp = getAxis(t);
            	}
            }
            if (keyRight) {
            	rotation.y += mouseSensitivity * delta * speed;
            	dr.y = -mouseSensitivity * delta * speed;
            	for (TreeNode t:tree) {
            		if (t.thetaTemp != 0){
            			Rotation rot = new Rotation(t.thetaTemp, t.axisTemp);
            			t.rotation.add(rot);
            			t.thetaTemp = 0;
            		}

            		t.axisTemp = getAxis(t);
            	}
            }
//            if (keyClock) {
//            	rotation.z -= mouseSensitivity * delta * speed;
//            	dr.z = mouseSensitivity * delta * speed;
//            }
//            if (keyAnticlock) {
//            	rotation.z += mouseSensitivity * delta * speed;
//            	dr.z = -mouseSensitivity * delta * speed;
//            }
                        
                        
//            if (keyFlyUp) {
//                pos.y += mouseSensitivity * speed;
//            }
//            if (keyFlyDown) {
//                pos.y -= mouseSensitivity * speed;
//            }

//            if (keyDown) {
//                pos.x -= Math.sin(Math.toRadians(rotation.y)) * mouseSensitivity * speed;
//                pos.z += Math.cos(Math.toRadians(rotation.y)) * mouseSensitivity * speed;
//            }
//            if (keyUp) {
//                pos.x += Math.sin(Math.toRadians(rotation.y)) * mouseSensitivity * speed;
//                pos.z -= Math.cos(Math.toRadians(rotation.y)) * mouseSensitivity * speed;
//            }
//            if (keyLeft) {
//            	pos.x += Math.sin(Math.toRadians(rotation.y - 90)) * mouseSensitivity * speed;
//            	pos.z -= Math.cos(Math.toRadians(rotation.y - 90)) * mouseSensitivity * speed;
//            }
//            if (keyRight) {
//            	pos.x += Math.sin(Math.toRadians(rotation.y + 90)) * mouseSensitivity * speed;
//            	pos.z -= Math.cos(Math.toRadians(rotation.y + 90)) * mouseSensitivity * speed;
//            }
        }

        public static void setSpeed(float speed) {
            moveSpeed = speed;
        }

        public static void setPos(Vector3f pos) {
            Camera.pos = pos;
        }

        public static Vector3f getPos() {
            return pos;
        }

        public static void setX(float x) {
            pos.x = x;
        }

        public static float getX() {
            return pos.x;
        }

        public static void addToX(float x) {
            pos.x += x;
        }

        public static void setY(float y) {
            pos.y = y;
        }

        public static float getY() {
            return pos.y;
        }

        public static void addToY(float y) {
            pos.y += y;
        }

        public static void setZ(float z) {
            pos.z = z;
        }

        public static float getZ() {
            return pos.z;
        }

        public static void addToZ(float z) {
            pos.z += z;
        }

        public static void setRotation(Vector3f rotation) {
            Camera.rotation = rotation;
        }

        public static Vector3f getRotation() {
            return rotation;
        }

        public static void setRotationX(float x) {
            rotation.x = x;
        }

        public static float getRotationX() {
            return rotation.x;
        }

        public static void addToRotationX(float x) {
            rotation.x += x;
        }

        public static void setRotationY(float y) {
            rotation.y = y;
        }

        public static float getRotationY() {
            return rotation.y;
        }

        public static void addToRotationY(float y) {
            rotation.y += y;
        }

        public static void setRotationZ(float z) {
            rotation.z = z;
        }

        public static float getRotationZ() {
            return rotation.z;
        }

        public static void addToRotationZ(float z) {
            rotation.z += z;
        }

        public static void setMaxLook(float maxLook) {
            Camera.maxLook = maxLook;
        }

        public static float getMaxLook() {
            return maxLook;
        }

        public static void setMouseSensitivity(float mouseSensitivity) {
            Camera.mouseSensitivity = mouseSensitivity;
        }

        public static float getMouseSensitivity() {
            return mouseSensitivity;
        }
    }
	
}