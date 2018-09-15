package com.cjburkey.freeboi.value;

import java.util.Objects;
import org.joml.Vector3f;

public class Pos {
    
    public final int x;
    public final int y;
    public final int z;
    
    public Pos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
    
    public Pos add(Pos other) {
        return new Pos(x + other.x, y + other.y, z + other.z);
    }
    
    public Pos sub(Pos other) {
        return new Pos(x - other.x, y - other.y, z - other.z);
    }
    
    public Pos mul(Pos other) {
        return new Pos(x * other.x, y * other.y, z * other.z);
    }
    
    public Pos div(Pos other) {
        return new Pos(x / other.x, y / other.y, z / other.z);
    }
    
    public Pos add(int scalar) {
        return new Pos(x + scalar, y + scalar, z + scalar);
    }
    
    public Pos sub(int scalar) {
        return new Pos(x - scalar, y - scalar, z - scalar);
    }
    
    public Pos mul(int scalar) {
        return new Pos(x * scalar, y * scalar, z * scalar);
    }
    
    public Pos div(int scalar) {
        return new Pos(x / scalar, y / scalar, z / scalar);
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
