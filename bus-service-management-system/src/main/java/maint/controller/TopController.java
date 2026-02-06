package maint.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maint.dto.system.permissions.PermissionRoleDetailsDto;
import maint.mapper.MenusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import maint.session.UserSession;

/**
 * トップページ用コントローラ
 *
 * @author administrator
 */
@Controller
@RequestMapping(value = "/")
public class TopController extends BaseController {

  private static final String INDEX_PAGE = "index";

  /** メニューMAPPER */
  @Autowired
  MenusMapper menusMapper;

  /**
   * トップページ
   *
   * @param model MODEL
   * @return トップページ
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  public String index(Model model) {

    model.addAttribute("errorMsgList", new ArrayList<>());
    model.addAttribute("successMsgList", new ArrayList<>());

    // パスワード変更フラグ取得
    UserSession userSession =
        (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    model.addAttribute("passChangeFlag", userSession.getPasswordChangedFlag());

    // 権限(参照)マップをModelに設定（key: menuCode, value: readPermFlg==1）
    List<PermissionRoleDetailsDto> menuList = menusMapper.getMenus(userSession.getPermRoleId());
    Map<String, Boolean> permRead = new HashMap<>();
      for (PermissionRoleDetailsDto dto : menuList) {
        permRead.put(dto.getMenuCode(), dto.getReadPermFlg() != null && dto.getReadPermFlg() == 1);
      }
    model.addAttribute("permRead", permRead);

    return INDEX_PAGE;
  }

}
