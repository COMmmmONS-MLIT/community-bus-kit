package communitybus.lambda.utils;

/**
 * 距離計算 Utils
 *
 * @author jeon
 *
 */
public class HaversineUtils {

  private static final double EARTH_RADIUS_KM = 6371.0;

  /**
   * ハーサイン（Haversine）公式を使用して、地球上の2地点間の距離（km）を計算します。
   *
   * @param lat1 最初の地点の緯度（度）
   * @param lon1 最初の地点の経度（度）
   * @param lat2 2番目の地点の緯度（度）
   * @param lon2 2番目の地点の経度（度）
   * @return 2地点間の距離（キロメートル単位）
   */
  public static double calculate(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double rLat1 = Math.toRadians(lat1);
    double rLat2 = Math.toRadians(lat2);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(rLat1) * Math.cos(rLat2)
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_KM * c;
  }

  /**
   * ハーサイン（Haversine）公式を使用して、地球上の2地点間の距離（m）を計算します。
   *
   * @param lat1 最初の地点の緯度（度）
   * @param lon1 最初の地点の経度（度）
   * @param lat2 2番目の地点の緯度（度）
   * @param lon2 2番目の地点の経度（度）
   * @return 2地点間の距離（メートル単位）
   */
  public static double calculate_m(double lat1, double lon1, double lat2, double lon2) {
    return calculate(lat1, lon1, lat2, lon2) * 1000;
  }
}
