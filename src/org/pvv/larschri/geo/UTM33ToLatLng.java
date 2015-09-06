package org.pvv.larschri.geo;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for converting "UTM33" coordinates from Kartverket to
 * longitude/latitude. Kartverket uses UTM33 for all of Norway in some maps,
 * although UTM33 is not designed to cover that much. UTM has large errors when
 * used like this and all UTM software does not handle this in one unique way.
 * Kartverket has defined their conversion formula in a closed source library
 * for Microsoft Windows (Skt2lan1.dll), and this class calculates corresponding
 * results by polynomial interpolation from a set of known conversions. The
 * known points are provided by © Kartverket (http://kartverket.no/).
 */
public class UTM33ToLatLng {
    /**
     * Type for converted values.
     */
    public static class LatLng {
        public final double latitude;
        public final double longitude;
        private LatLng(double lat, double lng) {
            this.latitude = lat;
            this.longitude = lng;
        }
    }

    /**
     * The conversion function. See class documentation.
     */
    public static LatLng convert(double easting, double northing) {
        GridLookup g = new GridLookup(easting, northing);
        return interpolate(
                interpolate(g.get(0, 0), g.get(1, 0), g.get(2, 0), easting - g.easting),
                interpolate(g.get(0, 1), g.get(1, 1), g.get(2, 1), easting - g.easting),
                interpolate(g.get(0, 2), g.get(1, 2), g.get(2, 2), easting - g.easting),
                northing - g.northing);
    }

    /**
     * Granularity for the grid of points used for interpolation.
     */
    private static final int GRANULARITY = 50000;

    /**
     * Return f(x), given f(0), f(1), f(2) and x. f is a parabolic function.
     */
    private static double interpolate(double f0, double f1, double f2, double x) {
        double a = (f2 + f0) / 2 - f1;
        return a * x * x + (f1 - f0 - a) * x + f0;
    }

    /**
     * Same as {@ link #interpolate(double, double, double, double)}, but in {@link LatLng}.
     */
    private static LatLng interpolate(LatLng f0, LatLng f1, LatLng f2, double x) {
        return new LatLng(
                interpolate(f0.latitude, f1.latitude, f2.latitude, x / GRANULARITY),
                interpolate(f0.longitude, f1.longitude, f2.longitude, x / GRANULARITY));
    }

    /**
     * Class for grid lookups. Easting and northing are rounded (floored) and
     * used to look up known conversions.
     */
    private static class GridLookup {
        final int easting, northing;
        GridLookup(double easting, double northing) {
            this.easting = (int) easting / GRANULARITY * GRANULARITY;
            this.northing = (int) northing / GRANULARITY * GRANULARITY;
        }

        LatLng get(int e, int n) {
            return GRID.get(String.format("%s,%s", easting + e * GRANULARITY, northing + n * GRANULARITY));
        }
    }

    /**
     * The grid used for lookups. Values correspond to those provided at
     * http://norgeskart.no/
     */
    private static Map<String, LatLng> GRID = new HashMap<>();
    static {
        GRID.put("-50000,6500000", new LatLng(58.294318615,5.599072875));
        GRID.put("-50000,6550000", new LatLng(58.737273719,5.478760473));
        GRID.put("-50000,6600000", new LatLng(59.180039899,5.354770266));
        GRID.put("-50000,6650000", new LatLng(59.622610464,5.226944439));
        GRID.put("-50000,6700000", new LatLng(60.064978323,5.095116004));
        GRID.put("-50000,6750000", new LatLng(60.507135959,4.959108129));
        GRID.put("-50000,6800000", new LatLng(60.949075389,4.818733415));
        GRID.put("-50000,6850000", new LatLng(61.390788134,4.673793104));
        GRID.put("-50000,6900000", new LatLng(61.832265177,4.524076217));
        GRID.put("-50000,6950000", new LatLng(62.273496922,4.369358615));
        GRID.put("0,6400000", new LatLng(57.465512314,6.652390742));
        GRID.put("0,6450000", new LatLng(57.909817592,6.548862031));
        GRID.put("0,6500000", new LatLng(58.353970602,6.442228876));
        GRID.put("0,6550000", new LatLng(58.797966398,6.332361066));
        GRID.put("0,6600000", new LatLng(59.241799745,6.219120965));
        GRID.put("0,6650000", new LatLng(59.685465095,6.102362982));
        GRID.put("0,6700000", new LatLng(60.128956567,5.981933001));
        GRID.put("0,6750000", new LatLng(60.572267922,5.857667744));
        GRID.put("0,6800000", new LatLng(61.015392528,5.729394098));
        GRID.put("0,6850000", new LatLng(61.458323339,5.596928366));
        GRID.put("0,6900000", new LatLng(61.901052858,5.460075457));
        GRID.put("0,6950000", new LatLng(62.343573096,5.318627998));
        GRID.put("0,7000000", new LatLng(62.785875541,5.172365367));
        GRID.put("0,7050000", new LatLng(63.227951108,5.021052626));
        GRID.put("50000,6400000", new LatLng(57.517799083,7.478508284));
        GRID.put("50000,6450000", new LatLng(57.963005949,7.384970624));
        GRID.put("50000,6500000", new LatLng(58.408082864,7.288618297));
        GRID.put("50000,6550000", new LatLng(58.853025830,7.189332626));
        GRID.put("50000,6600000", new LatLng(59.297830614,7.086988124));
        GRID.put("50000,6650000", new LatLng(59.742492724,6.981452001));
        GRID.put("50000,6700000", new LatLng(60.187007397,6.872583634));
        GRID.put("50000,6750000", new LatLng(60.631369577,6.760233987));
        GRID.put("50000,6800000", new LatLng(61.075573885,6.644244970));
        GRID.put("50000,6850000", new LatLng(61.519614602,6.524448761));
        GRID.put("50000,6900000", new LatLng(61.963485640,6.400667039));
        GRID.put("50000,6950000", new LatLng(62.407180506,6.272710171));
        GRID.put("50000,7000000", new LatLng(62.850692275,6.140376298));
        GRID.put("50000,7050000", new LatLng(63.294013556,6.003450353));
        GRID.put("50000,7100000", new LatLng(63.737136441,5.861702970));
        GRID.put("100000,6400000", new LatLng(57.564688090,8.307327184));
        GRID.put("100000,6450000", new LatLng(58.010705727,8.223892826));
        GRID.put("100000,6500000", new LatLng(58.456613603,8.137939929));
        GRID.put("100000,6550000", new LatLng(58.902408585,8.049361803));
        GRID.put("100000,6600000", new LatLng(59.348087351,7.958045598));
        GRID.put("100000,6650000", new LatLng(59.793646374,7.863871861));
        GRID.put("100000,6700000", new LatLng(60.239081912,7.766714043));
        GRID.put("100000,6750000", new LatLng(60.684389988,7.666437976));
        GRID.put("100000,6800000", new LatLng(61.129566370,7.562901290));
        GRID.put("100000,6850000", new LatLng(61.574606553,7.455952783));
        GRID.put("100000,6900000", new LatLng(62.019505737,7.345431735));
        GRID.put("100000,6950000", new LatLng(62.464258799,7.231167148));
        GRID.put("100000,7000000", new LatLng(62.908860271,7.112976921));
        GRID.put("100000,7050000", new LatLng(63.353304310,6.990666940));
        GRID.put("100000,7100000", new LatLng(63.797584659,6.864030081));
        GRID.put("100000,7150000", new LatLng(64.241694622,6.732845109));
        GRID.put("150000,6400000", new LatLng(57.606144355,9.138562786));
        GRID.put("150000,6450000", new LatLng(58.052880609,9.065332609));
        GRID.put("150000,6500000", new LatLng(58.499525094,8.989885764));
        GRID.put("150000,6550000", new LatLng(58.946075452,8.912127957));
        GRID.put("150000,6600000", new LatLng(59.392529178,8.831959425));
        GRID.put("150000,6650000", new LatLng(59.838883613,8.749274533));
        GRID.put("150000,6700000", new LatLng(60.285135930,8.663961337));
        GRID.put("150000,6750000", new LatLng(60.731283123,8.575901115));
        GRID.put("150000,6800000", new LatLng(61.177321991,8.484967843));
        GRID.put("150000,6850000", new LatLng(61.623249120,8.391027636));
        GRID.put("150000,6900000", new LatLng(62.069060871,8.293938121));
        GRID.put("150000,6950000", new LatLng(62.514753355,8.193547766));
        GRID.put("150000,7000000", new LatLng(62.960322416,8.089695132));
        GRID.put("150000,7050000", new LatLng(63.405763609,7.982208056));
        GRID.put("150000,7100000", new LatLng(63.851072170,7.870902754));
        GRID.put("150000,7150000", new LatLng(64.296242990,7.755582828));
        GRID.put("150000,7200000", new LatLng(64.741270588,7.636038172));
        GRID.put("200000,6400000", new LatLng(57.642136786,9.971925075));
        GRID.put("200000,6450000", new LatLng(58.089498310,9.908988211));
        GRID.put("200000,6500000", new LatLng(58.536783794,9.844141648));
        GRID.put("200000,6550000", new LatLng(58.983991559,9.777303859));
        GRID.put("200000,6600000", new LatLng(59.431119822,9.708388563));
        GRID.put("200000,6650000", new LatLng(59.878166684,9.637304378));
        GRID.put("200000,6700000", new LatLng(60.325130127,9.563954442));
        GRID.put("200000,6750000", new LatLng(60.772007999,9.488235993));
        GRID.put("200000,6800000", new LatLng(61.218798005,9.410039921));
        GRID.put("200000,6850000", new LatLng(61.665497697,9.329250271));
        GRID.put("200000,6900000", new LatLng(62.112104458,9.245743699));
        GRID.put("200000,6950000", new LatLng(62.558615487,9.159388878));
        GRID.put("200000,7000000", new LatLng(63.005027789,9.070045850));
        GRID.put("200000,7050000", new LatLng(63.451338153,8.977565299));
        GRID.put("200000,7100000", new LatLng(63.897543132,8.881787771));
        GRID.put("200000,7150000", new LatLng(64.343639026,8.782542794));
        GRID.put("200000,7200000", new LatLng(64.789621859,8.679647921));
        GRID.put("250000,6450000", new LatLng(58.120530663,10.754552774));
        GRID.put("250000,6500000", new LatLng(58.568360431,10.700387969));
        GRID.put("250000,6550000", new LatLng(59.016126471,10.644556418));
        GRID.put("250000,6600000", new LatLng(59.463827616,10.586985676));
        GRID.put("250000,6650000", new LatLng(59.911462622,10.527598994));
        GRID.put("250000,6700000", new LatLng(60.359030161,10.466315002));
        GRID.put("250000,6750000", new LatLng(60.806528816,10.403047355));
        GRID.put("250000,6800000", new LatLng(61.253957070,10.337704348));
        GRID.put("250000,6850000", new LatLng(61.701313303,10.270188499));
        GRID.put("250000,6900000", new LatLng(62.148595775,10.200396086));
        GRID.put("250000,6950000", new LatLng(62.595802623,10.128216642));
        GRID.put("250000,7000000", new LatLng(63.042931847,10.053532401));
        GRID.put("250000,7050000", new LatLng(63.489981301,9.976217687));
        GRID.put("250000,7100000", new LatLng(63.936948669,9.896138243));
        GRID.put("250000,7150000", new LatLng(64.383831468,9.813150485));
        GRID.put("250000,7200000", new LatLng(64.830627014,9.727100689));
        GRID.put("250000,7250000", new LatLng(65.277332414,9.637824073));
        GRID.put("250000,7300000", new LatLng(65.723944546,9.545143797));
        GRID.put("300000,6500000", new LatLng(58.594230093,11.558300367));
        GRID.put("300000,6550000", new LatLng(59.042454279,11.513547454));
        GRID.put("300000,6600000", new LatLng(59.490625601,11.467397959));
        GRID.put("300000,6650000", new LatLng(59.938743353,11.419790092));
        GRID.put("300000,6700000", new LatLng(60.386806782,11.370658326));
        GRID.put("300000,6750000", new LatLng(60.834815077,11.319933114));
        GRID.put("300000,6800000", new LatLng(61.282767366,11.267540578));
        GRID.put("300000,6850000", new LatLng(61.730662713,11.213402170));
        GRID.put("300000,6900000", new LatLng(62.178500107,11.157434290));
        GRID.put("300000,6950000", new LatLng(62.626278462,11.099547887));
        GRID.put("300000,7000000", new LatLng(63.073996603,11.039647997));
        GRID.put("300000,7050000", new LatLng(63.521653264,10.977633254));
        GRID.put("300000,7100000", new LatLng(63.969247075,10.913395339));
        GRID.put("300000,7150000", new LatLng(64.416776556,10.846818377));
        GRID.put("300000,7200000", new LatLng(64.864240101,10.777778268));
        GRID.put("300000,7250000", new LatLng(65.311635972,10.706141947));
        GRID.put("300000,7300000", new LatLng(65.758962284,10.631766560));
        GRID.put("300000,7350000", new LatLng(66.206216989,10.554498553));
        GRID.put("300000,7400000", new LatLng(66.653397863,10.474172652));
        GRID.put("300000,7450000", new LatLng(67.100502482,10.390610727));
        GRID.put("350000,6500000", new LatLng(58.614372297,12.417550489));
        GRID.put("350000,6550000", new LatLng(59.062953679,12.383934492));
        GRID.put("350000,6600000", new LatLng(59.511491603,12.349267994));
        GRID.put("350000,6650000", new LatLng(59.959985787,12.313504420));
        GRID.put("350000,6700000", new LatLng(60.408435927,12.276594374));
        GRID.put("350000,6750000", new LatLng(60.856841688,12.238485418));
        GRID.put("350000,6800000", new LatLng(61.305202705,12.199121842));
        GRID.put("350000,6850000", new LatLng(61.753518580,12.158444402));
        GRID.put("350000,6900000", new LatLng(62.201788875,12.116390035));
        GRID.put("350000,6950000", new LatLng(62.650013111,12.072891552));
        GRID.put("350000,7000000", new LatLng(63.098190765,12.027877289));
        GRID.put("350000,7050000", new LatLng(63.546321265,11.981270735));
        GRID.put("350000,7100000", new LatLng(63.994403979,11.932990113));
        GRID.put("350000,7150000", new LatLng(64.442438221,11.882947924));
        GRID.put("350000,7200000", new LatLng(64.890423233,11.831050432));
        GRID.put("350000,7250000", new LatLng(65.338358187,11.777197106));
        GRID.put("350000,7300000", new LatLng(65.786242172,11.721279992));
        GRID.put("350000,7350000", new LatLng(66.234074190,11.663183017));
        GRID.put("350000,7400000", new LatLng(66.681853144,11.602781211));
        GRID.put("350000,7450000", new LatLng(67.129577827,11.539939842));
        GRID.put("350000,7500000", new LatLng(67.577246915,11.474513444));
        GRID.put("350000,7550000", new LatLng(68.024858947,11.406344731));
        GRID.put("350000,7600000", new LatLng(68.472412318,11.335263372));
        GRID.put("400000,6500000", new LatLng(58.628771049,13.277806753));
        GRID.put("400000,6550000", new LatLng(59.077608034,13.255371593));
        GRID.put("400000,6600000", new LatLng(59.526408306,13.232234634));
        GRID.put("400000,6650000", new LatLng(59.975171887,13.208364716));
        GRID.put("400000,6700000", new LatLng(60.423898797,13.183728786));
        GRID.put("400000,6750000", new LatLng(60.872589044,13.158291754));
        GRID.put("400000,6800000", new LatLng(61.321242626,13.132016334));
        GRID.put("400000,6850000", new LatLng(61.769859532,13.104862872));
        GRID.put("400000,6900000", new LatLng(62.218439738,13.076789153));
        GRID.put("400000,6950000", new LatLng(62.666983202,13.047750189));
        GRID.put("400000,7000000", new LatLng(63.115489870,13.017697995));
        GRID.put("400000,7050000", new LatLng(63.563959668,12.986581330));
        GRID.put("400000,7100000", new LatLng(64.012392498,12.954345419));
        GRID.put("400000,7150000", new LatLng(64.460788246,12.920931642));
        GRID.put("400000,7200000", new LatLng(64.909146765,12.886277194));
        GRID.put("400000,7250000", new LatLng(65.357467881,12.850314702));
        GRID.put("400000,7300000", new LatLng(65.805751389,12.812971806));
        GRID.put("400000,7350000", new LatLng(66.253997048,12.774170688));
        GRID.put("400000,7400000", new LatLng(66.702204574,12.733827548));
        GRID.put("400000,7450000", new LatLng(67.150373638,12.691852018));
        GRID.put("400000,7500000", new LatLng(67.598503865,12.648146510));
        GRID.put("400000,7550000", new LatLng(68.046594818,12.602605478));
        GRID.put("400000,7600000", new LatLng(68.494646002,12.555114591));
        GRID.put("400000,7650000", new LatLng(68.942656850,12.505549805));
        GRID.put("450000,6600000", new LatLng(59.535363301,14.115933919));
        GRID.put("450000,6650000", new LatLng(59.984288728,14.103990685));
        GRID.put("450000,6700000", new LatLng(60.433181920,14.091663934));
        GRID.put("450000,6750000", new LatLng(60.882043091,14.078936082));
        GRID.put("450000,6800000", new LatLng(61.330872460,14.065788443));
        GRID.put("450000,6850000", new LatLng(61.779670248,14.052201140));
        GRID.put("450000,6900000", new LatLng(62.228436679,14.038153015));
        GRID.put("450000,6950000", new LatLng(62.677171978,14.023621515));
        GRID.put("450000,7000000", new LatLng(63.125876370,14.008582584));
        GRID.put("450000,7050000", new LatLng(63.574550085,13.993010532));
        GRID.put("450000,7100000", new LatLng(64.023193347,13.976877893));
        GRID.put("450000,7150000", new LatLng(64.471806385,13.960155271));
        GRID.put("450000,7200000", new LatLng(64.920389421,13.942811169));
        GRID.put("450000,7250000", new LatLng(65.368942678,13.924811794));
        GRID.put("450000,7300000", new LatLng(65.817466376,13.906120847));
        GRID.put("450000,7350000", new LatLng(66.265960731,13.886699289));
        GRID.put("450000,7400000", new LatLng(66.714425951,13.866505072));
        GRID.put("450000,7450000", new LatLng(67.162862238,13.845492849));
        GRID.put("450000,7500000", new LatLng(67.611269790,13.823613639));
        GRID.put("450000,7550000", new LatLng(68.059648790,13.800814462));
        GRID.put("450000,7600000", new LatLng(68.507999414,13.777037919));
        GRID.put("450000,7650000", new LatLng(68.956321822,13.752221723));
        GRID.put("450000,7700000", new LatLng(69.404616160,13.726298165));
        GRID.put("450000,7750000", new LatLng(69.852882555,13.699193516));
        GRID.put("500000,7050000", new LatLng(63.578081455,15.000000000));
        GRID.put("500000,7100000", new LatLng(64.026794921,15.000000000));
        GRID.put("500000,7150000", new LatLng(64.475480453,15.000000000));
        GRID.put("500000,7200000", new LatLng(64.924138397,15.000000000));
        GRID.put("500000,7250000", new LatLng(65.372769109,15.000000000));
        GRID.put("500000,7300000", new LatLng(65.821372950,15.000000000));
        GRID.put("500000,7350000", new LatLng(66.269950289,15.000000000));
        GRID.put("500000,7400000", new LatLng(66.718501499,15.000000000));
        GRID.put("500000,7450000", new LatLng(67.167026960,15.000000000));
        GRID.put("500000,7500000", new LatLng(67.615527061,15.000000000));
        GRID.put("500000,7550000", new LatLng(68.064002193,15.000000000));
        GRID.put("500000,7600000", new LatLng(68.512452755,15.000000000));
        GRID.put("500000,7650000", new LatLng(68.960879154,15.000000000));
        GRID.put("500000,7700000", new LatLng(69.409281798,15.000000000));
        GRID.put("500000,7750000", new LatLng(69.857661103,15.000000000));
        GRID.put("500000,7800000", new LatLng(70.306017493,15.000000000));
        GRID.put("550000,7050000", new LatLng(63.574550085,16.006989468));
        GRID.put("550000,7100000", new LatLng(64.023193347,16.023122107));
        GRID.put("550000,7150000", new LatLng(64.471806385,16.039844729));
        GRID.put("550000,7200000", new LatLng(64.920389421,16.057188831));
        GRID.put("550000,7250000", new LatLng(65.368942678,16.075188206));
        GRID.put("550000,7300000", new LatLng(65.817466376,16.093879153));
        GRID.put("550000,7350000", new LatLng(66.265960731,16.113300711));
        GRID.put("550000,7400000", new LatLng(66.714425951,16.133494928));
        GRID.put("550000,7450000", new LatLng(67.162862238,16.154507151));
        GRID.put("550000,7500000", new LatLng(67.611269790,16.176386361));
        GRID.put("550000,7550000", new LatLng(68.059648790,16.199185538));
        GRID.put("550000,7600000", new LatLng(68.507999414,16.222962081));
        GRID.put("550000,7650000", new LatLng(68.956321822,16.247778277));
        GRID.put("550000,7700000", new LatLng(69.404616160,16.273701835));
        GRID.put("550000,7750000", new LatLng(69.852882555,16.300806484));
        GRID.put("550000,7800000", new LatLng(70.301121115,16.329172660));
        GRID.put("600000,7300000", new LatLng(65.805751389,17.187028194));
        GRID.put("600000,7350000", new LatLng(66.253997048,17.225829312));
        GRID.put("600000,7400000", new LatLng(66.702204574,17.266172452));
        GRID.put("600000,7450000", new LatLng(67.150373638,17.308147982));
        GRID.put("600000,7500000", new LatLng(67.598503865,17.351853490));
        GRID.put("600000,7550000", new LatLng(68.046594818,17.397394522));
        GRID.put("600000,7600000", new LatLng(68.494646002,17.444885409));
        GRID.put("600000,7650000", new LatLng(68.942656850,17.494450195));
        GRID.put("600000,7700000", new LatLng(69.390626714,17.546223695));
        GRID.put("600000,7750000", new LatLng(69.838554862,17.600352687));
        GRID.put("600000,7800000", new LatLng(70.286440460,17.656997264));
        GRID.put("600000,7850000", new LatLng(70.734282567,17.716332383));
        GRID.put("600000,7900000", new LatLng(71.182080113,17.778549621));
        GRID.put("650000,7400000", new LatLng(66.681853144,18.397218789));
        GRID.put("650000,7450000", new LatLng(67.129577827,18.460060158));
        GRID.put("650000,7500000", new LatLng(67.577246915,18.525486556));
        GRID.put("650000,7550000", new LatLng(68.024858947,18.593655269));
        GRID.put("650000,7600000", new LatLng(68.472412318,18.664736628));
        GRID.put("650000,7650000", new LatLng(68.919905257,18.738915387));
        GRID.put("650000,7700000", new LatLng(69.367335811,18.816392278));
        GRID.put("650000,7750000", new LatLng(69.814701823,18.897385774));
        GRID.put("650000,7800000", new LatLng(70.262000910,18.982134091));
        GRID.put("650000,7850000", new LatLng(70.709230436,19.070897461));
        GRID.put("650000,7900000", new LatLng(71.156387477,19.163960729));
        GRID.put("700000,7500000", new LatLng(67.547528209,19.696379477));
        GRID.put("700000,7550000", new LatLng(67.994472168,19.787005768));
        GRID.put("700000,7600000", new LatLng(68.441331218,19.881493105));
        GRID.put("700000,7650000", new LatLng(68.888101924,19.980085397));
        GRID.put("700000,7700000", new LatLng(69.334780531,20.083047537));
        GRID.put("700000,7750000", new LatLng(69.781362914,20.190667702));
        GRID.put("700000,7800000", new LatLng(70.227844553,20.303259959));
        GRID.put("700000,7850000", new LatLng(70.674220475,20.421167225));
        GRID.put("700000,7900000", new LatLng(71.120485202,20.544764653));
        GRID.put("700000,7950000", new LatLng(71.566632693,20.674463492));
        GRID.put("750000,7550000", new LatLng(67.955477609,20.976498375));
        GRID.put("750000,7600000", new LatLng(68.401448413,21.094148117));
        GRID.put("750000,7650000", new LatLng(68.847295367,21.216889430));
        GRID.put("750000,7700000", new LatLng(69.293012432,21.345049076));
        GRID.put("750000,7750000", new LatLng(69.738593009,21.478982337));
        GRID.put("750000,7800000", new LatLng(70.184029874,21.619076175));
        GRID.put("750000,7850000", new LatLng(70.629315115,21.765752828));
        GRID.put("750000,7900000", new LatLng(71.074440040,21.919473902));
        GRID.put("750000,7950000", new LatLng(71.519395088,22.080745043));
        GRID.put("800000,7550000", new LatLng(67.907930275,22.161204318));
        GRID.put("800000,7600000", new LatLng(68.352822188,22.301715837));
        GRID.put("800000,7650000", new LatLng(68.797547419,22.448279872));
        GRID.put("800000,7700000", new LatLng(69.242097204,22.601282270));
        GRID.put("800000,7750000", new LatLng(69.686461985,22.761142260));
        GRID.put("800000,7800000", new LatLng(70.130631321,22.928316120));
        GRID.put("800000,7850000", new LatLng(70.574593792,23.103301330));
        GRID.put("800000,7900000", new LatLng(71.018336882,23.286641295));
        GRID.put("800000,7950000", new LatLng(71.461846844,23.478930724));
        GRID.put("800000,8000000", new LatLng(71.905108557,23.680821785));
        GRID.put("850000,7600000", new LatLng(68.295523041,23.503236054));
        GRID.put("850000,7650000", new LatLng(68.738932839,23.673237504));
        GRID.put("850000,7700000", new LatLng(69.182114229,23.850664032));
        GRID.put("850000,7750000", new LatLng(69.625054249,24.035995164));
        GRID.put("850000,7800000", new LatLng(70.067738767,24.229752352));
        GRID.put("850000,7850000", new LatLng(70.510152343,24.432503609));
        GRID.put("850000,7900000", new LatLng(70.952278080,24.644868774));
        GRID.put("850000,7950000", new LatLng(71.394097451,24.867525494));
        GRID.put("850000,8000000", new LatLng(71.835590103,25.101216048));
        GRID.put("900000,7600000", new LatLng(68.229633259,24.697778580));
        GRID.put("900000,7650000", new LatLng(68.671538843,24.890776348));
        GRID.put("900000,7700000", new LatLng(69.113156061,25.092148162));
        GRID.put("900000,7750000", new LatLng(69.554468154,25.302429757));
        GRID.put("900000,7800000", new LatLng(69.995456867,25.522203132));
        GRID.put("900000,7850000", new LatLng(70.436102289,25.752101591));
        GRID.put("900000,7900000", new LatLng(70.876382651,25.992815442));
        GRID.put("900000,7950000", new LatLng(71.316274119,26.245098470));
        GRID.put("900000,8000000", new LatLng(71.755750548,26.509775293));
        GRID.put("950000,7600000", new LatLng(68.155246434,25.884447287));
        GRID.put("950000,7650000", new LatLng(68.595464565,26.099948099));
        GRID.put("950000,7700000", new LatLng(69.035327834,26.324730206));
        GRID.put("950000,7750000", new LatLng(69.474815337,26.559381101));
        GRID.put("950000,7800000", new LatLng(69.913904328,26.804538308));
        GRID.put("950000,7850000", new LatLng(70.352570023,27.060894735));
        GRID.put("950000,7900000", new LatLng(70.790785366,27.329204710));
        GRID.put("950000,7950000", new LatLng(71.228520769,27.610290823));
        GRID.put("950000,8000000", new LatLng(71.665743819,27.905051670));
        GRID.put("1000000,7600000", new LatLng(68.072466914,27.062383742));
        GRID.put("1000000,7650000", new LatLng(68.510820454,27.299846088));
        GRID.put("1000000,7700000", new LatLng(68.948746599,27.547451784));
        GRID.put("1000000,7750000", new LatLng(69.386219995,27.805835345));
        GRID.put("1000000,7800000", new LatLng(69.823213094,28.075684481));
        GRID.put("1000000,7850000", new LatLng(70.259695910,28.357745680));
        GRID.put("1000000,7900000", new LatLng(70.695635749,28.652830479));
        GRID.put("1000000,7950000", new LatLng(71.130996909,28.961822545));
        GRID.put("1000000,8000000", new LatLng(71.565740329,29.285685674));
        GRID.put("1050000,7700000", new LatLng(68.853540603,28.759404347));
        GRID.put("1050000,7750000", new LatLng(69.288818088,29.040833789));
        GRID.put("1050000,7800000", new LatLng(69.723527475,29.334629412));
        GRID.put("1050000,7850000", new LatLng(70.157633312,29.641585023));
        GRID.put("1050000,7900000", new LatLng(70.591096999,29.962562306));
        GRID.put("1050000,7950000", new LatLng(71.023876438,30.298498033));
        GRID.put("1050000,8000000", new LatLng(71.455925635,30.650412170));
        GRID.put("1100000,7700000", new LatLng(68.749848533,29.959732341));
        GRID.put("1100000,7750000", new LatLng(69.182756497,30.263476277));
        GRID.put("1100000,7800000", new LatLng(69.615003212,30.580425646));
        GRID.put("1100000,7850000", new LatLng(70.046547566,30.911415181));
        GRID.put("1100000,7900000", new LatLng(70.477344853,31.257349527));
        GRID.put("1100000,7950000", new LatLng(70.907346383,31.619210470));
        GRID.put("1100000,8000000", new LatLng(71.336499039,31.998065022));
        GRID.put("1150000,7700000", new LatLng(68.637818707,31.147635763));
        GRID.put("1150000,7750000", new LatLng(69.068192145,31.472923890));
        GRID.put("1150000,7800000", new LatLng(69.497806515,31.812193348));
        GRID.put("1150000,7850000", new LatLng(69.926614912,32.166313358));
        GRID.put("1150000,7900000", new LatLng(70.354566402,32.536224330));
        GRID.put("1150000,7950000", new LatLng(70.781605591,32.922944985));
        GRID.put("1150000,8000000", new LatLng(71.207672141,33.327580318));
        GRID.put("1200000,7750000", new LatLng(68.945291091,32.668400953));
        GRID.put("1200000,7800000", new LatLng(69.372113063,33.029122340));
        GRID.put("1200000,7850000", new LatLng(69.798021401,33.405433605));
        GRID.put("1200000,7900000", new LatLng(70.222958892,33.798303802));
        GRID.put("1200000,7950000", new LatLng(70.646863404,34.208780653));
        GRID.put("1200000,8000000", new LatLng(71.069667369,34.637998261));
    }
}
