package math.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.xml.internal.ws.assembler.jaxws.HandlerTubeFactory;

class BoundingBox {
	int xl, xr, yt, yb;
}

// ---------------------------------------------------------------------------
// Red-Black tree code (based on C version of "rbtree" by Franck Bui-Huu
// https://github.com/fbuihuu/libtree/blob/master/rb.c

class RBTree {

	CircleEvent root;

	RBTree() {
		root = null;
	}

	void rbInsertSuccessor(CircleEvent node, CircleEvent successor) {
		CircleEvent parent;
		if (node != null) {
			successor.rbPrevious = node;
			successor.rbNext = node.rbNext;
			if (node.rbNext != null) {
				node.rbNext.rbPrevious = successor;
			}
			node.rbNext = successor;
			if (node.rbRight != null) {
				// in-place expansion of node.rbRight.getFirst();
				node = node.rbRight;
				while (node.rbLeft != null) {
					node = node.rbLeft;
				}
				node.rbLeft = successor;
			} else {
				node.rbRight = successor;
			}
			parent = node;
		} else if (root != null) {
			node = this.getFirst(this.root);

			successor.rbPrevious = null;
			successor.rbNext = node;
			node.rbPrevious = successor;

			node.rbLeft = successor;
			parent = node;
		} else {
			successor.rbPrevious = successor.rbNext = null;
			this.root = successor;
			parent = null;
		}
		successor.rbLeft = successor.rbRight = null;
		successor.rbParent = parent;
		successor.rbRed = true;
		// Fixup the modified tree by recoloring nodes and performing
		// rotations (2 at most) hence the red-black tree properties are
		// preserved.
		CircleEvent grandpa, uncle;

		node = successor;
		while (parent != null && parent.rbRed) {
			grandpa = parent.rbParent;
			if (parent == grandpa.rbLeft) {
				uncle = grandpa.rbRight;
				if (uncle != null && uncle.rbRed) {
					parent.rbRed = uncle.rbRed = false;
					grandpa.rbRed = true;
					node = grandpa;
				} else {
					if (node == parent.rbRight) {
						this.rbRotateLeft(parent);
						node = parent;
						parent = node.rbParent;
					}
					parent.rbRed = false;
					grandpa.rbRed = true;
					this.rbRotateRight(grandpa);
				}
			} else {
				uncle = grandpa.rbLeft;
				if (uncle != null && uncle.rbRed) {
					parent.rbRed = uncle.rbRed = false;
					grandpa.rbRed = true;
					node = grandpa;
				} else {
					if (node == parent.rbLeft) {
						this.rbRotateRight(parent);
						node = parent;
						parent = node.rbParent;
					}
					parent.rbRed = false;
					grandpa.rbRed = true;
					this.rbRotateLeft(grandpa);
				}
			}
			parent = node.rbParent;
		}
		root.rbRed = false;
	}

	void rbRemoveNode(CircleEvent node) {
		if (node.rbNext != null) {
			node.rbNext.rbPrevious = node.rbPrevious;
		}
		if (node.rbPrevious != null) {
			node.rbPrevious.rbNext = node.rbNext;
		}
		node.rbNext = node.rbPrevious = null;

		CircleEvent parent = node.rbParent, left = node.rbLeft,
						right = node.rbRight, next;
		if (left == null) {
			next = right;
		} else if (right == null) {
			next = left;
		} else {
			next = getFirst(right);
		}
		if (parent != null) {
			if (parent.rbLeft == node) {
				parent.rbLeft = next;
			} else {
				parent.rbRight = next;
			}
		} else {
			this.root = next;
		}

		// enforce red-black rules
		boolean isRed;

		if (left != null && right != null) {
			isRed = next.rbRed;
			next.rbRed = node.rbRed;
			next.rbLeft = left;
			left.rbParent = next;

			if (next != right) {
				parent = next.rbParent;
				next.rbParent = node.rbParent;
				node = next.rbRight;
				parent.rbLeft = node;
				next.rbRight = right;
				right.rbParent = next;
			} else {
				next.rbParent = parent;
				parent = next;
				node = next.rbRight;
			}
		} else {
			isRed = node.rbRed;
			node = next;
		}

		// 'node' is now the sole successor's child and 'parent' its
		// new parent (since the successor can have been moved)
		if (node != null) {
			node.rbParent = parent;
		}
		// the 'easy' cases
		if (isRed) {
			return;
		}
		if (node != null && node.rbRed) {
			node.rbRed = false;
			return;
		}

		// the other cases
		CircleEvent sibling;
		do {
			if (node == root) {
				break;
			}

			if (node == parent.rbLeft) {
				sibling = parent.rbRight;
				if (sibling.rbRed) {
					sibling.rbRed = false;
					parent.rbRed = true;
					rbRotateLeft(parent);
					sibling = parent.rbRight;
				}
				if ((sibling.rbLeft != null && sibling.rbLeft.rbRed)
								|| (sibling.rbRight != null
												&& sibling.rbRight.rbRed)) {
					if (sibling.rbRight == null || !sibling.rbRight.rbRed) {
						sibling.rbLeft.rbRed = false;
						sibling.rbRed = true;
						rbRotateRight(sibling);
						sibling = parent.rbRight;
					}
					sibling.rbRed = parent.rbRed;
					parent.rbRed = sibling.rbRight.rbRed = false;
					rbRotateLeft(parent);
					node = root;
					break;
				}
			} else {
				sibling = parent.rbLeft;
				if (sibling.rbRed) {
					sibling.rbRed = false;
					parent.rbRed = true;
					rbRotateRight(parent);
					sibling = parent.rbLeft;
				}
				if ((sibling.rbLeft != null && sibling.rbLeft.rbRed)
								|| (sibling.rbRight != null
												&& sibling.rbRight.rbRed)) {
					if (sibling.rbLeft == null || !sibling.rbLeft.rbRed) {
						sibling.rbRight.rbRed = false;
						sibling.rbRed = true;
						rbRotateLeft(sibling);
						sibling = parent.rbLeft;
					}
					sibling.rbRed = parent.rbRed;
					parent.rbRed = sibling.rbLeft.rbRed = false;
					rbRotateRight(parent);
					node = root;
					break;
				}
			}
			sibling.rbRed = true;
			node = parent;
			parent = parent.rbParent;
		} while (!node.rbRed);

		if (node != null) {
			node.rbRed = false;
		}
	}

	void rbRotateLeft(CircleEvent node) {
		CircleEvent p = node, q = node.rbRight, // can't be null
						parent = p.rbParent;
		if (parent != null) {
			if (parent.rbLeft == p) {
				parent.rbLeft = q;
			} else {
				parent.rbRight = q;
			}
		} else {
			this.root = q;
		}
		q.rbParent = parent;
		p.rbParent = q;
		p.rbRight = q.rbLeft;
		if (p.rbRight != null) {
			p.rbRight.rbParent = p;
		}
		q.rbLeft = p;
	}

	void rbRotateRight(CircleEvent node) {
		CircleEvent p = node, q = node.rbLeft, // can't be null
						parent = p.rbParent;
		if (parent != null) {
			if (parent.rbLeft == p) {
				parent.rbLeft = q;
			} else {
				parent.rbRight = q;
			}
		} else {
			this.root = q;
		}
		q.rbParent = parent;
		p.rbParent = q;
		p.rbLeft = q.rbRight;
		if (p.rbLeft != null) {
			p.rbLeft.rbParent = p;
		}
		q.rbRight = p;
	}

	CircleEvent getFirst(CircleEvent node) {
		while (node.rbLeft != null) {
			node = node.rbLeft;
		}
		return node;
	}

	CircleEvent getLast(CircleEvent node) {
		while (node.rbRight != null) {
			node = node.rbRight;
		}
		return node;
	}
}

// ---------------------------------------------------------------------------
// Diagram methods

class Diagram {
	Vertex site;

	Diagram(Vertex site) {

		this.site = site;
	}
}

// ---------------------------------------------------------------------------
// Cell methods

class Cell {
	Vertex site;
	List<Halfedge> halfedges;
	boolean closeMe;

	Cell(Vertex site) {
		this.site = site;
		halfedges = new ArrayList<Halfedge>();
		closeMe = false;
	}
}

// ---------------------------------------------------------------------------
// Edge methods
//

class Vertex implements Comparable<Vertex> {
	double x, y;
	int voronoiId;

	Vertex(double x, double y) {

		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Vertex o) {
		double r = o.y - y;
		if (r < 0) {
			return -1;
		}
		if (r > 0) {
			return 1;
		}
		r = o.x - x;
		if (r < 0) {
			return -1;
		}
		if (r > 0) {
			return 1;
		}
		return 0;
	}
}

class Edge {
	Vertex lSite, rSite, va, vb;

	Edge(Vertex lSite, Vertex rSite) {

		this.lSite = lSite;
		this.rSite = rSite;
		this.va = this.vb = null;
	}
}

class Halfedge {
	Vertex site;
	Edge edge;
	double angle;

	Halfedge(Edge edge, Vertex lSite, Vertex rSite) {

		this.site = lSite;
		this.edge = edge;
		// 'angle' is a value to be used for properly sorting the
		// halfsegments counterclockwise. By convention, we will
		// use the angle of the line defined by the 'site to the left'
		// to the 'site to the right'.
		// However, border edges have no 'site to the right': thus we
		// use the angle of line perpendicular to the halfsegment (the
		// edge should have both end points defined in such case.)
		if (rSite != null) {
			this.angle = Math.atan2(rSite.y - lSite.y, rSite.x - lSite.x);
		} else {
			Vertex va = edge.va, vb = edge.vb;

			this.angle = edge.lSite == lSite
							? Math.atan2(vb.x - va.x, va.y - vb.y)
							: Math.atan2(va.x - vb.x, vb.y - va.y);
		}
	}
}

// ---------------------------------------------------------------------------
// Circle event methods

class CircleEvent {

	CircleEvent arc, rbLeft, rbNext, rbParent, rbPrevious, rbRight, circleEvent;
	boolean rbRed;
	Vertex site;
	Edge edge;
	double x, y, yCenter;

	CircleEvent() {
		this.arc = null;
		this.rbLeft = null;
		this.rbNext = null;
		this.rbParent = null;
		this.rbPrevious = null;
		this.rbRed = false;
		this.rbRight = null;
		this.circleEvent = null;
		this.site = null;
		this.edge = null;
		this.x = this.y = this.yCenter = 0;
	}
}

/******************************************************************************/

public class Voronoi {

	private static final double EPSILON = 1e-9;
	private static final double INV_EPSILON = 1.0 / EPSILON;

	private List<Vertex> vertices;
	private List<Edge> edges;
	private Cell[] cells;
	private RBTree beachline;
	private RBTree circleEvents;
	private CircleEvent firstCircleEvent;

	Voronoi() {
		vertices = new ArrayList<Vertex>();
		;
		edges = new ArrayList<Edge>();
		cells = null;

		beachline = new RBTree();
		circleEvents = new RBTree();

		firstCircleEvent = null;
	}

	boolean equalWithEpsilon(double a, double b) {
		return Math.abs(a - b) < EPSILON;
	}

	boolean greaterThanWithEpsilon(double a, double b) {
		return a - b > EPSILON;
	}

	boolean greaterThanOrEqualWithEpsilon(double a, double b) {
		return b - a < EPSILON;
	}

	boolean lessThanWithEpsilon(double a, double b) {
		return b - a > EPSILON;
	}

	boolean lessThanOrEqualWithEpsilon(double a, double b) {
		return a - b < EPSILON;
	}

	// this create and add an edge to internal collection, and also create
	// two halfedges which are added to each site's counterclockwise array
	// of halfedges.

	// this create and add a vertex to the internal collection

	Vertex createVertex(double x, double y) {
		Vertex v = new Vertex(x, y);
		this.vertices.add(v);
		return v;
	}

	Edge createEdge(Vertex lSite, Vertex rSite, Vertex va, Vertex vb) {
		Edge edge = new Edge(lSite, rSite);

		edges.add(edge);
		if (va != null) {
			setEdgeStartpoint(edge, lSite, rSite, va);
		}
		if (vb != null) {
			setEdgeEndpoint(edge, lSite, rSite, vb);
		}
		this.cells[lSite.voronoiId].halfedges
						.add(new Halfedge(edge, lSite, rSite));
		this.cells[rSite.voronoiId].halfedges
						.add(new Halfedge(edge, rSite, lSite));
		return edge;
	}

	void setEdgeStartpoint(Edge edge, Vertex lSite, Vertex rSite,
					Vertex vertex) {
		if (edge.va == null && edge.vb == null) {
			edge.va = vertex;
			edge.lSite = lSite;
			edge.rSite = rSite;
		} else if (edge.lSite == rSite) {
			edge.vb = vertex;
		} else {
			edge.va = vertex;
		}
	}

	void setEdgeEndpoint(Edge edge, Vertex lSite, Vertex rSite, Vertex vertex) {
		setEdgeStartpoint(edge, rSite, lSite, vertex);
	}

	CircleEvent createBeachsection(Vertex site) {
		CircleEvent beachsection = new CircleEvent();
		beachsection.site = site;
		return beachsection;
	}

	void addBeachsection(Vertex site) {
		double x = site.x, directrix = site.y;

		// find the left and right beach sections which will surround the newly
		// created beach section.

		CircleEvent lArc = null, rArc = null;
		double dxl, dxr;
		CircleEvent node = beachline.root;

		while (node != null) {
			dxl = leftBreakPoint(node, directrix) - x;
			// x lessThanWithEpsilon xl => falls somewhere before the left edge
			// of the beachsection
			if (dxl > EPSILON) {
				// this case should never happen
				// if (!node.rbLeft) {
				// rArc = node.rbLeft;
				// break;
				// }
				node = node.rbLeft;
			} else {
				dxr = x - rightBreakPoint(node, directrix);
				// x greaterThanWithEpsilon xr => falls somewhere after the
				// right edge of the beachsection
				if (dxr > EPSILON) {
					if (node.rbRight == null) {
						lArc = node;
						break;
					}
					node = node.rbRight;
				} else {
					// x equalWithEpsilon xl => falls exactly on the left edge
					// of the beachsection
					if (dxl > -EPSILON) {
						lArc = node.rbPrevious;
						rArc = node;
					}
					// x equalWithEpsilon xr => falls exactly on the right edge
					// of the beachsection
					else if (dxr > -EPSILON) {
						lArc = node;
						rArc = node.rbNext;
					}
					// falls exactly somewhere in the middle of the beachsection
					else {
						lArc = rArc = node;
					}
					break;
				}
			}
		}
		// at this point, keep in mind that lArc and/or rArc could be
		// undefined or null.

		// create a new beach section object for the site and add it to RB-tree
		CircleEvent newArc = createBeachsection(site);
		beachline.rbInsertSuccessor(lArc, newArc);

		// cases:
		//

		// [null,null]
		// least likely case: new beach section is the first beach section on
		// the
		// beachline.
		// This case means:
		// no new transition appears
		// no collapsing beach section
		// new beachsection become root of the RB-tree
		if (lArc == null && rArc == null) {
			return;
		}

		// [lArc,rArc] where lArc == rArc
		// most likely case: new beach section split an existing beach
		// section.
		// This case means:
		// one new transition appears
		// the left and right beach section might be collapsing as a result
		// two new nodes added to the RB-tree
		if (lArc == rArc) {
			// invalidate circle event of split beach section
			detachCircleEvent(lArc);

			// split the beach section into two separate beach sections
			rArc = this.createBeachsection(lArc.site);
			this.beachline.rbInsertSuccessor(newArc, rArc);

			// since we have a new transition between two beach sections,
			// a new edge is born
			newArc.edge = rArc.edge = this.createEdge(lArc.site, newArc.site,
							null, null);

			// check whether the left and right beach sections are collapsing
			// and if so create circle events, to be notified when the point of
			// collapse is reached.
			this.attachCircleEvent(lArc);
			this.attachCircleEvent(rArc);
			return;
		}

		// [lArc,null]
		// even less likely case: new beach section is the *last* beach section
		// on the beachline -- this can happen *only* if *all* the previous
		// beach
		// sections currently on the beachline share the same y value as
		// the new beach section.
		// This case means:
		// one new transition appears
		// no collapsing beach section as a result
		// new beach section become right-most node of the RB-tree
		if (lArc != null && rArc == null) {
			newArc.edge = createEdge(lArc.site, newArc.site, null, null);
			return;
		}

		// [lArc,rArc] where lArc != rArc
		// somewhat less likely case: new beach section falls *exactly* in
		// between two
		// existing beach sections
		// This case means:
		// one transition disappears
		// two new transitions appear
		// the left and right beach section might be collapsing as a result
		// only one new node added to the RB-tree
		if (lArc != rArc) {
			// invalidate circle events of left and right sites
			this.detachCircleEvent(lArc);
			this.detachCircleEvent(rArc);

			// an existing transition disappears, meaning a vertex is defined at
			// the disappearance point.
			// since the disappearance is caused by the new beachsection, the
			// vertex is at the center of the circumscribed circle of the left,
			// new and right beachsections.
			// http://mathforum.org/library/drmath/view/55002.html
			// Except that I bring the origin at A to simplify
			// calculation
			Vertex lSite = lArc.site;
			double ax = lSite.x, ay = lSite.y, bx = site.x - ax,
							by = site.y - ay;
			Vertex rSite = rArc.site;
			double cx = rSite.x - ax, cy = rSite.y - ay,
							d = 2 * (bx * cy - by * cx), hb = bx * bx + by * by,
							hc = cx * cx + cy * cy;
			Vertex vertex = createVertex((cy * hb - by * hc) / d + ax,
							(bx * hc - cx * hb) / d + ay);

			// one transition disappear
			setEdgeStartpoint(rArc.edge, lSite, rSite, vertex);

			// two new transitions appear at the new vertex location
			newArc.edge = createEdge(lSite, site, null, vertex);
			rArc.edge = createEdge(site, rSite, null, vertex);

			// check whether the left and right beach sections are collapsing
			// and if so create circle events, to handle the point of collapse.
			this.attachCircleEvent(lArc);
			this.attachCircleEvent(rArc);
			return;
		}
	}

	void attachCircleEvent(CircleEvent arc) {
		CircleEvent lArc = arc.rbPrevious, rArc = arc.rbNext;
		if (lArc == null || rArc == null) {
			return;
		} // does that ever happen?
		Vertex lSite = lArc.site, cSite = arc.site, rSite = rArc.site;

		// If site of left beachsection is same as site of
		// right beachsection, there can't be convergence
		if (lSite == rSite) {
			return;
		}

		// Find the circumscribed circle for the three sites associated
		// with the beachsection triplet.
		double bx = cSite.x, by = cSite.y, ax = lSite.x - bx, ay = lSite.y - by,
						cx = rSite.x - bx, cy = rSite.y - by;

		// If points l->c->r are clockwise, then center beach section does not
		// collapse, hence it can't end up as a vertex (we reuse 'd' here, which
		// sign is reverse of the orientation, hence we reverse the test.
		// http://en.wikipedia.org/wiki/Curve_orientation#Orientation_of_a_simple_polygon
		double d = 2 * (ax * cy - ay * cx);
		if (d >= -2e-12) {
			return;
		}

		double ha = ax * ax + ay * ay, hc = cx * cx + cy * cy,
						x = (cy * ha - ay * hc) / d,
						y = (ax * hc - cx * ha) / d, ycenter = y + by;

		// Important: ybottom should always be under or at sweep, so no need
		// to waste CPU cycles by checking

		// recycle circle event object if possible
		CircleEvent circleEvent = new CircleEvent();

		circleEvent.arc = arc;
		circleEvent.site = cSite;
		circleEvent.x = x + bx;
		circleEvent.y = ycenter + Math.sqrt(x * x + y * y); // y bottom
		circleEvent.yCenter = ycenter;
		arc.circleEvent = circleEvent;

		// find insertion point in RB-tree: circle events are ordered from
		// smallest to largest
		CircleEvent predecessor = null, node = this.circleEvents.root;
		while (node != null) {
			if (circleEvent.y < node.y || (circleEvent.y == node.y
							&& circleEvent.x <= node.x)) {
				if (node.rbLeft != null) {
					node = node.rbLeft;
				} else {
					predecessor = node.rbPrevious;
					break;
				}
			} else {
				if (node.rbRight != null) {
					node = node.rbRight;
				} else {
					predecessor = node;
					break;
				}
			}
		}
		this.circleEvents.rbInsertSuccessor(predecessor, circleEvent);
		if (predecessor == null) {
			this.firstCircleEvent = circleEvent;
		}
	}

	void detachCircleEvent(CircleEvent arc) {
		CircleEvent circleEvent = arc.circleEvent;
		if (circleEvent != null) {
			if (circleEvent.rbPrevious == null) {
				this.firstCircleEvent = circleEvent.rbNext;
			}
			circleEvents.rbRemoveNode(circleEvent); // remove from RB-tree
			arc.circleEvent = null;
		}
	}

	// calculate the left break point of a particular beach section,
	// given a particular sweep line
	double leftBreakPoint(CircleEvent arc, double directrix) {
		// http://en.wikipedia.org/wiki/Parabola
		// http://en.wikipedia.org/wiki/Quadratic_equation
		// h1 = x1,
		// k1 = (y1+directrix)/2,
		// h2 = x2,
		// k2 = (y2+directrix)/2,
		// p1 = k1-directrix,
		// a1 = 1/(4*p1),
		// b1 = -h1/(2*p1),
		// c1 = h1*h1/(4*p1)+k1,
		// p2 = k2-directrix,
		// a2 = 1/(4*p2),
		// b2 = -h2/(2*p2),
		// c2 = h2*h2/(4*p2)+k2,
		// x = (-(b2-b1) + Math.sqrt((b2-b1)*(b2-b1) - 4*(a2-a1)*(c2-c1))) /
		// (2*(a2-a1))
		// When x1 become the x-origin:
		// h1 = 0,
		// k1 = (y1+directrix)/2,
		// h2 = x2-x1,
		// k2 = (y2+directrix)/2,
		// p1 = k1-directrix,
		// a1 = 1/(4*p1),
		// b1 = 0,
		// c1 = k1,
		// p2 = k2-directrix,
		// a2 = 1/(4*p2),
		// b2 = -h2/(2*p2),
		// c2 = h2*h2/(4*p2)+k2,
		// x = (-b2 + Math.sqrt(b2*b2 - 4*(a2-a1)*(c2-k1))) / (2*(a2-a1)) + x1

		// change code below at your own risk: care has been taken to
		// reduce errors due to computers' finite arithmetic precision.
		// Maybe can still be improved, will see if any more of this
		// kind of errors pop up again.
		Vertex site = arc.site;
		double rfocx = site.x;
		double rfocy = site.y;
		double pby2 = rfocy - directrix;
		// parabola in degenerate case where focus is on directrix
		if (pby2 == 0) {
			return rfocx;
		}
		CircleEvent lArc = arc.rbPrevious;
		if (lArc == null) {
			return Double.NEGATIVE_INFINITY;
		}
		site = lArc.site;
		double lfocx = site.x, lfocy = site.y, plby2 = lfocy - directrix;
		// parabola in degenerate case where focus is on directrix
		if (plby2 == 0) {
			return lfocx;
		}
		double hl = lfocx - rfocx, aby2 = 1 / pby2 - 1 / plby2, b = hl / plby2;
		if (aby2 != 0) {
			return (-b + Math.sqrt(b * b - 2 * aby2
							* (hl * hl / (-2 * plby2) - lfocy + plby2 / 2
											+ rfocy - pby2 / 2)))
							/ aby2 + rfocx;
		}
		// both parabolas have same distance to directrix, thus break point is
		// midway
		return (rfocx + lfocx) / 2;
	}

	// calculate the right break point of a particular beach section,
	// given a particular directrix
	double rightBreakPoint(CircleEvent arc, double directrix) {
		CircleEvent rArc = arc.rbNext;
		if (rArc != null) {
			return this.leftBreakPoint(rArc, directrix);
		}
		Vertex site = arc.site;
		return site.y == directrix ? site.x : Double.POSITIVE_INFINITY;
	}

	// ---------------------------------------------------------------------------
	// Top-level Fortune loop

	// rhill 2011-05-19:
	// Voronoi sites are kept client-side now, to allow
	// user to freely modify content. At compute time,
	// *references* to sites are copied locally.

	Diagram compute(Vertex[] sites, BoundingBox bbox){
	// to measure execution time
	long startTime = System.currentTimeMillis();

	// Initialize site event queue
	Vertex[] siteEvents = sites.clone();
	Arrays.sort(siteEvents);

    // process queue
    Vertex site = siteEvents[0];
    int siteid = 0;
    double xsitex, xsitey;
    cells = new Cell[sites.length];
    CircleEvent circle;

    // main loop
    for (;;) {
        // we need to figure whether we handle a site or circle event
        // for this we find out if there is a site event and it is
        // 'earlier' than the circle event
        circle = this.firstCircleEvent;

        // add beach section
        if (site != null && (circle == null || site.y < circle.y || (site.y == circle.y && site.x < circle.x))) {
            // only if site is not a duplicate
            if (site.x != xsitex || site.y != xsitey) {
                // first create cell for new site
                cells[siteid] = new Cell(site);
                site.voronoiId = siteid++;
                // then create a beachsection for that site
                this.addBeachsection(site);
                // remember last site coords to detect duplicate
                xsitey = site.y;
                xsitex = site.x;
                }
            site = siteEvents[siteid];
            }

        // remove beach section
        else if (circle != null) {
            this.removeBeachsection(circle.arc);
            }

        // all done, quit
        else {
            break;
            }
        }

    // wrapping-up:
    //   connect dangling edges to bounding box
    //   cut edges as per bounding box
    //   discard edges completely outside bounding box
    //   discard edges which are point-like
    this.clipEdges(bbox);

    //   add missing edges in order to close opened cells
    this.closeCells(bbox);

    // to measure execution time
    var stopTime = new Date();

    // prepare return values
    var diagram = new this.Diagram();
    diagram.cells = this.cells;
    diagram.edges = this.edges;
    diagram.vertices = this.vertices;
    diagram.execTime = stopTime.getTime()-startTime.getTime();

    // clean up
    this.reset();

    return diagram;
    }

	public static void main(String[] args) {
		Voronoi voronoi = new Voronoi();
		BoundingBox bbox = new BoundingBox();
		bbox.xl = 0;
		bbox.xr = 10;
		bbox.yt = 0;
		bbox.yb = 10;

		Vertex[] sites = new Vertex[2];
		Vertex v1 = new Vertex(0, 0);
		Vertex v2 = new Vertex(5, 5);
		sites[0] = v1;
		sites[1] = v2;

		// a 'vertex' is an object exhibiting 'x' and 'y' properties. The
		// Voronoi object will add a unique 'voronoiId' property to all
		// sites. The 'voronoiId' can be used as a key to lookup the
		// associated cell
		// in diagram.cells.

		Diagram diagram = voronoi.compute(sites, bbox);
	}
}
