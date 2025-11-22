package com.yupi.yupicturebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupicturebackend.exception.BusinessException;
import com.yupi.yupicturebackend.exception.ErrorCode;
import com.yupi.yupicturebackend.exception.ThrowUtils;
import com.yupi.yupicturebackend.mapper.SpaceMapper;
import com.yupi.yupicturebackend.model.dto.space.analyze.SpaceAnalyzeRequest;
import com.yupi.yupicturebackend.model.entity.Picture;
import com.yupi.yupicturebackend.model.entity.Space;
import com.yupi.yupicturebackend.model.entity.User;
import com.yupi.yupicturebackend.service.SpaceAnalyzeService;
import com.yupi.yupicturebackend.service.SpaceService;
import com.yupi.yupicturebackend.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author hqqhqq
 * @createDate 2025-11-02 16:48:07
 */
@Service
public class SpaceAnalyzeServiceImpl extends ServiceImpl<SpaceMapper, Space> implements SpaceAnalyzeService {

  @Resource
  private UserService userService;

  @Resource
  private SpaceService spaceService;

  /**
   * 校验空间分析权限
   *
   * @param spaceAnalyzeRequest
   * @param loginUser
   */
  private void checkSpaceAnalyzeAuth(SpaceAnalyzeRequest spaceAnalyzeRequest, User loginUser) {

    boolean queryPublic = spaceAnalyzeRequest.isQueryPublic();
    boolean queryAll = spaceAnalyzeRequest.isQueryAll();
    // 全空间分析或者公共图库权限校验 仅管理员可访问
    if (queryAll || queryPublic) {
      ThrowUtils.throwIf(!userService.isAdmin(loginUser), ErrorCode.NO_AUTH_ERROR);
    } else {
      // 分析指定的空间 仅本人或者管理员可用
      Long spaceId = spaceAnalyzeRequest.getSpaceId();
      ThrowUtils.throwIf(spaceId == null, ErrorCode.PARAMS_ERROR);
      Space space = spaceService.getById(spaceId);
      ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR, "空间不存在");
      spaceService.checkSpaceAuth(loginUser, space);

    }


  }

  /**
   * 根据请求对象封装查询条件
   *
   * @param spaceAnalyzeRequest
   * @param queryWrapper
   */
  private static void fillAnalyzeQueryWrapper(SpaceAnalyzeRequest spaceAnalyzeRequest, QueryWrapper<Picture> queryWrapper) {
    boolean queryAll = spaceAnalyzeRequest.isQueryAll();
    // 全空间分析
    if (queryAll) {
      return;
    }
    // 公共图库
    boolean queryPublic = spaceAnalyzeRequest.isQueryPublic();
    if (queryPublic) {
      queryWrapper.isNull("spaceId");
    }
    // 分析特定空间
    Long spaceId = spaceAnalyzeRequest.getSpaceId();
    if (spaceId != null) {
      queryWrapper.eq("spaceId", spaceId);
    }

    throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有指定查询范围");
  }


}




