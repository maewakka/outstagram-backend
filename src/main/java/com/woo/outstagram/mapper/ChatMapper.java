package com.woo.outstagram.mapper;

import com.woo.outstagram.dto.chat.ChatUserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    List<ChatUserDto> getChatUserList(Long ownId);

}
