package com.kpbochenek

import java.time.Instant

//import cats.free.Free

class BankAccount {

}


case class AccountId(id: String)
case class Account(no: AccountId, money: Int, openDate: Instant, closeDate: Option[String], suspended: Boolean)

trait AccountServiceF[T]
case class Open[T](money: Int) extends AccountServiceF[T]
case class Close[T](no: AccountId) extends AccountServiceF[T]
case class Get[T](no: AccountId) extends AccountServiceF[T]
case class Suspend[T](no: AccountId) extends AccountServiceF[T]
case class Restore[T](no: AccountId) extends AccountServiceF[T]
case class Transfer(from: AccountId, to: AccountId, amount: Int) extends AccountServiceF[Unit]

object AccountService {
//  def open(money: Int): Free[AccountServiceF, Account] = Free.liftF(Open(money))
}
