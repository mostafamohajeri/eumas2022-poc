package nl.uva.cci
package normativeservices

import bb.expstyla.exp._

object ASCToEFlintStringifier {
  def createString(term: GenericTerm): String = term match {
    case v: VarTerm => createString(v.ref)
    case IntTerm(value) => value.toString
    case DoubleTerm(value) => value.toString
    case StringTerm(value) => s"\"$value\""
    case BooleanTerm(value) => value.toString
    case StructTerm(functor, terms) => functor match {
      case "," => terms.map(t=> createString(t)).mkString("&&")
      case _functor :String =>
        val newFunctor = _functor match {
          case "exists" => "?"
          case "enabled" => "?Enabled"
          case _ => _functor
        }
        newFunctor + (if (terms.nonEmpty) ("(" + terms.map(t => createString(t)).mkString(",") + ")")
        else "")
    }
  }

  def parseTerm(input:String) : GenericTerm ={
    val structWithTerm= raw"([a-zA-Z_][a-zA-Z_0-9]*)\((.*)\)".r
    val structNoTermLowerStart= raw"([a-z_][a-zA-Z_0-9]*)".r
    val structNoTermUpperStart= raw"([A-Z_][a-zA-Z_0-9]*)".r
    val integer= raw"([0-9]+)".r
    val double= raw"([0-9]+\.[0.9]+)".r
    val string= raw""""([a-zA-Z\-_0-9]*)"""".r

    input match {
      case structWithTerm(f,terms) => StructTerm(f,
        terms.split(",").map(s=>s.trim).map(parseTerm)
      )
      case structNoTermUpperStart(f) => StringTerm(f)
      case structNoTermLowerStart(f) => StructTerm(f)
      case integer(v) => IntTerm(v.toInt)
      case double(v) => DoubleTerm(v.toDouble)
      case string(v) => StringTerm(v)
      case "True" => BooleanTerm(true)
      case "False" => BooleanTerm(false)
      case _ => throw new RuntimeException(f"could not match $input")
     }
  }

}
