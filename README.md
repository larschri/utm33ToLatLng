# utm33ToLatLng

Simple Java library for converting "UTM33" coordinates from Kartverket to
longitude/latitude. Kartverket uses UTM33 for all of Norway in some maps,
although UTM33 is not designed to cover that much. UTM has large errors when
used like this, and UTM software does not handle this in one unique way.
Kartverket has defined their conversion formula in a closed source library
for Microsoft Windows (Skt2lan1.dll), and this library calculates corresponding
results by polynomial interpolation from a set of known conversions. The
known points are provided by © Kartverket (http://kartverket.no/)

Example usage:
```java
import org.pvv.larschri.geo.UTM33ToLatLng;
...
UTM33ToLatLng.LatLng galdhopiggen =
    UTM33ToLatLng.convert(146001.89, 6851888.74);
System.out.println(galdhopiggen.latitude + " " + galdhopiggen.longitude);
```
# Accuracy

There are no guarantees about the accuracy, and it has not been thorougly tested.
The results are "fairly close" to the official conversion by Kartverket, while
some other general UTM software differs wildly.

# Thanks!
Thanks to © Kartverket for making Norwegian geo data publicly available!
