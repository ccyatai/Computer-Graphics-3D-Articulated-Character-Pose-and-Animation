                                            Computer Graphics Project Description
                                             3D Articulated Character Animation
                                            Programming application: Java, OpenGL
In this project, I have done the work of animating and posing 3D articulated characters by implementing forward and inverse kinematic methods. The user is able to animate character joint angles to demonstrate character moving (forward kinematics), as well as pose the character by dragging the points on screen and moving the skeletons (inverse kinematics).
I used Blender to draw a 3D robot model consisting of 15 body parts (such as a head, two forearms, two backarms, two thighs, two shanks, etc). Each body part was linked by joints. 

The first step is to model each rigid-body part (bones) of the character in a rest pose (θ_j=0,j=1…n). I started by defining each rigid link's geometry, and then I defined each joint's center position c_j, and the rotation axis r_j. I also built a tree structure of the kinematic model to maintain the parent/child relationships among those rigid links. These relationships would be used to construct the transformation matrix to transform from each link's local frame of reference into the world frame of reference.

For the forward kinematic algorithm, I implemented the keyboard-based user interface to enable a user to interactively adjust the joint angles by pressing some keys. In the meanwhile, I update the body-to-world transformation matrices, and then draw the articulated geometry. 

For the inverse kinematic, implement an inverse kinematics solver based on a damped least-squares solver. The key part of my implementation is to solve least-square problems of the form
J∆θ= ∆p,
where ∆θ are the changes of N joint angles, ∆p are the changes in M constraint positions (e.g. the link positions specifed by mouse dragging) in the world frame of reference, and J=∂p/∂θ  ∈〖R 〗^(M×N) is the Jacobian matrix. Depending on the number of position constraints, you may end up with an over-determined (M > N) or under-determined (M < N) system. The damped least-square solver will generate angle updates of the form
∆θ=〖(J^T J+δI)〗^(-1) J^T ∆p,
where δ>0 is a user-specified regularization parameter to avoid singular matrices. 

Based on these algorithms and structure, you can self-design any geometry model and have fun to do any animation and posing you like running my code.



Interactions
Keyboard:
KEY	Function
1	Select/Deselect head
2	Select/Deselect body
3	Select/Deselect bottom
4	Select/Deselect left backarm
5	Select/Deselect left forearm
6	Select/Deselect left hand
7	Select/Deselect right backarm
8	Select/Deselect right forearm
9	Select/Deselect right hand
A	Select/Deselect left thigh
B	Select/Deselect left shank
C	Select/Deselect left foot
D	Select/Deselect right thigh
E	Select/Deselect right shank
F	Select/Deselect right foot
UP	Rotate selected parts clockwise around joint axis
DOWN	Rotate selected parts counter-clockwise around joint axis
I	Rotate the angle of the camera view (upward)
K	Rotate the angle of the camera view (downward)
J	Rotate the angle of the camera view (leftward)
L	Rotate the angle of the camera view (rightward)
Q	Move the camera view point faster
S	Move the camera view point slower
Esc	Close the window
P	Take a snapshot

Mouse:
LeftButton: Pick and drag the constraint joint (Inverse Kinematic)
RightButton: Deselect all selected parts
Welcome to continue to see my showcase movie on https://youtu.be/4NuE9PDqOIc!

