package docspell.query

import minitest._
import docspell.query.ItemQuery.{Operator, TagOperator}
import docspell.query.internal.OperatorParser

object OperatorParserTest extends SimpleTestSuite {
  test("operator values") {
    val p = OperatorParser.op
    assertEquals(p.parseAll("="), Right(Operator.Eq))
    assertEquals(p.parseAll(":"), Right(Operator.Like))
    assertEquals(p.parseAll("<"), Right(Operator.Lt))
    assertEquals(p.parseAll(">"), Right(Operator.Gt))
    assertEquals(p.parseAll("<="), Right(Operator.Lte))
    assertEquals(p.parseAll(">="), Right(Operator.Gte))
  }

  test("tag operators") {
    val p = OperatorParser.tagOp
    assertEquals(p.parseAll(":"), Right(TagOperator.AnyMatch))
    assertEquals(p.parseAll("="), Right(TagOperator.AllMatch))
  }
}
