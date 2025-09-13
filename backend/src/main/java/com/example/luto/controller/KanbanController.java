package com.example.luto.controller;

import com.example.luto.dto.Board;
import com.example.luto.dto.Card;
import com.example.luto.dto.Column;
import com.example.luto.dto.CreateBoardRequest;
import com.example.luto.dto.CreateCardRequest;
import com.example.luto.dto.CreateColumnRequest;
import com.example.luto.dto.MoveCardRequest;
import com.example.luto.kanban.KanbanRuleService;
import com.example.luto.kanban.RuleViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/v1")
public class KanbanController {

  private final KanbanRuleService rules;

  public KanbanController(KanbanRuleService rules) { this.rules = rules; }

  @GetMapping("/kanban/boards")
  public ResponseEntity<List<Board>> listBoards() {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/kanban/boards")
  public ResponseEntity<Board> createBoard(@RequestBody CreateBoardRequest req, @RequestHeader("Idempotency-Key") String idem) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/kanban/boards/{boardId}/columns")
  public ResponseEntity<Column> addColumn(@PathVariable("boardId") UUID boardId, @RequestBody CreateColumnRequest req, @RequestHeader("Idempotency-Key") String idem) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/kanban/cards")
  public ResponseEntity<Card> createCard(@RequestBody CreateCardRequest req, @RequestHeader("Idempotency-Key") String idem) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/kanban/cards/{cardId}/move")
  public ResponseEntity<?> moveCard(@PathVariable("cardId") UUID cardId, @RequestBody MoveCardRequest req, @RequestHeader("Idempotency-Key") String idem) {
    Map<String,Object> data = new HashMap<>();
    data.put("checklistCompleted", false); // placeholder; trocar pela leitura do BD
    Card current = new Card(cardId, UUID.randomUUID(), UUID.randomUUID(), "Placeholder", data, null, null, "OPEN", null);

    try {
      String targetCategory = "SUCCESS"; // TODO: buscar categoria da coluna alvo no BD
      rules.validateTransition(current, targetCategory, req);
      return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    } catch (RuleViolationException e) {
      Map<String,String> err = new HashMap<>();
      err.put("code", e.getCode());
      err.put("message", e.getMessage());
      return ResponseEntity.status(409).body(err);
    }
  }
}
