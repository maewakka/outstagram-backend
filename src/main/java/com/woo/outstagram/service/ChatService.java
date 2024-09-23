package com.woo.outstagram.service;

import com.woo.exception.util.BizException;
import com.woo.outstagram.dto.chat.*;
import com.woo.outstagram.entity.chat.Chat;
import com.woo.outstagram.entity.chat.ChatRoom;
import com.woo.outstagram.entity.chat.ChatRoomUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.mapper.ChatMapper;
import com.woo.outstagram.repository.chat.ChatRepository;
import com.woo.outstagram.repository.chat.ChatRoomRepository;
import com.woo.outstagram.repository.chat.ChatRoomUserRepository;
import com.woo.outstagram.repository.user.UserRepository;
import com.woo.outstagram.util.minio.MinioUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final SimpMessagingTemplate simpMessageTemplate;
    private final MinioUtil minioUtil;
    private final ChatMapper chatMapper;

    /**
     * 요청 유저를 제외한 다른 유저들의 목록을 반환해준다.
     * @param user
     * @return ChatUserDto List
     */
    @Transactional
    public ChatUserListResponseDto getChatUserList(User user) {
        List<ChatUserDto> chatUserDtoList = chatMapper.getChatUserList(user.getId());
        chatUserDtoList.forEach(chatUserDto -> chatUserDto.setProfileUrl(minioUtil.getUrlFromMinioObject(chatUserDto.getProfileUrl())));

        return ChatUserListResponseDto.builder().chatRoomList(chatUserDtoList).build();
    }

    /**
     * 채팅할 User와 채팅방이 없다면 생성하고, 그 정보를 저장한다.
     * @param user, target
     */
    public ChatUserListResponseDto createChatRoom(User user, String target) {
        createChatRoomDetail(user, target);

        return this.getChatUserList(user);
    }

    @Transactional
    public void createChatRoomDetail(User user, String target) {
        User targetUser = userRepository.findByEmail(target).orElseThrow(() -> new BizException("user_not_found"));

        ChatRoomUser userChatRoom = chatRoomUserRepository.findByUserAndTargetUser(user, targetUser).orElse(null);
        ChatRoomUser targetChatRoom = chatRoomUserRepository.findByUserAndTargetUser(targetUser, user).orElse(null);

        if(userChatRoom == null && targetChatRoom == null) {
            ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().user(user).build());
            chatRoomUserRepository.save(ChatRoomUser.builder().chatRoom(chatRoom).user(user).targetUser(targetUser).build());
            chatRoomUserRepository.save(ChatRoomUser.builder().chatRoom(chatRoom).user(targetUser).targetUser(user).build());
        }

    }

    /**
     * 채팅방 목록을 반환한다.
     * @param chatRoomId
     */
    @Transactional(readOnly = true)
    public ChatResponseDto getChatList(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new BizException("chat_room_not_found"));

        return ChatResponseDto
                .builder()
                .chatList(chatRepository.findAllByChatRoom(chatRoom).stream().map(chat -> ChatDto.of(chat, minioUtil)).toList())
                .build();
    }

    /**
     * 해당 채팅방에 유저가 전송한 메세지를 저장한다.
     * @param user, ChatRequestDto
     */
    public ChatResponseDto saveChat(User user, ChatRequestDto requestDto) {
        saveChatDetail(user, requestDto);

        return this.getChatList(requestDto.getChatRoomId());
    }

    @Transactional
    public void saveChatDetail(User user, ChatRequestDto requestDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(requestDto.getChatRoomId()).orElseThrow(() -> new BizException("chat_room_not_found"));

        chatRepository.save(Chat.builder()
                .chatRoom(chatRoom)
                .user(user)
                .content(requestDto.getMessage()).build());

        chatRoom.setLastMessage(requestDto.getMessage());
        chatRoomRepository.save(chatRoom);
    }

    /**
     * 같은 채팅방 번호를 구독한 유저들에게 simpMesageTemplate 형태로 메세지를 전송한다.
     * @param requestDto
     */
    @Transactional(readOnly = true)
    public void sendMessage(ChatRequestDto requestDto) {
        User sendUser = userRepository.findByEmail(requestDto.getSenderEmail()).orElseThrow(() -> new BizException("user_not_found"));

        simpMessageTemplate.convertAndSend("/subscribe/rooms/" + requestDto.getChatRoomId(),
                ChatMessage.builder()
                        .email(sendUser.getEmail())
                        .content(requestDto.getMessage())
                        .sendDate(LocalDateTime.now())
                        .nickname(sendUser.getNickname())
                        .profileUrl(minioUtil.getUrlFromMinioObject(sendUser.getProfileImgUrl()))
                        .build());
    }
}
