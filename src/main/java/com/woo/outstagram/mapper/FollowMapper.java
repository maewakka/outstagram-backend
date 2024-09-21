package com.woo.outstagram.mapper;

import com.woo.outstagram.dto.follow.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {

    List<UserDto> getUserLists(Long ownId);

}
