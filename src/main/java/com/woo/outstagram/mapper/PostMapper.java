package com.woo.outstagram.mapper;

import com.woo.outstagram.dto.post.entity.PostDto;
import com.woo.outstagram.entity.user.User;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface PostMapper {
    List<PostDto> getPostList(List<Long> ids, Long following);
    List<PostDto> getMyPostList(Long id);
    List<PostDto> getSearchPostList(Long following, String query);
}
