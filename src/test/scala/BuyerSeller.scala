package nl.uva.cci

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorSystem, Scheduler}
import akka.util.Timeout
import asl.{buyer, buyer_norm_agent, seller, seller_norm_agent}
import bb.expstyla.exp.{IntTerm, StringTerm, StructTerm}
import infrastructure._
import nl.uva.cci.normativeservices.EFlintBBFactory
import nl.uva.cci.utils.{Environment, PlantUMLCommunicationLogger}
import org.scalatest.wordspec.AnyWordSpecLike
import std.DefaultCommunications

import _root_.scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

class BuyerSeller extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  import org.apache.log4j.BasicConfigurator
  BasicConfigurator.configure()
  val logger = PlantUMLCommunicationLogger()
  val loggableComs = new DefaultCommunications(logger)
  Environment.comsLogger = logger
  Environment.logActors(Seq("BuyerAgent","SellerAgent","BuyerAdvisor","SellerAdvisor"))


  val mas = MAS()

  override def beforeAll(): Unit = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()


    // Create System

    val system: ActorSystem[IMessage] = typed.ActorSystem(mas(), "MAS")

    val BB = new EFlintBBFactory("src/main/eflint/buy.eflint")
    //    ClusterBootstrap(system).start()
    implicit val timeout: Timeout = 5000.milliseconds
    implicit val ec: ExecutionContextExecutor = system.executionContext
    implicit val scheduler: Scheduler = system.scheduler

    // Ask the system to create agents
    val result: Future[IMessage] = system.ask(ref => AgentRequestMessage(
      Seq(
        AgentRequest(new seller_norm_agent(beliefBaseFactory = BB,coms = loggableComs).agentBuilder, "SellerAdvisor", 1),
        AgentRequest(new buyer_norm_agent(beliefBaseFactory = BB,coms = loggableComs).agentBuilder, "BuyerAdvisor", 1),
        AgentRequest(new seller(coms = loggableComs).agentBuilder, "SellerAgent", 1),
        AgentRequest(new buyer(coms = loggableComs).agentBuilder, "BuyerAgent", 1),
      ),ref))(timeout,scheduler)

    //wait for response
    val system_ready : Boolean = try {
      val response = Await.result(result, timeout.duration).asInstanceOf[ReadyMessage]

      true
    }
    catch {
      case x : Throwable =>
        x.printStackTrace()
        false
    }

    if(system_ready)
      println("agent created")

  }

  "the agents" should {
    "exist in yellow pages if it was created before" in {
        assert(mas.yellowPages.getAgent("BuyerAdvisor").isDefined)
        assert(mas.yellowPages.getAgent("SellerAdvisor").isDefined)
    }
  }


  "SellerAdvisor agent" should {
    "work!" in {
      val prob = testKit.createTestProbe[IMessage]()
      mas.yellowPages.getAgent("SellerAdvisor").get.asInstanceOf[AkkaMessageSource].address()  ! GoalMessage(
        StructTerm("perform",Seq(StructTerm("offer"))),AkkaMessageSource(prob.ref)
      )

      val m = prob.receiveMessage()
      println(m)

    }
  }

  "Buyer agent starting" should {
    "init" in {
      val prob = testKit.createTestProbe[IMessage]()
      mas.yellowPages.getAgent("SellerAgent").get.asInstanceOf[AkkaMessageSource].address()  ! BeliefMessage(
        StructTerm("buyer",Seq(StringTerm("BuyerAgent"))),AkkaMessageSource(prob.ref)
      )

      Thread.sleep(5000)
    }
  }


  "Buyer/seller agents" should {
    "init" in {
      val prob = testKit.createTestProbe[IMessage]()
      mas.yellowPages.getAgent("SellerAgent").get.asInstanceOf[AkkaMessageSource].address()  ! GoalMessage(
        StructTerm("initiate_buy",Seq(StringTerm("Book"))),AkkaMessageSource(prob.ref)
      )

      Thread.sleep(5000)
    }
  }


  override def afterAll(): Unit = {
    Thread.sleep(2000)

    println("WRITING TO FILE ...")
    // write the message to file "./logs/<name>.png"
    Environment.print_to_file("all")
    testKit.shutdownTestKit()
  }
}