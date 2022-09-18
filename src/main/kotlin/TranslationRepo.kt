import Vault.dbHost
import Vault.dbName
import Vault.dbPassword
import Vault.dbUsername
import Vault.translationCacheSize
import deepl.DeeplClient
import deepl.SourceLang
import deepl.TargetLang
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import util.enumValueOfOrNull

object TranslationRepo {
    suspend fun translate(text: String, targetLang: TargetLang, sourceLang: SourceLang? = null): String {
        val cachedTranslation = getCachedTranslation(text, targetLang, sourceLang)

        return cachedTranslation ?: deepl.translate(text, targetLang, sourceLang).also { translatedText ->
            cache(text, targetLang, sourceLang, translatedText)
        }
    }


    private fun getCachedTranslation(text: String, targetLang: TargetLang, sourceLang: SourceLang?) = transaction(db) {
        addLogger(Slf4jSqlDebugLogger)

        Translation.find {
            (Translations.sourceText eq text) and
            (Translations.targetLang eq targetLang.name) and
            (Translations.sourceLang eq sourceLang?.name)
        }.firstOrNull()?.translatedText
    }

    private fun cache(
        sourceText: String,
        targetLang: TargetLang,
        sourceLang: SourceLang?,
        translatedText: String
    ): Unit = transaction(db) {
        addLogger(Slf4jSqlDebugLogger)

        Translation.new {
            this.sourceText = sourceText
            this.targetLang = targetLang
            this.sourceLang = sourceLang
            this.translatedText = translatedText
        }

        capCache()
    }

    private fun capCache(): Unit = transaction(db) {
        addLogger(Slf4jSqlDebugLogger)

        val cacheSize = Translation.count(Op.TRUE)
        val amountToRemove = maxOf(cacheSize - translationCacheSize, 0)

        val translationsToBeRemoved =
            Translation.find {Op.TRUE}.orderBy(Translations.id to SortOrder.DESC).limit(amountToRemove.toInt())

        for (translation in translationsToBeRemoved) {
            translation.delete()
        }
    }

    private val deepl = DeeplClient(Vault.deeplAuthKey)

    private val db = Database.connect(
        "jdbc:postgresql://$dbHost/$dbName",
        user = dbUsername,
        password = dbPassword
    )

    init {
        transaction(db) {
            addLogger(Slf4jSqlDebugLogger)

            SchemaUtils.create(Translations)
        }
    }
}

object Translations : IntIdTable() {
    val sourceText = text("text")
    val targetLang = varchar("targetLang", 50)
    val sourceLang = varchar("sourceLang", 50).nullable()

    val translatedText = text("translatedText")
}

class Translation(id: EntityID<Int>) : IntEntity(id) {
    var sourceText by Translations.sourceText
    var targetLang by Translations.targetLang.memoizedTransform({it.name}, {enumValueOf<TargetLang>(it)})
    var sourceLang by Translations.sourceLang.memoizedTransform({it?.name}, {enumValueOfOrNull<SourceLang>(it ?: "")})

    var translatedText by Translations.translatedText

    companion object : IntEntityClass<Translation>(Translations)
}
