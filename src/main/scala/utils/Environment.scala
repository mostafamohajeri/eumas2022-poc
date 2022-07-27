package nl.uva.cci
package utils

import bb.expstyla.exp.{GenericTerm, IntTerm, StringTerm, StructTerm}
import infrastructure.{AkkaMessageSource, ExecutionContext, GoalMessage, IMessageSource}

import scala.collection.mutable

object Environment {

  var environmentActor : AkkaMessageSource = null
  var comsLogger : PlantUMLCommunicationLogger = null

  def print_to_file(caseId: String): Unit = {
      comsLogger.writeToFile(caseId,"","")
  }


  def logEvent(content:GenericTerm)(implicit executionContext: ExecutionContext): Unit = {
    comsLogger.logEvent(executionContext.name,content.getStringValue)
  }

  def logActors(actorNames:Seq[String]): Unit = {
    comsLogger.logActor(actorNames)
  }




}
