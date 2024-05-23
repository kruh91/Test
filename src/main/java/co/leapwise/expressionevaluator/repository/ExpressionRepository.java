package co.leapwise.expressionevaluator.repository;

import co.leapwise.expressionevaluator.model.Expression;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("expressionRepository")
public interface ExpressionRepository extends CrudRepository<Expression, Long> {
}
