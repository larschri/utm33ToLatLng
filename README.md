# utm33ToLatLng

Simple Java library for translating "UTM33" coordinates from Kartverket to longitude/latitude.

# Background

Kartverket uses UTM33 to cover all of Norway (26° of longitude), although UTM33 is designed to be accurate only in a narrow band of 6° longitude. Conversion of coordinates outside the normal 6° longitude band are supported in various ways by different libraries. This means that general UTM conversion software may translate these coordinates very different from Kartverket or not at all. Kartverket has defined the actual translation formula in a proprietary library for Microsoft Windows only (Skt2lan1.dll), so it is not publicly available.

# How it works

Utm33ToLatLng translates "UTM33" cooordinates from Kartverket by using polynomial interpolation from a grid (50x50 kilometers) of known conversions. The known conversion points are provided by © Kartverket (http://kartverket.no/).

# Accuracy

Accuracy for this library means how closely it approximates the original conversion by Kartverket. The accuracy can be increased by adding more conversion points to get a finer granularity.

There are no guarantees about the accuracy, and it has not been thorougly tested.

# Example code
```java
UTM33ToLatLng.LatLng galdhopiggen =
	UTM33ToLatLng.convert(146001.89, 6851888.74);
assert Math.abs(galdhopiggen.latitude - 61.636432) < 0.00001;
assert Math.abs(galdhopiggen.longitude - 8.312486) < 0.00001;
```

# Thanks!
Thanks to © Kartverket for making geographic data for Norway publicly available!
