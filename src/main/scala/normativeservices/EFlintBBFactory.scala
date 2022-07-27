package nl.uva.cci
package normativeservices

import bb.expstyla.exp.GenericTerm
import bb.{IBeliefBase, IBeliefBaseFactory}

class EFlintBBFactory(file: String) extends IBeliefBaseFactory {
  override def apply(): IBeliefBase[GenericTerm] = new EflintBeliefBase(file)
}
