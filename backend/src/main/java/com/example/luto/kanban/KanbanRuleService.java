package com.example.luto.kanban;

import com.example.luto.dto.Card;
import com.example.luto.dto.MoveCardRequest;
import org.springframework.stereotype.Service;

@Service
public class KanbanRuleService {

  public void validateTransition(Card currentCard, String targetColumnCategory, MoveCardRequest req) {
    boolean checklistOk = false;
    if (currentCard != null && currentCard.data() != null) {
      Object v = currentCard.data().get("checklistCompleted");
      checklistOk = (v instanceof Boolean && (Boolean) v) || "true".equalsIgnoreCase(String.valueOf(v));
    }
    if ("SUCCESS".equals(targetColumnCategory) && !checklistOk) {
      throw new RuleViolationException("KANBAN_CHECKLIST_REQUIRED", "Checklist obrigatório para concluir (SUCCESS).");
    }
    if ("FAILURE".equals(targetColumnCategory)) {
      if (req == null || req.justification() == null || req.justification().isBlank()) {
        throw new RuleViolationException("KANBAN_JUSTIFICATION_REQUIRED", "Justificativa obrigatória ao mover para 'Falha'.");
      }
    }
  }
}
