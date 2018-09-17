package com.cjburkey.freeboi.value;

import com.cjburkey.freeboi.util.Util;
import java.util.Objects;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Pos {
    
    public final int x;
    public final int y;
    public final int z;
    
    public Pos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Pos(Vector3i position) {
        this(position.x, position.y, position.z);
    }
    
    public Pos(Vector3f position) {
        this(Util.floor(position.x), Util.floor(position.y), Util.floor(position.z));
    }
    
    public Pos(int x, int y) {
        this(x, y, 0);
    }
    
    public Pos(int x) {
        this(x, 0);
    }
    
    public Pos() {
        this(0);
    }
    
    public Pos add(int x, int y, int z) {
        return new Pos(this.x + x, this.y + y, this.z + z);
    }
    
    public Pos sub(int x, int y, int z) {
        return new Pos(this.x - x, this.y - y, this.z - z);
    }
    
    public Pos mul(int x, int y, int z) {
        return new Pos(this.x * x, this.y * y, this.z * z);
    }
    
    public Pos div(int x, int y, int z) {
        return new Pos(this.x / x, this.y / y, this.z / z);
    }
    
    public Pos add(Pos other) {
        return add(other.x, other.y, other.z);
    }
    
    public Pos sub(Pos other) {
        return sub(other.x, other.y, other.z);
    }
    
    public Pos mul(Pos other) {
        return mul(other.x, other.y, other.z);
    }
    
    public Pos div(Pos other) {
        return div(other.x, other.y, other.z);
    }
    
    public Pos add(int scalar) {
        return add(scalar, scalar, scalar);
    }
    
    public Pos sub(int scalar) {
        return sub(scalar, scalar, scalar);
    }
    
    public Pos mul(int scalar) {
        return mul(scalar, scalar, scalar);
    }
    
    public Pos div(int scalar) {
        return div(scalar, scalar, scalar);
    }
    
    public Pos divFloor(int scalar) {
        return new Pos(Util.divFloor(x, scalar), Util.divFloor(y, scalar), Util.divFloor(y, scalar));
    }
    
    public Pos move(Dir dir, int amount) {
        return add(dir.vector.mul(amount));
    }
    
    public Pos move(Dir dir) {
        return add(dir.vector);
    }
    
    public Pos north(int amt) {
        return move(Dir.NORTH, amt);
    }
    
    public Pos south(int amt) {
        return move(Dir.SOUTH, amt);
    }
    
    public Pos east(int amt) {
        return move(Dir.EAST, amt);
    }
    
    public Pos west(int amt) {
        return move(Dir.WEST, amt);
    }
    
    public Pos up(int amt) {
        return move(Dir.UP, amt);
    }
    
    public Pos down(int amt) {
        return move(Dir.DOWN, amt);
    }
    
    public Pos north() {
        return move(Dir.NORTH);
    }
    
    public Pos south() {
        return move(Dir.SOUTH);
    }
    
    public Pos east() {
        return move(Dir.EAST);
    }
    
    public Pos west() {
        return move(Dir.WEST);
    }
    
    public Pos up() {
        return move(Dir.UP);
    }
    
    public Pos down() {
        return move(Dir.DOWN);
    }
    
    public String toString() {
        return x + ", " + y + ", " + z;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pos pos = (Pos) o;
        return x == pos.x && y == pos.y && z == pos.z;
    }
    
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    public enum Dir {
        
        NORTH(new Pos(0, 0, -1)),
        SOUTH(new Pos(0, 0, 1)),
        EAST(new Pos(1, 0, 0)),
        WEST(new Pos(-1, 0, 0)),
        UP(new Pos(0, 1, 0)),
        DOWN(new Pos(0, -1, 0)),
        
        ;
        
        public final Pos vector;
        private final Vector3f dir;
        
        Dir(Pos vector) {
            this.vector = vector;
            dir = new Vector3f(vector.x, vector.y, vector.z);
        }
        
        public Vector3f getDir() {
            return new Vector3f(dir);
        }
        
    }
    
}
