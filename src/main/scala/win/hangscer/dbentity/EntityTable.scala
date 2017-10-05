package win.hangscer.dbentity

import akka.event.LoggingAdapter
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object DButils {
  lazy val db = Database.forConfig("dbConnection")
  //cc是一个存放TableQuery类型字段的对象的实例，比如对于object EntityTable，直接传入EntityTable就好，对于trait EntityTable的话，传入其匿名实例就行
  def checkDBSchema(cc: AnyRef, log: LoggingAdapter,db:Database) = {
    cc.getClass.getDeclaredFields.foreach(field => {
      field.setAccessible(true)
      field.get(cc) match {
        case items: TableQuery[_] => db.run(items.result.headOption).onComplete {
          case Success(result) => result match {
            case None => log.warning(s"""${field.getName}    =====> 不存在数据""")
            case Some(_) => log.info(s"""${field.getName} -----> OK,模式与数据库匹配 """)
          }
          case Failure(e) => log.error(s"""${field.getName} 模式存在问题 ***********> ${e.getMessage}""")
        }
        case _ => () //不是TableQuery类型，不管它
      }
    }
    )
  }

}

object Protocols {

  final case class AppEntity(appId: Long, appName: String, indexName: String, iconPath: Option[String] = None, appAlias: String, appType: String = "normal", status: Int = 0, remark: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class AppChartEntity(chartId: Long, appId: Long, content: String, option: String, timeFilter: String = "{}", spl: String, title: String, searchType: String, status: Int = 1, description: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class AppPanelEntity(id: Long, appId: Long, timeFilter: String = "{}", theme: String, title: String, inputs: String = "[]", status: Int = 1, description: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class AppPanelDetailEntity(panelId: Long, detailId: Long, chartId: Long, sizeX: Int, sizeY: Int, row: Int, col: Int, appId: Long, content: String, option: String, timeFilter: String = "{}", spl: String, title: String, searchType: String, description: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class AppPermissionEntity(appPermissionId: Long, appId: Long, entityId: Long, entityType: String, permissionType: String, priority: Int, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class AppSearchCollectEntity(searchCollectId: Long, appId: Long, content: String, option: String, timeFilter: String, description: Option[String] = None, spl: String, title: String, searchType: String, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class AppSearchHistoryEntity(searchHistoryId: Long, appId: Long, spl: String, created: Long, createdBy: Long)

  final case class BakStorageEntity(storageId: Long, name: String, storageType: String, path: String, uri: Option[String] = None, status: Byte = 1, isZip: Byte = 0, isSlice: Byte = 0, maxBakRate: Option[Short] = None, maxRestoreRate: Option[Short] = None, description: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class BasicDataEntity(id: Long, code: String, alias: String, category: String, status: Int = 0, isSystem: Int, parentId: Long = 0L, index: Option[Int] = None, remark: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class CharacterEntity(characterId: Long, appId: Long, characterName: String, condition: String, description: String, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class DsAgentEntity(agentId: Long, guid: String, addressInfo: String, hostName: String, os: String, ip: String, ipAll: String, ipAllHash: String, connect: Int, approved: Option[Long] = None, approvedBy: Option[Long] = None, status: Int = 0, remark: Option[String] = None, agentTag: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long, lastHeartBeat: Option[Long] = None)

  final case class DsDBEntity(id: Long, sourceType: String, guid: String, datasourceName: String, hostName: String, serverPort: String, dbName: String, instanceName: Option[String] = None, userName: String, password: String, status: Int = 0, statusDescription: Option[String] = None, description: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class DsTruesightEntity(id: Long, sourceType: String, guid: String, datasourceName: String, hostName: String, userName: String, password: String, tenant: Option[String] = None, truesightVersion: String, osUserName: Option[String] = None, osPassword: Option[String] = None, osPort: Option[String] = None, transferProtocol: String, transferPort: String, initTimeoutSecond: Int, status: Int = 0, statusDescription: Option[String] = None, description: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class DsTruesightMetricEntity(guid: String, dsTruesightId: Long, metricGroup: String, metricType: String, metricName: String)

  final case class DsTypeEntity(dsTypeId: Long, typeName: String, typeCode: String, typeTag: String, description: Option[String] = None, api: String, parentTypeId: Long = 0L, docType: Option[String] = None)

  final case class FilterGroupEntity(filterGroupId: Long, streamingId: Long, sampleData: String, filtersJson: String, yamlJson: String, elements: String, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class FilterGroupHistoryEntity(id: Long, appName: String, jobId: String, state: String, jobResource: String, topic: String, retCode: Option[String] = None, message: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class FilterGroupTemplateEntity(filterGroupId: Long, appId: Long, sampleData: String, filtersJson: String, yamlJson: String, elements: String, category: Option[String] = None, globalFlag: Option[String] = Some("0"), alias: String, status: Int = 0, remark: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class InputDBEntity(streamingId: Long, dsDBId: Long, guid: String, syncMode: String, refreshInterval: Int, sql: String, increment: String, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class InputFileEntity(streamingId: Long, agentId: Long, guid: String, inputPath: Option[String] = None, splitter: Option[String] = None, charset: Option[String] = None, pathFilter: Option[String] = None, pathType: Int, contentType: Int, contentFilter: Option[String] = None, recursive: Int = 0, multiline: Int = 0, proName: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class InputFileAttrEntity(id: Long, streamingId: Long, attrType: String, savePos: Int, readFromLast: Int, pollInterval: Int, dirCheckInterval: Int, activeFiles: Int, renameCheck: Int, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class InputTcpUdpEntity(streamingId: Long, agentId: Long, guid: String, catetory: String, `type`: Option[String] = None, ip: String, port: Int, format: Option[String] = None, charset: Option[String] = None, multiline: Option[String] = None, include: Option[String] = None, exclude: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class InputTruesightEntity(streamingId: Long, dsTruesightId: Long, guid: String, configRefreshHour: Int, metricRefreshSecond: Int, collectConcurrentCount: Int, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class InputTruesightMetricEntity(streamingId: Long, dsTruesightMetricGuid: String)

  final case class JobEntity(jobId: Long, name: String, status: Byte = 1, lifespan: Short, jobType: Byte = 1, description: Option[String] = None, informOnSuccess: Byte = 0, informOnFailure: Byte = 0, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class JobBakEntity(bakId: Long, jobId: Long, storageId: Long, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class JobBakAppsEntity(bakId: Long, appId: Long)

  final case class JobCycleEntity(cycleId: Long, jobId: Long, planType: String, beginDate: Option[Long] = None, endDate: Option[Long] = None, isLastDay: Byte = 0, day: Option[String] = None, once: Option[java.sql.Date] = None, hour: Byte = 0, min: Byte = 0, status: Option[Byte] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class JobTaskBakEntity(taskBakId: Long, taskBakName: String, bakId: Long, storageName: String, status: Byte = 0, created: Long, updated: Long)

  final case class JobTaskBakSpecEntity(specId: Long, taskBakId: Long, appId: Long, indexName: String, status: Byte = 0, result: Option[String] = None)

  final case class LdapConfigEntity(ldapId: Long, host: String, port: Int, baseDN: String, baseFilter: Option[String] = None, bindDN: String, bindPwd: String, isSSL: Int, status: Int = 0, order: Int, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class StreamingEntity(streamingId: Long, streamingName: String, stream: String, appId: Long, timestamp: Option[String] = None, status: Int, sourceType: String, created: Long, createdBy: Long, updated: Long, updatedBy: Long, remark: Option[String] = None)

  final case class TransBaseConfigEntity(id: Long, sourceName: String, sourceFieldName: String, sourceFieldType: String, required: Int = 1)

  final case class TransMonitorEntity(transactionMonitorId: Long, appId: Long, systemName: String, groupName: String, order: Int, streamingId: Option[Long] = None, componentType: Int, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class TransMonitorDetailEntity(transactionMonitorId: Long, baseMonitorConfigId: Long, targetField: Option[String] = None, config: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class TransThresholdEntity(id: Long, name: String, alias: String)

  final case class TransThresholdValueEntity(transactionMonitorId: Long, thresholdId: Long, value0: Int, value1: Int)

  final case class UserEntity(userId: Long, userName: String, loginName: String, password: String, roleCode: String, status: Int = 0, remark: Option[String] = None, lastLoginTime: Option[Long] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class UserDefaultPanelEntity(userId: Long, panelId: Long)

  final case class UserGroupEntity(userGroupId: Long, groupName: String, status: Int = 0, remark: Option[String] = None, created: Long, createdBy: Long, updated: Long, updatedBy: Long)

  final case class UserGroupRelationEntity(userGroupId: Long, userId: Long, created: Long, createdBy: Long)

}

object EntityTable {

  import Protocols._

  class App(_tableTag: Tag) extends Table[AppEntity](_tableTag, "app") {
    def * = (appId, appName, indexName, iconPath, appAlias, appType, status, remark, created, createdBy, updated, updatedBy) <> (AppEntity.tupled, AppEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(appId), Rep.Some(appName), Rep.Some(indexName), iconPath, Rep.Some(appAlias), Rep.Some(appType), Rep.Some(status), remark, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => AppEntity.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8, _9.get, _10.get, _11.get, _12.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column appId SqlType(BIGINT), PrimaryKey */
    val appId: Rep[Long] = column[Long]("appId", O.PrimaryKey)
    /** Database column appName SqlType(VARCHAR), Length(255,true) */
    val appName: Rep[String] = column[String]("appName")
    /** Database column indexName SqlType(VARCHAR), Length(255,true) */
    val indexName: Rep[String] = column[String]("indexName")
    /** Database column iconPath SqlType(VARCHAR), Length(500,true), Default(None) */
    val iconPath: Rep[Option[String]] = column[Option[String]]("iconPath", O.Default(None))
    /** Database column appAlias SqlType(VARCHAR), Length(255,true) */
    val appAlias: Rep[String] = column[String]("appAlias")
    /** Database column appType SqlType(VARCHAR), Length(255,true), Default(normal) */
    val appType: Rep[String] = column[String]("appType", O.Default("normal"))
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column remark SqlType(VARCHAR), Length(500,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Uniqueness Index over (appName) (database name appName) */
    val index1 = index("appName", appName, unique = true)
  }

  val apps = new TableQuery(tag => new App(tag))


  class AppChart(_tableTag: Tag) extends Table[AppChartEntity](_tableTag, "appChart") {
    def * = (chartId, appId, content, option, timeFilter, spl, title, searchType, status, description, created, createdBy, updated, updatedBy) <> (AppChartEntity.tupled, AppChartEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(chartId), Rep.Some(appId), Rep.Some(content), Rep.Some(option), Rep.Some(timeFilter), Rep.Some(spl), Rep.Some(title), Rep.Some(searchType), Rep.Some(status), description, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => AppChartEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11.get, _12.get, _13.get, _14.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column chartId SqlType(BIGINT), PrimaryKey */
    val chartId: Rep[Long] = column[Long]("chartId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column content SqlType(ENUM), Length(11,false) */
    val content: Rep[String] = column[String]("content")
    /** Database column option SqlType(TEXT) */
    val option: Rep[String] = column[String]("option")
    /** Database column timeFilter SqlType(VARCHAR), Length(255,true), Default({}) */
    val timeFilter: Rep[String] = column[String]("timeFilter", O.Default("{}"))
    /** Database column spl SqlType(VARCHAR), Length(255,true) */
    val spl: Rep[String] = column[String]("spl")
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title")
    /** Database column searchType SqlType(ENUM), Length(11,false) */
    val searchType: Rep[String] = column[String]("searchType")
    /** Database column status SqlType(INT), Default(1) */
    val status: Rep[Int] = column[Int]("status", O.Default(1))
    /** Database column description SqlType(VARCHAR), Length(1024,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val appCharts = new TableQuery(tag => new AppChart(tag))


  class AppPanel(_tableTag: Tag) extends Table[AppPanelEntity](_tableTag, "appPanel") {
    def * = (id, appId, timeFilter, theme, title, inputs, status, description, created, createdBy, updated, updatedBy) <> (AppPanelEntity.tupled, AppPanelEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(appId), Rep.Some(timeFilter), Rep.Some(theme), Rep.Some(title), Rep.Some(inputs), Rep.Some(status), description, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => AppPanelEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9.get, _10.get, _11.get, _12.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column timeFilter SqlType(VARCHAR), Length(255,true), Default({}) */
    val timeFilter: Rep[String] = column[String]("timeFilter", O.Default("{}"))
    /** Database column theme SqlType(ENUM), Length(11,false) */
    val theme: Rep[String] = column[String]("theme")
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title")
    /** Database column inputs SqlType(VARCHAR), Length(2048,true), Default([]) */
    val inputs: Rep[String] = column[String]("inputs", O.Default("[]"))
    /** Database column status SqlType(INT), Default(1) */
    val status: Rep[Int] = column[Int]("status", O.Default(1))
    /** Database column description SqlType(VARCHAR), Length(1024,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val appPanels = new TableQuery(tag => new AppPanel(tag))


  class AppPanelDetail(_tableTag: Tag) extends Table[AppPanelDetailEntity](_tableTag, "appPanelDetail") {
    def * = (panelId, detailId, chartId, sizeX, sizeY, row, col, appId, content, option, timeFilter, spl, title, searchType, description, created, createdBy, updated, updatedBy) <> (AppPanelDetailEntity.tupled, AppPanelDetailEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(panelId), Rep.Some(detailId), Rep.Some(chartId), Rep.Some(sizeX), Rep.Some(sizeY), Rep.Some(row), Rep.Some(col), Rep.Some(appId), Rep.Some(content), Rep.Some(option), Rep.Some(timeFilter), Rep.Some(spl), Rep.Some(title), Rep.Some(searchType), description, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => AppPanelDetailEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15, _16.get, _17.get, _18.get, _19.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column panelId SqlType(BIGINT) */
    val panelId: Rep[Long] = column[Long]("panelId")
    /** Database column detailId SqlType(BIGINT) */
    val detailId: Rep[Long] = column[Long]("detailId")
    /** Database column chartId SqlType(BIGINT) */
    val chartId: Rep[Long] = column[Long]("chartId")
    /** Database column sizeX SqlType(INT) */
    val sizeX: Rep[Int] = column[Int]("sizeX")
    /** Database column sizeY SqlType(INT) */
    val sizeY: Rep[Int] = column[Int]("sizeY")
    /** Database column row SqlType(INT) */
    val row: Rep[Int] = column[Int]("row")
    /** Database column col SqlType(INT) */
    val col: Rep[Int] = column[Int]("col")
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column content SqlType(ENUM), Length(11,false) */
    val content: Rep[String] = column[String]("content")
    /** Database column option SqlType(TEXT) */
    val option: Rep[String] = column[String]("option")
    /** Database column timeFilter SqlType(VARCHAR), Length(255,true), Default({}) */
    val timeFilter: Rep[String] = column[String]("timeFilter", O.Default("{}"))
    /** Database column spl SqlType(VARCHAR), Length(5120,true) */
    val spl: Rep[String] = column[String]("spl")
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title")
    /** Database column searchType SqlType(ENUM), Length(11,false) */
    val searchType: Rep[String] = column[String]("searchType")
    /** Database column description SqlType(VARCHAR), Length(1024,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Primary key of AppPanelDetail (database name appPanelDetail_PK) */
    val pk = primaryKey("appPanelDetail_PK", (panelId, detailId))
  }

  val appPanelDetails = new TableQuery(tag => new AppPanelDetail(tag))


  class AppPermission(_tableTag: Tag) extends Table[AppPermissionEntity](_tableTag, "appPermission") {
    def * = (appPermissionId, appId, entityId, entityType, permissionType, priority, created, createdBy, updated, updatedBy) <> (AppPermissionEntity.tupled, AppPermissionEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(appPermissionId), Rep.Some(appId), Rep.Some(entityId), Rep.Some(entityType), Rep.Some(permissionType), Rep.Some(priority), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => AppPermissionEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column appPermissionId SqlType(BIGINT), PrimaryKey */
    val appPermissionId: Rep[Long] = column[Long]("appPermissionId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column entityId SqlType(BIGINT) */
    val entityId: Rep[Long] = column[Long]("entityId")
    /** Database column entityType SqlType(VARCHAR), Length(50,true) */
    val entityType: Rep[String] = column[String]("entityType")
    /** Database column permissionType SqlType(VARCHAR), Length(50,true) */
    val permissionType: Rep[String] = column[String]("permissionType")
    /** Database column priority SqlType(INT) */
    val priority: Rep[Int] = column[Int]("priority")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val appPermissions = new TableQuery(tag => new AppPermission(tag))


  class AppSearchCollect(_tableTag: Tag) extends Table[AppSearchCollectEntity](_tableTag, "appSearchCollect") {
    def * = (searchCollectId, appId, content, option, timeFilter, description, spl, title, searchType, created, createdBy, updated, updatedBy) <> (AppSearchCollectEntity.tupled, AppSearchCollectEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(searchCollectId), Rep.Some(appId), Rep.Some(content), Rep.Some(option), Rep.Some(timeFilter), description, Rep.Some(spl), Rep.Some(title), Rep.Some(searchType), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => AppSearchCollectEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column searchCollectId SqlType(BIGINT), PrimaryKey */
    val searchCollectId: Rep[Long] = column[Long]("searchCollectId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column content SqlType(ENUM), Length(11,false) */
    val content: Rep[String] = column[String]("content")
    /** Database column option SqlType(TEXT) */
    val option: Rep[String] = column[String]("option")
    /** Database column timeFilter SqlType(VARCHAR), Length(255,true) */
    val timeFilter: Rep[String] = column[String]("timeFilter")
    /** Database column description SqlType(VARCHAR), Length(255,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column spl SqlType(VARCHAR), Length(255,true) */
    val spl: Rep[String] = column[String]("spl")
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title")
    /** Database column searchType SqlType(ENUM), Length(11,false) */
    val searchType: Rep[String] = column[String]("searchType")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val appSearchCollects = new TableQuery(tag => new AppSearchCollect(tag))


  class AppSearchHistory(_tableTag: Tag) extends Table[AppSearchHistoryEntity](_tableTag, "appSearchHistory") {
    def * = (searchHistoryId, appId, spl, created, createdBy) <> (AppSearchHistoryEntity.tupled, AppSearchHistoryEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(searchHistoryId), Rep.Some(appId), Rep.Some(spl), Rep.Some(created), Rep.Some(createdBy)).shaped.<>({ r => import r._; _1.map(_ => AppSearchHistoryEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column searchHistoryId SqlType(BIGINT), PrimaryKey */
    val searchHistoryId: Rep[Long] = column[Long]("searchHistoryId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column spl SqlType(VARCHAR), Length(255,true) */
    val spl: Rep[String] = column[String]("spl")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
  }

  val appSearchHistorys = new TableQuery(tag => new AppSearchHistory(tag))

  class BakStorage(_tableTag: Tag) extends Table[BakStorageEntity](_tableTag, Some("itoaManagementUATNew"), "bakStorage") {
    def * = (storageId, name, storageType, path, uri, status, isZip, isSlice, maxBakRate, maxRestoreRate, description, created, createdBy, updated, updatedBy) <> (BakStorageEntity.tupled, BakStorageEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(storageId), Rep.Some(name), Rep.Some(storageType), Rep.Some(path), uri, Rep.Some(status), Rep.Some(isZip), Rep.Some(isSlice), maxBakRate, maxRestoreRate, description, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => BakStorageEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9, _10, _11, _12.get, _13.get, _14.get, _15.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column storageId SqlType(BIGINT UNSIGNED) */
    val storageId: Rep[Long] = column[Long]("storageId")
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name")
    /** Database column storageType SqlType(ENUM), Length(4,false) */
    val storageType: Rep[String] = column[String]("storageType")
    /** Database column path SqlType(VARCHAR), Length(510,true) */
    val path: Rep[String] = column[String]("path")
    /** Database column uri SqlType(VARCHAR), Length(255,true), Default(None) */
    val uri: Rep[Option[String]] = column[Option[String]]("uri", O.Default(None))
    /** Database column status SqlType(TINYINT UNSIGNED), Default(1) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(1))
    /** Database column isZip SqlType(TINYINT UNSIGNED), Default(0) */
    val isZip: Rep[Byte] = column[Byte]("isZip", O.Default(0))
    /** Database column isSlice SqlType(TINYINT UNSIGNED), Default(0) */
    val isSlice: Rep[Byte] = column[Byte]("isSlice", O.Default(0))
    /** Database column maxBakRate SqlType(SMALLINT UNSIGNED), Default(None) */
    val maxBakRate: Rep[Option[Short]] = column[Option[Short]]("maxBakRate", O.Default(None))
    /** Database column maxRestoreRate SqlType(SMALLINT UNSIGNED), Default(None) */
    val maxRestoreRate: Rep[Option[Short]] = column[Option[Short]]("maxRestoreRate", O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(1022,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column created SqlType(BIGINT UNSIGNED) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT UNSIGNED) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT UNSIGNED) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT UNSIGNED) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val bakStorages = new TableQuery(tag => new BakStorage(tag))


  class BasicData(_tableTag: Tag) extends Table[BasicDataEntity](_tableTag, Some("itoaManagementUATNew"), "basicData") {
    def * = (id, code, alias, category, status, isSystem, parentId, index, remark, created, createdBy, updated, updatedBy) <> (BasicDataEntity.tupled, BasicDataEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(code), Rep.Some(alias), Rep.Some(category), Rep.Some(status), Rep.Some(isSystem), Rep.Some(parentId), index, remark, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => BasicDataEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9, _10.get, _11.get, _12.get, _13.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column code SqlType(VARCHAR), Length(255,true) */
    val code: Rep[String] = column[String]("code")
    /** Database column alias SqlType(VARCHAR), Length(255,true) */
    val alias: Rep[String] = column[String]("alias")
    /** Database column category SqlType(VARCHAR), Length(150,true) */
    val category: Rep[String] = column[String]("category")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column isSystem SqlType(INT) */
    val isSystem: Rep[Int] = column[Int]("isSystem")
    /** Database column parentId SqlType(BIGINT), Default(0) */
    val parentId: Rep[Long] = column[Long]("parentId", O.Default(0L))
    /** Database column index SqlType(INT), Default(None) */
    val index: Rep[Option[Int]] = column[Option[Int]]("index", O.Default(None))
    /** Database column remark SqlType(VARCHAR), Length(5120,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val basicDatas = new TableQuery(tag => new BasicData(tag))


  class Character(_tableTag: Tag) extends Table[CharacterEntity](_tableTag, "character") {
    def * = (characterId, appId, characterName, condition, description, created, createdBy, updated, updatedBy) <> (CharacterEntity.tupled, CharacterEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(characterId), Rep.Some(appId), Rep.Some(characterName), Rep.Some(condition), Rep.Some(description), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => CharacterEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column characterId SqlType(BIGINT), PrimaryKey */
    val characterId: Rep[Long] = column[Long]("characterId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column characterName SqlType(VARCHAR), Length(255,true) */
    val characterName: Rep[String] = column[String]("characterName")
    /** Database column condition SqlType(VARCHAR), Length(255,true) */
    val condition: Rep[String] = column[String]("condition")
    /** Database column description SqlType(VARCHAR), Length(500,true) */
    val description: Rep[String] = column[String]("description")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val characters = new TableQuery(tag => new Character(tag))


  class DsAgent(_tableTag: Tag) extends Table[DsAgentEntity](_tableTag, "dsAgent") {
    def * = (agentId, guid, addressInfo, hostName, os, ip, ipAll, ipAllHash, connect, approved, approvedBy, status, remark, agentTag, created, createdBy, updated, updatedBy, lastHeartBeat) <> (DsAgentEntity.tupled, DsAgentEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(agentId), Rep.Some(guid), Rep.Some(addressInfo), Rep.Some(hostName), Rep.Some(os), Rep.Some(ip), Rep.Some(ipAll), Rep.Some(ipAllHash), Rep.Some(connect), approved, approvedBy, Rep.Some(status), remark, agentTag, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy), lastHeartBeat).shaped.<>({ r => import r._; _1.map(_ => DsAgentEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11, _12.get, _13, _14, _15.get, _16.get, _17.get, _18.get, _19))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column agentId SqlType(BIGINT), PrimaryKey */
    val agentId: Rep[Long] = column[Long]("agentId", O.PrimaryKey)
    /** Database column guid SqlType(VARCHAR), Length(50,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column addressInfo SqlType(VARCHAR), Length(1024,true) */
    val addressInfo: Rep[String] = column[String]("addressInfo")
    /** Database column hostName SqlType(VARCHAR), Length(255,true) */
    val hostName: Rep[String] = column[String]("hostName")
    /** Database column os SqlType(VARCHAR), Length(255,true) */
    val os: Rep[String] = column[String]("os")
    /** Database column ip SqlType(VARCHAR), Length(1024,true) */
    val ip: Rep[String] = column[String]("ip")
    /** Database column ipAll SqlType(VARCHAR), Length(1024,true) */
    val ipAll: Rep[String] = column[String]("ipAll")
    /** Database column ipAllHash SqlType(VARCHAR), Length(50,true) */
    val ipAllHash: Rep[String] = column[String]("ipAllHash")
    /** Database column connect SqlType(INT) */
    val connect: Rep[Int] = column[Int]("connect")
    /** Database column approved SqlType(BIGINT), Default(None) */
    val approved: Rep[Option[Long]] = column[Option[Long]]("approved", O.Default(None))
    /** Database column approvedBy SqlType(BIGINT), Default(None) */
    val approvedBy: Rep[Option[Long]] = column[Option[Long]]("approvedBy", O.Default(None))
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column remark SqlType(VARCHAR), Length(1024,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
    /** Database column agentTag SqlType(VARCHAR), Length(1024,true), Default(None) */
    val agentTag: Rep[Option[String]] = column[Option[String]]("agentTag", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
    /** Database column lastHeartBeat SqlType(BIGINT), Default(None) */
    val lastHeartBeat: Rep[Option[Long]] = column[Option[Long]]("lastHeartBeat", O.Default(None))

    /** Uniqueness Index over (guid) (database name agentId_UNIQUE) */
    val index1 = index("agentId_UNIQUE", guid, unique = true)
  }

  val dsAgents = new TableQuery(tag => new DsAgent(tag))


  class DsDB(_tableTag: Tag) extends Table[DsDBEntity](_tableTag, "dsDB") {
    def * = (id, sourceType, guid, datasourceName, hostName, serverPort, dbName, instanceName, userName, password, status, statusDescription, description, created, createdBy, updated, updatedBy) <> (DsDBEntity.tupled, DsDBEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(sourceType), Rep.Some(guid), Rep.Some(datasourceName), Rep.Some(hostName), Rep.Some(serverPort), Rep.Some(dbName), instanceName, Rep.Some(userName), Rep.Some(password), Rep.Some(status), statusDescription, description, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => DsDBEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9.get, _10.get, _11.get, _12, _13, _14.get, _15.get, _16.get, _17.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column sourceType SqlType(VARCHAR), Length(255,true) */
    val sourceType: Rep[String] = column[String]("sourceType")
    /** Database column guid SqlType(VARCHAR), Length(255,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column datasourceName SqlType(VARCHAR), Length(255,true) */
    val datasourceName: Rep[String] = column[String]("datasourceName")
    /** Database column hostName SqlType(VARCHAR), Length(255,true) */
    val hostName: Rep[String] = column[String]("hostName")
    /** Database column serverPort SqlType(VARCHAR), Length(255,true) */
    val serverPort: Rep[String] = column[String]("serverPort")
    /** Database column dbName SqlType(VARCHAR), Length(255,true) */
    val dbName: Rep[String] = column[String]("dbName")
    /** Database column instanceName SqlType(VARCHAR), Length(255,true), Default(None) */
    val instanceName: Rep[Option[String]] = column[Option[String]]("instanceName", O.Default(None))
    /** Database column userName SqlType(VARCHAR), Length(255,true) */
    val userName: Rep[String] = column[String]("userName")
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column statusDescription SqlType(VARCHAR), Length(255,true), Default(None) */
    val statusDescription: Rep[Option[String]] = column[Option[String]]("statusDescription", O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(255,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Uniqueness Index over (guid) (database name guid_UNIQUE) */
    val index1 = index("guid_UNIQUE", guid, unique = true)
  }

  val dsDBs = new TableQuery(tag => new DsDB(tag))


  class DsTruesight(_tableTag: Tag) extends Table[DsTruesightEntity](_tableTag, "dsTruesight") {
    def * = (id, sourceType, guid, datasourceName, hostName, userName, password, tenant, truesightVersion, osUserName, osPassword, osPort, transferProtocol, transferPort, initTimeoutSecond, status, statusDescription, description, created, createdBy, updated, updatedBy) <> (DsTruesightEntity.tupled, DsTruesightEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(sourceType), Rep.Some(guid), Rep.Some(datasourceName), Rep.Some(hostName), Rep.Some(userName), Rep.Some(password), tenant, Rep.Some(truesightVersion), osUserName, osPassword, osPort, Rep.Some(transferProtocol), Rep.Some(transferPort), Rep.Some(initTimeoutSecond), Rep.Some(status), statusDescription, description, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => DsTruesightEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9.get, _10, _11, _12, _13.get, _14.get, _15.get, _16.get, _17, _18, _19.get, _20.get, _21.get, _22.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column sourceType SqlType(VARCHAR), Length(255,true) */
    val sourceType: Rep[String] = column[String]("sourceType")
    /** Database column guid SqlType(VARCHAR), Length(255,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column datasourceName SqlType(VARCHAR), Length(255,true) */
    val datasourceName: Rep[String] = column[String]("datasourceName")
    /** Database column hostName SqlType(VARCHAR), Length(255,true) */
    val hostName: Rep[String] = column[String]("hostName")
    /** Database column userName SqlType(VARCHAR), Length(255,true) */
    val userName: Rep[String] = column[String]("userName")
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password")
    /** Database column tenant SqlType(VARCHAR), Length(255,true), Default(None) */
    val tenant: Rep[Option[String]] = column[Option[String]]("tenant", O.Default(None))
    /** Database column truesightVersion SqlType(VARCHAR), Length(255,true) */
    val truesightVersion: Rep[String] = column[String]("truesightVersion")
    /** Database column osUserName SqlType(VARCHAR), Length(255,true), Default(None) */
    val osUserName: Rep[Option[String]] = column[Option[String]]("osUserName", O.Default(None))
    /** Database column osPassword SqlType(VARCHAR), Length(255,true), Default(None) */
    val osPassword: Rep[Option[String]] = column[Option[String]]("osPassword", O.Default(None))
    /** Database column osPort SqlType(VARCHAR), Length(255,true), Default(None) */
    val osPort: Rep[Option[String]] = column[Option[String]]("osPort", O.Default(None))
    /** Database column transferProtocol SqlType(VARCHAR), Length(255,true) */
    val transferProtocol: Rep[String] = column[String]("transferProtocol")
    /** Database column transferPort SqlType(VARCHAR), Length(255,true) */
    val transferPort: Rep[String] = column[String]("transferPort")
    /** Database column initTimeoutSecond SqlType(INT) */
    val initTimeoutSecond: Rep[Int] = column[Int]("initTimeoutSecond")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column statusDescription SqlType(VARCHAR), Length(255,true), Default(None) */
    val statusDescription: Rep[Option[String]] = column[Option[String]]("statusDescription", O.Default(None))
    /** Database column description SqlType(VARCHAR), Length(255,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Uniqueness Index over (guid) (database name guid_UNIQUE) */
    val index1 = index("guid_UNIQUE", guid, unique = true)
    /** Uniqueness Index over (hostName) (database name hostName_UNIQUE) */
    val index2 = index("hostName_UNIQUE", hostName, unique = true)
  }

  val dsTruesights = new TableQuery(tag => new DsTruesight(tag))


  class DsTruesightMetric(_tableTag: Tag) extends Table[DsTruesightMetricEntity](_tableTag, "dsTruesightMetric") {
    def * = (guid, dsTruesightId, metricGroup, metricType, metricName) <> (DsTruesightMetricEntity.tupled, DsTruesightMetricEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(guid), Rep.Some(dsTruesightId), Rep.Some(metricGroup), Rep.Some(metricType), Rep.Some(metricName)).shaped.<>({ r => import r._; _1.map(_ => DsTruesightMetricEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column guid SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val guid: Rep[String] = column[String]("guid", O.PrimaryKey)
    /** Database column dsTruesightId SqlType(BIGINT) */
    val dsTruesightId: Rep[Long] = column[Long]("dsTruesightId")
    /** Database column metricGroup SqlType(VARCHAR), Length(255,true) */
    val metricGroup: Rep[String] = column[String]("metricGroup")
    /** Database column metricType SqlType(VARCHAR), Length(255,true) */
    val metricType: Rep[String] = column[String]("metricType")
    /** Database column metricName SqlType(VARCHAR), Length(255,true) */
    val metricName: Rep[String] = column[String]("metricName")
  }

  val dsTruesightMetrics = new TableQuery(tag => new DsTruesightMetric(tag))


  class DsType(_tableTag: Tag) extends Table[DsTypeEntity](_tableTag, "dsType") {
    def * = (dsTypeId, typeName, typeCode, typeTag, description, api, parentTypeId, docType) <> (DsTypeEntity.tupled, DsTypeEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(dsTypeId), Rep.Some(typeName), Rep.Some(typeCode), Rep.Some(typeTag), description, Rep.Some(api), Rep.Some(parentTypeId), docType).shaped.<>({ r => import r._; _1.map(_ => DsTypeEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column dsTypeId SqlType(BIGINT), PrimaryKey */
    val dsTypeId: Rep[Long] = column[Long]("dsTypeId", O.PrimaryKey)
    /** Database column typeName SqlType(VARCHAR), Length(255,true) */
    val typeName: Rep[String] = column[String]("typeName")
    /** Database column typeCode SqlType(VARCHAR), Length(255,true) */
    val typeCode: Rep[String] = column[String]("typeCode")
    /** Database column typeTag SqlType(VARCHAR), Length(255,true) */
    val typeTag: Rep[String] = column[String]("typeTag")
    /** Database column description SqlType(VARCHAR), Length(255,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column api SqlType(VARCHAR), Length(255,true) */
    val api: Rep[String] = column[String]("api")
    /** Database column parentTypeId SqlType(BIGINT), Default(0) */
    val parentTypeId: Rep[Long] = column[Long]("parentTypeId", O.Default(0L))
    /** Database column docType SqlType(VARCHAR), Length(255,true), Default(None) */
    val docType: Rep[Option[String]] = column[Option[String]]("docType", O.Default(None))

    /** Uniqueness Index over (typeCode) (database name typeCode_UNIQUE) */
    val index1 = index("typeCode_UNIQUE", typeCode, unique = true)
  }

  val dsTypes = new TableQuery(tag => new DsType(tag))


  class FilterGroup(_tableTag: Tag) extends Table[FilterGroupEntity](_tableTag, "filterGroup") {
    def * = (filterGroupId, streamingId, sampleData, filtersJson, yamlJson, elements, created, createdBy, updated, updatedBy) <> (FilterGroupEntity.tupled, FilterGroupEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(filterGroupId), Rep.Some(streamingId), Rep.Some(sampleData), Rep.Some(filtersJson), Rep.Some(yamlJson), Rep.Some(elements), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => FilterGroupEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column filterGroupId SqlType(BIGINT), PrimaryKey */
    val filterGroupId: Rep[Long] = column[Long]("filterGroupId", O.PrimaryKey)
    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column sampleData SqlType(LONGTEXT), Length(2147483647,true) */
    val sampleData: Rep[String] = column[String]("sampleData")
    /** Database column filtersJson SqlType(LONGTEXT), Length(2147483647,true) */
    val filtersJson: Rep[String] = column[String]("filtersJson")
    /** Database column yamlJson SqlType(LONGTEXT), Length(2147483647,true) */
    val yamlJson: Rep[String] = column[String]("yamlJson")
    /** Database column elements SqlType(TEXT) */
    val elements: Rep[String] = column[String]("elements")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Index over (streamingId) (database name streamingId) */
    val index1 = index("streamingId", streamingId)
  }

  val filterGroups = new TableQuery(tag => new FilterGroup(tag))


  class FilterGroupHistory(_tableTag: Tag) extends Table[FilterGroupHistoryEntity](_tableTag, "filterGroupHistory") {
    def * = (id, appName, jobId, state, jobResource, topic, retCode, message, created, createdBy, updated, updatedBy) <> (FilterGroupHistoryEntity.tupled, FilterGroupHistoryEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(appName), Rep.Some(jobId), Rep.Some(state), Rep.Some(jobResource), Rep.Some(topic), retCode, message, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => FilterGroupHistoryEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9.get, _10.get, _11.get, _12.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column appName SqlType(VARCHAR), Length(255,true) */
    val appName: Rep[String] = column[String]("appName")
    /** Database column jobId SqlType(VARCHAR), Length(200,true) */
    val jobId: Rep[String] = column[String]("jobId")
    /** Database column state SqlType(VARCHAR), Length(100,true) */
    val state: Rep[String] = column[String]("state")
    /** Database column jobResource SqlType(TEXT) */
    val jobResource: Rep[String] = column[String]("jobResource")
    /** Database column topic SqlType(VARCHAR), Length(255,true) */
    val topic: Rep[String] = column[String]("topic")
    /** Database column retCode SqlType(VARCHAR), Length(100,true), Default(None) */
    val retCode: Rep[Option[String]] = column[Option[String]]("retCode", O.Default(None))
    /** Database column message SqlType(VARCHAR), Length(500,true), Default(None) */
    val message: Rep[Option[String]] = column[Option[String]]("message", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val filterGroupHistorys = new TableQuery(tag => new FilterGroupHistory(tag))


  class FilterGroupTemplate(_tableTag: Tag) extends Table[FilterGroupTemplateEntity](_tableTag, "filterGroupTemplate") {
    def * = (filterGroupId, appId, sampleData, filtersJson, yamlJson, elements, category, globalFlag, alias, status, remark, created, createdBy, updated, updatedBy) <> (FilterGroupTemplateEntity.tupled, FilterGroupTemplateEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(filterGroupId), Rep.Some(appId), Rep.Some(sampleData), Rep.Some(filtersJson), Rep.Some(yamlJson), Rep.Some(elements), category, globalFlag, Rep.Some(alias), Rep.Some(status), remark, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => FilterGroupTemplateEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9.get, _10.get, _11, _12.get, _13.get, _14.get, _15.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column filterGroupId SqlType(BIGINT), PrimaryKey */
    val filterGroupId: Rep[Long] = column[Long]("filterGroupId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column sampleData SqlType(LONGTEXT), Length(2147483647,true) */
    val sampleData: Rep[String] = column[String]("sampleData")
    /** Database column filtersJson SqlType(LONGTEXT), Length(2147483647,true) */
    val filtersJson: Rep[String] = column[String]("filtersJson")
    /** Database column yamlJson SqlType(LONGTEXT), Length(2147483647,true) */
    val yamlJson: Rep[String] = column[String]("yamlJson")
    /** Database column elements SqlType(TEXT) */
    val elements: Rep[String] = column[String]("elements")
    /** Database column category SqlType(VARCHAR), Length(50,true), Default(None) */
    val category: Rep[Option[String]] = column[Option[String]]("category", O.Default(None))
    /** Database column globalFlag SqlType(VARCHAR), Length(2,true), Default(Some(0)) */
    val globalFlag: Rep[Option[String]] = column[Option[String]]("globalFlag", O.Default(Some("0")))
    /** Database column alias SqlType(VARCHAR), Length(200,true) */
    val alias: Rep[String] = column[String]("alias")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column remark SqlType(VARCHAR), Length(255,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Index over (appId) (database name appId) */
    val index1 = index("appId", appId)
  }

  val filterGroupTemplates = new TableQuery(tag => new FilterGroupTemplate(tag))


  class InputDB(_tableTag: Tag) extends Table[InputDBEntity](_tableTag, "inputDB") {
    def * = (streamingId, dsDBId, guid, syncMode, refreshInterval, sql, increment, created, createdBy, updated, updatedBy) <> (InputDBEntity.tupled, InputDBEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(streamingId), Rep.Some(dsDBId), Rep.Some(guid), Rep.Some(syncMode), Rep.Some(refreshInterval), Rep.Some(sql), Rep.Some(increment), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => InputDBEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column dsDBId SqlType(BIGINT) */
    val dsDBId: Rep[Long] = column[Long]("dsDBId")
    /** Database column guid SqlType(VARCHAR), Length(50,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column syncMode SqlType(VARCHAR), Length(255,true) */
    val syncMode: Rep[String] = column[String]("syncMode")
    /** Database column refreshInterval SqlType(INT) */
    val refreshInterval: Rep[Int] = column[Int]("refreshInterval")
    /** Database column sql SqlType(VARCHAR), Length(5120,true) */
    val sql: Rep[String] = column[String]("sql")
    /** Database column increment SqlType(VARCHAR), Length(255,true) */
    val increment: Rep[String] = column[String]("increment")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Primary key of InputDB (database name inputDB_PK) */
    val pk = primaryKey("inputDB_PK", (streamingId, dsDBId))

    /** Uniqueness Index over (guid) (database name guid_UNIQUE) */
    val index1 = index("guid_UNIQUE", guid, unique = true)
  }

  val inputDBs = new TableQuery(tag => new InputDB(tag))


  class InputFile(_tableTag: Tag) extends Table[InputFileEntity](_tableTag, "inputFile") {
    def * = (streamingId, agentId, guid, inputPath, splitter, charset, pathFilter, pathType, contentType, contentFilter, recursive, multiline, proName, created, createdBy, updated, updatedBy) <> (InputFileEntity.tupled, InputFileEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(streamingId), Rep.Some(agentId), Rep.Some(guid), inputPath, splitter, charset, pathFilter, Rep.Some(pathType), Rep.Some(contentType), contentFilter, Rep.Some(recursive), Rep.Some(multiline), proName, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => InputFileEntity.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8.get, _9.get, _10, _11.get, _12.get, _13, _14.get, _15.get, _16.get, _17.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column agentId SqlType(BIGINT) */
    val agentId: Rep[Long] = column[Long]("agentId")
    /** Database column guid SqlType(VARCHAR), Length(50,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column inputPath SqlType(VARCHAR), Length(1024,true), Default(None) */
    val inputPath: Rep[Option[String]] = column[Option[String]]("inputPath", O.Default(None))
    /** Database column splitter SqlType(VARCHAR), Length(1024,true), Default(None) */
    val splitter: Rep[Option[String]] = column[Option[String]]("splitter", O.Default(None))
    /** Database column charset SqlType(VARCHAR), Length(255,true), Default(None) */
    val charset: Rep[Option[String]] = column[Option[String]]("charset", O.Default(None))
    /** Database column pathFilter SqlType(VARCHAR), Length(1024,true), Default(None) */
    val pathFilter: Rep[Option[String]] = column[Option[String]]("pathFilter", O.Default(None))
    /** Database column pathType SqlType(INT) */
    val pathType: Rep[Int] = column[Int]("pathType")
    /** Database column contentType SqlType(INT) */
    val contentType: Rep[Int] = column[Int]("contentType")
    /** Database column contentFilter SqlType(VARCHAR), Length(2048,true), Default(None) */
    val contentFilter: Rep[Option[String]] = column[Option[String]]("contentFilter", O.Default(None))
    /** Database column recursive SqlType(INT), Default(0) */
    val recursive: Rep[Int] = column[Int]("recursive", O.Default(0))
    /** Database column multiline SqlType(INT), Default(0) */
    val multiline: Rep[Int] = column[Int]("multiline", O.Default(0))
    /** Database column proName SqlType(VARCHAR), Length(50,true), Default(None) */
    val proName: Rep[Option[String]] = column[Option[String]]("proName", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Primary key of InputFile (database name inputFile_PK) */
    val pk = primaryKey("inputFile_PK", (streamingId, agentId))

    /** Uniqueness Index over (guid) (database name guid_UNIQUE) */
    val index1 = index("guid_UNIQUE", guid, unique = true)
  }

  val inputFiles = new TableQuery(tag => new InputFile(tag))


  class InputFileAttr(_tableTag: Tag) extends Table[InputFileAttrEntity](_tableTag, "inputFileAttr") {
    def * = (id, streamingId, attrType, savePos, readFromLast, pollInterval, dirCheckInterval, activeFiles, renameCheck, created, createdBy, updated, updatedBy) <> (InputFileAttrEntity.tupled, InputFileAttrEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(streamingId), Rep.Some(attrType), Rep.Some(savePos), Rep.Some(readFromLast), Rep.Some(pollInterval), Rep.Some(dirCheckInterval), Rep.Some(activeFiles), Rep.Some(renameCheck), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => InputFileAttrEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column attrType SqlType(VARCHAR), Length(255,true) */
    val attrType: Rep[String] = column[String]("attrType")
    /** Database column savePos SqlType(INT) */
    val savePos: Rep[Int] = column[Int]("savePos")
    /** Database column readFromLast SqlType(INT) */
    val readFromLast: Rep[Int] = column[Int]("readFromLast")
    /** Database column pollInterval SqlType(INT) */
    val pollInterval: Rep[Int] = column[Int]("pollInterval")
    /** Database column dirCheckInterval SqlType(INT) */
    val dirCheckInterval: Rep[Int] = column[Int]("dirCheckInterval")
    /** Database column activeFiles SqlType(INT) */
    val activeFiles: Rep[Int] = column[Int]("activeFiles")
    /** Database column renameCheck SqlType(INT) */
    val renameCheck: Rep[Int] = column[Int]("renameCheck")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val inputFileAttrs = new TableQuery(tag => new InputFileAttr(tag))


  class InputTcpUdp(_tableTag: Tag) extends Table[InputTcpUdpEntity](_tableTag, "inputTcpUdp") {
    def * = (streamingId, agentId, guid, catetory, `type`, ip, port, format, charset, multiline, include, exclude, created, createdBy, updated, updatedBy) <> (InputTcpUdpEntity.tupled, InputTcpUdpEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(streamingId), Rep.Some(agentId), Rep.Some(guid), Rep.Some(catetory), `type`, Rep.Some(ip), Rep.Some(port), format, charset, multiline, include, exclude, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => InputTcpUdpEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8, _9, _10, _11, _12, _13.get, _14.get, _15.get, _16.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column agentId SqlType(BIGINT) */
    val agentId: Rep[Long] = column[Long]("agentId")
    /** Database column guid SqlType(VARCHAR), Length(50,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column catetory SqlType(VARCHAR), Length(255,true) */
    val catetory: Rep[String] = column[String]("catetory")
    /** Database column type SqlType(VARCHAR), Length(255,true), Default(None)
      * NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Option[String]] = column[Option[String]]("type", O.Default(None))
    /** Database column ip SqlType(VARCHAR), Length(1024,true) */
    val ip: Rep[String] = column[String]("ip")
    /** Database column port SqlType(INT) */
    val port: Rep[Int] = column[Int]("port")
    /** Database column format SqlType(VARCHAR), Length(1024,true), Default(None) */
    val format: Rep[Option[String]] = column[Option[String]]("format", O.Default(None))
    /** Database column charset SqlType(VARCHAR), Length(255,true), Default(None) */
    val charset: Rep[Option[String]] = column[Option[String]]("charset", O.Default(None))
    /** Database column multiline SqlType(VARCHAR), Length(1024,true), Default(None) */
    val multiline: Rep[Option[String]] = column[Option[String]]("multiline", O.Default(None))
    /** Database column include SqlType(VARCHAR), Length(1024,true), Default(None) */
    val include: Rep[Option[String]] = column[Option[String]]("include", O.Default(None))
    /** Database column exclude SqlType(VARCHAR), Length(1024,true), Default(None) */
    val exclude: Rep[Option[String]] = column[Option[String]]("exclude", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Primary key of InputTcpUdp (database name inputTcpUdp_PK) */
    val pk = primaryKey("inputTcpUdp_PK", (streamingId, agentId))

    /** Uniqueness Index over (guid) (database name guid_UNIQUE) */
    val index1 = index("guid_UNIQUE", guid, unique = true)
  }

  val inputTcpUdps = new TableQuery(tag => new InputTcpUdp(tag))


  class InputTruesight(_tableTag: Tag) extends Table[InputTruesightEntity](_tableTag, "inputTruesight") {
    def * = (streamingId, dsTruesightId, guid, configRefreshHour, metricRefreshSecond, collectConcurrentCount, created, createdBy, updated, updatedBy) <> (InputTruesightEntity.tupled, InputTruesightEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(streamingId), Rep.Some(dsTruesightId), Rep.Some(guid), Rep.Some(configRefreshHour), Rep.Some(metricRefreshSecond), Rep.Some(collectConcurrentCount), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => InputTruesightEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column dsTruesightId SqlType(BIGINT) */
    val dsTruesightId: Rep[Long] = column[Long]("dsTruesightId")
    /** Database column guid SqlType(VARCHAR), Length(50,true) */
    val guid: Rep[String] = column[String]("guid")
    /** Database column configRefreshHour SqlType(INT) */
    val configRefreshHour: Rep[Int] = column[Int]("configRefreshHour")
    /** Database column metricRefreshSecond SqlType(INT) */
    val metricRefreshSecond: Rep[Int] = column[Int]("metricRefreshSecond")
    /** Database column collectConcurrentCount SqlType(INT) */
    val collectConcurrentCount: Rep[Int] = column[Int]("collectConcurrentCount")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Primary key of InputTruesight (database name inputTruesight_PK) */
    val pk = primaryKey("inputTruesight_PK", (streamingId, dsTruesightId))

    /** Uniqueness Index over (guid) (database name guid_UNIQUE) */
    val index1 = index("guid_UNIQUE", guid, unique = true)
  }

  val inputTruesights = new TableQuery(tag => new InputTruesight(tag))


  class InputTruesightMetric(_tableTag: Tag) extends Table[InputTruesightMetricEntity](_tableTag, "inputTruesightMetric") {
    def * = (streamingId, dsTruesightMetricGuid) <> (InputTruesightMetricEntity.tupled, InputTruesightMetricEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(streamingId), Rep.Some(dsTruesightMetricGuid)).shaped.<>({ r => import r._; _1.map(_ => InputTruesightMetricEntity.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column streamingId SqlType(BIGINT) */
    val streamingId: Rep[Long] = column[Long]("streamingId")
    /** Database column dsTruesightMetricGuid SqlType(VARCHAR), Length(50,true) */
    val dsTruesightMetricGuid: Rep[String] = column[String]("dsTruesightMetricGuid")

    /** Primary key of InputTruesightMetric (database name inputTruesightMetric_PK) */
    val pk = primaryKey("inputTruesightMetric_PK", (dsTruesightMetricGuid, streamingId))
  }

  val inputTruesightMetrics = new TableQuery(tag => new InputTruesightMetric(tag))


  class Job(_tableTag: Tag) extends Table[JobEntity](_tableTag, Some("itoaManagementUATNew"), "job") {
    def * = (jobId, name, status, lifespan, jobType, description, informOnSuccess, informOnFailure, created, createdBy, updated, updatedBy) <> (JobEntity.tupled, JobEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(jobId), Rep.Some(name), Rep.Some(status), Rep.Some(lifespan), Rep.Some(jobType), description, Rep.Some(informOnSuccess), Rep.Some(informOnFailure), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => JobEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column jobId SqlType(BIGINT), PrimaryKey */
    val jobId: Rep[Long] = column[Long]("jobId", O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name")
    /** Database column status SqlType(TINYINT UNSIGNED), Default(1) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(1))
    /** Database column lifespan SqlType(SMALLINT UNSIGNED) */
    val lifespan: Rep[Short] = column[Short]("lifespan")
    /** Database column jobType SqlType(TINYINT UNSIGNED), Default(1) */
    val jobType: Rep[Byte] = column[Byte]("jobType", O.Default(1))
    /** Database column description SqlType(VARCHAR), Length(1022,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column informOnSuccess SqlType(TINYINT UNSIGNED), Default(0) */
    val informOnSuccess: Rep[Byte] = column[Byte]("informOnSuccess", O.Default(0))
    /** Database column informOnFailure SqlType(TINYINT UNSIGNED), Default(0) */
    val informOnFailure: Rep[Byte] = column[Byte]("informOnFailure", O.Default(0))
    /** Database column created SqlType(BIGINT UNSIGNED) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT UNSIGNED) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT UNSIGNED) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT UNSIGNED) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val jobs = new TableQuery(tag => new Job(tag))


  class JobBak(_tableTag: Tag) extends Table[JobBakEntity](_tableTag, Some("itoaManagementUATNew"), "jobBak") {
    def * = (bakId, jobId, storageId, created, createdBy, updated, updatedBy) <> (JobBakEntity.tupled, JobBakEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(bakId), Rep.Some(jobId), Rep.Some(storageId), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => JobBakEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column bakId SqlType(BIGINT UNSIGNED), PrimaryKey */
    val bakId: Rep[Long] = column[Long]("bakId", O.PrimaryKey)
    /** Database column jobId SqlType(BIGINT UNSIGNED) */
    val jobId: Rep[Long] = column[Long]("jobId")
    /** Database column storageId SqlType(BIGINT UNSIGNED) */
    val storageId: Rep[Long] = column[Long]("storageId")
    /** Database column created SqlType(BIGINT UNSIGNED) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT UNSIGNED) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT UNSIGNED) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT UNSIGNED) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Uniqueness Index over (jobId) (database name jobId) */
    val index1 = index("jobId", jobId, unique = true)
  }

  val jobBaks = new TableQuery(tag => new JobBak(tag))


  class JobBakApps(_tableTag: Tag) extends Table[JobBakAppsEntity](_tableTag, Some("itoaManagementUATNew"), "jobBakApps") {
    def * = (bakId, appId) <> (JobBakAppsEntity.tupled, JobBakAppsEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(bakId), Rep.Some(appId)).shaped.<>({ r => import r._; _1.map(_ => JobBakAppsEntity.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column bakId SqlType(BIGINT UNSIGNED) */
    val bakId: Rep[Long] = column[Long]("bakId")
    /** Database column appId SqlType(BIGINT UNSIGNED) */
    val appId: Rep[Long] = column[Long]("appId")

    /** Index over (bakId) (database name bakId) */
    val index1 = index("bakId", bakId)
  }

  val jobBakAppss = new TableQuery(tag => new JobBakApps(tag))


  class JobCycle(_tableTag: Tag) extends Table[JobCycleEntity](_tableTag, Some("itoaManagementUATNew"), "jobCycle") {
    def * = (cycleId, jobId, planType, beginDate, endDate, isLastDay, day, once, hour, min, status, created, createdBy, updated, updatedBy) <> (JobCycleEntity.tupled, JobCycleEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(cycleId), Rep.Some(jobId), Rep.Some(planType), beginDate, endDate, Rep.Some(isLastDay), day, once, Rep.Some(hour), Rep.Some(min), status, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => JobCycleEntity.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7, _8, _9.get, _10.get, _11, _12.get, _13.get, _14.get, _15.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column cycleId SqlType(BIGINT), PrimaryKey */
    val cycleId: Rep[Long] = column[Long]("cycleId", O.PrimaryKey)
    /** Database column jobId SqlType(BIGINT) */
    val jobId: Rep[Long] = column[Long]("jobId")
    /** Database column planType SqlType(ENUM), Length(5,false) */
    val planType: Rep[String] = column[String]("planType")
    /** Database column beginDate SqlType(BIGINT), Default(None) */
    val beginDate: Rep[Option[Long]] = column[Option[Long]]("beginDate", O.Default(None))
    /** Database column endDate SqlType(BIGINT), Default(None) */
    val endDate: Rep[Option[Long]] = column[Option[Long]]("endDate", O.Default(None))
    /** Database column isLastDay SqlType(TINYINT UNSIGNED), Default(0) */
    val isLastDay: Rep[Byte] = column[Byte]("isLastDay", O.Default(0))
    /** Database column day SqlType(SET), Length(85,false), Default(None) */
    val day: Rep[Option[String]] = column[Option[String]]("day", O.Default(None))
    /** Database column once SqlType(DATE), Default(None) */
    val once: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("once", O.Default(None))
    /** Database column hour SqlType(TINYINT UNSIGNED), Default(0) */
    val hour: Rep[Byte] = column[Byte]("hour", O.Default(0))
    /** Database column min SqlType(TINYINT UNSIGNED), Default(0) */
    val min: Rep[Byte] = column[Byte]("min", O.Default(0))
    /** Database column status SqlType(TINYINT UNSIGNED), Default(None) */
    val status: Rep[Option[Byte]] = column[Option[Byte]]("status", O.Default(None))
    /** Database column created SqlType(BIGINT UNSIGNED) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT UNSIGNED) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT UNSIGNED) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT UNSIGNED) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Uniqueness Index over (jobId) (database name jobId) */
    val index1 = index("jobId", jobId, unique = true)
  }

  val jobCycles = new TableQuery(tag => new JobCycle(tag))


  class JobTaskBak(_tableTag: Tag) extends Table[JobTaskBakEntity](_tableTag, Some("itoaManagementUATNew"), "jobTaskBak") {
    def * = (taskBakId, taskBakName, bakId, storageName, status, created, updated) <> (JobTaskBakEntity.tupled, JobTaskBakEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(taskBakId), Rep.Some(taskBakName), Rep.Some(bakId), Rep.Some(storageName), Rep.Some(status), Rep.Some(created), Rep.Some(updated)).shaped.<>({ r => import r._; _1.map(_ => JobTaskBakEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column taskBakId SqlType(BIGINT UNSIGNED), PrimaryKey */
    val taskBakId: Rep[Long] = column[Long]("taskBakId", O.PrimaryKey)
    /** Database column taskBakName SqlType(VARCHAR), Length(255,true) */
    val taskBakName: Rep[String] = column[String]("taskBakName")
    /** Database column bakId SqlType(BIGINT UNSIGNED) */
    val bakId: Rep[Long] = column[Long]("bakId")
    /** Database column storageName SqlType(VARCHAR), Length(255,true) */
    val storageName: Rep[String] = column[String]("storageName")
    /** Database column status SqlType(TINYINT UNSIGNED), Default(0) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(0))
    /** Database column created SqlType(BIGINT UNSIGNED) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column updated SqlType(BIGINT UNSIGNED) */
    val updated: Rep[Long] = column[Long]("updated")

    /** Index over (bakId) (database name bakId) */
    val index1 = index("bakId", bakId)
  }

  val jobTaskBaks = new TableQuery(tag => new JobTaskBak(tag))


  class JobTaskBakSpec(_tableTag: Tag) extends Table[JobTaskBakSpecEntity](_tableTag, Some("itoaManagementUATNew"), "jobTaskBakSpec") {
    def * = (specId, taskBakId, appId, indexName, status, result) <> (JobTaskBakSpecEntity.tupled, JobTaskBakSpecEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(specId), Rep.Some(taskBakId), Rep.Some(appId), Rep.Some(indexName), Rep.Some(status), result).shaped.<>({ r => import r._; _1.map(_ => JobTaskBakSpecEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column specId SqlType(BIGINT UNSIGNED) */
    val specId: Rep[Long] = column[Long]("specId")
    /** Database column taskBakId SqlType(BIGINT UNSIGNED) */
    val taskBakId: Rep[Long] = column[Long]("taskBakId")
    /** Database column appId SqlType(BIGINT UNSIGNED) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column indexName SqlType(VARCHAR), Length(255,true) */
    val indexName: Rep[String] = column[String]("indexName")
    /** Database column status SqlType(TINYINT UNSIGNED), Default(0) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(0))
    /** Database column result SqlType(VARCHAR), Length(1022,true), Default(Some(0)) */
    val result: Rep[Option[String]] = column[Option[String]]("result", O.Default(Some("0")))
  }

  val jobTaskBakSpecs = new TableQuery(tag => new JobTaskBakSpec(tag))


  class LdapConfig(_tableTag: Tag) extends Table[LdapConfigEntity](_tableTag, Some("itoaManagementUATNew"), "ldapConfig") {
    def * = (ldapId, host, port, baseDN, baseFilter, bindDN, bindPwd, isSSL, status, order, created, createdBy, updated, updatedBy) <> (LdapConfigEntity.tupled, LdapConfigEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(ldapId), Rep.Some(host), Rep.Some(port), Rep.Some(baseDN), baseFilter, Rep.Some(bindDN), Rep.Some(bindPwd), Rep.Some(isSSL), Rep.Some(status), Rep.Some(order), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => LdapConfigEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column ldapId SqlType(BIGINT), PrimaryKey */
    val ldapId: Rep[Long] = column[Long]("ldapId", O.PrimaryKey)
    /** Database column host SqlType(VARCHAR), Length(50,true) */
    val host: Rep[String] = column[String]("host")
    /** Database column port SqlType(INT) */
    val port: Rep[Int] = column[Int]("port")
    /** Database column baseDN SqlType(VARCHAR), Length(255,true) */
    val baseDN: Rep[String] = column[String]("baseDN")
    /** Database column baseFilter SqlType(VARCHAR), Length(255,true), Default(None) */
    val baseFilter: Rep[Option[String]] = column[Option[String]]("baseFilter", O.Default(None))
    /** Database column bindDN SqlType(VARCHAR), Length(100,true) */
    val bindDN: Rep[String] = column[String]("bindDN")
    /** Database column bindPwd SqlType(VARCHAR), Length(100,true) */
    val bindPwd: Rep[String] = column[String]("bindPwd")
    /** Database column isSSL SqlType(INT) */
    val isSSL: Rep[Int] = column[Int]("isSSL")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column order SqlType(INT) */
    val order: Rep[Int] = column[Int]("order")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val ldapConfigs = new TableQuery(tag => new LdapConfig(tag))


  class Streaming(_tableTag: Tag) extends Table[StreamingEntity](_tableTag, "streaming") {
    def * = (streamingId, streamingName, stream, appId, timestamp, status, sourceType, created, createdBy, updated, updatedBy, remark) <> (StreamingEntity.tupled, StreamingEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(streamingId), Rep.Some(streamingName), Rep.Some(stream), Rep.Some(appId), timestamp, Rep.Some(status), Rep.Some(sourceType), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy), remark).shaped.<>({ r => import r._; _1.map(_ => StreamingEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column streamingId SqlType(BIGINT), PrimaryKey */
    val streamingId: Rep[Long] = column[Long]("streamingId", O.PrimaryKey)
    /** Database column streamingName SqlType(VARCHAR), Length(255,true) */
    val streamingName: Rep[String] = column[String]("streamingName")
    /** Database column stream SqlType(VARCHAR), Length(50,true) */
    val stream: Rep[String] = column[String]("stream")
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column timestamp SqlType(VARCHAR), Length(255,true), Default(None) */
    val timestamp: Rep[Option[String]] = column[Option[String]]("timestamp", O.Default(None))
    /** Database column status SqlType(INT) */
    val status: Rep[Int] = column[Int]("status")
    /** Database column sourceType SqlType(VARCHAR), Length(255,true) */
    val sourceType: Rep[String] = column[String]("sourceType")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
    /** Database column remark SqlType(VARCHAR), Length(500,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
  }

  val streamings = new TableQuery(tag => new Streaming(tag))


  class TransBaseConfig(_tableTag: Tag) extends Table[TransBaseConfigEntity](_tableTag, "transBaseConfig") {
    def * = (id, sourceName, sourceFieldName, sourceFieldType, required) <> (TransBaseConfigEntity.tupled, TransBaseConfigEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(sourceName), Rep.Some(sourceFieldName), Rep.Some(sourceFieldType), Rep.Some(required)).shaped.<>({ r => import r._; _1.map(_ => TransBaseConfigEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column sourceName SqlType(VARCHAR), Length(255,true) */
    val sourceName: Rep[String] = column[String]("sourceName")
    /** Database column sourceFieldName SqlType(VARCHAR), Length(255,true) */
    val sourceFieldName: Rep[String] = column[String]("sourceFieldName")
    /** Database column sourceFieldType SqlType(VARCHAR), Length(50,true) */
    val sourceFieldType: Rep[String] = column[String]("sourceFieldType")
    /** Database column required SqlType(INT), Default(1) */
    val required: Rep[Int] = column[Int]("required", O.Default(1))
  }

  val transBaseConfigs = new TableQuery(tag => new TransBaseConfig(tag))


  class TransMonitor(_tableTag: Tag) extends Table[TransMonitorEntity](_tableTag, "transMonitor") {
    def * = (transactionMonitorId, appId, systemName, groupName, order, streamingId, componentType, created, createdBy, updated, updatedBy) <> (TransMonitorEntity.tupled, TransMonitorEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(transactionMonitorId), Rep.Some(appId), Rep.Some(systemName), Rep.Some(groupName), Rep.Some(order), streamingId, Rep.Some(componentType), Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => TransMonitorEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get, _11.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column transactionMonitorId SqlType(BIGINT), PrimaryKey */
    val transactionMonitorId: Rep[Long] = column[Long]("transactionMonitorId", O.PrimaryKey)
    /** Database column appId SqlType(BIGINT) */
    val appId: Rep[Long] = column[Long]("appId")
    /** Database column systemName SqlType(VARCHAR), Length(255,true) */
    val systemName: Rep[String] = column[String]("systemName")
    /** Database column groupName SqlType(VARCHAR), Length(50,true) */
    val groupName: Rep[String] = column[String]("groupName")
    /** Database column order SqlType(INT) */
    val order: Rep[Int] = column[Int]("order")
    /** Database column streamingId SqlType(BIGINT), Default(None) */
    val streamingId: Rep[Option[Long]] = column[Option[Long]]("streamingId", O.Default(None))
    /** Database column componentType SqlType(INT) */
    val componentType: Rep[Int] = column[Int]("componentType")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val transMonitors = new TableQuery(tag => new TransMonitor(tag))


  class TransMonitorDetail(_tableTag: Tag) extends Table[TransMonitorDetailEntity](_tableTag, "transMonitorDetail") {
    def * = (transactionMonitorId, baseMonitorConfigId, targetField, config, created, createdBy, updated, updatedBy) <> (TransMonitorDetailEntity.tupled, TransMonitorDetailEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(transactionMonitorId), Rep.Some(baseMonitorConfigId), targetField, config, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => TransMonitorDetailEntity.tupled((_1.get, _2.get, _3, _4, _5.get, _6.get, _7.get, _8.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column transactionMonitorId SqlType(BIGINT) */
    val transactionMonitorId: Rep[Long] = column[Long]("transactionMonitorId")
    /** Database column baseMonitorConfigId SqlType(BIGINT) */
    val baseMonitorConfigId: Rep[Long] = column[Long]("baseMonitorConfigId")
    /** Database column targetField SqlType(VARCHAR), Length(255,true), Default(None) */
    val targetField: Rep[Option[String]] = column[Option[String]]("targetField", O.Default(None))
    /** Database column config SqlType(VARCHAR), Length(500,true), Default(None) */
    val config: Rep[Option[String]] = column[Option[String]]("config", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")

    /** Primary key of TransMonitorDetail (database name transMonitorDetail_PK) */
    val pk = primaryKey("transMonitorDetail_PK", (transactionMonitorId, baseMonitorConfigId))
  }

  val transMonitorDetails = new TableQuery(tag => new TransMonitorDetail(tag))


  class TransThreshold(_tableTag: Tag) extends Table[TransThresholdEntity](_tableTag, "transThreshold") {
    def * = (id, name, alias) <> (TransThresholdEntity.tupled, TransThresholdEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(alias)).shaped.<>({ r => import r._; _1.map(_ => TransThresholdEntity.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name")
    /** Database column alias SqlType(VARCHAR), Length(255,true) */
    val alias: Rep[String] = column[String]("alias")
  }

  val transThresholds = new TableQuery(tag => new TransThreshold(tag))


  class TransThresholdValue(_tableTag: Tag) extends Table[TransThresholdValueEntity](_tableTag, "transThresholdValue") {
    def * = (transactionMonitorId, thresholdId, value0, value1) <> (TransThresholdValueEntity.tupled, TransThresholdValueEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(transactionMonitorId), Rep.Some(thresholdId), Rep.Some(value0), Rep.Some(value1)).shaped.<>({ r => import r._; _1.map(_ => TransThresholdValueEntity.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column transactionMonitorId SqlType(BIGINT) */
    val transactionMonitorId: Rep[Long] = column[Long]("transactionMonitorId")
    /** Database column thresholdId SqlType(BIGINT) */
    val thresholdId: Rep[Long] = column[Long]("thresholdId")
    /** Database column value0 SqlType(INT) */
    val value0: Rep[Int] = column[Int]("value0")
    /** Database column value1 SqlType(INT) */
    val value1: Rep[Int] = column[Int]("value1")

    /** Primary key of TransThresholdValue (database name transThresholdValue_PK) */
    val pk = primaryKey("transThresholdValue_PK", (transactionMonitorId, thresholdId))
  }

  val transThresholdValues = new TableQuery(tag => new TransThresholdValue(tag))


  class User(_tableTag: Tag) extends Table[UserEntity](_tableTag, "user") {
    def * = (userId, userName, loginName, password, roleCode, status, remark, lastLoginTime, created, createdBy, updated, updatedBy) <> (UserEntity.tupled, UserEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(userName), Rep.Some(loginName), Rep.Some(password), Rep.Some(roleCode), Rep.Some(status), remark, lastLoginTime, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => UserEntity.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9.get, _10.get, _11.get, _12.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column userId SqlType(BIGINT), PrimaryKey */
    val userId: Rep[Long] = column[Long]("userId", O.PrimaryKey)
    /** Database column userName SqlType(VARCHAR), Length(255,true) */
    val userName: Rep[String] = column[String]("userName")
    /** Database column loginName SqlType(VARCHAR), Length(150,true) */
    val loginName: Rep[String] = column[String]("loginName")
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password")
    /** Database column roleCode SqlType(VARCHAR), Length(10,true) */
    val roleCode: Rep[String] = column[String]("roleCode")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column remark SqlType(VARCHAR), Length(255,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
    /** Database column lastLoginTime SqlType(BIGINT), Default(None) */
    val lastLoginTime: Rep[Option[Long]] = column[Option[Long]]("lastLoginTime", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val users = new TableQuery(tag => new User(tag))

  class UserDefaultPanel(_tableTag: Tag) extends Table[UserDefaultPanelEntity](_tableTag, "userDefaultPanel") {
    def * = (userId, panelId) <> (UserDefaultPanelEntity.tupled, UserDefaultPanelEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(panelId)).shaped.<>({ r => import r._; _1.map(_ => UserDefaultPanelEntity.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column userId SqlType(BIGINT), PrimaryKey */
    val userId: Rep[Long] = column[Long]("userId", O.PrimaryKey)
    /** Database column panelId SqlType(BIGINT) */
    val panelId: Rep[Long] = column[Long]("panelId")
  }

  val userDefaultPanels = new TableQuery(tag => new UserDefaultPanel(tag))

  class UserGroup(_tableTag: Tag) extends Table[UserGroupEntity](_tableTag, "userGroup") {
    def * = (userGroupId, groupName, status, remark, created, createdBy, updated, updatedBy) <> (UserGroupEntity.tupled, UserGroupEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userGroupId), Rep.Some(groupName), Rep.Some(status), remark, Rep.Some(created), Rep.Some(createdBy), Rep.Some(updated), Rep.Some(updatedBy)).shaped.<>({ r => import r._; _1.map(_ => UserGroupEntity.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column userGroupId SqlType(BIGINT), PrimaryKey */
    val userGroupId: Rep[Long] = column[Long]("userGroupId", O.PrimaryKey)
    /** Database column groupName SqlType(VARCHAR), Length(255,true) */
    val groupName: Rep[String] = column[String]("groupName")
    /** Database column status SqlType(INT), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column remark SqlType(VARCHAR), Length(255,true), Default(None) */
    val remark: Rep[Option[String]] = column[Option[String]]("remark", O.Default(None))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")
    /** Database column updated SqlType(BIGINT) */
    val updated: Rep[Long] = column[Long]("updated")
    /** Database column updatedBy SqlType(BIGINT) */
    val updatedBy: Rep[Long] = column[Long]("updatedBy")
  }

  val userGroups = new TableQuery(tag => new UserGroup(tag))


  class UserGroupRelation(_tableTag: Tag) extends Table[UserGroupRelationEntity](_tableTag, "userGroupRelation") {
    def * = (userGroupId, userId, created, createdBy) <> (UserGroupRelationEntity.tupled, UserGroupRelationEntity.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userGroupId), Rep.Some(userId), Rep.Some(created), Rep.Some(createdBy)).shaped.<>({ r => import r._; _1.map(_ => UserGroupRelationEntity.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column userGroupId SqlType(BIGINT) */
    val userGroupId: Rep[Long] = column[Long]("userGroupId")
    /** Database column userId SqlType(BIGINT) */
    val userId: Rep[Long] = column[Long]("userId")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column createdBy SqlType(BIGINT) */
    val createdBy: Rep[Long] = column[Long]("createdBy")

    /** Primary key of UserGroupRelation (database name userGroupRelation_PK) */
    val pk = primaryKey("userGroupRelation_PK", (userGroupId, userId))
  }

  val userGroupRelations = new TableQuery(tag => new UserGroupRelation(tag))
}
