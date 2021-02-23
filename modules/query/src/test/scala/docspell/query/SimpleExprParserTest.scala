package docspell.query

import cats.data.{NonEmptyList => Nel}
import docspell.query.ItemQuery._
import docspell.query.internal.SimpleExprParser
import minitest._

object SimpleExprParserTest extends SimpleTestSuite {

  test("string expr") {
    val p = SimpleExprParser.stringExpr
    assertEquals(
      p.parseAll("name:hello"),
      Right(stringExpr(Operator.Like, Attr.ItemName, "hello"))
    )
    assertEquals(
      p.parseAll("name:  hello"),
      Right(stringExpr(Operator.Like, Attr.ItemName, "hello"))
    )
    assertEquals(
      p.parseAll("name:\"hello world\""),
      Right(stringExpr(Operator.Like, Attr.ItemName, "hello world"))
    )
    assertEquals(
      p.parseAll("name : \"hello world\""),
      Right(stringExpr(Operator.Like, Attr.ItemName, "hello world"))
    )
    assertEquals(
      p.parseAll("conc.pers.id=Aaiet-aied"),
      Right(stringExpr(Operator.Eq, Attr.Concerning.PersonId, "Aaiet-aied"))
    )
  }

  test("date expr") {
    val p = SimpleExprParser.dateExpr
    assertEquals(
      p.parseAll("due:2021-03-14"),
      Right(dateExpr(Operator.Like, Attr.DueDate, ld(2021, 3, 14)))
    )
    assertEquals(
      p.parseAll("due<2021-03-14"),
      Right(dateExpr(Operator.Lt, Attr.DueDate, ld(2021, 3, 14)))
    )
  }

  test("exists expr") {
    val p = SimpleExprParser.existsExpr
    assertEquals(p.parseAll("exists:name"), Right(Expr.Exists(Attr.ItemName)))
    assert(p.parseAll("exists:blabla").isLeft)
    assertEquals(
      p.parseAll("exists:conc.pers.id"),
      Right(Expr.Exists(Attr.Concerning.PersonId))
    )
  }

  test("fulltext expr") {
    val p = SimpleExprParser.fulltextExpr
    assertEquals(p.parseAll("content:test"), Right(Expr.Fulltext("test")))
    assertEquals(
      p.parseAll("content:\"hello world\""),
      Right(Expr.Fulltext("hello world"))
    )
  }

  test("category expr") {
    val p = SimpleExprParser.catExpr
    assertEquals(
      p.parseAll("cat:expense,doctype"),
      Right(Expr.TagCategoryMatch(TagOperator.AnyMatch, Nel.of("expense", "doctype")))
    )
  }

  test("custom field") {
    val p = SimpleExprParser.customFieldExpr
    assertEquals(
      p.parseAll("f:usd=26.66"),
      Right(Expr.CustomFieldMatch("usd", Operator.Eq, "26.66"))
    )
  }

  test("tag id expr") {
    val p = SimpleExprParser.tagIdExpr
    assertEquals(
      p.parseAll("tag.id:a,b,c"),
      Right(Expr.TagIdsMatch(TagOperator.AnyMatch, Nel.of("a", "b", "c")))
    )
    assertEquals(
      p.parseAll("tag.id:a"),
      Right(Expr.TagIdsMatch(TagOperator.AnyMatch, Nel.of("a")))
    )
    assertEquals(
      p.parseAll("tag.id=a,b,c"),
      Right(Expr.TagIdsMatch(TagOperator.AllMatch, Nel.of("a", "b", "c")))
    )
    assertEquals(
      p.parseAll("tag.id=a"),
      Right(Expr.TagIdsMatch(TagOperator.AllMatch, Nel.of("a")))
    )
    assertEquals(
      p.parseAll("tag.id=a,\"x y\""),
      Right(Expr.TagIdsMatch(TagOperator.AllMatch, Nel.of("a", "x y")))
    )
  }

  test("simple expr") {
    val p = SimpleExprParser.simpleExpr
    assertEquals(
      p.parseAll("name:hello"),
      Right(stringExpr(Operator.Like, Attr.ItemName, "hello"))
    )
    assertEquals(
      p.parseAll("name:hello"),
      Right(stringExpr(Operator.Like, Attr.ItemName, "hello"))
    )
    assertEquals(
      p.parseAll("due:2021-03-14"),
      Right(dateExpr(Operator.Like, Attr.DueDate, ld(2021, 3, 14)))
    )
    assertEquals(
      p.parseAll("due<2021-03-14"),
      Right(dateExpr(Operator.Lt, Attr.DueDate, ld(2021, 3, 14)))
    )
    assertEquals(
      p.parseAll("exists:conc.pers.id"),
      Right(Expr.Exists(Attr.Concerning.PersonId))
    )
    assertEquals(p.parseAll("content:test"), Right(Expr.Fulltext("test")))
    assertEquals(
      p.parseAll("tag.id:a"),
      Right(Expr.TagIdsMatch(TagOperator.AnyMatch, Nel.of("a")))
    )
    assertEquals(
      p.parseAll("tag.id=a,b,c"),
      Right(Expr.TagIdsMatch(TagOperator.AllMatch, Nel.of("a", "b", "c")))
    )
    assertEquals(
      p.parseAll("cat:expense,doctype"),
      Right(Expr.TagCategoryMatch(TagOperator.AnyMatch, Nel.of("expense", "doctype")))
    )
    assertEquals(
      p.parseAll("f:usd=26.66"),
      Right(Expr.CustomFieldMatch("usd", Operator.Eq, "26.66"))
    )
  }

  def ld(y: Int, m: Int, d: Int) =
    DateParserTest.ld(y, m, d)

  def stringExpr(op: Operator, name: Attr.StringAttr, value: String): Expr.SimpleExpr =
    Expr.SimpleExpr(op, Property.StringProperty(name, value))

  def dateExpr(op: Operator, name: Attr.DateAttr, value: Date): Expr.SimpleExpr =
    Expr.SimpleExpr(op, Property.DateProperty(name, value))
}
