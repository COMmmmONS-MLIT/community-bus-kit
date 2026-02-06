package maint.utils;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

public class ExFileUtils {

  /**
   * イメージファイルチェック
   *
   * @param baseStr BASE64文字列
   * @return チェック結果
   */
  public static boolean isImageFile(String baseStr) {
    if (StringUtils.isBlank(baseStr))
      return false;
    try {
      if (ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(baseStr))) != null)
        return true;
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Base64のファイルサイズ(Byte)取得
   *
   * @param base64Str
   * @return
   */
  public static int fileSizeOfBase64(String base64Str) {
    return Base64.getDecoder().decode(base64Str).length;
  }

  /**
   * ファイルの拡張子チェック
   *
   * @param fileName         ファイル名
   * @param acceptExtensions 許可する拡張子
   * @return チェック結果
   */
  public static boolean isAcceptableExtension(String fileName, String[] acceptExtensions) {
    // ファイル名が空の場合はNG
    if (StringUtils.isBlank(fileName))
      return false;
    // 許可する拡張子が空の場合はNG
    if (ExObjectUtils.isBlank(acceptExtensions))
      return false;
    // ファイル名の末尾が許可する拡張子で終わっているかチェック
    for (String acceptExtension : acceptExtensions) {
      if (fileName.endsWith(acceptExtension))
        return true;
    }
    return false;
  }

}
