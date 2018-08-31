package math.geometry;

import java.util.ArrayList;
import java.util.List;

public class Geometry {
	// Modified C++ routines for computational geometry.

	static final double INF = 1e100;
	static final double EPS = 1e-12;

	static double dot(PT p, PT q) {
		return p.x * q.x + p.y * q.y;
	}

	static double dist2(PT p, PT q) {
		return dot(p.subtract(q), p.subtract(q));
	}

	static double cross(PT p, PT q) {
		return p.x * q.y - p.y * q.x;
	}

	// rotate a point CCW or CW around the origin
	static PT RotateCCW90(PT p) {
		return new PT(-p.y, p.x);
	}

	static PT RotateCW90(PT p) {
		return new PT(p.y, -p.x);
	}

	static PT RotateCCW(PT p, double t) {
		return new PT(p.x * Math.cos(t) - p.y * Math.sin(t), p.x * Math.sin(t) + p.y * Math.cos(t));
	}

	// project point c onto line through a and b
	// assuming a != b
	static PT ProjectPointLine(PT a, PT b, PT c) {
		return a.add(
				b.subtract(a).multiply(dot(c.subtract(a), b.subtract(a))).divide(dot(b.subtract(a), b.subtract(a))));
	}

	// project point c onto line segment through a and b
	static PT ProjectPointSegment(PT a, PT b, PT c) {
		double r = dot(b.subtract(a), b.subtract(a));
		if (Math.abs(r) < EPS)
			return a;
		r = dot(c.subtract(a), b.subtract(a)) / r;
		if (r < 0)
			return a;
		if (r > 1)
			return b;
		return a.add(b.subtract(a).multiply(r));
	}

	// compute distance from c to segment between a and b
	static double DistancePointSegment(PT a, PT b, PT c) {
		return Math.sqrt(dist2(c, ProjectPointSegment(a, b, c)));
	}

	// compute distance between point (x,y,z) and plane ax+by+cz=d
	static double DistancePointPlane(double x, double y, double z, double a, double b, double c, double d) {
		return Math.abs(a * x + b * y + c * z - d) / Math.sqrt(a * a + b * b + c * c);
	}

	// determine if lines from a to b and c to d are parallel or collinear
	static boolean LinesParallel(PT a, PT b, PT c, PT d) {
		return Math.abs(cross(b.subtract(a), c.subtract(d))) < EPS;
	}

	static boolean LinesCollinear(PT a, PT b, PT c, PT d) {
		return LinesParallel(a, b, c, d) && Math.abs(cross(a.subtract(b), a.subtract(c))) < EPS
				&& Math.abs(cross(c.subtract(d), c.subtract(a))) < EPS;
	}

	// determine if line segment from a to b intersects with
	// line segment from c to d
	static boolean SegmentsIntersect(PT a, PT b, PT c, PT d) {
		if (LinesCollinear(a, b, c, d)) {
			if (dist2(a, c) < EPS || dist2(a, d) < EPS || dist2(b, c) < EPS || dist2(b, d) < EPS)
				return true;
			if (dot(c.subtract(a), c.subtract(b)) > 0 && dot(d.subtract(a), d.subtract(b)) > 0
					&& dot(c.subtract(b), d.subtract(b)) > 0)
				return false;
			return true;
		}
		if (cross(d.subtract(a), b.subtract(a)) * cross(c.subtract(a), b.subtract(a)) > 0)
			return false;
		if (cross(a.subtract(c), d.subtract(c)) * cross(b.subtract(c), d.subtract(c)) > 0)
			return false;
		return true;
	}

	// compute intersection of line passing through a and b
	// with line passing through c and d, assuming that unique
	// intersection exists; for segment intersection, check if
	// segments intersect first
	static PT ComputeLineIntersection(PT a, PT b, PT c, PT d) {
		b = b.subtract(a);
		d = c.subtract(d);
		c = c.subtract(a);
		assert (dot(b, b) > EPS && dot(d, d) > EPS);
		return a.add(b.multiply(cross(c, d)).divide(cross(b, d)));
	}

	// compute center of circle given three points
	static PT ComputeCircleCenter(PT a, PT b, PT c) {
		b = (a.add(b)).divide(2);
		c = (a.add(c)).divide(2);
		return ComputeLineIntersection(b, b.add(RotateCW90(a.subtract(b))), c, c.add(RotateCW90(a.subtract(c))));
	}

	// determine if point is in a possibly non-convex polygon (by William
	// Randolph Franklin); returns 1 for strictly interior points, 0 for
	// strictly exterior points, and 0 or 1 for the remaining points.
	// Note that it is possible to convert this into an *exact* test using
	// integer arithmetic by taking care of the division appropriately
	// (making sure to deal with signs properly) and then by writing exact
	// tests for checking point on polygon boundary
	static boolean PointInPolygon(final PT[] p, PT q) {
		boolean c = false;
		for (int i = 0; i < p.length; i++) {
			int j = (i + 1) % p.length;
			if ((p[i].y <= q.y && q.y < p[j].y || p[j].y <= q.y && q.y < p[i].y)
					&& q.x < p[i].x + (p[j].x - p[i].x) * (q.y - p[i].y) / (p[j].y - p[i].y))
				c = !c;
		}
		return c;
	}

	// determine if point is on the boundary of a polygon
	static boolean PointOnPolygon(final PT[] p, PT q) {
		for (int i = 0; i < p.length; i++)
			if (dist2(ProjectPointSegment(p[i], p[(i + 1) % p.length], q), q) < EPS)
				return true;
		return false;
	}

	// compute intersection of line through points a and b with
	// circle centered at c with radius r > 0
	static List<PT> CircleLineIntersection(PT a, PT b, PT c, double r) {
		List<PT> ret = new ArrayList<PT>();
		b = b.subtract(a);
		a = a.subtract(c);

		double A = dot(b, b);
		double B = dot(a, b);
		double C = dot(a, a) - r * r;
		double D = B * B - A * C;

		if (D < -EPS)
			return ret;
		ret.add(c.add(a).add(b.multiply((-B + Math.sqrt(D + EPS))).divide(A)));
		if (D > EPS)
			ret.add(c.add(a.add(b.multiply((-B - Math.sqrt(D))).divide(A))));
		return ret;
	}

	// compute intersection of circle centered at a with radius r
	// with circle centered at b with radius R
	static List<PT> CircleCircleIntersection(PT a, PT b, double r, double R) {
		List<PT> ret = new ArrayList<PT>();
		double d = Math.sqrt(dist2(a, b));
		if (d > r + R || d + Math.min(r, R) < Math.max(r, R))
			return ret;
		double x = (d * d - R * R + r * r) / (2 * d);
		double y = Math.sqrt(r * r - x * x);
		PT v = (b.subtract(a)).divide(d);
		ret.add(a.add(v.multiply(x).add(RotateCCW90(v).multiply(y))));
		if (y > 0)
			ret.add(a.add(v.multiply(x).subtract(RotateCCW90(v).multiply(y))));
		return ret;
	}

	// This code computes the area or centroid of a (possibly nonconvex)
	// polygon, assuming that the coordinates are listed in a clockwise or
	// counterclockwise fashion. Note that the centroid is often known as
	// the "center of gravity" or "center of mass".
	static double ComputeSignedArea(final PT[] p) {
		double area = 0;
		for (int i = 0; i < p.length; i++) {
			int j = (i + 1) % p.length;
			area += p[i].x * p[j].y - p[j].x * p[i].y;
		}
		return area / 2.0;
	}

	static double ComputeArea(final PT[] p) {
		return Math.abs(ComputeSignedArea(p));
	}

	static PT ComputeCentroid(final PT[] p) {
		PT c = new PT(0, 0);
		double scale = 6.0 * ComputeSignedArea(p);

		for (int i = 0; i < p.length; i++) {
			int j = (i + 1) % p.length;
			c = c.add((p[i].add(p[j])).multiply(p[i].x * p[j].y - p[j].x * p[i].y));
		}
		return c.divide(scale);
	}

	// tests whether or not a given polygon (in CW or CCW order) is simple
	static boolean IsSimple(final PT[] p) {
		for (int i = 0; i < p.length; i++) {
			for (int k = i + 1; k < p.length; k++) {
				int j = (i + 1) % p.length;
				int l = (k + 1) % p.length;
				if (i == l || j == k)
					continue;
				if (SegmentsIntersect(p[i], p[j], p[k], p[l]))
					return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {

		// expected: (-5,2)
		System.err.println(RotateCCW90(new PT(2, 5)));

		// expected: (5,-2)
		System.err.println(RotateCW90(new PT(2, 5)));

		// expected: (-5,2)
		System.err.println(RotateCCW(new PT(2, 5), Math.PI / 2));

		// expected: (5,2)
		System.err.println(ProjectPointLine(new PT(-5, -2), new PT(10, 4), new PT(3, 7)));

		// expected: (5,2) (7.5,3) (2.5,1)
		System.err.println(ProjectPointSegment(new PT(-5, -2), new PT(10, 4), new PT(3, 7)) + " "
				+ ProjectPointSegment(new PT(7.5, 3), new PT(10, 4), new PT(3, 7)) + " "
				+ ProjectPointSegment(new PT(-5, -2), new PT(2.5, 1), new PT(3, 7)));

		// expected: 6.78903
		System.err.println(DistancePointPlane(4, -4, 3, 2, -2, 5, -8));

		// expected: 1 0 1
		System.err.println(LinesParallel(new PT(1, 1), new PT(3, 5), new PT(2, 1), new PT(4, 5)) + " "
				+ LinesParallel(new PT(1, 1), new PT(3, 5), new PT(2, 0), new PT(4, 5)) + " "
				+ LinesParallel(new PT(1, 1), new PT(3, 5), new PT(5, 9), new PT(7, 13)));

		// expected: 0 0 1
		System.err.println(LinesCollinear(new PT(1, 1), new PT(3, 5), new PT(2, 1), new PT(4, 5)) + " "
				+ LinesCollinear(new PT(1, 1), new PT(3, 5), new PT(2, 0), new PT(4, 5)) + " "
				+ LinesCollinear(new PT(1, 1), new PT(3, 5), new PT(5, 9), new PT(7, 13)));

		// expected: 1 1 1 0
		System.err.println(SegmentsIntersect(new PT(0, 0), new PT(2, 4), new PT(3, 1), new PT(-1, 3)) + " "
				+ SegmentsIntersect(new PT(0, 0), new PT(2, 4), new PT(4, 3), new PT(0, 5)) + " "
				+ SegmentsIntersect(new PT(0, 0), new PT(2, 4), new PT(2, -1), new PT(-2, 1)) + " "
				+ SegmentsIntersect(new PT(0, 0), new PT(2, 4), new PT(5, 5), new PT(1, 7)));

		// expected: (1,2)
		System.err.println(ComputeLineIntersection(new PT(0, 0), new PT(2, 4), new PT(3, 1), new PT(-1, 3)));

		// expected: (1,1)
		System.err.println(ComputeCircleCenter(new PT(-3, 4), new PT(6, 1), new PT(4, 5)));

		PT[] v = new PT[4];
		v[0] = new PT(0, 0);
		v[1] = new PT(5, 0);
		v[2] = new PT(5, 5);
		v[3] = new PT(0, 5);

		// expected: 1 1 1 0 0
		System.err.println(PointInPolygon(v, new PT(2, 2)) + " " + PointInPolygon(v, new PT(2, 0)) + " "
				+ PointInPolygon(v, new PT(0, 2)) + " " + PointInPolygon(v, new PT(5, 2)) + " "
				+ PointInPolygon(v, new PT(2, 5)));

		// expected: 0 1 1 1 1
		System.err.println(PointOnPolygon(v, new PT(2, 2)) + " " + PointOnPolygon(v, new PT(2, 0)) + " "
				+ PointOnPolygon(v, new PT(0, 2)) + " " + PointOnPolygon(v, new PT(5, 2)) + " "
				+ PointOnPolygon(v, new PT(2, 5)));

		// expected: (1,6)
		// (5,4) (4,5)
		// blank line
		// (4,5) (5,4)
		// blank line
		// (4,5) (5,4)
		List<PT> u = CircleLineIntersection(new PT(0, 6), new PT(2, 6), new PT(1, 1), 5);
		for (int i = 0; i < u.size(); i++)
			System.err.print(u.get(i) + " ");
		System.err.println();
		
		u = CircleLineIntersection(new PT(0, 9), new PT(9, 0), new PT(1, 1), 5);
		for (int i = 0; i < u.size(); i++)
			System.err.print(u.get(i) + " ");
		System.err.println();

		u = CircleCircleIntersection(new PT(1, 1), new PT(10, 10), 5, 5);
		for (int i = 0; i < u.size(); i++)
			System.err.print(u.get(i) + " ");
		System.err.println();
		
		u = CircleCircleIntersection(new PT(1, 1), new PT(8, 8), 5, 5);
		for (int i = 0; i < u.size(); i++)
			System.err.print(u.get(i) + " ");
		System.err.println();
		
		u = CircleCircleIntersection(new PT(1, 1), new PT(4.5, 4.5), 10, Math.sqrt(2.0) / 2.0);
		for (int i = 0; i < u.size(); i++)
			System.err.print(u.get(i) + " ");
		System.err.println();
		
		u = CircleCircleIntersection(new PT(1, 1), new PT(4.5, 4.5), 5, Math.sqrt(2.0) / 2.0);
		for (int i = 0; i < u.size(); i++)
			System.err.print(u.get(i) + " ");
		System.err.println();

		// area should be 5.0
		// centroid should be (1.1666666, 1.166666)
		PT[] pa = new PT[] { new PT(0, 0), new PT(5, 0), new PT(1, 1), new PT(0, 5) };

		PT c = ComputeCentroid(pa);
		System.err.println("Area: " + ComputeArea(pa));
		System.err.println("Centroid: " + c);
	}
}

class PT {
	double x, y;

	PT() {
	}

	PT(double x, double y) {
		this.x = x;
		this.y = y;
	}

	PT(PT p) {
		this.x = p.x;
		this.y = p.y;
		{
		}
	}

	PT add(final PT p) {
		return new PT(x + p.x, y + p.y);
	}

	PT subtract(final PT p) {
		return new PT(x - p.x, y - p.y);
	}

	PT multiply(double c) {
		return new PT(x * c, y * c);
	}

	PT divide(double c) {
		return new PT(x / c, y / c);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}