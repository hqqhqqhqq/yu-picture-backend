package com.yupi.yupicturebackend.manager.auth;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.yupicturebackend.manager.auth.model.SpaceUserAuthConfig;
import com.yupi.yupicturebackend.manager.auth.model.SpaceUserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 空间成员权限管理
 */
@Component
public class SpaceUserAuthManager {

  public static final SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;

  static {
    String json = ResourceUtil.readUtf8Str("biz/spaceUserAuthConfig.json");
    SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(json, SpaceUserAuthConfig.class);
  }

  public List<String> getPermissionsByRole(String spaceRole) {
    if (StrUtil.isBlank(spaceRole)) {
      return new ArrayList<>();
    }
    SpaceUserRole role = SPACE_USER_AUTH_CONFIG.getRoles()
            .stream()
            .filter(spaceUserRole -> spaceUserRole.getKey().equals(spaceRole))
            .findFirst().
            orElse(null);

    if (role == null) {
      return new ArrayList<>();
    }

    return role.getPermissions();
  }


}
