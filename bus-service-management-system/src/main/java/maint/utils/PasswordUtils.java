package maint.utils;

import java.lang.invoke.MethodHandles;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * パスワード Utils
 *
 * @author administrato
 */
@Component
public class PasswordUtils {

  /** ロガー */
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static int PASSWORD_MINIMUM = 1;
  private static int PASSWORD_MAXIMUM = 12;

  public static String getRandomPassword() {

    int cnt1 = (int) (Math.random() * (PASSWORD_MAXIMUM - 2)) + PASSWORD_MINIMUM;
    int cnt2 =
        (int) (Math.random() * (PASSWORD_MAXIMUM - cnt1 - PASSWORD_MINIMUM)) + PASSWORD_MINIMUM;
    int cnt3 = PASSWORD_MAXIMUM - cnt1 - cnt2;

    String p1 = getRandomString(cnt1, "abcdefghijklmnopqrstuvwxyz");
    String p2 = getRandomString(cnt2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    String p3 = getRandomString(cnt3, "0123456789");

    return shuffle(p1 + p2 + p3);
  }

  /**
   * UUIDからランダムパスワードを生成
   *
   * @return ランダムパスワード
   */
  public static String getRandomPasswordByUuid() {
    return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
  }

  private static String getRandomString(int cnt, String text) {
    return RandomStringUtils.random(cnt, 0, text.length(), true, true, text.toCharArray());
  }

  private static String shuffle(String input) {
    List<Character> characters = new ArrayList<Character>();
    for (char c : input.toCharArray()) {
      characters.add(c);
    }
    StringBuilder output = new StringBuilder(input.length());
    while (characters.size() != 0) {
      int randPicker = (int) (Math.random() * characters.size());
      output.append(characters.remove(randPicker));
    }
    return output.toString();
  }

  public static String getBCryptStr(String password) {

    StringBuffer sb = new StringBuffer();
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
      md.update(password.getBytes());

      byte[] msgb = md.digest();

      for (int i = 0; i < msgb.length; i++) {
        sb.append(Integer.toString((msgb[i] & 0xff) + 0x100, 16).substring(1));
      }

      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      return null;
    }

  }

  public boolean isMatchPassword(String password, String encodedPassword) {
    String endcodePass = getBCryptStr(password);
    LOGGER.info("encode Password:" + endcodePass);
    return encodedPassword.equals(endcodePass);
  }

  /**
   * パスワードが要件を満たしているか（8桁以上30桁未満、英大文字・小文字・数字・記号のうち3種以上）
   *
   * @param password パスワード
   * @return チェック結果
   */
  public static boolean isStrongPassword(String password) {
    if (ExObjectUtils.isBlank(password) || password.length() < 8 || password.length() >= 30) {
      return false;
    }

    int count = 0;
    if (password.matches(".*[a-z].*")) {
      count++;// 小文字
    }
    if (password.matches(".*[A-Z].*")) {
      count++;// 大文字
    }
    if (password.matches(".*\\d.*")) {
      count++; // 数字
    }
    if (password.matches(".*[^a-zA-Z0-9].*")) {
      count++;// 記号
    }
    return count >= 3;
  }

  /**
   * ログインIDと一致するかどうか
   *
   * @param password パスワード
   * @param email  メール
   * @return チェック結果
   */
  public static boolean equalsToEmail(String password, String email) {
    return password.equals(email);
  }
}
