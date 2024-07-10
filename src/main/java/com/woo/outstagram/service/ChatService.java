package com.woo.outstagram.service;

import com.woo.outstagram.dto.chat.*;
import com.woo.outstagram.entity.chat.Chat;
import com.woo.outstagram.entity.chat.ChatRoom;
import com.woo.outstagram.entity.chat.ChatRoomUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.repository.chat.ChatRepository;
import com.woo.outstagram.repository.chat.ChatRoomRepository;
import com.woo.outstagram.repository.chat.ChatRoomUserRepository;
import com.woo.outstagram.repository.user.UserRepository;
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
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucket;

    /**
     * 요청 유저를 제외한 다른 유저들의 목록을 반환해준다.
     * @param user
     * @return ChatUserDto List
     */
    @Transactional
    public ChatUserListResponseDto getChatUserList(User user) {
        List<User> userList = userRepository.findAll();
        List<ChatUserDto> chatUserDtoList = new ArrayList<>();

        userList.forEach((member) -> {
            if(!member.getEmail().equals(user.getEmail())) {
                ChatRoomUser chatRoomUser = chatRoomUserRepository.findByUserAndTargetUser(user, member).orElse(null);
                Long chatRoomId = null;
                ChatRoom chatRoom = new ChatRoom();
                String lastMessage = null;

                if(chatRoomUser != null) {
                    chatRoomId = chatRoomUser.getChatRoom().getId();
                    chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
                    if(chatRoom != null) {
                        lastMessage = chatRoom.getLastMessage();
                    }
                }

                String profileImgUrl = null;

                try {
                    profileImgUrl = minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucket)
                                    .object(member.getProfileImgUrl())
                                    .expiry(2, TimeUnit.HOURS)
                                    .build());
                } catch (Exception e) {
                    throw  new RuntimeException();
                }

                chatUserDtoList.add(
                        ChatUserDto.builder()
                                .email(member.getEmail())
                                .nickname(member.getNickname())
                                .profileUrl(profileImgUrl)
                                .chatRoomId(chatRoomId)
                                .lastMessage(lastMessage)
                                .modifiedDate(chatRoom.getModifiedDate())
                                .build()
                );
            }
        });

        return ChatUserListResponseDto.builder().chatRoomList(chatUserDtoList).build();
    }

    /**
     * 채팅할 User와 채팅방이 없다면 생성하고, 그 정보를 저장한다.
     * @param user, target
     */
    @Transactional
    public ChatUserListResponseDto createChatRoom(User user, String target) throws Exception {
        User targetUser = userRepository.findByEmail(target).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        ChatRoomUser userChatRoom = chatRoomUserRepository.findByUserAndTargetUser(user, targetUser).orElse(null);
        ChatRoomUser targetChatRoom = chatRoomUserRepository.findByUserAndTargetUser(targetUser, user).orElse(null);

        if(userChatRoom == null && targetChatRoom == null) {
            ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().user(user).build());
            chatRoomUserRepository.save(ChatRoomUser.builder().chatRoom(chatRoom).user(user).targetUser(targetUser).build());
            chatRoomUserRepository.save(ChatRoomUser.builder().chatRoom(chatRoom).user(targetUser).targetUser(user).build());
        }

        return this.getChatUserList(user);
    }

    /**
     * 채팅방 목록을 반환한다.
     * @param chatRoomId
     */
    @Transactional
    public ChatResponseDto getChatList(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new EntityNotFoundException());

        List<Chat> chatList = chatRepository.findAllByChatRoom(chatRoom);
        List<ChatDto> chatDtoList = new ArrayList<>();

        chatList.forEach((chat) -> {
            String profileImgUrl = null;

            try {
                profileImgUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucket)
                                .object(chat.getUser().getProfileImgUrl())
                                .expiry(2, TimeUnit.HOURS)
                                .build());
            } catch (Exception e) {
                throw  new RuntimeException();
            }

            chatDtoList.add(ChatDto.toDto(chat, profileImgUrl));
        });

        return ChatResponseDto.builder().chatList(chatDtoList).build();
    }

    /**
     * 해당 채팅방에 유저가 전송한 메세지를 저장한다.
     * @param user, ChatRequestDto
     */
    @Transactional
    public ChatResponseDto saveChat(User user, ChatRequestDto requestDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(requestDto.getChatRoomId()).orElseThrow(() -> new EntityNotFoundException());

        chatRepository.save(Chat.builder()
                .chatRoom(chatRoom)
                .user(user)
                .content(requestDto.getMessage()).build());

        chatRoom.setLastMessage(requestDto.getMessage());

        return this.getChatList(requestDto.getChatRoomId());
    }

    /**
     * 같은 채팅방 번호를 구독한 유저들에게 simpMesageTemplate 형태로 메세지를 전송한다.
     * @param requestDto
     */
    @Transactional
    public void sendMessage(ChatRequestDto requestDto) {
        User sendUser = userRepository.findByEmail(requestDto.getSenderEmail()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        String profileImgUrl = null;
        try {
            profileImgUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(sendUser.getProfileImgUrl())
                            .expiry(2, TimeUnit.HOURS)
                            .build());
        } catch (Exception e) {
            throw  new RuntimeException();
        }

        simpMessageTemplate.convertAndSend("/subscribe/rooms/" + requestDto.getChatRoomId(),
                ChatMessage.builder()
                        .email(sendUser.getEmail())
                        .content(requestDto.getMessage())
                        .sendDate(LocalDateTime.now())
                        .nickname(sendUser.getNickname())
                        .profileUrl(profileImgUrl)
                        .build());
    }
}
