package docspell.store.generator

import docspell.store.StoreFixture
import docspell.store.records._
import minitest._
import docspell.common._
import docspell.query.ItemQueryParser
import docspell.store.qb.generator.{ItemQueryGenerator, Tables}
import docspell.store.impl.DoobieMeta._
import docspell.store.qb.impl.ConditionBuilder

object ItemQueryGeneratorTest extends SimpleTestSuite with StoreFixture {

  val tables = Tables(
    RItem.as("i"),
    ROrganization.as("co"),
    RPerson.as("cp"),
    RPerson.as("np"),
    REquipment.as("ne"),
    RFolder.as("f"),
    RAttachment.as("a"),
    RAttachmentMeta.as("m")
  )

  test("migration") {
    val q = ItemQueryParser
      .parseUnsafe("(& name:hello date>2020-02-01 (| source=expense tag:[car,expense]))")
    val cond = ItemQueryGenerator(tables, Ident.unsafe("coll"))(q)
    print(s"${ConditionBuilder.build(cond)}")
  }

//  test("migration2") {
//    withStore("db2") { store =>
//      val c = RCollective(
//        Ident.unsafe("coll1"),
//        CollectiveState.Active,
//        Language.German,
//        true,
//        Timestamp.Epoch
//      )
//      val e =
//        REquipment(
//          Ident.unsafe("equip"),
//          Ident.unsafe("coll1"),
//          "name",
//          Timestamp.Epoch,
//          Timestamp.Epoch,
//          None
//        )
//
//      for {
//        _ <- store.transact(RCollective.insert(c))
//        _ <- store.transact(REquipment.insert(e)).map(_ => ())
//      } yield ()
//    }
//  }
}
