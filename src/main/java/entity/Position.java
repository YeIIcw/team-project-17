package entity;

public class Position {
  private final int x;
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Position move(int dx, int dy) {
    return new Position(x + dx, y + dy);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Position))
      return false;
    Position other = (Position) o;
    return x == other.x && y == other.y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
