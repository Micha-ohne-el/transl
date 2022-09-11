
import Vault.dbHost
import Vault.dbName
import Vault.dbPassword
import Vault.dbUsername
import deepl.TargetLang
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object GuildLangRepo {
    fun getGuildLang(guildId: Long): TargetLang? = transaction(db) {
        addLogger(StdOutSqlLogger)

        val guildLang = GuildLang.findById(guildId)

        guildLang?.lang
    }

    fun setGuildLang(guildId: Long, lang: TargetLang): Unit = transaction(db) {
        addLogger(StdOutSqlLogger)

        val existingGuildLang = GuildLang.findById(guildId)

        if (existingGuildLang == null) {
            GuildLang.new(guildId) {this.lang = lang}
        } else {
            existingGuildLang.lang = lang
        }
    }

    fun clearGuildLang(guildId: Long): Unit = transaction(db) {
        addLogger(StdOutSqlLogger)

        GuildLang.findById(guildId)?.delete()
    }

    val defaultGuildLang = TargetLang.BritishEnglish


    private val db = Database.connect(
        "jdbc:postgresql://$dbHost/$dbName",
        user = dbUsername,
        password = dbPassword
    )

    init {
        transaction(db) {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(GuildLangs)
        }
    }
}

object GuildLangs : LongIdTable(columnName = "guildId") {
    val lang = varchar("lang", 50)
}

class GuildLang(id: EntityID<Long>) : LongEntity(id) {
    var lang: TargetLang by GuildLangs.lang.memoizedTransform({it.name}, {enumValueOf(it)})

    companion object : LongEntityClass<GuildLang>(GuildLangs)
}