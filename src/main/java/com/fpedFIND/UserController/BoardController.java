package com.fpedFIND.UserController;

import org.springframework.web.bind.annotation.*;

import com.fpedFIND.Entity.Board;
import com.fpedFIND.Repository.BoardRepository;

import java.util.List;

@RestController
@RequestMapping("/boards")
@CrossOrigin(origins = "*")
public class BoardController {

    private final BoardRepository boardRepository;

    
    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // GET: Retrieve all boards
    @GetMapping("/allboard")
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // POST: Add a new board
    @PostMapping("/add")
    public void addBoard(@RequestBody Board board) {
        boardRepository.save(board);
    }



    // DELETE: Delete a board by ID
    @DeleteMapping("/delete/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
    
    
 // PUT: Update a board by ID
    @PutMapping("/update/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board newBoard) {
        return boardRepository.findById(id)
            .map(board -> {
                board.setTitle(newBoard.getTitle());
                board.setContent(newBoard.getContent());
                board.setDatecreation(newBoard.getDatecreation());
                board.setDateexpiration(newBoard.getDateexpiration());
                board.setLink(newBoard.getLink());
                return boardRepository.save(board);
            })
            .orElseGet(() -> {
                newBoard.setId(id);
                return boardRepository.save(newBoard);
            });
    }
    
}
