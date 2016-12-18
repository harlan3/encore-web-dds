package DDSSolarSystem;

public final class Planet {

    public int planetId;
    public java.lang.String planetName = "";
    public java.lang.String planetColor = "";
    public double xPos;
    public double yPos;
    public double theta;
    public double planetSize;
    public double orbitalRadius;
    public double orbitalVelocity;

    public Planet() {
    }

    public Planet(
        int _planetId,
        java.lang.String _planetName,
        java.lang.String _planetColor,
        double _xPos,
        double _yPos,
        double _theta,
        double _planetSize,
        double _orbitalRadius,
        double _orbitalVelocity)
    {
        planetId = _planetId;
        planetName = _planetName;
        planetColor = _planetColor;
        xPos = _xPos;
        yPos = _yPos;
        theta = _theta;
        planetSize = _planetSize;
        orbitalRadius = _orbitalRadius;
        orbitalVelocity = _orbitalVelocity;
    }

}
