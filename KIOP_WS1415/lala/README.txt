Sprachen
========

Das Paket lala (= "Lambda Language") enthält Implementierungen zu den unterschiedlichen
Lambda-Sprachen. Zu jeder Sprache gehört 
  - ein Parser 
  - eine Evaluierungsstrategy (CallByValue oder NormalOder)
  - und ggf. ein TypChecker



Sprache: v01u = Einfacher, ungetypter Lambda-Kalkül
===================================================
Enthält den einfachen, ungetypten Lambda-Kalkül, bestehend aus Variable, Abstraction
und Application. 

Beispielworte der Sprache:
  - (x)
  - ((x)(y))
  - (λx.(x))
  - ((x)((y)(z)))
  - (λx.(λy.(y)))


Sprache: v02t = Einfacher, getypter Lambda-Kalkül
=================================================
Enthält den einfachen, ungetypten Lambda-Kalkül, bestehend aus Variable, Abstraction
und Application. Als Typen sind Bool, Num und FunctionType (z.B. Bool-> Num) implementiert.
Die Sprache enthält noch keine ExpressionConstants, d.h. es gibt noch kein true, false, etc.

Beispielworte der Sprache:
  - (λx:Bool.(x))
  - (λx:(Bool->Bool).(x))
  - (λx:(Bool->Num).(x))

 => ES IST NOCH KEIN TYPCHECK IMPLEMENTIERT!!!!
 
 
 
