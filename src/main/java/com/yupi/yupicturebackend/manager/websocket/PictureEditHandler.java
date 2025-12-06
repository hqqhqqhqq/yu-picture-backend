package com.yupi.yupicturebackend.manager.websocket;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yupi.yupicturebackend.manager.websocket.model.PictureEditResponseMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片编辑处理器
 */
@Component
public class PictureEditHandler extends TextWebSocketHandler {

  // 每张图片的编辑状态，key: pictureId, value: 当前正在编辑的用户 ID
  private final Map<Long, Long> pictureEditingUsers = new ConcurrentHashMap<>();

  // 保存所有连接的会话，key: pictureId, value: 用户会话集合
  private final Map<Long, Set<WebSocketSession>> pictureSessions = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    super.afterConnectionEstablished(session);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    super.handleTextMessage(session, message);
  }


  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    super.afterConnectionClosed(session, status);
  }


  /**
   * 广播给该图片的所有用户
   * @param pictureId
   * @param pictureEditResponseMessage
   * @param excludeSession
   * @throws Exception
   */
  private void broadcastToPicture(Long pictureId, PictureEditResponseMessage pictureEditResponseMessage, WebSocketSession excludeSession) throws Exception {
    Set<WebSocketSession> sessionSet = pictureSessions.get(pictureId);
    if (CollUtil.isNotEmpty(sessionSet)) {
      // 创建 ObjectMapper
      ObjectMapper objectMapper = new ObjectMapper();
      // 配置序列化：将 Long 类型转为 String，解决丢失精度问题
      SimpleModule module = new SimpleModule();
      module.addSerializer(Long.class, ToStringSerializer.instance);
      module.addSerializer(Long.TYPE, ToStringSerializer.instance); // 支持 long 基本类型
      objectMapper.registerModule(module);
      // 序列化为 JSON 字符串
      String message = objectMapper.writeValueAsString(pictureEditResponseMessage);
      TextMessage textMessage = new TextMessage(message);
      for (WebSocketSession session : sessionSet) {
        // 排除掉的 session 不发送
        if (excludeSession != null && excludeSession.equals(session)) {
          continue;
        }
        if (session.isOpen()) {
          session.sendMessage(textMessage);
        }
      }
    }
  }

}
