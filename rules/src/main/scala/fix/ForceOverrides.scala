package fix

import scalafix.v1._
import scala.meta._

class ForceOverrides extends SemanticRule("ForceOverrides") {

  private def getMethods(semanticType: SemanticType)(implicit doc: SemanticDocument): List[(String, MethodSignature)] =
    semanticType match {
      case TypeRef(prefix, symbol, typeArguments) =>
        symbol.info match {
          case Some(info) =>
            info.signature match {
              case ClassSignature(typeParameters, parents, self, declarations) =>
                declarations.collect {
                  case declaration if declaration.isDef =>
                    declaration.signature match {
                      case signature: MethodSignature =>
                        declaration.displayName -> signature
                    }
                }
              case _ => Nil
            }
          case None => Nil
        }
      case _ => Nil
    }

  private def paramEq(s1: SymbolInformation, s2: SymbolInformation): Boolean = {
    s1.signature == s2.signature
  }

  private def paramListEq(l1: List[SymbolInformation], l2: List[SymbolInformation]): Boolean =
    (l1, l2) match {
      case (h1 :: t1, h2 :: t2) => paramEq(h1, h2) && paramListEq(t1, t2)
      case (Nil, Nil) => true
      case _ => false
    }

  private def paramListListEq(l1: List[List[SymbolInformation]], l2: List[List[SymbolInformation]]): Boolean =
    (l1, l2) match {
      case (h1 :: t1, h2 :: t2) => paramListEq(h1, h2) && paramListListEq(t1, t2)
      case (Nil, Nil) => true
      case _ => false
    }

  private def defnFix(t: Tree)(implicit doc: SemanticDocument): List[Patch] = {
    val parentMethods = t.symbol.info match {
      case Some(info) =>
        info.signature match {
          case ClassSignature(typeParameters, parents, self, declarations) =>
            parents.flatMap(getMethods)
          case _ => Nil
        }
      case None => Nil
    }
    t.collect {
      case t@Defn.Def(mods, name, tparams, paramss, typeOpt, term) =>
        t.symbol.info match {
          case Some(info) =>
            info.signature match {
              case signature@MethodSignature(typeParameters, parameterLists, returnType) =>
                if (parentMethods.exists {
                  case (methodName, methodSignature) =>
                    methodName == name.value &&
                      paramListEq(methodSignature.typeParameters, signature.typeParameters) &&
                      paramListListEq(methodSignature.parameterLists, signature.parameterLists) &&
                      methodSignature.returnType == signature.returnType
                }) {
                  Patch.addLeft(t, "override ")
                } else {
                  Patch.empty
                }
            }
          case None => Patch.empty
        }
    }
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t : Defn.Class =>
        defnFix(t)
      case t @ Defn.Object(mod, termName, template) =>
        defnFix(t)
    }
  }.flatten.asPatch

}
