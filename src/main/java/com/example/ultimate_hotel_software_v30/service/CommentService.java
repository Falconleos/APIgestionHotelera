package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.CommentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.CommentDTOResponse;

import java.util.List;

public interface CommentService {
    CommentDTOResponse createComment(CommentDTORequest request);
    CommentDTOResponse updateComment(Long id, CommentDTORequest request);
    List<CommentDTOResponse> getAllComments();
    List<CommentDTOResponse> getMyComments();
    void deleteComment(Long id);
}