package Structure;
import java.util.Vector;
import java.lang.Math;

import Compare3D.Compare3D;
import Compare3D.Direction;
import DataObjects.Point;
public class prOctTree<T extends Compare3D<? super T>> {
	/**
	 * This class is abstracted in order to allow for either a leaf or internal
	 * node to be passed as a parameter to a method
	 * @author Eric
	 *
	 */
	abstract class prOctNode{}
	/**
	 * This class represents a leaf value of the tree.
	 * @author Eric
	 *
	 */
	class prOctLeaf extends prOctNode{
		Vector<T> Elements;
		public prOctLeaf(){
			Elements = new Vector<T>();
		}
	}
	/**
	 * This class represent an internal node of the tree.
	 * @author Eric
	 *
	 */
	class prOctNodeInternal extends prOctNode{
		prOctNode UNW, UNE, USE, USW, DNW, DNE, DSE, DSW;
	}
	//The root of the tree
	prOctNode root;
	double xMin, xMax, yMin, yMax, zMin, zMax;
	//The maximum number of elements in each node.
	private int bucketSize;
	//The last deleted value, to be used to determine if deletion happened
	private T deletedValue;

	/**
	 * This is the basic constructor, sets the region bound and the bucket size to 4
	 * @param xMin	The minimum x value of the region
	 * @param xMax	The maximum x value of the region
	 * @param yMin  The minimum y value of the region
	 * @param yMax	The maximum y value of the region
	 * @param zMin	The minimum z value of the region
	 * @param zMax	The maximum z value of the region
	 */
	public prOctTree(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax){
		root = null;
		bucketSize = 4;
		this.xMax = xMax;
		this.xMin = xMin;
		this.yMax = yMax;
		this.yMin = yMin;
		this.zMax = zMax;
		this.zMin = zMin;
	}
	
	/**
	 * Attempts to insert an object into the tree. The object must be within the bound of the tree
	 * @param elem	The element to attempt to insert
	 * @return		Will return true if the insertion was successful, false if otherwise.
	 */
	public boolean insert(T elem){
		if(elem == null)
			return false;
		//Starts the recursive call to insert
		prOctNode temp = insertHelper(elem, root, xMax, xMin, yMax, yMin, zMax, zMin);
		if(temp == null)
			return false;
		root = temp;
		return true;
	}
	
	/**
	 * This is a recursive function to help the insert method do its job.
	 * @param elem	The object to attempt to insert
	 * @param root	The root of the tree to insert the object under
	 * @param xMax	The maximum x value of the region
	 * @param xMin	The minimum x value of the region
	 * @param yMax  The maximum y value of the region
	 * @param yMin	The minimum y value of the region
	 * @param zMax	The maximum z value of the region
	 * @param zMin	The minimum y value of the region
	 * @return		Will return the root node with the object inserted in the proper location or null if the insertion could not be done.
	 */
	private prOctNode insertHelper(T elem, prOctNode root, double xMax,
			double xMin, double yMax, double yMin, double zMax, double zMin) {
		//Null check on the insertion as well as checking the object exists in the bounds
		if(elem == null || !elem.inBox(xMin, xMax, yMin, yMax, zMin, zMax))
			return null;
		//Insertion into an empty leaf
		if(root == null){
			root = new prOctLeaf();
			((prOctLeaf)root).Elements.add(elem);
			return root;
		}
		//Insertion into a leaf with one or more elements
		else if(root instanceof prOctTree.prOctLeaf){
			prOctLeaf handle = (prOctLeaf) root;
			//checks that the element isn't already there
			for(T x: handle.Elements){
				if(x.equals(elem))
					return null;
			}
			//Checks if there is room in the leaf for the object.
			if(handle.Elements.size() < bucketSize){
				handle.Elements.add(elem);
			}
			//If there isn't enough room for the object.
			else{
				//Captures the objects from the leaf
				Vector<T> temp = handle.Elements;
				temp.add(elem);
				//Changes the type of the root to internal node, which can be thought of as splitting the leaf
				root = new prOctNodeInternal();
				//Reinserts the objects from the root into the tree 
				for(T x: temp)
					root = insertHelper(x, root, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			return root;
		}
		//If the root is an internal node.
		else if(root instanceof prOctTree.prOctNodeInternal){
			prOctNodeInternal handle = (prOctNodeInternal) root;
			//Determines where to insert the object
			Direction destDirection = elem.inQuadrant(xMin, xMax, yMin, yMax, zMin, zMax);
			double averageX = (xMax + xMin) / 2;
			double averageY = (yMax + yMin) / 2;
			double averageZ = (zMax + zMin) / 2;
			//If it is destined for the north, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.UNW || destDirection == Direction.DNE || destDirection == Direction.DNW)
				yMin = averageY;
			else
				yMax = averageY;
			//If it is destined for the east, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.USE || destDirection == Direction.DNE || destDirection == Direction.DSE)
				xMin = averageX;	
			else
				xMax = averageX;
			//If it is destined for up, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.UNW || destDirection == Direction.USW || destDirection == Direction.USE)
				zMin = averageZ;
			else
				zMax = averageZ;
			//Determines where is should be sent.
			switch(destDirection){
			case DNE:{
				handle.DNE = insertHelper(elem, handle.DNE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case DNW:{
				handle.DNW = insertHelper(elem, handle.DNW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case DSE:{
				handle.DSE = insertHelper(elem, handle.DSE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case DSW:{
				handle.DSW = insertHelper(elem, handle.DSW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;			
			}
			case UNE:{
				handle.UNE = insertHelper(elem, handle.UNE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case UNW:{
				handle.UNW= insertHelper(elem, handle.UNW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case USE:{
				handle.USE= insertHelper(elem, handle.USE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case USW:{
				handle.USW = insertHelper(elem, handle.USW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			//If we get here either the object exists at the origin or something went wrong.
			case NOQUADRANT:{
				if(elem.getX() == averageX && elem.getY() ==  averageY && elem.getZ() == averageZ)
					handle.UNE = insertHelper(elem, handle.UNE, xMax, xMin, yMax, yMin, zMax, zMin);
				return null;
			}
			}
			return root;
		}
		return null;
	}
	
	/**
	 * Attempts to find and delete a given object.
	 * @param Elem	The object to find and delete.
	 * @return		Whether the deletion was successful or not.
	 */
	public boolean delete(T Elem) {
		if(Elem == null)
			return false;
		//resets the deletedValue reference incase delete has already been called
		deletedValue = null;
		//may or may not effect root
		root = deleteHelper(Elem, root, xMax, xMin, yMax, yMin, zMax, zMin);
		//used to determine if root has changed thanks to the delete call
		if(deletedValue != null)
			return true;
		return false;
	}
	
	/**
	 * A helper method to assist the delete method.
	 * @param elem	The object to attempt to delete
	 * @param root	The root of the subtree to look under
	 * @param xMax	The maximum x value of the subtree.
	 * @param xMin	The minimum x value of the subtree.	
	 * @param yMax	The maximum y value of the subtree.	
	 * @param yMin	The minimum y value of the subtree.	
	 * @param zMax	The maximum z value of the subtree.	
	 * @param zMin	The minimum z value of the subtree.	
	 * @return		Will either return the subtree with the object deleted or the subtree unchanged.
	 */
	@SuppressWarnings("unchecked")
	private prOctNode deleteHelper(T elem, prOctNode root, double xMax, double xMin, double yMax, double yMin, double zMax, double zMin){
		//Basic null and in region check.
		if(root == null || !elem.inBox(xMin, xMax, yMin, yMax, zMin, zMax))
			return root;
		//When the subtree is a leaf node.
		else if(root instanceof prOctTree.prOctLeaf){
			prOctLeaf handle = (prOctLeaf) root;
			if(handle.Elements.contains(elem))
				deletedValue = elem;
			handle.Elements.remove(elem);
			//Leaf nodes should not exist with nothing in them, doesn't make sense.
			if(handle.Elements.isEmpty())
				root = null;
			return root;
		}
		//When the subtree is an internal node.
		else if(root instanceof prOctTree.prOctNodeInternal){
			prOctNodeInternal handle = (prOctNodeInternal) root;
			Direction destDirection = elem.inQuadrant(xMin, xMax, yMin, yMax, zMin, zMax);
			double averageX = (xMin + xMax) / 2;
			double averageY = (yMin + yMax) / 2;
			double averageZ = (zMin + zMax) / 2;
			//If it is destined for the north, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.UNW || destDirection == Direction.DNE || destDirection == Direction.DNW)
				yMin = averageY;
			else
				yMax = averageY;
			//If it is destined for the east, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.USE || destDirection == Direction.DNE || destDirection == Direction.DSE)
				xMin = averageX;	
			else
				xMax = averageX;
			//If it is destined for up, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.UNW || destDirection == Direction.USW || destDirection == Direction.USE)
				zMin = averageZ;
			else
				zMax = averageZ;
			switch(destDirection){
			case DNE:{
				handle.DNE = deleteHelper(elem, handle.DNE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case DNW:{
				handle.DNW = deleteHelper(elem, handle.DNW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case DSE:{
				handle.DSE = deleteHelper(elem, handle.DSE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case DSW:{
				handle.DSW = deleteHelper(elem, handle.DSW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case UNE:{
				handle.UNE = deleteHelper(elem, handle.UNE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case UNW:{
				handle.UNW = deleteHelper(elem, handle.UNW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case USE:{
				handle.USE = deleteHelper(elem, handle.USE, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case USW:{
				handle.USW = deleteHelper(elem, handle.USW, xMax, xMin, yMax, yMin, zMax, zMin);
				break;
			}
			case NOQUADRANT:{
				return root;
			}
			}
			//Logic to make sure that if a internal node as less than 4 objects in its subtree that it pulls those four up into a new leaf node.
			root = consolidate(handle);
		}
		return root;
	}
	
	/**
	 * This method checks an given subtree contains the fewest possible nodes.  For example if an internal node had 3 objects in various
	 * leaf nodes under it then it can be changes to a single leaf node containing three objects. 
	 * @param root	The subtree to consolidate.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private prOctNode consolidate(prOctNode root){
		if(root == null)
			return null;
		if(root instanceof prOctTree.prOctLeaf)
			return root;
		if(root instanceof prOctTree.prOctNodeInternal){
			prOctNodeInternal handle = (prOctNodeInternal) root;
			Vector<T> holder = new Vector<T>();
			//Used to keep track of how many sub-nodes of the root are internal nodes.
			int internalCount = 0;
			if(handle.DNE instanceof prOctTree.prOctLeaf){
				prOctLeaf handle2 = (prOctLeaf) handle.DNE;
				holder.addAll(handle2.Elements);
			}
			else if(handle.DNE instanceof prOctTree.prOctNodeInternal){
				internalCount++;
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.DNW instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.DNW;
					holder.addAll(handle2.Elements);
				}
				else if(handle.DNW instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.DSW instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.DSW;
					holder.addAll(handle2.Elements);
				}
				else if(handle.DSW instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.DSE instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.DSE;
					holder.addAll(handle2.Elements);
				}
				else if(handle.DSE instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.UNE instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.UNE;
					holder.addAll(handle2.Elements);
				}
				else if(handle.UNE instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.UNW instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.UNW;
					holder.addAll(handle2.Elements);
				}
				else if(handle.UNW instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.USW instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.USW;
					holder.addAll(handle2.Elements);
				}
				else if(handle.USW instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){

				if(handle.USE instanceof prOctTree.prOctLeaf){
					prOctLeaf handle2 = (prOctLeaf) handle.USE;
					holder.addAll(handle2.Elements);
				}
				else if(handle.USE instanceof prOctTree.prOctNodeInternal){
					internalCount++;
				}
			}
			if(internalCount == 0 && holder.size() <= bucketSize){
				root = new prOctLeaf();
				for(T x: holder)
					((prOctLeaf)root).Elements.add(x);
			}
			return root;
		}
		return null;
	}
	
	/**
	 * Attempts to find an object in the tree.
	 * @param elem	The object to find.
	 * @return		Will return null if the object is not found, a valid object if otherwise.
	 */
	public T find(T elem){
		T tracer = findHelper(elem, root, xMax, xMin, yMax, yMin, zMax, zMin);
		if(tracer == null)
			return null;
		return tracer;
	}
	
	/**
	 * This is a recursive method to help the find method do it's job. 
	 * @param elem	The object to find
	 * @param root	The subtree to look under.
	 * @param xMax	The maximum x value of the region. 
	 * @param xMin	The minimum x value of the region. 
	 * @param yMax	The maximum y value of the region. 
	 * @param yMin	The minimum y value of the region. 
	 * @param zMax	The maximum z value of the region. 
	 * @param zMin	The minimum z value of the region. 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T findHelper(T elem, prOctNode root, double xMax, double xMin, double yMax, double yMin, double zMax, double zMin){
		if(root == null || !elem.inBox(xMin, xMax, yMin, yMax, zMin, zMax))
			return null;
		else if(root instanceof prOctTree.prOctLeaf){
			prOctLeaf handle = (prOctLeaf) root;
			for(T x: handle.Elements)
				//if any of the elements in the Vector equal the search item, return this node.
				if(x.equals(elem))
					return x;
			return null;
		}
		else if(root instanceof prOctTree.prOctNodeInternal){
			prOctNodeInternal handle = (prOctNodeInternal) root;
			Direction destDirection = elem.inQuadrant(xMin, xMax, yMin, yMax, zMin, zMax);
			double averageX = (xMin + xMax) / 2;
			double averageY = (yMin + yMax) / 2;
			double averageZ = (zMin + zMax) / 2;
			//If it is destined for the north, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.UNW || destDirection == Direction.DNE || destDirection == Direction.DNW)
				yMin = averageY;
			else
				yMax = averageY;
			//If it is destined for the east, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.USE || destDirection == Direction.DNE || destDirection == Direction.DSE)
				xMin = averageX;	
			else
				xMax = averageX;
			//If it is destined for up, bump up the min, otherwise bump down the max
			if(destDirection == Direction.UNE || destDirection == Direction.UNW || destDirection == Direction.USW || destDirection == Direction.USE)
				zMin = averageZ;
			else
				zMax = averageZ;
			switch(destDirection){
			case DNE:{
				return findHelper(elem, handle.DNE, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case DNW:{
				return findHelper(elem, handle.DNW, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case DSE:{
				return findHelper(elem, handle.DSE, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case DSW:{
				return findHelper(elem, handle.DSW, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case UNE:{
				return findHelper(elem, handle.UNE, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case UNW:{
				return findHelper(elem, handle.UNW, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case USE:{
				return findHelper(elem, handle.USE, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case USW:{
				return findHelper(elem, handle.USW, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			case NOQUADRANT:{
				return findHelper(elem, handle.UNE, xMax, xMin, yMax, yMin, zMax, zMin);
			}
			}
		}
		return null;
	}
	
	/**
	 * This method finds all the points that fall on the line segment drawn between a and b.
	 * @param a
	 * @param b
	 * @return
	 */
	public Vector<T> findOnLine(Point a, Point b){
		return findOnLineHelper(root, a, b, xMin, xMax, yMin, yMax, zMin, zMax);
	}
	/**
	 * 
	 * @param root
	 * @param a
	 * @param b
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param zMin
	 * @param zMax
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Vector<T> findOnLineHelper(prOctNode root, Point a, Point b, double xMin,
			double xMax, double yMin, double yMax, double zMin, double zMax) {
		if(root == null || a == null || b == null)
			return null;
		Point norm = normalize(a, b);
		Vector<T> holder = new Vector<T>();
		if(root instanceof prOctTree.prOctLeaf){
			prOctLeaf handle = (prOctLeaf) root;
			double xLoAB;
			double xHiAB;
			double yLoAB;
			double yHiAB;
			double zLoAB;
			double zHiAB;
			if(a.getX() <= b.getX()){
				xLoAB = a.getX();
				xHiAB = b.getX();
			}
			else{
				xLoAB = b.getX();
				xHiAB = a.getX();
			}
			if(a.getY() <= b.getY()){
				yLoAB = a.getY();
				yHiAB = b.getY();
			}
			else{
				yLoAB = b.getY();
				yHiAB = a.getY();
			}
			if(a.getZ() <= b.getZ()){
				zLoAB = a.getZ();
				zHiAB = b.getZ();
			}
			else{
				zLoAB = b.getZ();
				zHiAB = a.getZ();
			}
			for(T x: handle.Elements){
				if(x.inBox(xLoAB, xHiAB, yLoAB, yHiAB, zLoAB, zHiAB) && onLine(a, norm, new Point(x.getX(), x.getY(), x.getZ())))
					holder.add(x);
			}
			return holder;
		}
		else if(root instanceof prOctTree.prOctNodeInternal){
			prOctNodeInternal handle = (prOctNodeInternal) root;
			double xAvg = (xMin + xMax) / 2;
			double yAvg = (yMin + yMax) / 2;
			double zAvg = (zMin + zMax) / 2;
			//Check in the following order, UNE-UNW-USW-USE-DNE-DNW-DSW-DSE
			//UNE
			if
			(
				lineIntersectsBox(a, b, new Point(xAvg, yAvg, zAvg), new Point(xMax, yMax, zMax)) || 
				a.inBox(xAvg, xMax, yAvg, yMax, zAvg, zMax) || 
				b.inBox(xAvg, xMax, yAvg, yMax, zAvg, zMax)
			)
				holder.addAll(findOnLineHelper(handle.UNE, a, b, xAvg, xMax, yAvg, yMax, zAvg, zMax));
			//UNW
			if
			(
				lineIntersectsBox(a, b, new Point(xMin, yAvg, zAvg), new Point(xAvg, yMax, zMax)) || 
				a.inBox(xMin, xAvg, yAvg, yMax, zAvg, zMax) || 
				b.inBox(xMin, xAvg, yAvg, yMax, zAvg, zMax)
			)
				holder.addAll(findOnLineHelper(handle.UNW, a, b, xMin, xAvg, yAvg, yMax, zAvg, zMax));
			//USW
			if
			(
				lineIntersectsBox(a, b, new Point(xMin, yMin, zAvg), new Point(xAvg, yAvg, zMax)) || 
				a.inBox(xMin, xAvg, yMin, yAvg, zAvg, zMax) || 
				b.inBox(xMin, xAvg, yMin, yAvg, zAvg, zMax)
			)
				holder.addAll(findOnLineHelper(handle.USW, a, b, xMin, xAvg, yMin, yAvg, zAvg, zMax));
			//USE
			if
			(
				lineIntersectsBox(a, b, new Point(xAvg, yMin, zAvg), new Point(xMax, yAvg, zMax)) || 
				a.inBox(xAvg, xMax, yMin, yAvg, zAvg, zMax) || 
				b.inBox(xAvg, xMax, yMin, yAvg, zAvg, zMax)
			)
				holder.addAll(findOnLineHelper(handle.USE, a, b, xAvg, xMax, yMin, yAvg, zAvg, zMax));
			//DNE
			if
			(
				lineIntersectsBox(a, b, new Point(xAvg, yAvg, zMin), new Point(xMax, yMax, zAvg)) || 
				a.inBox(xAvg, xMax, yAvg, yMax, zMin, zAvg) || 
				b.inBox(xAvg, xMax, yAvg, yMax, zMin, zAvg)
			)
				holder.addAll(findOnLineHelper(handle.DNE, a, b, xAvg, xMax, yAvg, yMax, zMin, zAvg));
			//DNW
			if
			(
				lineIntersectsBox(a, b, new Point(xMin, yAvg, zMin), new Point(xAvg, yMax, zAvg)) || 
				a.inBox(xMin, xAvg, yAvg, yMax, zMin, zAvg) || 
				b.inBox(xMin, xAvg, yAvg, yMax, zMin, zAvg)
			)
				holder.addAll(findOnLineHelper(handle.DNW, a, b, xMin, xAvg, yAvg, yMax, zMin, zAvg));
			//DSW
			if(
				lineIntersectsBox(a, b, new Point(xMin, yMin, zMin), new Point(xAvg, yAvg, zAvg)) || 
				a.inBox(xMin, xAvg, yMin, yAvg, zMin, zAvg) || 
				b.inBox(xMin, xAvg, yMin, yAvg, zMin, zAvg)
			)
				holder.addAll(findOnLineHelper(handle.DSW, a, b, xMin, xAvg, yMin, yAvg, zMin, zAvg));
			//DSE
			if
			(
				lineIntersectsBox(a, b, new Point(xAvg, yMin, zMin), new Point(xMax, yAvg, zAvg)) || 
				a.inBox(xAvg, xMax, yMin, yAvg, zMin, zAvg) || 
				b.inBox(xAvg, xMax, yMin, yAvg, zMin, zAvg)
			)
				holder.addAll(findOnLineHelper(handle.DSE, a, b, xAvg, xMax, yMin, yAvg, zMin, zAvg));	
			return holder;
		}
		return null;
	}
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private Point normalize(Point a, Point b){
		if(a == null || b == null)
			return null;
		double diffX = a.getX() - b.getX();
		double diffY = a.getY() - b.getY();
		double diffZ = a.getZ() - b.getZ();
		double magnitude = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2));
		return new Point(diffX / magnitude, diffY / magnitude, diffZ / magnitude);
	}
	/**
	 * 
	 * @param rayStart
	 * @param vector
	 * @param test
	 * @return
	 */
	private boolean onLine(Point rayStart, Point vector, Point test){
		double xT = (test.getX() - rayStart.getX()) / vector.getX();
		double yT = (test.getY() - rayStart.getY()) / vector.getY();
		double zT = (test.getZ() - rayStart.getZ()) / vector.getZ();
		int nanCount = 0;
		if(new Double(xT).isNaN())
			nanCount++;
		if(new Double(yT).isNaN())
			nanCount++;
		if(new Double(zT).isNaN())
			nanCount++;
		if(nanCount == 2)
			return true;
		if(nanCount == 1){
			if(new Double(xT).isNaN())
				return yT == zT;
			if(new Double(yT).isNaN())
				return xT == zT;
			if(new Double(zT).isNaN())
				return xT == yT;
		}
		return (xT == yT) && (xT == zT);
	}

	/**
	 * Determines if the line segment between A and B intersects the Rectangle specified
	 * by SW and NE.  SW and NE should fall on the same horizontal or vertical plane
	 * @param A
	 * @param B
	 * @param SW
	 * @param NE
	 * @return
	 */
	private boolean lineIntersectsHorizontalSquare(Point A, Point B, Point SW, Point NE){
		//determine whether it is x constant, y constant, or z constant
		Point norm = normalize(A, B);double xMin;
		//determines the region bounds set by A and B
		double xMax;
		double yMin;
		double yMax;
		double zMin;
		double zMax;
		if(A.getX() <= B.getX()){
			xMin = A.getX();
			xMax = B.getX();
		}
		else{
			xMin = B.getX();
			xMax = A.getX();
		}
		if(A.getY() <= B.getY()){
			yMin = A.getY();
			yMax = B.getY();
		}
		else{
			yMin = B.getY();
			yMax = A.getY();
		}
		if(A.getZ() <= B.getZ()){
			zMin = A.getZ();
			zMax = B.getZ();
		}
		else{
			zMin = B.getZ();
			zMax = A.getZ();
		}
		if(NE.getX() == SW.getX()){
			double t = (NE.getX() - A.getX()) / norm.getX();
			double yIntersect = A.getY() + t * norm.getY(); 
			double zIntersect = A.getZ() + t * norm.getZ();
			Point intersect = new Point(NE.getX(), yIntersect, zIntersect);
			//check that the intersection point lies in the box
			if(!intersect.inBox(SW.getX(), NE.getX(), SW.getY(), NE.getY(), SW.getZ(), NE.getZ()))
				return false;
			//check that the intersection point lies between A and B
			return intersect.inBox(xMin, xMax, yMin, yMax, zMin, zMax);
		}
		else if(NE.getY() == SW.getY()){
			double t = (NE.getY() - A.getY()) / norm.getY();
			double xIntersect = A.getX() + t * norm.getX(); 
			double zIntersect = A.getZ() + t * norm.getZ();
			Point intersect = new Point(xIntersect, NE.getY(), zIntersect);
			//check that the intersection point lies in the box
			if(!intersect.inBox(SW.getX(), NE.getX(), SW.getY(), NE.getY(), SW.getZ(), NE.getZ()))
				return false;
			//check that the intersection point lies between A and B
			return intersect.inBox(xMin, xMax, yMin, yMax, zMin, zMax);
		}
		else if(NE.getZ() == SW.getZ()){
			double t = (NE.getZ() - A.getZ()) / norm.getZ();
			double xIntersect = A.getX() + t * norm.getX(); 
			double yIntersect = A.getY() + t * norm.getY();
			Point intersect = new Point(xIntersect, yIntersect, NE.getZ());
			//check that the intersection point lies in the box
			if(!intersect.inBox(SW.getX(), NE.getX(), SW.getY(), NE.getY(), SW.getZ(), NE.getZ()))
				return false;
			//check that the intersection point lies between A and B
			return intersect.inBox(xMin, xMax, yMin, yMax, zMin, zMax);
		}
		return false;
	}
	/**
	 * 
	 * @param A
	 * @param B
	 * @param DSW
	 * @param UNE
	 * @return
	 */
	private boolean lineIntersectsBox(Point A, Point B, Point DSW, Point UNE){
		//Test in order of front, left, back, right, up, then down.
		return  (lineIntersectsHorizontalSquare(A, B, new Point(DSW.getX(), DSW.getY(), UNE.getZ()), new Point(UNE.getX(), UNE.getY(), UNE.getZ()))) ||
				(lineIntersectsHorizontalSquare(A, B, new Point(DSW.getZ(), DSW.getY(), DSW.getX()), new Point(UNE.getZ(), UNE.getY(), DSW.getX()))) || 
				(lineIntersectsHorizontalSquare(A, B, new Point(DSW.getX(), DSW.getY(), DSW.getZ()), new Point(UNE.getX(), UNE.getY(), DSW.getZ()))) || 
				(lineIntersectsHorizontalSquare(A, B, new Point(DSW.getZ(), DSW.getY(), UNE.getX()), new Point(UNE.getZ(), UNE.getY(), UNE.getX()))) || 
				(lineIntersectsHorizontalSquare(A, B, new Point(DSW.getX(), UNE.getY(), DSW.getZ()), new Point(UNE.getX(), UNE.getY(), UNE.getZ()))) || 
				(lineIntersectsHorizontalSquare(A, B, new Point(DSW.getX(), DSW.getY(), DSW.getZ()), new Point(UNE.getX(), DSW.getY(), UNE.getZ())));
	}
}
