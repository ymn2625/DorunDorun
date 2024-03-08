package com.example.dorundorun.service;

import com.example.dorundorun.dto.BoardDTO;
import com.example.dorundorun.dto.BoardLikeDTO;
import com.example.dorundorun.dto.MemberDTO;
import com.example.dorundorun.entity.BoardEntity;
import com.example.dorundorun.entity.BoardFileEntity;
import com.example.dorundorun.entity.BoardLikeEntity;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.repository.BoardFileRepository;
import com.example.dorundorun.repository.BoardLikeRepository;
import com.example.dorundorun.repository.BoardRepository;
import com.example.dorundorun.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final EntityManager entityManager;

    public void save(BoardDTO boardDTO, String username) throws IOException {

        //파일첨부 여부에 따른 로직 분리할 것
        if(boardDTO.getBoardFile().isEmpty()) {

            MemberEntity memberEntity = memberRepository.findByUsername(username).get();
            BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO, memberEntity);

            boardRepository.save(boardEntity);
        }else{
            MemberEntity memberEntity = memberRepository.findByUsername(username).get();
            BoardEntity boardEntity = BoardEntity.toFileBoardEntity(boardDTO, memberEntity);
            Long boardId = boardRepository.save(boardEntity).getBoardId();
            BoardEntity board = boardRepository.findById(boardId).get();
            for(MultipartFile boardFile:boardDTO.getBoardFile()){
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis()+"_"+username+"_"+originalFilename;
                String savePath = "C:/pg/springboot_img/"+storedFileName;
                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toboardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

//    @Transactional
//    public List<BoardDTO> findAll() {
//        List<BoardEntity> boardEntityList = boardRepository.findAll();
//        List<BoardDTO> boardDTOList = new ArrayList<>();
//        for(BoardEntity boardEntity: boardEntityList){
//            String nickname = boardEntity.getMemberEntity().getMemberNickname();
//            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity,nickname));
//        }
//
//        return boardDTOList;
//    }

    @Transactional
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;
        Page<BoardEntity> boardEntities = boardRepository.findAll(
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "boardId"))
        );

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getBoardId(), board.getMemberEntity().getMemberNickname(), board.getBoardTitle(), board.getBoardCategory(), board.getBoardHits(), board.getCreatedTime()));

        return boardDTOS;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> byId = boardRepository.findById(id);
        String username = byId.get().getMemberEntity().getUsername();
        if(byId.isPresent()){
            BoardEntity board = byId.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(board,username);
            return boardDTO;
        }else{
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO, String username) {

        MemberEntity memberEntity = memberRepository.findByUsername(username).get();
        boardRepository.save(BoardEntity.toUpdateBoardEntity(boardDTO, memberEntity));
        return findById(boardDTO.getBoardId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public BoardLikeDTO findByBoardId(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).get();
        MemberEntity memberEntity = boardEntity.getMemberEntity();

        int countLike = boardLikeRepository.countLike(id);
        int countHate = boardLikeRepository.countHate(id);
        BoardLikeEntity boardLikeEntity = boardLikeRepository.findByBoardEntityAndMemberEntity(boardEntity, memberEntity);

        BoardLikeDTO boardLikeDTO = new BoardLikeDTO();
        if(boardLikeEntity!=null) {
            boardLikeDTO=BoardLikeDTO.toBoardLikeDTO(boardLikeEntity);
            boardLikeDTO.setCountLike(countLike);
            boardLikeDTO.setCountHate(countHate);
        }else{
            boardLikeDTO.setCountLike(countLike);
            boardLikeDTO.setCountHate(countHate);
        }
        return boardLikeDTO;

    }

    @Transactional
    public Boolean like(BoardLikeDTO boardLikeDTO) {
        //부모엔티티 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardLikeDTO.getBoardId());
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(boardLikeDTO.getMemberId());
        if(optionalBoardEntity.isPresent() && optionalMemberEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            MemberEntity memberEntity = optionalMemberEntity.get();
            BoardLikeEntity boardLike = BoardLikeEntity.toBoardLikeEntity(boardEntity,memberEntity);
            BoardLikeEntity boardLikeEntity = boardLikeRepository.findByBoardEntityAndMemberEntity(boardEntity, memberEntity);
            if(boardLikeEntity != null) {
                if (boardLikeEntity.getBoardLike() == 1) {
                    boardLikeRepository.deleteById(boardLikeEntity.getBoardLikeId());
                } else {
                    if (boardLikeEntity.getBoardHate() == 1) {
                        boardLikeEntity.setBoardHate(0);
                        boardLikeEntity.setBoardLike(1);
                        boardLikeRepository.save(boardLikeEntity);
                    } else {
                        boardLike.setBoardLike(1);
                        boardLike.setBoardHate(0);
                        boardLikeRepository.save(boardLike);
                    }
                }
            }else{
                boardLike.setBoardHate(0);
                boardLike.setBoardLike(1);
                boardLikeRepository.save(boardLike);
            }
            return true;
        }else{
            return false;
        }
    }



    public Boolean hate(BoardLikeDTO boardLikeDTO) {
        //부모엔티티 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardLikeDTO.getBoardId());
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUsername(boardLikeDTO.getMemberId());
        if(optionalBoardEntity.isPresent() && optionalMemberEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            MemberEntity memberEntity = optionalMemberEntity.get();
            BoardLikeEntity boardLike = BoardLikeEntity.toBoardLikeEntity(boardEntity,memberEntity);
            BoardLikeEntity boardLikeEntity = boardLikeRepository.findByBoardEntityAndMemberEntity(boardEntity, memberEntity);
            if(boardLikeEntity != null) {
                if (boardLikeEntity.getBoardHate() == 1) {
                    boardLikeRepository.deleteById(boardLikeEntity.getBoardLikeId());
                } else {
                    if (boardLikeEntity.getBoardLike() == 1) {
                        boardLikeEntity.setBoardLike(0);
                        boardLikeEntity.setBoardHate(1);
                        boardLikeRepository.save(boardLikeEntity);
                    } else {
                        boardLike.setBoardHate(1);
                        boardLike.setBoardLike(0);
                        boardLikeRepository.save(boardLike);
                    }
                }
            }else{
                boardLike.setBoardLike(0);
                boardLike.setBoardHate(1);
                boardLikeRepository.save(boardLike);
            }
            return true;
        }else{
            return false;
        }
    }
}
