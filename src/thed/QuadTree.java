package thed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuadTree<T extends QTProvider> {
    private static final int MaxPoints = 5;

    private Rectangle bounds;

    private List<T> objs = null;

    private QuadTree<T> northEast = null;
    private QuadTree<T> southEast = null;
    private QuadTree<T> southWest = null;
    private QuadTree<T> northWest = null;

    public QuadTree(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void subdivide() {
        float mw = bounds.w / 2f;
        float mh = bounds.h / 2f;
        float r = bounds.right();
        float b = bounds.bottom();
        float eastx = bounds.x + mw;
        float southy = bounds.y + mh;

        northEast = new QuadTree<T>(new Rectangle(eastx, bounds.y, r - eastx, mh));
        southEast = new QuadTree<T>(new Rectangle(eastx, southy, r - eastx, b - southy));
        southWest = new QuadTree<T>(new Rectangle(bounds.x, southy, mw, b - southy));
        northWest = new QuadTree<T>(new Rectangle(bounds.x, bounds.y, mw, mh));
    }

    public boolean insert(T obj) {
        if (!bounds.intersects(obj.loc()))
            return false;

        if (objs == null) {
            objs = new ArrayList<T>();
            objs.add(obj);
            return true;
        } else if (objs.size() < MaxPoints) {
            objs.add(obj);
            return true;
        }

        if (northEast == null)
            subdivide();

        boolean inserted = false;
        inserted |= northEast.insert(obj);
        inserted |= southEast.insert(obj);
        inserted |= southWest.insert(obj);
        inserted |= northWest.insert(obj);

        return inserted;
    }

    public void remove(T obj) {
        if (!bounds.intersects(obj.loc()))
            return;

        if (objs == null)
            return;
        else if (objs.contains(obj)) {
            objs.remove(obj);
            return;
        }

        if (northEast != null) {
            northEast.remove(obj);
            southEast.remove(obj);
            southWest.remove(obj);
            northWest.remove(obj);
            if (northEast.northEast == null
                    && (northEast.objs == null || northEast.objs.size() == 0)
                    && southEast.northEast == null
                    && (southEast.objs == null || southEast.objs.size() == 0)
                    && southWest.northEast == null
                    && (southWest.objs == null || southWest.objs.size() == 0)
                    && northWest.northEast == null
                    && (northWest.objs == null || northWest.objs.size() == 0)) {
                northEast = null;
                southEast = null;
                southWest = null;
                northWest = null;
            }
        }
    }

    public Set<T> getCollisions(Rectangle location) {
        Set<T> retval = new HashSet<T>();
        getCollisions(retval, location);
        return retval;
    }

    public void getCollisions(Set<T> list, Rectangle location) {
        if (!bounds.intersects(location)) {
            return;
        }
        if (objs == null) {
            return;
        }
        int size = objs.size();
        for (int i = 0; i < size; i++) {
            T myobj = objs.get(i);
            if (!list.contains(myobj)) {
                if (myobj.loc().intersects(location)) {
                    list.add(myobj);
                }
            }
        }
        if (northEast != null) {
            northEast.getCollisions(list, location);
            northWest.getCollisions(list, location);
            southEast.getCollisions(list, location);
            southWest.getCollisions(list, location);
        }
    }

    public boolean hasCollisions(Rectangle location) {
        if (!bounds.intersects(location)) {
            return false;
        }
        if (objs == null) {
            return false;
        }

        int size = objs.size();
        for (int i = 0; i < size; i++) {
            T myobj = objs.get(i);
            if (myobj.loc().intersects(location)) {
                return true;
            }
        }
        if (northEast != null) {
            if (northEast.hasCollisions(location)) {
                return true;
            }
            if (northWest.hasCollisions(location)) {
                return true;
            }
            if (southEast.hasCollisions(location)) {
                return true;
            }
            if (southWest.hasCollisions(location)) {
                return true;
            }
        }
        return false;
    }

    public boolean inBounds(Rectangle rect) {
        return this.bounds.contains(rect);
    }
}
