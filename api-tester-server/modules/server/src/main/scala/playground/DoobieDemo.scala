package playground

import cats.effect.kernel.Resource
import cats.effect.{IO, IOApp, MonadCancelThrow}
import doobie.hikari.HikariTransactor
import doobie.implicits.*
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor

object DoobieDemo extends IOApp.Simple {

  case class Student(id: Long, name: String)

  val xa: Transactor[IO] = Transactor.fromDriverManager(
    driver = "com.mysql.cj.jdbc.Driver",
    url = "jdbc:mysql://localhost/students_db",
    user = "admin",
    password = "sys",
    logHandler = None
  )

  def findAllStudentNames(): IO[List[String]] = {
    val query = sql"select name from students".query[String]
    val action = query.to[List]

    action.transact(xa)
  }

  def saveStudent(name: String): IO[Int] = {
    val query = sql"insert into students (name) values ($name)"
    val action = query.update.run

    action.transact(xa)
  }

  def findStudentsByInitial(letter: String): IO[List[Student]] = {
    val selectPart = fr"select id, name"
    val fromPart = fr"from students"
    val wherePart = fr"where left(name, 1) = $letter"

    val statement = selectPart ++ fromPart ++ wherePart
    val query = statement.query[Student]
    val action = query.to[List]

    action.transact(xa)
  }

  // repository
  trait Students[F[_]] {
    def findById(id: Int): F[Option[Student]]
    def findAll(): F[List[Student]]
    def create(name: String): F[Int]
  }

  object Students {
    def make[F[_]: MonadCancelThrow](xa: Transactor[F]): Students[F] = new Students[F] {
      
      override def findById(id: Int): F[Option[Student]] = {
        val query = sql"select id, name from students where id = $id".query[Student]
        val action = query.option
        
        action.transact(xa)
      }

      override def findAll(): F[List[Student]] = {
        val query = sql"select id, name from students".query[Student]
        val action = query.to[List]

        action.transact(xa)
      }

      override def create(name: String): F[Int] = {
        val query = sql"insert into students (name) values ($name)"
        val action = query.update.run
        
        action.transact(xa)
      }
    }
  }
  
  val mysqlResource: Resource[IO, HikariTransactor[IO]] = for {
    ec <- ExecutionContexts.fixedThreadPool[IO](16)
    xa <- HikariTransactor.newHikariTransactor[IO](
      driverClassName = "com.mysql.cj.jdbc.Driver",
      url = "jdbc:mysql://localhost/students_db",
      user = "admin",
      pass = "sys",
      connectEC = ec,
      logHandler = None
    )
  } yield xa
  
  val program = mysqlResource.use { xa =>
    val repo: Students[IO] = Students.make(xa)
    
    for {
      allStudents <- repo.findAll()
      _ <- IO.println(allStudents)
    } yield ()
  }

  override def run: IO[Unit] = program
}
