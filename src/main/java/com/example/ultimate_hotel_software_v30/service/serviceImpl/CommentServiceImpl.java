package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.CommentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.CommentDTOResponse;
import com.example.ultimate_hotel_software_v30.mapper.CommentMapper;
import com.example.ultimate_hotel_software_v30.model.CommentEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.CommentRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements com.example.ultimate_hotel_software_v30.service.CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDTOResponse createComment(CommentDTORequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        CommentEntity comment = commentMapper.toCommentEntity(request);
        comment.setUserEntity(user);

        CommentEntity saved = commentRepository.save(comment);
        return commentMapper.toCommentDTOResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTOResponse> getMyComments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return commentRepository.findByUserEntity_Id(user.getId()).stream()
                .map(commentMapper::toCommentDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTOResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toCommentDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getRole().name().equalsIgnoreCase("ADMIN"));
        boolean isOwner = comment.getUserEntity().getId().equals(user.getId());

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("No tienes permisos para eliminar este comentario.");
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public CommentDTOResponse updateComment(Long id, CommentDTORequest request) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getRole().name().equalsIgnoreCase("ADMIN"));
        boolean isOwner = comment.getUserEntity().getId().equals(user.getId());

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("No tienes permisos para modificar este comentario.");
        }

        comment.setContent(request.getContent());
        comment.setRating(request.getRating());

        CommentEntity updatedComment = commentRepository.save(comment);
        return commentMapper.toCommentDTOResponse(updatedComment);
    }
}